package in.sms;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MoveToSecret extends ListActivity {
	String number = null;

	public void moveToSecretDb(Context c, String name, String number,
			String body, long id, long date) {

		SecretDb db = new SecretDb(c);
		db.write();
		db.putEntry(name, number, body, id, date);
		db.close();
		Uri uri = Uri.parse("content://sms/");
		System.out.println(uri);
		try {

			// c.getContentResolver().q
			c.getContentResolver().delete(uri, "_id=?",
					new String[] { id + "" });
			Toast.makeText(c, "Moved To Secret Database", Toast.LENGTH_LONG)
					.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(c, "Move Unsuccessful", Toast.LENGTH_LONG).show();
		}
	}

}
