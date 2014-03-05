package in.sms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ThreadListAdapter extends ArrayAdapter<SMSData> {

	ImageView iv;
	long contactId;
	// List context
	private final Context context;
	// List values
	private final List<SMSData> smsList;

	public ThreadListAdapter(Context context, List<SMSData> smsList) {
		super(context, R.layout.inbox, smsList);
		this.context = context;
		this.smsList = smsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.inbox, parent, false);

		TextView time = (TextView) rowView.findViewById(R.id.time);
		TextView sentMsg = (TextView) rowView.findViewById(R.id.tvMsg);
		iv = (ImageView) rowView.findViewById(R.id.photo);

		String contact = smsList.get(position).getName();
		contactId = smsList.get(position).getContactId();
		sentMsg.setText(smsList.get(position).getBody());
		int type = smsList.get(position).getType();
		long date = smsList.get(position).getDate();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		// Date dateI = new Date(dateToBeStored);
		String strDate = dateFormat.format(date);
		time.setText(strDate);
		if(type == 1){
			if (contactId != 0) {
				InputStream photo_stream = openDisplayPhoto(contactId);
				if (photo_stream != null) {
					BufferedInputStream buf = new BufferedInputStream(photo_stream);
					Bitmap my_btmp = BitmapFactory.decodeStream(buf);

					iv.setImageBitmap(my_btmp);
				}

			}
		}
		
		return rowView;
	}

	public InputStream openDisplayPhoto(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		Uri displayPhotoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.DISPLAY_PHOTO);
		try {
			AssetFileDescriptor fd = context.getContentResolver()
					.openAssetFileDescriptor(displayPhotoUri, "r");
			return fd.createInputStream();
		} catch (IOException e) {
			return null;
		}
	}
}
