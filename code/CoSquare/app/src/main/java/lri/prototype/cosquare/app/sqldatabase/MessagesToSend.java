package lri.prototype.cosquare.app.sqldatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by louis on 10/07/2014.
 */
public class MessagesToSend implements BaseColumns {

    private static final String TAG = "MESSAGESTOSEND";

    private static final String COMMA_SEP = ",";
    public static final String TABLE_NAME = "messagestosend";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_MESSAGE = "message";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_TYPE + " TEXT" + COMMA_SEP +
                    COLUMN_TIME + " TEXT" + COMMA_SEP +
                    COLUMN_MESSAGE + " TEXT" +
                    " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    static FeedReaderDbHelper mDbHelper;

    public MessagesToSend(Context context){
        mDbHelper = new FeedReaderDbHelper(context);
    }

    public void add(String type, String time,String message){
        Log.d(TAG, "add");
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_MESSAGE, message);
        db.insert(TABLE_NAME,"null",values);
    }

    public Cursor getAll(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = { _ID,COLUMN_TYPE,COLUMN_TIME,COLUMN_MESSAGE};
        // How you want the results sorted in the resulting Cursor
        String sortOrder = COLUMN_TIME + " DESC";
        Cursor c = db.query(TABLE_NAME,projection,null,null,null,null,sortOrder);
        Log.d(TAG,"Cursor : " + c.toString() + " : " + c.getCount());
        return c;
    }

    static public void suppress(Intent intent){
        //TODO implements
    }
}
