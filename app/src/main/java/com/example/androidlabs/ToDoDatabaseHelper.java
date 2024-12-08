package com.example.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDatabaseHelper extends SQLiteOpenHelper {

    // Database constants
    public static final String DATABASE_NAME = "ToDoDB";
    public static final int DATABASE_VERSION = 1;

    // Table and columns
    public static final String TABLE_NAME = "todos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_URGENT = "urgent";

    // SQL statements
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TEXT + " TEXT, "
            + COLUMN_URGENT + " INTEGER)";

    public ToDoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
