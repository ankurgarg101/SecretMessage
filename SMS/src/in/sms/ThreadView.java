package in.sms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class ThreadView extends ListActivity implements OnItemLongClickListener {

	int type;
	String number;
	String body;
	long date;
	long id;
	String status;

	String d = "";

	String msg;
	String num;
	String nameToBeStored;
	long dateToBeStored;
	long idToBeStored;
	long contactIdToBeStored;
	int threadIdToBeStored;

	int thread;
	long contactId;
	String nameOrNumber;
	ListView lv;
// only inbox and outbox should be here
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		thread = getIntent().getIntExtra("thread", -1);
		contactId = getIntent().getLongExtra("id", 0);
		nameOrNumber = getIntent().getStringExtra("contact");
		number = getIntent().getStringExtra("number");

		System.out.println(thread);

		lv = getListView();
		lv.setOnItemLongClickListener(this);
		registerForContextMenu(lv);
		lv.setOnCreateContextMenuListener(this);
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
//					else
//						date = cur.getLong(cur
//								.getColumnIndexOrThrow("date_sent"));

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					// Date dateI = new Date(dateToBeStored);
					String strDate = dateFormat.format(date);
					Log.i("date", body + " : " + strDate);
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
					smsList.add(sms);

				}
			}
			cur.close();

			// Set smsList in the ListAdapter using the array of smsData as a
			// resource

		} catch (Exception e) {
			Log.e("Inbox Read Error", e.toString());
		}
		Collections.sort(smsList,new SMSData());
		setListAdapter(new ThreadListAdapter(this, smsList));
		lv.setSelection(lv.getAdapter().getCount()-1);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle("Options");
		String[] menuItems = { "Delete", "Move to Secret", "Info" };
		for (int i = 0; i < menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = { "Delete", "Move to Secret", "Info" };
		String menuItemName = menuItems[menuItemIndex];

		if (menuItemIndex == 0) {
			Delete del = new Delete(this);
			del.deleteMsg(idToBeStored);
			Intent refresh = new Intent(this, ThreadView.class);
			refresh.putExtra("thread", threadIdToBeStored);
			refresh.putExtra("number", num);
			refresh.putExtra("contact", nameToBeStored);
			refresh.putExtra("id", contactIdToBeStored);
			finish();
			startActivity(refresh);

		} else if (menuItemIndex == 1) {
			MoveToSecret mTs = new MoveToSecret();
			mTs.moveToSecretDb(this, nameToBeStored, num, msg, idToBeStored,
					dateToBeStored, contactIdToBeStored, threadIdToBeStored);
			Intent refresh = new Intent(this, ThreadView.class);
			refresh.putExtra("thread", threadIdToBeStored);
			refresh.putExtra("number", num);
			refresh.putExtra("contact", nameToBeStored);
			refresh.putExtra("id", contactIdToBeStored);
			finish();
			startActivity(refresh);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Message Information");

			String d = nameToBeStored;
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			// Date dateI = new Date(dateToBeStored);
			String strDate = dateFormat.format(dateToBeStored);

			String information = "Draft";
			if (type == 1)
				information = "Type : Text Message \n" + "From : " + d + "\n"
						+ "Received : " + strDate;
			else if (type == 2) {
				information = "Type : Text Message \n" + "To : " + d + "\n"
						+ "Sent : " + strDate;
			}
			builder.setMessage(information);
			builder.setNegativeButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});

			AlertDialog viewInfo = builder.create();
			viewInfo.show();
		}

		return true;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		// check for msgs sent by me

		SMSData sms = (SMSData) getListAdapter().getItem(arg2);
		msg = sms.getBody();
		nameToBeStored = sms.getName();
		num = sms.getNumber();
		dateToBeStored = sms.getDate();
		idToBeStored = sms.getId();
		contactIdToBeStored = sms.getContactId();
		threadIdToBeStored = sms.getThread();
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
