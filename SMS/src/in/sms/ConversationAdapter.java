package in.sms;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
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

public class ConversationAdapter extends ArrayAdapter<ConversationClass> {

	ImageView iv;
	// List context
	private final Context context;
	// List values
	private final List<ConversationClass> smsList;

	public ConversationAdapter(Context context, List<ConversationClass> smsList) {
		super(context, R.layout.conversation, smsList);
		this.context = context;
		this.smsList = smsList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.conversation, parent, false);
		
		
		TextView contact = (TextView) rowView.findViewById(R.id.contact);
		TextView snippet = (TextView) rowView.findViewById(R.id.snippet);
		iv = (ImageView) rowView.findViewById(R.id.iv);
		
		if(!smsList.get(position).getName().contentEquals(""))
			contact.setText(smsList.get(position).getName());
		else
			contact.setText(smsList.get(position).getNumber());
		
		snippet.setText(smsList.get(position).getSnippet());
		long photoUri = smsList.get(position).getUri();
//		
		if (photoUri != 0){
			InputStream photo_stream = openDisplayPhoto(photoUri);    
			if(photo_stream != null){
				BufferedInputStream buf =new BufferedInputStream(photo_stream);
				Bitmap my_btmp = BitmapFactory.decodeStream(buf);
				
				
				iv.setImageBitmap(my_btmp);
			}
			
		}
		
		//getting the image from database
	
		return rowView;
	}
	
	public InputStream openPhoto(long contactId) {
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
	     Cursor cursor = context.getContentResolver().query(photoUri,
	          new String[] {Contacts.Photo.PHOTO}, null, null, null);
	     if (cursor == null) {
	         return null;
	     }
	     try {
	         if (cursor.moveToFirst()) {
	             byte[] data = cursor.getBlob(0);
	             if (data != null) {
	                 return new ByteArrayInputStream(data);
	             }
	         }
	     } finally {
	         cursor.close();
	     }
	     return null;
	 }
	 
	public InputStream openDisplayPhoto(long contactId) {
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri displayPhotoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.DISPLAY_PHOTO);
	     try {
	         AssetFileDescriptor fd =
	             context.getContentResolver().openAssetFileDescriptor(displayPhotoUri, "r");
	         return fd.createInputStream();
	     } catch (IOException e) {
	         return null;
	     }
	 }
	
}
