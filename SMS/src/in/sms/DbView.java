package in.sms;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DbView extends Activity{

	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dbview);
		tv = (TextView) findViewById(R.id.getInfo);
		DbHandler info = new DbHandler(this);
		info.write();
		String data = info.getInfo();
		info.close();
		tv.setText(data);
		
		
	}
}
