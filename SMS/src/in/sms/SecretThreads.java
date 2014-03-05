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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SecretThreads extends Activity implements OnClickListener{

	Button send;
	EditText write;
	long date;
	String status;
	long id;
	int type;
	int thread;
	long contactId;
	String nameOrNumber, number, body;
	ListView lv;

	// only inbox and outbox should be here
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secret_thread);
		send = (Button) findViewById(R.id.send);
		write = (EditText) findViewById(R.id.write);
		
		thread = getIntent().getIntExtra("thread", -1);
		contactId = getIntent().getLongExtra("id", 0);
		nameOrNumber = getIntent().getStringExtra("contact");
		number = getIntent().getStringExtra("number");

		
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
			r.remove(thread, number);
			r.putEntry(nameOrNumber, number, dr, 23455, dateS, contactId, thread, 1);
		}else{
			int isDr = r.getDraftStatus(number);
			if(isDr == 1){
				r.remove(thread,number);
				SecretDb s = new SecretDb(this);
				s.write();
				String m = s.getBody(number);
				String n = s.getName(number);
				long id = s.getId(number);
				long date = s.getDate(number);
				long cId = s.getContactId(number);
				r.putEntry(n, number, m, id, date, cId, thread, 0);
				s.close();
			}
		}
		r.close();
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.send){
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

}
