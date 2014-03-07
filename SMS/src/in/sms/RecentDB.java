package in.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecentDB {

	public static final String ROW = "serial";
	public static final String NAME = "name";
	public static final String NUMBER = "address";
	public static final String MESSAGE = "body";
	public static final String DATE = "date";
	public static final String ID = "id";
	public static final String CONTACT_ID = "contact_id";
	public static final String THREAD_ID = "thread_id";
	public static final String DRAFT = "draft";     // 1 for yes and 0 for no
	// public static final String

	private static final String DB_NAME = "RecentDB";
	private static final String TABLE_NAME = "Recent_Msgs";
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
					+ " TEXT NOT NULL, " + NUMBER + " TEXT NOT NULL, "
					+ CONTACT_ID + " INTEGER NOT NULL, " + DRAFT + " TEXT NOT NULL, "+ THREAD_ID
					+ " INTEGER NOT NULL, " + DATE + " INTEGER NOT NULL, " + ID
					+ " INTEGER NOT NULL, " + MESSAGE + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + " ;");
			onCreate(db);
		}

	}

	public RecentDB(Context c) {
		context = c;
	}

	public RecentDB write() throws SQLException {

		ourHelper = new DbHelper(context);
		ourDb = ourHelper.getWritableDatabase();

		// Toast.makeText(context,"Done", Toast.LENGTH_LONG).show();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long putEntry(String dbName, String dbNumber, String dbBody,
			long id, long date, long contactId, int thread_id, String draft) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		// Toast.makeText(context,"Done", Toast.LENGTH_LONG).show();
		cv.put(NAME, dbName);
		cv.put(NUMBER, dbNumber);
		cv.put(MESSAGE, dbBody);
		cv.put(ID, id);
		cv.put(DATE, date);
		cv.put(CONTACT_ID, contactId);
		cv.put(THREAD_ID, thread_id);
		cv.put(DRAFT, draft);
		return ourDb.insert(TABLE_NAME, null, cv);
	}

	public long getSize() {
		long numRows = DatabaseUtils.longForQuery(ourDb,
				"SELECT COUNT(*) FROM " + TABLE_NAME, null);
		
		return numRows;
	}

	public String getName(long l) {
		// TODO Auto-generated method stub
		// String[] columns = new String[] { ROW, NAME, NUMBER };
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		String resName = null;
		if (c != null) {
			c.moveToFirst();
			resName = c.getString(c.getColumnIndexOrThrow(NAME));
		}
		c.close();
		return resName;
	}

	public String getPhoneNumber(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		String resNumber = null;
		if (c != null) {
			c.moveToFirst();
			resNumber = c.getString(c.getColumnIndexOrThrow(NUMBER));
		}
		c.close();
		return resNumber;
	}

	public long getDate(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		long resNumber = -1;
		if (c != null) {
			c.moveToFirst();
			resNumber = c.getLong(c.getColumnIndexOrThrow(DATE));
		}
		c.close();
		return resNumber;
	}
	
	public long getDate(String number) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, NUMBER + "='" + number +"'", null, null,
				null, null);
		long resNumber = -1;
		if (c != null) {
			if(c.moveToFirst()){
				resNumber = c.getLong(c.getColumnIndexOrThrow(DATE));
			}
			
		}
		c.close();
		return resNumber;
	}
	public String getBody(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		String resNumber = null;
		if (c != null) {
			if(c.moveToFirst()){
				resNumber = c.getString(c.getColumnIndexOrThrow(MESSAGE));
			}
			
		}
		c.close();
		return resNumber;
	}

	public long getId(long l) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + l, null, null,
				null, null);
		long resNumber = -1;
		if (c != null) {
			c.moveToFirst();
			resNumber = c.getLong(c.getColumnIndexOrThrow(ID));
		}
		c.close();
		return resNumber;
	}

	public boolean findById(long id) {
		Cursor c = ourDb.query(TABLE_NAME, null, ID + "=" + id, null, null,
				null, null);
		if (c == null)
			return false;
		c.close();
		return true;
	}

	public boolean findNumber(String number) {
		Cursor c = ourDb.query(TABLE_NAME, null, NUMBER + "='" + number + "'",
				null, null, null, null);
		if (c == null)
			return false;
		c.close();
		return true;
	}

	public long getContactId(String number) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, NUMBER + "='" + number + "'",
				null, null, null, null);
		long resNumber = -1;
		if (c != null) {
			if (c.moveToFirst()) {
				resNumber = c.getLong(c.getColumnIndexOrThrow(CONTACT_ID));
			}
		}
		c.close();
		return resNumber;
	}

	public String getDraft(String number) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, NUMBER + "='" + number + "'",
				null, null, null, null);
		String resNumber = null;
		if (c != null) {
			if (c.moveToFirst()) {
				resNumber = c.getString(c.getColumnIndexOrThrow(DRAFT));
			}
		}
		c.close();
		return resNumber;
	}
	public long getContactId(long id) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + id, null, null,
				null, null);
		long resNumber = -1;
		if (c != null) {
			if (c.moveToFirst()) {
				resNumber = c.getLong(c.getColumnIndexOrThrow(CONTACT_ID));
			}
		}
		c.close();
		return resNumber;
	}

	public int getThreadId(long id) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, ROW + "=" + id, null, null,
				null, null);
		int resNumber = -1;
		if (c != null) {
			if (c.moveToFirst()) {
				resNumber = c.getInt(c.getColumnIndexOrThrow(THREAD_ID));
			}
			c.close();
		}
		return resNumber;
	}
	
	public int getThreadId(String number) {
		// TODO Auto-generated method stub
		Cursor c = ourDb.query(TABLE_NAME, null, NUMBER + "='" + number +"'", null, null,
				null, null);
		int resNumber = -1;
		if (c != null) {
			if (c.moveToFirst()) {
				resNumber = c.getInt(c.getColumnIndexOrThrow(THREAD_ID));
			}
		}
		c.close();
		return resNumber;
	}
	
	public void modify(long lR, String mName, String mNumber) {
		// TODO Auto-generated method stub

		ContentValues cvUpadte = new ContentValues();
		cvUpadte.put(NAME, mName);
		cvUpadte.put(NUMBER, mNumber);
		ourDb.update(TABLE_NAME, cvUpadte, ROW + "=" + lR, null);
	}

	public void modify(String mNumber, String draft) {
		// TODO Auto-generated method stub

		ContentValues cvUpadte = new ContentValues();
		cvUpadte.put(DRAFT, draft);
		cvUpadte.put(NUMBER, mNumber);
		ourDb.update(TABLE_NAME, cvUpadte, NUMBER + "='" + mNumber + "'", null);
	}
	public void remove(long id) {
		// TODO Auto-generated method stub
		ourDb.delete(TABLE_NAME, ID + "=" + id, null);
	}

	public void remove(long thread, String number) {
		// TODO Auto-generated method stub
		ourDb.delete(TABLE_NAME, THREAD_ID + "=" + thread, null);
	}
	public String getName(String number) {
		// TODO Auto-generated method stub
		// String[] columns = new String[] { ROW, NAME, NUMBER };
		Cursor c = ourDb.query(TABLE_NAME, null, NUMBER + "='" + number + "'",
				null, null, null, null);
		String resName = null;

		if (c != null) {
			if (c.moveToFirst()) {
				resName = c.getString(c.getColumnIndexOrThrow(NAME));
			}

		}
		c.close();
		return resName;
	}
}
