package com.example.daria.habits;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Daria on 7/16/2017.
 */

public class SQLController {
    private DbHelper dbhelper;
    private Context context;
    private SQLiteDatabase database;

    public SQLController(Context c) {
        context = c;
    }

    public SQLController open() throws SQLException {
        dbhelper = new DbHelper(context);
        database = dbhelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbhelper.close();
    }

    public long insertName(String name) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_NAME, name);
        long rowId = database.insert(DbHelper.TBL_NAME, null, cv);
        return rowId;
    }

    public long insertEmail(String email, long name_id, long type_id) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_EMAIL, email);
        cv.put(DbHelper.NAME_ID, name_id);
        cv.put(DbHelper.TYPE_ID, type_id);
        long rowId = database.insert(DbHelper.TBL_EMAILS, null, cv);
        return rowId;
    }

    public long insertTel(String tel, long name_id, long type_id) {
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_TEL, tel);
        cv.put(DbHelper.NAME_ID, name_id);
        cv.put(DbHelper.TYPE_ID, type_id);
        long rowId = database.insert(DbHelper.TBL_TEL, null, cv);
        return rowId;
    }

    public Cursor readNames() {
        Cursor c = database.query(DbHelper.TBL_NAME, null,
                null, null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public Cursor readName(String name) {
        Cursor c = database.query(DbHelper.TBL_NAME, null,
                DbHelper.COL_NAME + " =?", new String[]{name}, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }


    public Cursor readEmails(int name_id) {
        Cursor c = database.query(DbHelper.TBL_EMAILS, null, DbHelper.NAME_ID + " = " + name_id, null, null, null, null);
        return c;
    }

    public Cursor readTels(int name_id) {
        Cursor c = database.query(DbHelper.TBL_TEL, null, DbHelper.NAME_ID + " = " + name_id, null, null, null, null);
        return c;
    }

    public Cursor readEmail(int id) {
        Cursor c = database.query(DbHelper.TBL_EMAILS, null,
                DbHelper.ID + " = " + id, null, null, null, null);
        if (c != null) c.moveToFirst();
        return c;
    }

    public int updateName(long id, String name) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DbHelper.COL_NAME, name);
        int i = database.update(DbHelper.TBL_NAME, cvUpdate,
                DbHelper.ID + " = " + id, null);
        return i;
    }

    public int updateEmail(long id, String email, long nameId, long type_id) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DbHelper.COL_EMAIL, email);
        cvUpdate.put(DbHelper.NAME_ID, nameId);
        cvUpdate.put(DbHelper.TYPE_ID, type_id);
        int i = database.update(DbHelper.TBL_EMAILS, cvUpdate,
                DbHelper.ID + " = " + id, null);
        return i;
    }

    public int updateTel(long id, String tel, long nameId, long type_id) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DbHelper.COL_TEL, tel);
        cvUpdate.put(DbHelper.NAME_ID, nameId);
        cvUpdate.put(DbHelper.TYPE_ID, type_id);
        int i = database.update(DbHelper.TBL_TEL, cvUpdate,
                DbHelper.ID + " = " + id, null);
        return i;
    }

    public void deleteName(long id) {
        database.delete(DbHelper.TBL_EMAILS, DbHelper.NAME_ID + "=" + id, null);
        database.delete(DbHelper.TBL_NAME, DbHelper.ID + "=" + id, null);
    }

    public void deleteEmail(long id) {
        database.delete(DbHelper.TBL_EMAILS, DbHelper.ID + "=" + id, null);
    }

    public void deleteTel(long id) {
        database.delete(DbHelper.TBL_TEL, DbHelper.ID + "=" + id, null);
    }

    public void deleteEveryEmailWithNameId(int id) {
        Cursor cur = readEmails(id);
        if (cur.moveToFirst()) {
            do {
                deleteEmail(cur.getInt(cur.getColumnIndex(DbHelper.ID)));
            } while (cur.moveToNext());
        }
    }

    public void deleteEveryTelWithNameId(int id) {
        Cursor cur = readTels(id);
        if (cur.moveToFirst()) {
            do {
                deleteTel(cur.getInt(cur.getColumnIndex(DbHelper.ID)));
            } while (cur.moveToNext());
        }
    }


}
