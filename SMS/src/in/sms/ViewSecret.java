package in.sms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

public class ViewSecret extends ListActivity {

	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		List<SMSData> smsList = new ArrayList<SMSData>();
		
		DbHandler info = new DbHandler(this);
		info.write();
		
		
		long move_through_db = info.getSize();
		
		Toast.makeText(getApplicationContext(), move_through_db + " ", Toast.LENGTH_LONG).show();
		
		while((move_through_db) > 0)
		{
			
			if(info.getBody(1) == null || info.getName(1) == null
					|| info.getPhoneNumber(1) == null)
				break;
			SMSData secret = new SMSData();
			secret.setBody(info.getBody(move_through_db));
			secret.setName(info.getName(move_through_db));
			secret.setNumber(info.getPhoneNumber(move_through_db));
			
					System.out.println(info.getBody(move_through_db));	
					move_through_db--;
		}
		
		info.close();
				// Set smsList in the ListAdapter
				setListAdapter(new ViewSecretListAdapter(this, smsList));
	}
}