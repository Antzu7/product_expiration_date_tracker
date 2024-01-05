package com.example.product_expiration_date_tracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "products";
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String EXPIRATION_DATE = "expirationDate";
    private static String DB_NAME = "products.db";
    private static String DB_LOCATION;
    private static final  int DB_VERSION = 2;
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + EXPIRATION_DATE + " TEXT NOT NULL);";
    //private final Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //this.myContext = context;
        //DB_LOCATION = context.getApplicationInfo().dataDir+"/databases/";

        //copyDB();
    }

//    private boolean checkDB() {
//        File fileDB = new File(DB_LOCATION+DB_NAME);
//        return fileDB.exists();
//    }
//    private void copyDB() {
//
//        if(checkDB()) {
//            this.getReadableDatabase();
//            try {
//                copyDBFile();
//            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    private void copyDBFile() throws IOException {
//        InputStream inputStream = myContext.getAssets().open(DB_NAME);
//        OutputStream outputStream = new FileOutputStream(DB_LOCATION+DB_NAME);
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = inputStream.read(buffer)) > 0) {
//            outputStream.write(buffer, 0, length);
//        }
//        outputStream.flush();
//        outputStream.close();
//        inputStream.close();
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
