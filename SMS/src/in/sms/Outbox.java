package in.sms;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Outbox extends ListActivity {

	// NotificationManager nm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Cancel the Notification
		List<SMSData> smsList = new ArrayList<SMSData>();

		Uri uri = Uri.parse("content://sms/sent");
		Cursor c = getContentResolver().query(uri, null, null, null, null);
		// startManagingCursor(c);

		// Read the sms data and store it in the list
		if (c.moveToFirst()) {
			for (int i = 0; i < c.getCount(); i++) {
				SMSData sms = new SMSData();
				sms.setBody(c.getString(c.getColumnIndexOrThrow("body"))
						.toString());
				sms.setNumber(c.getString(c.getColumnIndexOrThrow("address"))
						.toString());
				sms.setDate(c.getLong(c.getColumnIndexOrThrow("date")));
				// sms.setPersonId(c.getInt(c.getColumnIndexOrThrow("person")));
				sms.setName(GetContactName(c.getString(
						c.getColumnIndexOrThrow("address")).toString()));
				// sms.setStatus(c.getInt(c.getColumnIndexOrThrow("delivered")));
				smsList.add(sms);

				c.moveToNext();
			}
		}
		c.close();

		// Set smsList in the ListAdapter
		setListAdapter(new OutboxListAdapter(this, smsList));

	}

	private String GetContactName(String phoneNumber) {
		String contactName = "Contact is not Saved";
		// TODO Auto-generated method stub
		// define the columns I want the query to return
		// String[] projection = new String[] {
		// ContactsContract.PhoneLookup.DISPLAY_NAME,
		// ContactsContract.PhoneLookup._ID};

		String[] projection = new String[] { "display_name" };
		// encode the phone number and build the filter URI
		Uri contactUri = Uri
				.parse("content://com.android.contacts/phone_lookup");
		contactUri = Uri.withAppendedPath(contactUri, Uri.encode(phoneNumber));
		// query time
		Cursor cursor = this.getContentResolver().query(contactUri, projection,
				null, null, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				contactName = cursor.getString(0);
			}
			cursor.close();
			
		}
		return contactName;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		SMSData sms = (SMSData) getListAdapter().getItem(position);

		Toast.makeText(getApplicationContext(), sms.getBody(),
				Toast.LENGTH_SHORT).show();

	}

}
