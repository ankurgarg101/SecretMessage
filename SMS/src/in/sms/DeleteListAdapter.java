package in.sms;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DeleteListAdapter extends ArrayAdapter<SMSData> {

	//CheckBox cb;
	ImageView ivStatus;
	// List context
	private final Context context;
	// List values
	private final List<SMSData> smsList;

	public DeleteListAdapter(Context context, List<SMSData> smsList) {
		super(context, R.layout.inbox, smsList);
		this.context = context;
		this.smsList = smsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.inbox, parent, false);
		//cb = (CheckBox) rowView.findViewById(R.id.cb);
		//cb.setText(smsList.get(position).getBody());
		TextView receiverNumber = (TextView) rowView.findViewById(R.id.tvNumber);
		TextView sentMsg = (TextView) rowView.findViewById(R.id.tvMsg);
		ivStatus = (ImageView) rowView.findViewById(R.id.msgStatus);
		receiverNumber.setText(smsList.get(position).getNumber());
		sentMsg.setText(smsList.get(position).getName());
		//String strStatus = smsList.get(position).getStatus();
		//int intStatus = Integer.parseInt(strStatus);
		//decideImg(intStatus);
		return rowView;
	}

	/*private void decideImg(int status) {
		// TODO Auto-generated method stub
		if (status == 0) {
			InputStream is = context.getResources().openRawResource(
					R.drawable.unread);
			Bitmap bmp = BitmapFactory.decodeStream(is);
			ivStatus.setImageBitmap(bmp);
		}
	}*/

}
