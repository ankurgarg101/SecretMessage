package in.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;

public class Splash extends Activity {
	int firstTime;
	String meNumber;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		firstTime = getPrefs.getInt("hits", 0);
		editor = getPrefs.edit();

		Thread initial = new Thread() {
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

					if (firstTime == 0) {
						editor.putInt("hits", 0);
						editor.putInt("counter", 0);
						editor.putInt("ask", 0);
						editor.commit();
						Intent home = new Intent(Splash.this, NewPassword.class);
						startActivity(home);
					} else {
						Intent home = new Intent(Splash.this,
								Conversation.class);
						startActivity(home);
					}

				}

			}

		};
		initial.start();

	}

	public String fetchContactIdFromPhoneNumber(String phoneNumber) {
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = this.getContentResolver().query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME, PhoneLookup._ID },
				null, null, null);

		String contactId = "0";

		if (cursor.moveToFirst()) {
			do {
				contactId = cursor.getString(cursor
						.getColumnIndex(PhoneLookup._ID));
			} while (cursor.moveToNext());
		}

		return contactId;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();

	}

}
