package in.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.gsm.SmsMessage;

public class Receive extends BroadcastReceiver {
	NotificationManager nm;
	Notification n;
	static final int uniqueId = 2323;
	String body, sender;
	long id;
	private long length;

	@Override
	public void onReceive(Context context, Intent intent) {

		nm = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);

		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		body = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				sender = msgs[i].getOriginatingAddress();
				body = msgs[i].getMessageBody().toString();

			}

			SecretDb fetchNumber = new SecretDb(context);
			fetchNumber.write();
			// long length = fetchNumber.getSize();
			// for(;length>0;length--)
			// {
			if (fetchNumber.findNumber(sender)) {

				String name = fetchNumber.getName(sender);
				long contactId = fetchNumber.getContactId(sender);
				int threadId = fetchNumber.getThreadId(sender);
				long date = System.currentTimeMillis();
				fetchNumber.write();
				fetchNumber.putEntry(name, sender, body, 0, date, contactId, threadId);
				fetchNumber.close();
				abortBroadcast();
			}
		}
		// ---display the new SMS message as a notification---
		Intent goToInbox = new Intent(context, Thread.class);
		PendingIntent pi = PendingIntent.getActivity(context, 0, goToInbox, 0);
		n = new Notification(R.drawable.logo, body, 0);
		n.setLatestEventInfo(context, sender, body, pi);
		n.defaults = Notification.DEFAULT_ALL;
		nm.notify(uniqueId, n);

	}
}
