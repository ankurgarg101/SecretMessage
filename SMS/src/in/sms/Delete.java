package in.sms;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Delete {

	Context context;
	public Delete(Context c) {
		context = c;
		
	}
	
	public void deleteMsg(long id){
		Uri uri = Uri.parse("content://sms/");
		System.out.println(uri);
		try {

			// c.getContentResolver().q
			context.getContentResolver().delete(uri, "_id=?",
					new String[] { id + "" });
			Toast.makeText(context, "Delete Successful", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "Delete Unsuccessful", Toast.LENGTH_LONG).show();
		}
	}
	
	public void deleteThread(long thread, String num){
		Uri uri = Uri.parse("content://sms/");
		System.out.println(uri);
		try {

			// c.getContentResolver().q
			context.getContentResolver().delete(uri, "thread_id=?",
					new String[] { thread + "" });
			Toast.makeText(context, "Thread Deleted", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(context, "Delete Unsuccessful", Toast.LENGTH_LONG).show();
		}
	}
}
