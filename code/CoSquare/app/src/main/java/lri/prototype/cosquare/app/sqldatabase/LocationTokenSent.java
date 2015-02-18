package lri.prototype.cosquare.app.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.format.Time;
import android.util.Log;

import lri.prototype.cosquare.app.bff.BandManager;

/**
 * Created by louis on 30/06/2014.
 */
public class LocationTokenSent implements BaseColumns {

    private static final String TAG = "LOCATIONTOKENSENT";

    private static final String COMMA_SEP = ",";
    public static final String TABLE_NAME = "locationtokensent";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TIME + " TEXT" + COMMA_SEP +
                    COLUMN_DISTANCE + " INT" +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    static FeedReaderDbHelper mDbHelper;

    public LocationTokenSent(Context context){
        mDbHelper = new FeedReaderDbHelper(context);
    }

    public void add(String time,int distance){
        Log.d(TAG, "add");
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_DISTANCE, distance);

        db.insert(TABLE_NAME,"null",values);
    }

    public Cursor getAll(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { _ID,COLUMN_TIME,COLUMN_DISTANCE };
        // How you want the results sorted in the resulting Cursor
        String sortOrder = COLUMN_TIME + " DESC";
        Cursor c = db.query(TABLE_NAME,projection,null,null,null,null,sortOrder);
        Log.d(TAG,"Cursor : " + c.toString() + " : " + c.getCount());
        return c;
    }

    public void clearDatabase(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Time now = new Time();
        now.setToNow();
        long nowMillis = now.toMillis(false);
        String selection = COLUMN_TIME + " < (" + nowMillis + "-" + BandManager.DAY + " )";
        String[] selectionArgs = {};
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

}
