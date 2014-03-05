package in.sms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Question extends Activity implements OnClickListener{

	EditText ques,ans;
	Button btn;
	int ask;
	String q;
	String a, actualAns;
	
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question);
		ques = (EditText) findViewById(R.id.ques);
		ans = (EditText) findViewById(R.id.ans);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(this);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = pref.edit();
		q = pref.getString("ques", "!");
		actualAns = pref.getString("ans", "1");
		System.out.println(ask);
		ask = pref.getInt("ask", 1);
		if(ask == 1){
			ques.setText(q);
			ques.setKeyListener(null);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		q = ques.getText().toString();
		a = ans.getText().toString();
		System.out.println(q + " : " + a);
		if(ask == 0){
			
			a = ans.getText().toString();
			editor.putString("ques", q);
			editor.putString("ans", a);
			editor.putInt("ask", 1);
			editor.commit();
			Intent i = new Intent(this, Conversation.class);
			finish();
			startActivity(i);
		}else if(ask == 1){
			if(a.contentEquals(actualAns)){
				editor.putInt("mode", 0);
				editor.putInt("hits", 0);
				editor.commit();
				Intent home = new Intent(this, NewPassword.class);
				finish();
				startActivity(home);
			}
			else{
				ans.setText("");
				Toast.makeText(this, "Incorrect Answer", Toast.LENGTH_SHORT);
			}
		}
	}

	
}
