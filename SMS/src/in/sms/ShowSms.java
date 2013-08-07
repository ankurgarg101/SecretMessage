package in.sms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ShowSms extends Activity implements OnClickListener {

	Button bReply;
	TextView smsView, senderName;
	SMSData viewSms;
	String msg, name, number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.received);
		bReply = (Button) findViewById(R.id.bReply);
		smsView = (TextView) findViewById(R.id.smsView);
		senderName = (TextView) findViewById(R.id.senderName);
		try {
			ViewSms();
		} catch (Exception e) {
			smsView.setText(e.toString());
			Log.e("Error", e.toString());
		}
		bReply.setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onPause();
		name = (String) senderName.getText();
		msg = (String) smsView.getText();

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onResume();
		senderName.setText(name);
		smsView.setText(msg);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bReply:
			
			Intent i = new Intent(this,DbView.class);
			startActivity(i);
			
			
		/*	Bundle sendNumber = new Bundle();
			sendNumber.putString("num" , number);
			Intent reply = new Intent(this, WriteNew.class);
			reply.putExtras(sendNumber);
			startActivity(reply);*/
			break;
		}
	}

	private void ViewSms() {
		// TODO Auto-generated method stub

		Bundle getSms = getIntent().getExtras();
		senderName.setText(getSms.getString("name"));
		smsView.setText(getSms.getString("sms"));
		number = getSms.getString("number");
	}

}
