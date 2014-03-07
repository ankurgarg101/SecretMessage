package in.sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class SecretThreads extends Activity implements OnClickListener, OnItemLongClickListener{

	ImageButton send;
	EditText write;
	long date;
	String status;
	long id;
	int type;
	int thread;
	long contactId;
	String nameOrNumber, number, body;
	ListView lv;
	
	
	String nameToBeStored, numberToBeStored, msgStore; 
	long dateToBeStored, idToBeStored,contactIdToBeStored;
	int threadToBeStored;	

	// only inbox and outbox should be here
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secret_thread);
		send = (ImageButton) findViewById(R.id.sendMsg);
		write = (EditText) findViewById(R.id.write);
		
		thread = getIntent().getIntExtra("thread", -1);
		contactId = getIntent().getLongExtra("id", 0);
		nameOrNumber = getIntent().getStringExtra("contact");
		number = getIntent().getStringExtra("number");
		send.setOnClickListener(this);
		
		System.out.println(thread);

		lv = (ListView) findViewById(R.id.listView1);
		List<SMSData> smsList = new ArrayList<SMSData>();
		try {

			Uri uriInbox = Uri.parse("content://sms/");
			ContentResolver cr = getContentResolver();
			Cursor cur = cr.query(uriInbox, null, "thread_id=?",
					new String[] { thread + "" }, null);

			// startManagingCursor(c);

			// Read the sms data and store it in the list
			// type = 1 for received and type = 2 for sent

			if (cur.moveToFirst()) {
				cur.moveToPrevious();
				while (cur.moveToNext()) {

					// fetching datasets from the columns of the inbox databse

					body = cur.getString(cur.getColumnIndexOrThrow("body"))
							.toString();
					id = cur.getLong(cur.getColumnIndex("_id"));
					type = cur.getInt(cur.getColumnIndexOrThrow("type"));

					if (type == 1)

						date = cur.getLong(cur.getColumnIndexOrThrow("date"));
					// else
					// date = cur.getLong(cur
					// .getColumnIndexOrThrow("date_sent"));
					
					if (type == 1) {

						status = cur.getString(cur
								.getColumnIndexOrThrow("read"));

						if (status.contentEquals("0")) {
							// update the Status of the message in the data base

							ContentValues changeStatus = new ContentValues();
							changeStatus.put("read", true);
							// changeStatus
							getContentResolver().update(
									Uri.parse("content://sms/inbox"),
									changeStatus, "body=?",
									new String[] { body });

						}
					}

					// putting all the data in smsData class object and the
					// pushing this object in the array
					SMSData sms = new SMSData();
					sms.setId(id);
					sms.setBody(body);
					sms.setDate(date);
					sms.setName(nameOrNumber);
					sms.setContactId(contactId);
					sms.setNumber(number);
					sms.setThread(thread);
					sms.setType(type);
					sms.setSecret(false);
					smsList.add(sms);

				}
			}
			cur.close();
			
			SecretDb secret = new SecretDb(this);
			secret.write();
			long length = secret.getSize();
		
			while(length > 0){
				
				if (secret.getBody(length) == null || secret.getName(length) == null
						|| secret.getPhoneNumber(length) == null)
					break;
				
				if(number.contentEquals(secret.getPhoneNumber(length))){
					
					SMSData sms = new SMSData();
					sms.setBody(secret.getBody(length));
					// Log.e("msg",info.getId(move_through_db) + "");
					sms.setName(secret.getName(length));
					sms.setId(secret.getId(length));
					sms.setContactId(secret.getContactId(length));
					sms.setDate(secret.getDate(length));
					sms.setBody(secret.getBody(length));
					sms.setSecret(true);
					smsList.add(sms);
				}
				length--;
			}

			// Set smsList in the ListAdapter using the array of smsData as a
			// resource

		} catch (Exception e) {
			Log.e("Inbox Read Error", e.toString());
		}
		
		RecentDB r = new RecentDB(this);
		r.write();
		String dr = r.getDraft(number);
		if(!dr.contentEquals(""))
			write.setText(dr);
		Collections.sort(smsList, new SMSData());
		lv.setAdapter(new ThreadListAdapter(this, smsList));
		lv.setSelection(lv.getAdapter().getCount() - 1);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		String dr = write.getText().toString();
		RecentDB r = new RecentDB(this);
		r.write();
		if(!dr.contentEquals("")){
			System.out.println("dr : " + dr);
			long dateS = System.currentTimeMillis();
			r.modify(number, dr);
		}else{
			r.modify(number, "");
			
		}
		r.close();
		Intent i = new Intent(this, ViewSecret.class);
		finish();
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.sendMsg){
			String msg = write.getText().toString();
			String numberSend = number;
			WriteFromSecret wr = new WriteFromSecret(this);
			wr.sendMsg(numberSend, msg);
			
			Random r = new Random();
			int id = r.nextInt(500000);
			long dateS = System.currentTimeMillis();
			SecretDb secretDb = new SecretDb(this);
			secretDb.write();
			secretDb.putEntry(nameOrNumber, numberSend, msg, id, dateS, contactId, thread);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// check for msgs sent by me

		SMSData sms = (SMSData) lv.getAdapter().getItem(arg2);
		msgStore = sms.getBody();
		nameToBeStored = sms.getName();
		numberToBeStored = sms.getNumber();
		dateToBeStored = sms.getDate();
		idToBeStored = sms.getId();
		contactIdToBeStored = sms.getContactId();
		threadToBeStored = sms.getThread();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowMenu = getMenuInflater();
		blowMenu.inflate(R.menu.threads, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.reply:
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", "");
				sendIntent.setType("vnd.android-dir/mms-sms");
				sendIntent.putExtra("address", number);
				startActivity(sendIntent);
			} catch (Exception e) {
				Log.e("menu", e.toString());
			}
			break;
		case R.id.call:
			try {

				Uri uri = Uri.parse("tel:" + number.trim());
				Intent sendIntent = new Intent(Intent.ACTION_CALL, uri);
				sendIntent.setData(uri);
				startActivity(sendIntent);
			} catch (Exception e) {
				Log.e("menu", e.toString());
			}
		}
		return false;
	}
}
