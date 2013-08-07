package in.sms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHandler {

	public static final String ROW = "id";
	public static final String NAME = "name";
	public static final String SCALE = "scale";

	private static final String DB_NAME = "ScaleDb";
	private static final String TABLE_NAME = "tableName";
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
					+ " TEXT NOT NULL, " + SCALE + " TEXT NOT NULL);");

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
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	public long putEntry(String dbName, String dbScale) {
		// TODO Auto-generated method stub

		ContentValues cv = new ContentValues();
		cv.put(NAME, dbName);
		cv.put(SCALE, dbScale);
		return ourDb.insert(TABLE_NAME, null, cv);
	}

	public String getInfo() {
		// TODO Auto-generated method stub
		String[] columns = new String[] { ROW, NAME, SCALE };
		Cursor c = ourDb.query(TABLE_NAME, columns, null, null, null, null,
				null);
		String result = "";
		int iRow = c.getColumnIndex(ROW);
		int iName = c.getColumnIndex(NAME);
		int iScale = c.getColumnIndex(SCALE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			result = result + c.getString(iRow) + " " + c.getString(iName)
					+ " " + c.getString(iScale) + "\n";
		}
		return result;
	}

	public String getName(long l) {
		// TODO Auto-generated method stub
		String[] columns = new String[] { ROW, NAME, SCALE };
		Cursor c = ourDb.query(TABLE_NAME, columns, ROW + "=" + l, null, null,
				null, null);
		String resName = null;
		if (c != null) {
			c.moveToFirst();
			resName = c.getString(1);
		}
		return resName;
	}

	public String getScale(long l) {
		// TODO Auto-generated method stub
		String[] columns = new String[] { ROW, NAME, SCALE };
		Cursor c = ourDb.query(TABLE_NAME, columns, ROW + "=" + l, null, null,
				null, null);
		String resScale = "";
		if (c != null) {
			c.moveToFirst();
			resScale = c.getString(2);
		}
		return resScale;
	}

	public void modify(long lR, String mName, String mScale) {
		// TODO Auto-generated method stub
		
		ContentValues cvUpadte = new ContentValues();
		cvUpadte.put(NAME,mName);
		cvUpadte.put(SCALE, mScale);
		ourDb.update(TABLE_NAME, cvUpadte, ROW + "=" + lR  , null);
	}

	public void remove(long dLR) {
		// TODO Auto-generated method stub
		ourDb.delete(TABLE_NAME, ROW + "=" + dLR, null);
	}

	
}
