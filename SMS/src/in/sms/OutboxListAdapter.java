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

public class OutboxListAdapter extends ArrayAdapter<SMSData> {

	ImageView ivStatus;
	// List context
	private final Context context;
	// List values
	private final List<SMSData> smsList;

	public OutboxListAdapter(Context context, List<SMSData> smsList) {
		super(context, R.layout.outbox, smsList);
		this.context = context;
		this.smsList = smsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.outbox, parent, false);
		TextView senderNumber = (TextView) rowView.findViewById(R.id.tvNumber);
		TextView sentMsg = (TextView) rowView.findViewById(R.id.tvMsg);
		TextView IoS = (TextView) rowView.findViewById(R.id.tvStatus);
		ivStatus = (ImageView) rowView.findViewById(R.id.msgStatus);
		senderNumber.setText(smsList.get(position).getNumber());
		sentMsg.setText(smsList.get(position).getName());
		//int strStatus = smsList.get(position).getStatus();
		//int intStatus = Integer.parseInt(strStatus);
		//IoS.setText(""+ strStatus);
		//decideImg(intStatus);
		return rowView;
	}

	private void decideImg(int status) {
		// TODO Auto-generated method stub
		if (status == 0) {
			InputStream is = context.getResources().openRawResource(
					R.drawable.unread);
			Bitmap bmp = BitmapFactory.decodeStream(is);
			ivStatus.setImageBitmap(bmp);
		}
	}

}
