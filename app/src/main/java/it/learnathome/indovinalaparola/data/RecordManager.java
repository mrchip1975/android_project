package it.learnathome.indovinalaparola.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecordManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "indovina_db";
    private static final int DB_VERSION = 1;
    public RecordManager(Context ctx) {
        super(ctx,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
       String query = "CREATE TABLE records(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,word TEXT,attempts INTEGER,date TEXT DEFAULT CURRENT_TIMESTAMP)";
       db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
          db.execSQL("DROP TABLE records");
          onCreate(db);
        }
    }
    public void addRecord(Record recordToSave) {
        SQLiteDatabase db = getWritableDatabase();
        /*
        versione 1
        final String TEMPLATE_INSERT = "INSERT INTO records(name,word,attempts) VALUES('%s','%s',%d)";
        db.execSQL(String.format(TEMPLATE_INSERT,recordToSave.getName(),recordToSave.getWord(),recordToSave.getAttempt()));
        versione 2
        final String TEMPLATE_INSERT = "INSERT INTO records(name,word,attempts) VALUES(?,?,?)";
        db.rawQuery(TEMPLATE_INSERT,new String[]{recordToSave.getName(),recordToSave.getWord(),String.valueOf(recordToSave.getAttempt())});
        */
        ContentValues values = recordToSave.convertToContent();
        db.insert("records",null,values);
        db.close();
    }
    public void delete(int pk) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("records","id=?",new String[]{String.valueOf(pk)});
        db.close();
    }
    public void update(Record recordToUpdate) {
        SQLiteDatabase db = getWritableDatabase();
        db.update("records",recordToUpdate.convertToContent(),"id=?",new String[]{String.valueOf(recordToUpdate.getId())});
        db.close();
    }
}
