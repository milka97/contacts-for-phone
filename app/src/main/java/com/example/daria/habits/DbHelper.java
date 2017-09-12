package com.example.daria.habits;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daria on 7/16/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TBL_NAME = "names";
    public static final String TBL_EMAILS = "emails";
    public static final String TBL_TEL = "telephones";
    public static final String ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_TEL = "telephone";
    public static final String NAME_ID = "name_id";
    public static final String TYPE_ID = "type_id";

    private static final String DATABASE_CREATE1 = //
            "create table " + TBL_NAME + "( " //
                    + ID + " integer primary key autoincrement, " //
                    + COL_NAME + " text not null);"; //

    private static final String DATABASE_CREATE2 = //
            "create table " + TBL_EMAILS + "( " //
                    + ID + " integer primary key autoincrement, " //
                    + COL_EMAIL + " text not null, " //
                    + NAME_ID + " integer, " //
                    + TYPE_ID + " integer);"; //

    private static final String DATABASE_CREATE3 = //
            "create table " + TBL_TEL + "( " //
                    + ID + " integer primary key autoincrement, " //
                    + COL_TEL + " text not null, " //
                    + NAME_ID + " integer, " //
                    + TYPE_ID + " integer);"; //


    Context context;

    public DbHelper(Context cntx) {
        super(cntx, DATABASE_NAME, null, DATABASE_VERSION);
        context = cntx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE1);
        db.execSQL(DATABASE_CREATE2);
        db.execSQL(DATABASE_CREATE3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TBL_EMAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_TEL);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
        onCreate(db);

    }
}
