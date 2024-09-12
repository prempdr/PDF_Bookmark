package app.prempdr.pdfbookmark.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class favoriteDatabase extends SQLiteOpenHelper {

    public static final String FAVORITE_STATUS = "favoriteStatus";
    private static final String DB_NAME = "favoriteDatabase";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String ITEM_NAME = "name";
    public static String ITEM_DESC = "sub_title";
    public static String ITEM_URL = "url";
    public static String ITEM_IMAGE = "image";
    // space
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " TEXT, " + ITEM_NAME + " TEXT, " + ITEM_DESC + " TEXT, " + ITEM_URL + " TEXT, " + ITEM_IMAGE + " TEXT, " + FAVORITE_STATUS + " TEXT)";

    public favoriteDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertEmpty() {
        SQLiteDatabase writable = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 1; i < 11; i++) {
            contentValues.put(KEY_ID, i);
            contentValues.put(FAVORITE_STATUS, "0");
            writable.insert(TABLE_NAME, null, contentValues);
        }
    }

    public void insertIntoDatabase(String id, String name, String desc, String url, int image, String fav_status) {
        SQLiteDatabase writable = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(ITEM_NAME, name);
        contentValues.put(ITEM_DESC, desc);
        contentValues.put(ITEM_URL, url);
        contentValues.put(ITEM_IMAGE, image);
        contentValues.put(FAVORITE_STATUS, fav_status);
        writable.insert(TABLE_NAME, null, contentValues);
        Log.d("FavoriteStatus", name + "fav_status: -" + fav_status + "- ," + contentValues);
    }

    public Cursor readAllData(String id) {
        SQLiteDatabase readable = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + id;
        return readable.rawQuery(query, null, null);
    }

    public void removeFavorite(String id) {
        SQLiteDatabase writable = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + FAVORITE_STATUS + " = '0' WHERE " + KEY_ID + " = " + id;
        writable.execSQL(query);
        Log.d("FavoriteStatus", "removeFavorite: " + id);
    }

    public Cursor selectAllFavoriteList() {
        SQLiteDatabase readable = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + FAVORITE_STATUS + " = '1'";
        return readable.rawQuery(query, null, null);
    }
}
