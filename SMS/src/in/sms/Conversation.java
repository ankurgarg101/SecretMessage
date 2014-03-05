package in.sms;

import java.util.ArrayList;
import java.util.Collections;
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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Conversation extends ListActivity implements
		OnItemLongClickListener, OnItemClickListener {

	Context context;
	ListView lv;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	int convCount;
	int threadStore;
	long contactIdStore;
	String numStore, nameOrNumberStore;

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
		registerForContextMenu(lv);
		lv.setOnCreateContextMenuListener(this);
		convCount = 0;
		

		try {
			List<ConversationClass> smsList = new ArrayList<ConversationClass>();

			Uri convUri = Uri.parse("content://sms/conversations");
			Cursor convCur = getContentResolver().query(convUri, null, null,
					null, null);
			Uri allUri = Uri.parse("content://sms/");
			Cursor allCur = null;

			if (convCur.moveToFirst()) {

				convCur.moveToPrevious();

				while (convCur.moveToNext()) {

					convCount++;
					String snippet = convCur.getString(convCur
							.getColumnIndexOrThrow("snippet"));
					String num = "";
					String name = "";
					long contactId = 0;
					long time = 0;

					int thread = convCur.getInt(convCur
							.getColumnIndexOrThrow("thread_id"));
					allCur = getContentResolver().query(allUri, null,
							"thread_id=?", new String[] { thread + "" }, null);
					if (allCur.moveToFirst()) {
						num = allCur.getString(allCur
								.getColumnIndexOrThrow("address"));
						time = allCur.getLong(allCur
								.getColumnIndexOrThrow("date"));
//						SimpleDateFormat dateFormat = new SimpleDateFormat(
//								"yyyy-MM-dd hh:mm:ss");
//
//						String strDate = dateFormat.format(time);
						contactId = Long.valueOf(
								fetchContactIdFromPhoneNumber(num)).longValue();
						
						name = GetContactName(context, num);
						if (name == null)
							name = "";

					}

					ConversationClass convClass = new ConversationClass();
					convClass.setSnippet(snippet);
					convClass.setName(name);
					convClass.setNumber(num);
					convClass.setContactId(contactId);
					convClass.setThread(thread);
					convClass.setDate(time);
					smsList.add(convClass);

				}
				convCount--;
				convCur.close();
				allCur.close();
			}
			// for(int i=0;i<c.getColumnCount();i++)
			// {
			// Log.i("fields", c.getColumnName(i).toString());
			// }

			if (convCount == 0)
				Toast.makeText(this, "No Conversations", Toast.LENGTH_SHORT)
						.show();

			Collections.sort(smsList, new ConversationClass());

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

		ConversationClass data = (ConversationClass) getListAdapter().getItem(
				arg2);
		int thread = data.getThread();
		long contactId = data.getContactId();
		String nameOrnumber = data.getName();
		String num = data.getNumber();
		if (nameOrnumber.contentEquals(""))
			nameOrnumber = data.getNumber();

		Intent openConversation = new Intent(Intent.ACTION_VIEW);
		openConversation.setType("vnd.android-dir/mms-sms");
		openConversation.putExtra("address", num);
		startActivity(openConversation);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
		ConversationClass data = (ConversationClass) getListAdapter().getItem(arg2);
		threadStore = data.getThread();
		
		contactIdStore = data.getContactId();
		nameOrNumberStore = data.getName();
		numStore = data.getNumber();
		if (nameOrNumberStore.contentEquals(""))
			nameOrNumberStore = data.getNumber();

		
		return false;
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle("Options");
		String[] menuItems = { "Delete Thread", "Move To Secret"};//, "Move Thread to Secret"};
		for (int i = 0; i < menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = { "Delete", "Move To Secret"};//, "Move to Secret"};
		String menuItemName = menuItems[menuItemIndex];

		if (menuItemIndex == 0) {
			Delete del = new Delete(this);
			del.deleteThread(threadStore, "1111");
			Intent refresh = new Intent(this, Conversation.class);
			finish();
			startActivity(refresh);
		}
		else if (menuItemIndex == 1) {
			Intent openConversation = new Intent(this, ThreadView.class);
			openConversation.putExtra("thread", threadStore);
			openConversation.putExtra("id", contactIdStore);
			openConversation.putExtra("contact", nameOrNumberStore);
			openConversation.putExtra("number", numStore);
			startActivity(openConversation);
		}

		return true;
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
