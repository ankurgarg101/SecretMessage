package in.sms;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Delete extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// NotificationManager nm;
		String number = null;

		// Cancel the Notification
		List<SMSData> smsList = new ArrayList<SMSData>();

		Uri uriInbox = Uri.parse("content://sms/inbox");
		Cursor c = getContentResolver().query(uriInbox, null, null, null, null);
		// startManagingCursor(c);

		// Read the sms data and store it in the list
		if (c.moveToFirst()) {
			while (c.moveToNext()) {
				SMSData sms = new SMSData();

				sms.setId(c.getLong(0));
				// sms.setPersonId(c.getString(c.getColumnIndexOrThrow("person"))
				// .toString());
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
		long Id = sms.getId();
		Uri uri = null;
		URL url;
		
			uri = Uri.parse("content://sms/inbox/");
			try {
				Cursor c = getContentResolver().query(uri, null, null, null, null);
				//c.moveToFirst();
				getContentResolver().delete(uri,
						"body=" + sms.getBody(), null);
				Toast.makeText(getApplicationContext(), sms.getBody(),
						Toast.LENGTH_SHORT).show();
				String msg = "Deleted From Inbox";
				Dialog d = new Dialog(this);
				d.setTitle("Unable To Delete");
				TextView tv = new TextView(this);
				tv.setText(msg);
				d.setContentView(tv);
				d.show();
			}catch (Exception e) {
			// TODO Auto-generated catch block
			String error = e.toString();
			Dialog d = new Dialog(this);
			d.setTitle("Unable To Delete");
			TextView tv = new TextView(this);
			tv.setText(error);
			d.setContentView(tv);
			d.show();
		}
	}	

}
