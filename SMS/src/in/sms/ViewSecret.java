package in.sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ViewSecret extends ListActivity implements OnItemClickListener {

	Context context;
	String msg;
	String num;
	String nameToBeStored;
	long dateToBeStored;
	long idToBeStored;
	String statusToBeStored;
	boolean isSecret;
	long timeFromConv;
	ListView lv;
	int threadStore;
	long contactIdStore;
	String nameOrNumberStore, numStore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		// List<SMSData> smsList = new ArrayList<SMSData>();
		List<ConversationClass> convList = new ArrayList<ConversationClass>();

		// SecretDb info = new SecretDb(this);
		// info.write();
		String recentMsg = "";

		RecentDB recent = new RecentDB(this);
		recent.write();

		long move_through_db = recent.getSize();
		System.out.println(move_through_db);
		if (move_through_db == 0)
			Toast.makeText(this, "No Secret Messages", Toast.LENGTH_SHORT)
					.show();

		while ((move_through_db) > 0) {
			
			if (recent.getBody(move_through_db) == null || recent.getName(move_through_db) == null
					|| recent.getPhoneNumber(move_through_db) == null){
				move_through_db --;
				continue;
			}
			
			int thread = recent.getThreadId(move_through_db);
			String msg = recent.getBody(move_through_db);
			long timeDb = recent.getDate(move_through_db);
			String name = recent.getName(move_through_db);
			long contactId = recent.getContactId(move_through_db);
			String cNumber = recent.getPhoneNumber(move_through_db);
			String dr = recent.getDraft(cNumber);
			
			
			String recentConv = getRecentMsgFromConv(thread);
			if(dr.contentEquals("")){
				if (recentConv != null) {
					if (timeFromConv > timeDb) {
						msg = recentConv;
						timeDb = timeFromConv;
					}

				}
			}else{
				msg = dr;
			}
			

			System.out.println(msg + " : " + timeDb);

			ConversationClass convClass = new ConversationClass();
			convClass.setSnippet(msg);
			convClass.setName(name);
			convClass.setNumber(cNumber);
			convClass.setContactId(contactId);
			convClass.setThread(thread);
			convClass.setDate(timeDb);
			convList.add(convClass);
			move_through_db--;
		}
		recent.close();

		Collections.sort(convList, new ConversationClass());
		// Set smsList in the ListAdapter
		setListAdapter(new ConversationAdapter(this, convList));
		lv = getListView();
		lv.setOnItemClickListener(this);
	}

	private String getRecentMsgFromConv(int thread) {
		// TODO Auto-generated method stub

		Uri allUri = Uri.parse("content://sms/");
		Cursor allCur = null;

		allCur = getContentResolver().query(allUri, null, "thread_id=?",
				new String[] { thread + "" }, null);
		if (allCur.moveToFirst()) {

			timeFromConv = allCur.getLong(allCur.getColumnIndexOrThrow("date"));
			String snippet = allCur.getString(allCur
					.getColumnIndexOrThrow("body"));
			allCur.close();
			System.out.println(snippet);
			return snippet;
		}
		allCur.close();
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		ConversationClass data = (ConversationClass) getListAdapter().getItem(
				position);
		threadStore = data.getThread();

		contactIdStore = data.getContactId();
		nameOrNumberStore = data.getName();
		numStore = data.getNumber();
		Log.i("store num", numStore);
		if (nameOrNumberStore.contentEquals(""))
			nameOrNumberStore = data.getNumber();

		Intent openConversation = new Intent(this, SecretThreads.class);
		openConversation.putExtra("thread", threadStore);
		openConversation.putExtra("id", contactIdStore);
		openConversation.putExtra("contact", nameOrNumberStore);
		openConversation.putExtra("number", numStore);
		startActivity(openConversation);

	}

}