package in.sms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Password extends Activity implements OnClickListener {

	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	EditText et;
	TextView tv;
	Button btn;

	String password;
	String confirmPassword;

	int counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newpass);

		et = (EditText) findViewById(R.id.et);
		tv = (TextView) findViewById(R.id.tv);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();

		counter = prefs.getInt("hits", 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn) {

			password = et.getText().toString();
			if (password.contentEquals("")) {
				et.setText("");
				Toast.makeText(this, "Please Enter a Valid Password",
						Toast.LENGTH_SHORT).show();
			} else {
				int mode = prefs.getInt("mode", 0);
				if (password.contentEquals(prefs.getString("pass", "@#$"))) {
					Intent home;
					if (mode == 0)
						home = new Intent(this, ViewSecret.class);
					else {
						editor.putInt("mode", 0);
						editor.putInt("hits", 0);
						editor.commit();
						home = new Intent(this, NewPassword.class);
					}
					finish();
					startActivity(home);
				} else {
					et.setText("");
					Toast.makeText(this, "Incorrect Password",
							Toast.LENGTH_SHORT).show();
				}

			}

		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();
	}

}
