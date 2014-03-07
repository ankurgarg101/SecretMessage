package in.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewPassword extends Activity implements OnClickListener {

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
		et.setText("");
		tv.setText("Enter Your Password");
		btn.setOnClickListener(this);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();

		counter = prefs.getInt("hits", 0);
		if (counter == 0) {
			stateZero();
		}

	}

	private void stateZero() {
		// TODO Auto-generated method stub
		counter = 0;
		et.setText("");
		tv.setText("Set Your Password");
	}

	private void stateOne() {
		// TODO Auto-generated method stub
		counter = 1;
		et.setText("");
		tv.setText("Confirm Your Password");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn) {

			if (counter == 0) {
				password = et.getText().toString();
				if (password.contentEquals("")) {
					et.setText("");
					Toast.makeText(this, "Please Enter a Valid Password",
							Toast.LENGTH_SHORT).show();
				} else
					stateOne();
			} else if (counter == 1) {
				confirmPassword = et.getText().toString();
				if (confirmPassword.contentEquals("")) {
					et.setText("");
					Toast.makeText(this, "Please Enter a Valid Password",
							Toast.LENGTH_SHORT).show();
				} else {
					if (confirmPassword.contentEquals(password)) {
						editor.putInt("hits", 2);
						editor.commit();
						counter = 2;
						editor.putString("pass", password);

						editor.commit();
						int ask = prefs.getInt("ask", 0);
						if (ask == 0){
							getQues();
						}
							
						else {
							Intent home;
							home = new Intent(this, Conversation.class);
							finish();
							startActivity(home);
						}

					} else {
						stateZero();
					}
				}

			}
		}
	}

	private void getQues() {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(this);
		View dialogview = li.inflate(R.layout.dialog_view, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(dialogview);
		final EditText q = (EditText) dialogview.findViewById(R.id.editText1);
		final EditText a = (EditText) dialogview.findViewById(R.id.editText2);

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Save", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						String ques = q.getText().toString();
						String ans = a.getText().toString();
						if (ques.contentEquals("") || ans.contentEquals("")) {
							Toast.makeText(NewPassword.this,
									"Please Enter Valid Inputs",
									Toast.LENGTH_SHORT).show();
						} else {
							editor.putString("ques", ques);
							editor.putString("ans", ans);
							editor.commit();
							Intent i = new Intent(NewPassword.this, Conversation.class);
							startActivity(i);
							dialog.dismiss();
						}
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
