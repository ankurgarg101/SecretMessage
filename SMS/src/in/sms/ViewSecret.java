package in.sms;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

public class ViewSecret extends ListActivity implements
		OnItemLongClickListener, OnItemClickListener {

	Context context;
	String msg;
	String num;
	String nameToBeStored;
	long dateToBeStored;
	long idToBeStored;
	String statusToBeStored;
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = getApplicationContext();
		List<SMSData> smsList = new ArrayList<SMSData>();

		SecretDb info = new SecretDb(this);
		info.write();

		long move_through_db = info.getSize();
		if (move_through_db == 0)
			Toast.makeText(this, "No Secret Messages", Toast.LENGTH_SHORT)
					.show();

		while ((move_through_db) > 0) {

			if (info.getBody(1) == null || info.getName(1) == null
					|| info.getPhoneNumber(1) == null)
				break;
			SMSData secret = new SMSData();
			secret.setBody(info.getBody(move_through_db));
			// Log.e("msg",info.getId(move_through_db) + "");
			secret.setName(info.getName(move_through_db));
			secret.setNumber(info.getPhoneNumber(move_through_db));
			secret.setId(info.getId(move_through_db));
			smsList.add(secret);

			// System.out.println(info.getBody(move_through_db));
			move_through_db--;
		}

		info.close();
		// Set smsList in the ListAdapter
		setListAdapter(new ViewSecretListAdapter(this, smsList));
		lv = getListView();
		lv.setOnItemLongClickListener(this);
		lv.setOnItemClickListener(this);
		registerForContextMenu(lv);
		lv.setOnCreateContextMenuListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		SMSData sms = (SMSData) getListAdapter().getItem(position);
		String msg = sms.getBody();

		// Log.e("click", "short");
		Bundle showBundle = new Bundle();
		showBundle.putString("sms", msg);
		showBundle.putString("name", sms.getName());
		showBundle.putString("number", sms.getNumber());
		showBundle.putString("from", "inbox");

		Intent view = new Intent(this, ShowSms.class);
		view.putExtras(showBundle);
		startActivity(view);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle("options");
		String[] menuItems = { "Delete", "Info" };
		for (int i = 0; i < menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int menuItemIndex = item.getItemId();
		String[] menuItems = { "Delete", "Info" };
		String menuItemName = menuItems[menuItemIndex];

		if (menuItemIndex == 0) {
			SecretDb del = new SecretDb(context);
			del.write();
			del.remove(idToBeStored);
			del.close();
			Intent refresh = new Intent(this, ViewSecret.class);
			finish();
			startActivity(refresh);
			

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Message Information");

			String d = nameToBeStored;

			if (nameToBeStored.contentEquals(""))
				d = num;
			String information = "Type : Text Message \n" + "From : " + d;

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

		SMSData sms = (SMSData) getListAdapter().getItem(arg2);
		msg = sms.getBody();
		num = sms.getNumber();
		nameToBeStored = sms.getName();
		dateToBeStored = sms.getDate();
		idToBeStored = sms.getId();
		
		return false;
	}

}