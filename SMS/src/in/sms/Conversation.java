package in.sms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class Conversation extends ListActivity implements OnItemLongClickListener, OnItemClickListener {

	Context context;
	ListView lv;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.about);
		context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();
		lv = getListView();
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);

		try {
			List<ConversationClass> smsList = new ArrayList<ConversationClass>();

			Uri convUri = Uri.parse("content://sms/conversations");
			Cursor convCur = getContentResolver().query(convUri, null, null,
					null, null);
			Uri allUri = Uri.parse("content://sms/");
			Cursor allCur;
			
			if (convCur.moveToFirst()) {
				
				
				String snippet = convCur.getString(convCur
						.getColumnIndexOrThrow("snippet"));
				String num = "", name = "";
				long contactId = 0, time = 0;
				
				int thread = convCur.getInt(convCur
						.getColumnIndexOrThrow("thread_id"));
				allCur = getContentResolver().query(allUri, null,
						"thread_id=?", new String[] { thread + "" }, null);
				if (allCur.moveToFirst()) {
					num = allCur.getString(allCur
							.getColumnIndexOrThrow("address"));
					time = allCur.getLong(allCur
							.getColumnIndexOrThrow("date"));
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm:ss");
					
					String strDate = dateFormat.format(time);
					contactId = Long.valueOf(
							fetchContactIdFromPhoneNumber(num)).longValue();
					System.out.println(contactId);
					
					name = GetContactName(context, num);
					if (name == null)
						name = "";
					
					
					Log.i("msg", snippet + " : " + strDate);

				}
				ConversationClass convClass = new ConversationClass();
				convClass.setSnippet(snippet);
				convClass.setName(name);
				convClass.setNumber(num);
				convClass.setUri(contactId);
				convClass.setThread(thread);
				convClass.setDate(time);
				smsList.add(convClass);
				
				
				
				while (convCur.moveToNext()) {
					
					snippet = convCur.getString(convCur
							.getColumnIndexOrThrow("snippet"));
					num = "";
					name = "";
					contactId = 0;
					time = 0;
					
					thread = convCur.getInt(convCur
							.getColumnIndexOrThrow("thread_id"));
					allCur = getContentResolver().query(allUri, null,
							"thread_id=?", new String[] { thread + "" }, null);
					if (allCur.moveToFirst()) {
						num = allCur.getString(allCur
								.getColumnIndexOrThrow("address"));
						time = allCur.getLong(allCur
								.getColumnIndexOrThrow("date"));
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						
						String strDate = dateFormat.format(time);
						contactId = Long.valueOf(
								fetchContactIdFromPhoneNumber(num)).longValue();
						System.out.println(contactId);
						
						name = GetContactName(context, num);
						if (name == null)
							name = "";
						
						
						Log.i("msg", snippet + " : " + strDate);

					}
					convClass = new ConversationClass();
					convClass.setSnippet(snippet);
					convClass.setName(name);
					convClass.setNumber(num);
					convClass.setUri(contactId);
					convClass.setThread(thread);
					convClass.setDate(time);
					smsList.add(convClass);
				
				}

				convCur.close();
			}
			// for(int i=0;i<c.getColumnCount();i++)
			// {
			// Log.i("fields", c.getColumnName(i).toString());
			// }

			setListAdapter(new ConversationAdapter(this, smsList));
		} catch (Exception e) {
			Log.i("conv error", e.toString());
		}

	}

	private String GetContactName(Context context, String phoneNumber) {
		String contactName = "";
		// TODO Auto-generated method stub
		// define the columns I want the query to return

		String[] projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME };
		// encode the phone number and build the filter URI
		Uri contactUri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		// query time
		Cursor cursor = getApplicationContext().getContentResolver().query(
				contactUri, projection, null, null, null);

		if (cursor.moveToFirst())
			contactName = cursor.getString(cursor
					.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		cursor.close();
		return contactName;
	}

	public String fetchContactIdFromPhoneNumber(String phoneNumber) {
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = this.getContentResolver().query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
				null, null, null);

		String contactId = "0";

		if (cursor.moveToFirst()) {
			do {
				contactId = cursor.getString(cursor
						.getColumnIndex(PhoneLookup._ID));
			} while (cursor.moveToNext());
		}

		return contactId;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowMenu = getMenuInflater();
		blowMenu.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.write:
			try {
				Intent sendIntent = new Intent(Intent.ACTION_VIEW);
				sendIntent.putExtra("sms_body", "");
				sendIntent.setType("vnd.android-dir/mms-sms");
				startActivity(sendIntent);
			} catch (Exception e) {
				Log.e("menu", e.toString());
			}

			break;

		case R.id.secretView:
			try {
				Intent secretView = new Intent(this, Password.class);
				startActivity(secretView);
			} catch (Exception e) {
				Log.e("menu", e.toString());
			}
			break;
		case R.id.change:
			editor.putInt("mode", 1);
			editor.commit();
			Intent redirect = new Intent(this, Password.class);
			startActivity(redirect);
			break;
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
