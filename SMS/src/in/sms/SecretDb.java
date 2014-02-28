package in.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SecretDb {

	public static final String ROW = "serial";
	public static final String NAME = "name";
	public static final String NUMBER = "address";
	public static final String MESSAGE = "body";
	public static final String DATE = "date";
	public static final String ID = "id";
	//public static final String 

	private static final String DB_NAME = "SecretDb";
	private static final String TABLE_NAME = "Msg";
	private static final int VERSION = 1;

	private DbHelper ourHelper;
	private final Context context;
	private SQLiteDatabase ourDb;
	
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DB_NAME, null, VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

			db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ROW
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME
					+ " TEXT NOT NULL, " + NUMBER + " TEXT NOT NULL, " + DATE + " INTEGER NOT NULL, " + ID + " INTEGER NOT NULL, " + MESSAGE + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + " ;");
			onCreate(db);
		}

	}

	public SecretDb(Context c) {
		context = c;
	}

	public SecretDb write() throws SQLException {

		ourHelper = new DbHelper(context);
		ourDb = ourHelper.getWritableDatabase();
		
		//Toast.makeText(context,"Done", Toast.LENGTH_LONG).show();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long putEntry(String dbName, String dbNumber, String dbBody, long id, long date) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		//Toast.makeText(context,"Done", Toast.LENGTH_LONG).show();
		cv.put(NAME, dbName);
		cv.put(NUMBER, dbNumber);
		cv.put(MESSAGE, dbBody);
		cv.put(ID, id);
		cv.put(DATE, date);
		return ourDb.insert(TABLE_NAME, null, cv);
	}

	public long getSize()
	{
		long numRows = DatabaseUtils.longForQuery(ourDb, "SELECT COUNT(*) FROM " + TABLE_NAME, null);
		return numRows;
	}
	
	public String getName(long l) {
		// TODO Auto-generated method stub
		//String[] columns = new String[] { ROW, NAME, NUMBER };
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		String resName = null;
		if (c != null) {
			c.moveToFirst();
			resName = c.getString(1);
		}
		return resName;
	}

	public String getPhoneNumber(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		String resNumber = null;
		if (c != null) {
			c.moveToFirst();
			resNumber = c.getString(2);
		}
		return resNumber;
	}
	
	public String getBody(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		String resNumber = null;
		if (c != null) {
			c.moveToFirst();
			resNumber = c.getString(c.getColumnIndex(MESSAGE));
		}
		return resNumber;
	}
	
	public long getId(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		long resNumber = -1;
		if (c != null) {
			c.moveToFirst();
			resNumber = c.getLong(c.getColumnIndex(ID));
		}
		return resNumber;
	}
	
	public boolean findById(long id){
		Cursor c = ourDb.query(TABLE_NAME, null, ID + "=" + id, null, null,
				null, null);
		if(c == null)
			return false;
		return true;
	}
	

	public void modify(long lR, String mName, String mNumber) {
		// TODO Auto-generated method stub
		
		ContentValues cvUpadte = new ContentValues();
		cvUpadte.put(NAME,mName);
		cvUpadte.put(NUMBER, mNumber);
		ourDb.update(TABLE_NAME, cvUpadte, ROW + "=" + lR  , null);
	}

	public void remove(long id) {
		// TODO Auto-generated method stub
		ourDb.delete(TABLE_NAME, ID + "=" + id, null);
	}

	
}
