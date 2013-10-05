package in.sms;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Home extends ListActivity {

	String options[] = new String[] { "WriteNew", "Inbox", "Outbox", "Delete",
			"MoveToSecret", "ViewSecret" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Home.this,
				android.R.layout.simple_list_item_1, options));
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
		case R.id.abt:
			Intent i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.password:
			Intent a = new Intent(this, Password.class);
			startActivity(a);
			break;
		case R.id.exit:
			finish();
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
