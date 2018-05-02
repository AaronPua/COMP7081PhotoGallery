package comp7081.photogallery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.text.TextUtils;

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
    private static final String KEY_CAPTION = "caption";
    private static final String KEY_LOCATION = "location";


    // Table create statement
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + DB_TABLE + "("+
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_IMAGE + " BLOB," +
            KEY_NAME + " TEXT," +
            KEY_DATE + " TEXT," +
            KEY_CAPTION + " TEXT," +
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

    public void addPhotoEntry(byte[] image, String name, String date, String caption, String location) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMAGE,   image);
        cv.put(KEY_NAME,    name);
        cv.put(KEY_DATE,    date);

        if(!TextUtils.isEmpty(caption))
            cv.put(KEY_CAPTION, caption);

        if(!TextUtils.isEmpty(location))
            cv.put(KEY_LOCATION, location);

        database.insert(DB_TABLE, null, cv);
        database.close();
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
        database.close();
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
        database.close();
        return null;
    }

    public ArrayList<Bitmap> getPhotosByCaption(String caption) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE +
                " WHERE " + KEY_CAPTION + "= ?";
        String[] selectionArgs = new String[]{caption};

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
        database.close();
        return null;
    }

    public void updateCaption(int id, String caption) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(KEY_CAPTION, caption);

        // + 1 for the id because currentPhotoIndex is always bitmapArrayList.size() -1.
        id = id + 1;

        database.update(DB_TABLE, cv, KEY_ID + " = ?", new String[] {String.valueOf(id)});
        database.close();
    }

    public String getCaption(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery =  "SELECT " + KEY_CAPTION + " FROM " + DB_TABLE +
                " WHERE " + KEY_ID + "= ?";

        // + 1 for the id because currentPhotoIndex is always bitmapArrayList.size() - 1.
        id = id + 1;

        String[] selectionArgs = new String[]{String.valueOf(id)};

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

        try {
            Cursor cursor = database.rawQuery(selectQuery, selectionArgs);

            if(cursor != null && cursor.moveToFirst())
            {
                int index = cursor.getColumnIndexOrThrow("caption");
                String caption = cursor.getString(index);
                cursor.close();
                return caption;
            }
            cursor.close();
        }
        catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        database.close();
        return null;
    }
}
