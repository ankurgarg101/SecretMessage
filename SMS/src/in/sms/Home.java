package in.sms;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Home extends ListActivity {

	String options[] = new String[] { "Inbox", "Outbox", "Conversation"};
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Home.this,
				android.R.layout.simple_list_item_1, options));
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		// setContentView(R.layout.home);
		String opt = options[position];
		try {
				Class toHome = Class.forName("in.sms." + opt);
				Intent home = new Intent(Home.this, toHome);
				startActivity(home);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
