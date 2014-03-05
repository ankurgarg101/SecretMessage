package in.sms;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewSecretListAdapter extends ArrayAdapter<SMSData> {

	ImageView iv;
	long contactId;
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
		// TextView senderNumber = (TextView)
		// rowView.findViewById(R.id.tvNumber);
		TextView Msg = (TextView) rowView.findViewById(R.id.tvMsg);
		ImageView iv = (ImageView) rowView.findViewById(R.id.iv);
		// senderNumber.setText(smsList.get(position).getNumber());
		Msg.setText(smsList.get(position).getName());
		contactId = smsList.get(position).getContactId();

		if (contactId > 0) {
			InputStream photo_stream = openDisplayPhoto(contactId);
			if (photo_stream != null) {
				BufferedInputStream buf = new BufferedInputStream(photo_stream);
				Bitmap my_btmp = BitmapFactory.decodeStream(buf);

				iv.setImageBitmap(my_btmp);
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