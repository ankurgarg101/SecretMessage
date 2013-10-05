package in.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DbHandler {

	public static final String ROW = "id";
	public static final String NAME = "sender_name";
	public static final String NUMBER = "phone_number";
	public static final String MESSAGE = "message";
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
					+ " TEXT NOT NULL, " + NUMBER + " TEXT NOT NULL, " + MESSAGE + " TEXT NOT NULL);");

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + " ;");
			onCreate(db);
		}

	}

	public DbHandler(Context c) {
		context = c;
	}

	public DbHandler write() throws SQLException {

		ourHelper = new DbHelper(context);
		ourDb = ourHelper.getWritableDatabase();
		
		//Toast.makeText(context,"Done", Toast.LENGTH_LONG).show();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long putEntry(String dbName, String dbNumber, String dbBody) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		//Toast.makeText(context,"Done", Toast.LENGTH_LONG).show();
		cv.put(NAME, dbName);
		cv.put(NUMBER, dbNumber);
		cv.put(MESSAGE, dbBody);
		return ourDb.insert(TABLE_NAME, null, cv);
	}

	public long getSize()
	{
		long numRows = DatabaseUtils.longForQuery(ourDb, "SELECT COUNT(*) FROM " + TABLE_NAME, null);
		System.out.println(numRows);
		return numRows;
	}
	
	public String getInfo() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { ROW, NAME, NUMBER, MESSAGE };
		Cursor c = ourDb.query(TABLE_NAME, columns, null, null, null, null,
				null);
		String result = "";
		int iRow = c.getColumnIndex(ROW);
		int iName = c.getColumnIndex(NAME);
		int iScale = c.getColumnIndex(NUMBER);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			result = result + c.getString(iRow) + " " + c.getString(iName)
					+ " " + c.getString(iScale) + "\n";
		}
		return result;
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
			resNumber = c.getString(3);
		}
		return resNumber;
	}
	
	

	public void modify(long lR, String mName, String mNumber) {
		// TODO Auto-generated method stub
		
		ContentValues cvUpadte = new ContentValues();
		cvUpadte.put(NAME,mName);
		cvUpadte.put(NUMBER, mNumber);
		ourDb.update(TABLE_NAME, cvUpadte, ROW + "=" + lR  , null);
	}

	public void remove(long dLR) {
		// TODO Auto-generated method stub
		ourDb.delete(TABLE_NAME, ROW + "=" + dLR, null);
	}

	
}
