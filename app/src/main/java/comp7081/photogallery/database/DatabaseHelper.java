package comp7081.photogallery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PhotoGallery";

    // Table Names
    private static final String DB_TABLE = "Photos";

    // column names
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION = "location";


    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "("+
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_IMAGE + " BLOB," +
            KEY_NAME + " TEXT," +
            KEY_DATE + " TEXT," +
            KEY_LOCATION + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating table
        db.execSQL(CREATE_TABLE_IMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        // create new table
        onCreate(db);
    }

    public void addPhotoEntry(String date, byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMAGE,   image);
        //cv.put(KEY_NAME,    name);
        cv.put(KEY_DATE,    date);
        //cv.put(KEY_LOCATION,    location);
        database.insert(DB_TABLE, null, cv );
    }

    public byte[] getMostRecentPhoto() throws SQLiteException {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE;

        try {
            Cursor cursor = database.rawQuery(selectQuery, null);
            if(cursor != null) {
                cursor.moveToLast();
                int index = cursor.getColumnIndexOrThrow("image");
                byte[] image = cursor.getBlob(index);
                cursor.close();
                return image;
            }
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
