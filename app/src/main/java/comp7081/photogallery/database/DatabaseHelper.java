package comp7081.photogallery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;

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

    public void addPhotoEntry(String name, String date, byte[] image) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMAGE,   image);
        cv.put(KEY_NAME,    name);
        cv.put(KEY_DATE,    date);
        //cv.put(KEY_LOCATION,    location);
        database.insert(DB_TABLE, null, cv);
    }

    public ArrayList<Bitmap> getAllPhotos() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE;

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

        try {
            Cursor cursor = database.rawQuery(selectQuery, null);

            if(cursor != null && cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    int index = cursor.getColumnIndexOrThrow("image");
                    byte[] image = cursor.getBlob(index);
                    Bitmap bitmap = BitmapUtility.getImage(image);
                    bitmapArray.add(bitmap);
                    cursor.moveToNext();
                }
                cursor.close();
                return bitmapArray;
            }
            cursor.close();
        }
        catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public ArrayList<Bitmap> getPhotosByDate(String startDate, String endDate) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE +
                " WHERE " + KEY_DATE + " BETWEEN " + "?" + " AND " + "?";
        String[] selectionArgs = new String[]{startDate, endDate};

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

        try {
            Cursor cursor = database.rawQuery(selectQuery, selectionArgs);

            if(cursor != null && cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    int index = cursor.getColumnIndexOrThrow("image");
                    byte[] image = cursor.getBlob(index);
                    Bitmap bitmap = BitmapUtility.getImage(image);
                    bitmapArray.add(bitmap);
                    cursor.moveToNext();
                }
                cursor.close();
                return bitmapArray;
            }
            cursor.close();
        }
        catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
