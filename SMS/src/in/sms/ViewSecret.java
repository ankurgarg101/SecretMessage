package in.sms;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ViewSecret extends ListActivity {

	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		List<SMSData> smsList = new ArrayList<SMSData>();

		SecretDb info = new SecretDb(this);
		info.write();

		long move_through_db = info.getSize();
		String r = info.getInfo();
		System.out.println(r);

		Toast.makeText(getApplicationContext(), move_through_db + " ",
				Toast.LENGTH_LONG).show();

		while ((move_through_db) > 0) {

			if (info.getBody(1) == null || info.getName(1) == null
					|| info.getPhoneNumber(1) == null)
				break;
			SMSData secret = new SMSData();
			secret.setBody(info.getBody(move_through_db));
			secret.setName(info.getName(move_through_db));
			secret.setNumber(info.getPhoneNumber(move_through_db));
			smsList.add(secret);

			System.out.println(info.getBody(move_through_db));
			move_through_db--;
		}

		info.close();
		// Set smsList in the ListAdapter
		setListAdapter(new ViewSecretListAdapter(this, smsList));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		SMSData sms = (SMSData) getListAdapter().getItem(position);
		String msg = sms.getBody();

	//	Log.e("click", "short");
		Bundle showBundle = new Bundle();
		showBundle.putString("sms", msg);
		showBundle.putString("name", sms.getName());
		showBundle.putString("number", sms.getNumber());
		
		Toast.makeText(getApplicationContext(), msg,
				Toast.LENGTH_LONG).show();
		
		Intent view = new Intent(this, ShowSms.class);
		view.putExtras(showBundle);
		startActivity(view);
	}
	
}