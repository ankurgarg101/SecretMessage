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

public class ViewSecretListAdapter extends ArrayAdapter<SMSData> {

	//CheckBox cb;
	// List context
	private final Context context;
	// List values
	private final List<SMSData> smsList;

	public ViewSecretListAdapter(Context context, List<SMSData> smsList) {
		super(context, R.layout.secret, smsList);
		this.context = context;
		this.smsList = smsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.secret, parent, false);
		TextView senderNumber = (TextView) rowView.findViewById(R.id.tvNumber);
		TextView Msg = (TextView) rowView.findViewById(R.id.tvMsg);
		senderNumber.setText(smsList.get(position).getNumber());
		Msg.setText(smsList.get(position).getName());
		
		return rowView;
	}

}