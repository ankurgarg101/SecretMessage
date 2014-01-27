package in.sms;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class ShowSms extends Activity implements OnClickListener{

	Button bReply;
	TextView smsView, senderName;
	SMSData viewSms;
	String msg, name, number;
	TextToSpeech tts;

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bReply:

			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
	        sendIntent.putExtra("sms_body", "");
	        sendIntent.putExtra("address", number);
	        sendIntent.setType("vnd.android-dir/mms-sms");
	        startActivity(sendIntent);

			break;
		}
	}

	private void ViewSms() {
		// TODO Auto-generated method stub

		Bundle getSms = getIntent().getExtras();
		senderName.setText(getSms.getString("name"));
		smsView.setText(getSms.getString("sms"));
		number = getSms.getString("number");
		String from = getSms.getString("from");
		if(from.contentEquals("outbox"))
			bReply.setText("Forward");
		Toast.makeText(getApplicationContext(), getSms.getString("sms"),
				Toast.LENGTH_LONG).show();
	}

}
