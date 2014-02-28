package in.sms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class Splash extends Activity {
	int firstTime;
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
						editor.commit();
						Intent home = new Intent(Splash.this,NewPassword.class);
						startActivity(home);
					}else{
						Intent home = new Intent(Splash.this,Conversation.class);
						startActivity(home);
					}
					
				}

			}

		};
		initial.start();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();

	}

}
