package in.sms;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Inbox extends ListActivity {

	// NotificationManager nm;
	String number = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Cancel the Notification
		List<SMSData> smsList = new ArrayList<SMSData>();

		Uri uriInbox = Uri.parse("content://sms/inbox");
		Cursor c = getContentResolver().query(uriInbox, null, null, null, null);
		// startManagingCursor(c);

		// Read the sms data and store it in the list
		if (c.moveToFirst()) {
			while (c.moveToNext()) {
				SMSData sms = new SMSData();
				sms.setBody(c.getString(c.getColumnIndexOrThrow("body"))
						.toString());
				sms.setNumber(c.getString(c.getColumnIndexOrThrow("address"))
						.toString());
				sms.setDate(c.getLong(c.getColumnIndexOrThrow("date")));
				// sms.setPersonId(c.getInt(c.getColumnIndexOrThrow("person")));
				sms.setName(GetContactName(c.getString(
						c.getColumnIndexOrThrow("address")).toString()));
				sms.setStatus(c.getString(c.getColumnIndexOrThrow("read")));
				smsList.add(sms);
				c.moveToNext();
			}
		}
		c.close();

		// Set smsList in the ListAdapter
		setListAdapter(new InboxListAdapter(this, smsList));

	}

	private String GetContactName(String phoneNumber) {
		String contactName = "Contact is not Saved";
		// TODO Auto-generated method stub
		// define the columns I want the query to return

		String[] projection = new String[] { "display_name" };
		// encode the phone number and build the filter URI
		Uri contactUri = Uri
				.parse("content://com.android.contacts/phone_lookup");
		contactUri = Uri.withAppendedPath(contactUri, Uri.encode(phoneNumber));
		// query time
		Cursor cursor = this.getContentResolver().query(contactUri, projection,
				null, null, null);

		if (cursor.moveToFirst())
			contactName = cursor.getString(0);
		cursor.close();
		return contactName;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		SMSData sms = (SMSData) getListAdapter().getItem(position);
		String msg = sms.getBody();
		DbHandler db = new DbHandler(getApplicationContext());
		db.write();
		db.putEntry(sms.getBody(), sms.getNumber());
		db.close();
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
		if (sms.getStatus().contentEquals("0")) {
			// update the Status of the message in the data base

			Toast.makeText(getApplicationContext(), "read!", Toast.LENGTH_SHORT)
					.show();
			// refresh the Inbox Activity
			// Intent intent = new Intent(this,Inbox.class);
			// startActivity(intent);
		}
		Bundle b = new Bundle();
		b.putString("sms", msg);
		b.putString("name", sms.getName());
		b.putString("number", sms.getNumber());
		Intent view = new Intent(this, ShowSms.class);
		view.putExtras(b);
		startActivity(view);

	}
}
