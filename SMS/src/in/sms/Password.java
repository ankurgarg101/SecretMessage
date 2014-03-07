package in.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
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
	Button forget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);

		et = (EditText) findViewById(R.id.et1);
		tv = (TextView) findViewById(R.id.tv1);
		btn = (Button) findViewById(R.id.btn1);
		forget = (Button) findViewById(R.id.frgtBtn);
		btn.setOnClickListener(this);
		forget.setOnClickListener(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();

		counter = prefs.getInt("hits", 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn1) {

			password = et.getText().toString();
			if (password.contentEquals("")) {
				et.setText("");
				Toast.makeText(this, "Please Enter a Valid Password",
						Toast.LENGTH_SHORT).show();
			} else {
				
				if (password.contentEquals(prefs.getString("pass", "@#$"))) {
					Intent home;
						home = new Intent(this, ViewSecret.class);
					finish();
					startActivity(home);
				} else {
					et.setText("");
					Toast.makeText(this, "Incorrect Password",
							Toast.LENGTH_SHORT).show();
				}

			}

		}
		else if(v.getId() == R.id.frgtBtn){
			Log.i("forgot", "frgt pressed");
			editor.putInt("ask", 1);
			editor.commit();
			getAns();
		}
	}

	private void getAns() {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(this);
		View dialogview = li.inflate(R.layout.dialog_view, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(dialogview);
		final EditText q = (EditText) dialogview.findViewById(R.id.editText1);
		final EditText a = (EditText) dialogview.findViewById(R.id.editText2);
		
		q.setText(prefs.getString("ques", "Your Age"));
		q.setKeyListener(null);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Change passsword", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						String actual = prefs.getString("ans", "20");
						String ans = a.getText().toString();
						if (!ans.contentEquals(actual)){
							Toast.makeText(Password.this,
									"Incorrect Answer",
									Toast.LENGTH_SHORT).show();
							a.setText("");
						} else {
							editor.putInt("mode", 0);
							editor.putInt("hits", 0);
							editor.commit();
							Intent i = new Intent(Password.this, NewPassword.class);
							finish();
							startActivity(i);
							dialog.dismiss();
						}
					}
				}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();
	}

}
