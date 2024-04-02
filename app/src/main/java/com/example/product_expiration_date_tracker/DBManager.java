package com.example.product_expiration_date_tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {

    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String expirationDate) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DBHelper.NAME, name);
        contentValue.put(DBHelper.EXPIRATION_DATE, expirationDate);
        database.insert(DBHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        //String[] columns = new String[] { DBHelper._ID, DBHelper.NAME, DBHelper.EXPIRATION_DATE, DBHelper.INTERVAL };
        //Cursor cursor = database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.EXPIRATION_DATE);
        String query = "SELECT _id, name, expirationDate, round(julianday(expirationDate) - julianday('now')) AS interval FROM " +  DBHelper.TABLE_NAME + " ORDER BY interval";
        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String expirationDate) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.NAME, name);
        contentValues.put(DBHelper.EXPIRATION_DATE, expirationDate);
        int i = database.update(DBHelper.TABLE_NAME, contentValues, DBHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DBHelper.TABLE_NAME, DBHelper._ID + " = " + _id, null);
    }

    public Cursor getExpired() {
        String query = "SELECT _id, name FROM " +  DBHelper.TABLE_NAME + " WHERE date() = expirationDate";
        Cursor cursor = database.rawQuery(query, null);

        return cursor;
    }

}
