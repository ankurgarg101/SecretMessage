package in.sms;

import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InboxListAdapter extends ArrayAdapter<SMSData> {

	ImageView ivStatus;
	// List context
	private final Context context;
	// List values
	private final List<SMSData> smsList;

	public InboxListAdapter(Context context, List<SMSData> smsList) {
		super(context, R.layout.inbox, smsList);
		this.context = context;
		this.smsList = smsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.inbox, parent, false);
		
		
		TextView contact = (TextView) rowView.findViewById(R.id.contact);
		TextView sentMsg = (TextView) rowView.findViewById(R.id.tvMsg);
		ivStatus = (ImageView) rowView.findViewById(R.id.msgStatus);
		
		if(!smsList.get(position).getName().contentEquals(""))
			contact.setText(smsList.get(position).getName());
		else
			contact.setText(smsList.get(position).getNumber());
		
		sentMsg.setText(smsList.get(position).getBody());
		String strStatus = smsList.get(position).getStatus();
		int intStatus = Integer.parseInt(strStatus);
		setImg(intStatus);
		return rowView;
	}

	//sets the image corresponding to read or unread message
	private void setImg(int status) {
		// TODO Auto-generated method stub
		if (status == 0) {
			InputStream is = context.getResources().openRawResource(
					R.drawable.unread);
			Bitmap bmp = BitmapFactory.decodeStream(is);
			ivStatus.setImageBitmap(bmp);
		}
	}

}
