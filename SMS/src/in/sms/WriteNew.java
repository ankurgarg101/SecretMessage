package in.sms;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WriteNew extends Activity implements OnClickListener {

	EditText etNumber, etMsg;
	Button send, bInbox;
	String msg = "", numberString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write);
		
		if(CheckForBundle()!=null);
		Toast.makeText(this, "true", Toast.LENGTH_LONG).show();
			SetNumberFromBundle(CheckForBundle());
		etNumber = (EditText) findViewById(R.id.etPhoneNumber);
		etMsg = (EditText) findViewById(R.id.etMessage);
		send = (Button) findViewById(R.id.sendSms);
		bInbox = (Button) findViewById(R.id.bInbox);
		send.setOnClickListener(this);
		bInbox.setOnClickListener(this);
	}

		@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.sendSms:

			msg = etMsg.getText().toString();
			numberString = etNumber.getText().toString();
			if (numberString.length() == 0) {
				Toast.makeText(this, "Please Enter the Reciever's Number",
						Toast.LENGTH_LONG).show();
			} else if (msg.length() == 0) {
				Toast.makeText(this, "Please Enter A message",
						Toast.LENGTH_LONG).show();
			} else {
				sendMessage(numberString, msg);
			}
			break;

		case R.id.bInbox:
			try {
				Intent goInbox = new Intent(this, Inbox.class);
				Toast.makeText(this, "Opening", Toast.LENGTH_SHORT);
				startActivity(goInbox);
			} catch (Exception e) {
				Dialog d = new Dialog(this);
				d.setTitle(e.toString());
				d.show();
			}
			break;
		}

	}

	private void sendMessage(String number, String message) {
		// TODO Auto-generated method stub

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager send = SmsManager.getDefault();
		send.sendTextMessage(number, null, message, sentPI, deliveredPI);
		ContentValues values = new ContentValues();

		values.put("address", number);

		values.put("body", message);

		getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
	
	private void SetNumberFromBundle(Bundle b) {
		// TODO Auto-generated method stub
		try{
			
			String number = b.getString("num");
			etMsg.setText(number);
		}catch(Exception e){
			//etMsg.setText(e.toString());
		}
			
	}

	private Bundle CheckForBundle() {
		// TODO Auto-generated method stub
		if(getIntent().getExtras()!=null)
			return getIntent().getExtras();
		else
		return null;
	}

}
