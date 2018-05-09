package comp7081.photogallery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import comp7081.photogallery.database.models.LocationInfo;
import comp7081.photogallery.database.models.Photo;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseStorageInterface {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "PhotoGallery";

    // Table Names
    private static final String DB_TABLE_PHOTOS = "Photos";
    private static final String DB_TABLE_LOCATION = "Locations";

    // Photos Table Column Names
    private static final String KEY_TABLE_ID = "id";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_CAPTION = "caption";

    // LocationInfo Table Column Names
    private static final String KEY_LOCATION_ID = "id";
    private static final String KEY_PHOTO_NAME = "photo_name";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_POSTAL = "postal_code";


    // Photos Table Create Statement
    private static final String CREATE_TABLE_PHOTOS = "CREATE TABLE " + DB_TABLE_PHOTOS + "("+
            KEY_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_IMAGE + " BLOB," +
            KEY_NAME + " TEXT," +
            KEY_DATE + " TEXT," +
            KEY_CAPTION + " TEXT);";

    // Location Table Create Statement
    private static final String CREATE_TABLE_LOCATIONS = "CREATE TABLE " + DB_TABLE_LOCATION + "("+
            KEY_LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            KEY_PHOTO_NAME + " TEXT," +
            KEY_LATITUDE + " TEXT," +
            KEY_LONGITUDE + " TEXT," +
            KEY_ADDRESS + " TEXT," +
            KEY_CITY + " TEXT," +
            KEY_STATE + " TEXT," +
            KEY_COUNTRY + " TEXT," +
            KEY_POSTAL + " TEXT, " +
            "FOREIGN KEY " + "(" + KEY_PHOTO_NAME + ") REFERENCES " + DB_TABLE_PHOTOS + "(" + KEY_NAME + "))";

    private static final String PRAGMA_FOREIGN_KEY = "PRAGMA foreign_keys = ON;";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Required Tables
        db.execSQL(CREATE_TABLE_PHOTOS);
        db.execSQL(CREATE_TABLE_LOCATIONS);
        db.execSQL(PRAGMA_FOREIGN_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // On Upgrade Drop Older Tables
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_LOCATION);

        // Create New Table
        onCreate(db);
    }

    public void addPhotoEntry(Photo photo) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMAGE, photo.getImage());
        cv.put(KEY_NAME, photo.getName());
        cv.put(KEY_DATE, photo.getDate());
        cv.put(KEY_CAPTION, photo.getCaption());

        database.insert(DB_TABLE_PHOTOS, null, cv);
        database.close();
    }

    public ArrayList<Bitmap> getAllPhotos() {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE_PHOTOS;

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

        try {
            Cursor cursor = database.rawQuery(selectQuery, null);

            if(cursor != null && cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    int index = cursor.getColumnIndexOrThrow("image");
                    byte[] image = cursor.getBlob(index);
                    Bitmap bitmap = BitmapUtility.getImageFromBitmap(image);
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
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE_PHOTOS +
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
                    Bitmap bitmap = BitmapUtility.getImageFromBitmap(image);
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
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE_PHOTOS +
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
                    Bitmap bitmap = BitmapUtility.getImageFromBitmap(image);
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

        // id + 1 because of bitmapArrayList.size() - 1.
        id = id + 1;

        database.update(DB_TABLE_PHOTOS, cv, KEY_TABLE_ID + " = ?", new String[] {String.valueOf(id)});
        database.close();
    }

    public void addLocationForPhoto(Photo photo, LocationInfo locationInfo) throws SQLiteException {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PHOTO_NAME, photo.getName());
        cv.put(KEY_LATITUDE, locationInfo.getLatitude());
        cv.put(KEY_LONGITUDE, locationInfo.getLongitude());
        cv.put(KEY_ADDRESS, locationInfo.getAddress());
        cv.put(KEY_CITY, locationInfo.getCity());
        cv.put(KEY_STATE, locationInfo.getState());
        cv.put(KEY_COUNTRY, locationInfo.getCountry());
        cv.put(KEY_POSTAL, locationInfo.getPostalCode());

        database.insert(DB_TABLE_LOCATION, null, cv);
        database.close();
    }

    public ArrayList<Bitmap> getPhotosByLatLong(String latitude, String longitude) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery =  "SELECT " + KEY_IMAGE + " FROM " + DB_TABLE_PHOTOS +
                " JOIN " + DB_TABLE_LOCATION + " ON " + DB_TABLE_PHOTOS + "." + KEY_NAME +
                " = " + DB_TABLE_LOCATION + "." + KEY_PHOTO_NAME +
                " WHERE " + KEY_LATITUDE + "= ?" + " AND " + KEY_LONGITUDE + " = ?";
        String[] selectionArgs = new String[] {latitude, longitude};

        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

        try {
            Cursor cursor = database.rawQuery(selectQuery, selectionArgs);

            if(cursor != null && cursor.moveToFirst())
            {
                while(!cursor.isAfterLast())
                {
                    int index = cursor.getColumnIndexOrThrow("image");
                    byte[] image = cursor.getBlob(index);
                    Bitmap bitmap = BitmapUtility.getImageFromBitmap(image);
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
}
