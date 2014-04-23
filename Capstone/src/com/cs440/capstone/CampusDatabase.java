package com.cs440.capstone;

import java.util.Locale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CampusDatabase {
	// Set up the rows
	public static final String KEY_ROW = "_id";
	public static final String KEY_NAME = "building_name";
	public static final String KEY_TYPE = "building_type";
	public static final String KEY_IMAGE = "building_image";
	// public static final String KEY_INFO = "building_info";

	// Set up the database table
	private static final String DATABASE_NAME = "buildingdb";
	private static final String DATABASE_TABLE = "buildingTable";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper; // instance of class below
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	// Set up images
	public String image = ((Integer) R.drawable.ic_building).toString();
	public Integer intPic = R.drawable.ic_building;

	// Constructor
	public CampusDatabase(Context c) {
		ourContext = c;
	}

	public CampusDatabase open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		ourHelper.close();
	}

	// Create the entry
	public long createEntry(String name, String type, String image) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_TYPE, type);
		cv.put(KEY_IMAGE, image);
		// cv.put(KEY_INFO, info);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public Cursor getAllRows() {
		String[] columns = new String[] { KEY_ROW, KEY_NAME, KEY_TYPE,
				KEY_IMAGE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null);
		return c;
	}

	public Cursor searchByName(String searchKey) {
			
		fillDatabase(); //YOU WILL NEED TO FIX THIS //TODO
		
		String[] columns = new String[] { KEY_ROW, KEY_NAME, KEY_TYPE, KEY_IMAGE };

		searchKey = searchKey.toLowerCase(Locale.US); // set search name into all lower case to make searching easier for the user

		Cursor c = getAllRows();

		int iName = c.getColumnIndex(KEY_NAME);
		Integer iterations = 1;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

			String currentVal = c.getString(iName);

			currentVal = currentVal.toLowerCase(Locale.US);

			if (searchKey.contains(currentVal) || currentVal.contains(searchKey)) { // if we have found the value in the database
				Cursor returnCursor = ourDatabase.query(DATABASE_TABLE,columns, KEY_ROW + "=" + iterations, null, null, null,null);
				return returnCursor;
			}
			iterations = iterations + 1;
		}
		return null;
	}

	// Delete all entries in the table. //TODO might want to get rid of this one...
	public void clearDatabase() {
		ourDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

		ourDatabase.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROW
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
				+ " TEXT NOT NULL, " + KEY_TYPE + " TEXT NOT NULL, "
				+ KEY_IMAGE + " TEXT NOT NULL);");
	}

	public void fillDatabase() { //TODO might want to get rid of this one...
		clearDatabase();
		CampusDatabase newdb = new CampusDatabase(ourContext);
		newdb.open();

		newdb.createEntry("Phi Delta Theta", "Frat", image);
		newdb.createEntry("Kittredge", "Art Building", image);
		newdb.createEntry("Schiff Hall", "Dorm", image);
		newdb.createEntry("Anderson Langdon", "Dorm", image);
		newdb.createEntry("Kilworth Chapel", "Chapel", image);
		newdb.createEntry("Trimble Hall", "Dorm", image);
		newdb.createEntry("Seward Hall", "Dorm", image);
		newdb.createEntry("Regester Hall", "Dorm", image);
		newdb.createEntry("Todd Phibbs Hall", "Dorm", image);
		newdb.createEntry("Weyerhouser", "Building", image); // /////FIX
																// BUILDING TYPE
		newdb.createEntry("Wallace Memorial Pool", "Pool", image);
		newdb.createEntry("Wyatt", "Building", image);
		newdb.createEntry("Collins Memorial Library", "Library", image);
		newdb.createEntry("Harned Hall", "Building", image);
		newdb.createEntry("Thompson Hall", "Building", image);
		newdb.createEntry("Schneebeck Concert Hall", "Concert Hall", image);
		newdb.createEntry("Howarth Hall", "Building", image);
		newdb.createEntry("McIntyre Hall", "Building", image);
		newdb.createEntry("Jones Hall", "Building", image);
		newdb.close();
	}

	public Cursor getRow(long rowId) {
		String[] columns = new String[] { KEY_ROW, KEY_NAME, KEY_TYPE,
				KEY_IMAGE };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROW + "="
				+ rowId, null, null, null, null);
		return c;
	}

	public String getId(Cursor c) {
		int row = c.getColumnIndex(KEY_ROW);
		String id = c.getString(row);
		return id;
	}

	public String getBuildingName(Cursor c) {
		int row = c.getColumnIndex(KEY_NAME);
		String name = c.getString(row);
		return name;
	}

	public String getBuildingType(Cursor c) {
		int row = c.getColumnIndex(KEY_TYPE);
		String type = c.getString(row);
		return type;
	}

	public String getBuildingImage(Cursor c) {
		int row = c.getColumnIndex(KEY_IMAGE);
		String image = c.getString(row);
		return image;
	}

	// Class DbHelper
	private static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROW
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
					+ " TEXT NOT NULL, " + KEY_TYPE + " TEXT NOT NULL, "
					+ KEY_IMAGE + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}
}
