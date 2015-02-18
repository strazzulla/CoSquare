package lri.prototype.cosquare.app.sqldatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by louis on 19/06/2014.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CoSquare.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ColorTokenReceived.SQL_CREATE_ENTRIES);
        db.execSQL(ColorTokenSent.SQL_CREATE_ENTRIES);
        db.execSQL(LocationTokenReceived.SQL_CREATE_ENTRIES);
        db.execSQL(LocationTokenSent.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ColorTokenSent.SQL_DELETE_ENTRIES);
        db.execSQL(ColorTokenReceived.SQL_DELETE_ENTRIES);
        db.execSQL(LocationTokenReceived.SQL_DELETE_ENTRIES);
        db.execSQL(LocationTokenSent.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}