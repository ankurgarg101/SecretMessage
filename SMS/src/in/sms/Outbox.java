package in.sms;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class Outbox extends ListActivity implements OnItemClickListener,
		OnItemLongClickListener {

	String number = null;
	String body;
	long date;
	long id;
	int personId;
	String name;
	String status;
	String d = "";
	String msg;
	String num;
	String nameToBeStored;
	long dateToBeStored;
	long idToBeStored;
	String statusToBeStored;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		lv = getListView();
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);
		registerForContextMenu(lv);
		lv.setOnCreateContextMenuListener(this);

		try {

			List<SMSData> smsList = new ArrayList<SMSData>();

			Uri uriOutbox = Uri.parse("content://sms/sent/");
			Cursor cur = getContentResolver().query(uriOutbox, null, null, null,
					null);
			// startManagingCursor(c);

			// Read the sms data and store it in the list
			if (cur.moveToFirst()) {
				while (cur.moveToNext()) {

					// fetching datasets from the columns of the inbox databse
					body = cur.getString(cur.getColumnIndexOrThrow("body"))
							.toString();
					id = cur.getLong(cur.getColumnIndex("_id"));
					number = cur
							.getString(cur.getColumnIndexOrThrow("address"))
							.toString();
					date = cur.getLong(cur.getColumnIndexOrThrow("date"));
					name = GetContactName(this,
							cur.getString(cur.getColumnIndexOrThrow("address"))
									.toString());
					status = cur.getString(cur.getColumnIndexOrThrow("delivered")); //change the image to an error if it is not delivered
					personId = cur.getInt(cur.getColumnIndexOrThrow("person"));

					d += id + " : " + name + " : " + status +"\n";
					Log.e("status",d);

					// putting all the data in smsData class object and the
					// pushing this object in the array
					SMSData sms = new SMSData();
					sms.setId(id);
					sms.setBody(body);
					sms.setNumber(number);
					sms.setDate(date);
					sms.setPersonId(personId);
					sms.setName(name);
					sms.setStatus(status);
					smsList.add(sms);
					cur.moveToNext();
				}
			}
			cur.close();

			// Set smsList in the ListAdapter using the array of smsData as a
			// resource
			setListAdapter(new OutboxListAdapter(this, smsList));

		} catch (Exception e) {
			Log.e("Outbox Read Error", e.toString());
		}

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle("options");
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
			Delete del = new Delete(this, idToBeStored);
			Intent refresh = new Intent(this,Outbox.class);
			startActivity(refresh);
			
		} else if (menuItemIndex == 1) {
			MoveToSecret mTs = new MoveToSecret();
			mTs.moveToSecretDb(this, nameToBeStored, num, msg, idToBeStored,
					dateToBeStored);
			Intent refresh = new Intent(this,Outbox.class);
			startActivity(refresh);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Message Information");

			String d = nameToBeStored;
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			// Date dateI = new Date(dateToBeStored);
			String strDate = dateFormat.format(dateToBeStored);
			if (nameToBeStored.contentEquals(""))
				d = num;
			String information = "Type : Text Message \n" + "To : " + d
					+ "\n" + "Delivered : " + strDate;

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

	private String GetContactName(Context context, String phoneNumber) {
		String contactName = "";
		// TODO Auto-generated method stub
		// define the columns I want the query to return

		String[] projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME };
		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		// query time
		Cursor cursor = getApplicationContext().getContentResolver().query(
				contactUri, projection, null, null, null);

		if (cursor.moveToFirst())
			contactName = cursor.getString(cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		cursor.close();
		return contactName;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		SMSData sms = (SMSData) getListAdapter().getItem(arg2);
		String msg = sms.getBody();

		Bundle showBundle = new Bundle();
		showBundle.putString("sms", msg);
		showBundle.putString("name", sms.getName());
		showBundle.putString("number", sms.getNumber());
		showBundle.putString("from", "outbox");
		Intent view = new Intent(this, ShowSms.class);
		view.putExtras(showBundle);
		startActivity(view);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

		SMSData sms = (SMSData) getListAdapter().getItem(arg2);
		msg = sms.getBody();
		num = sms.getNumber();
		nameToBeStored = sms.getName();
		dateToBeStored = sms.getDate();
		idToBeStored = sms.getId();
		statusToBeStored = sms.getStatus();

		return false;
	}

}
