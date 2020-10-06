package com.th3md.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.th3md.babyneeds.R;
import com.th3md.babyneeds.model.BabyNeeds;
import com.th3md.babyneeds.util.Config;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Config.DB_NAME, null,Config.DATABASE_V);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+
                Config.TABLE_NAME + " ("+
                Config.KEY_ID + " INTEGER PRIMARY KEY, " +
                Config.KEY_TITLE + " TEXT, "+
                Config.KEY_QTY + " INTEGER, "+
                Config.KEY_COLOR + " TEXT, "+
                Config.KEY_SIZE +" INTEGER,"+
                Config.KEY_DATE+" LONG);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.drop_table);
        db.execSQL(DROP_TABLE, new String[]{Config.DB_NAME});

        onCreate(db);
    }

    public void addNeed(BabyNeeds need){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.KEY_TITLE, need.getTitle());
        values.put(Config.KEY_QTY,need.getQty());
        values.put(Config.KEY_COLOR,need.getColor());
        values.put(Config.KEY_SIZE,need.getSize());
        values.put(Config.KEY_DATE, java.lang.System.currentTimeMillis());

        db.insert(Config.TABLE_NAME,null,values);
        db.close();
    }

    public BabyNeeds getNeed(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                Config.TABLE_NAME,
                new String[]{Config.KEY_ID,Config.KEY_TITLE,Config.KEY_QTY,Config.KEY_COLOR,Config.KEY_SIZE,Config.KEY_DATE},
                Config.KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null,null,null
        );
        BabyNeeds need = new BabyNeeds();
        if(cursor != null){
            cursor.moveToFirst();
            need.setId(cursor.getInt(cursor.getColumnIndex(Config.KEY_ID)));
            need.setTitle(cursor.getString(cursor.getColumnIndex(Config.KEY_TITLE)));
            need.setQty(cursor.getInt(cursor.getColumnIndex(Config.KEY_QTY)));
            need.setColor(cursor.getString(cursor.getColumnIndex(Config.KEY_COLOR)));
            need.setSize(cursor.getInt(cursor.getColumnIndex(Config.KEY_SIZE)));

            DateFormat dateFormat = DateFormat.getDateInstance();
            String dataFormatted = dateFormat.format(
                    new Date(
                            cursor.getLong(cursor.getColumnIndex(Config.KEY_DATE))
                    ).getTime()
            );

            need.setDateAdded(dataFormatted);
            cursor.close();
        }

        return need;
    }

    public List<BabyNeeds> getNeeds(){
        List<BabyNeeds> babyNeeds = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                Config.TABLE_NAME,
                new String[]{Config.KEY_ID,Config.KEY_TITLE,Config.KEY_QTY,Config.KEY_COLOR,Config.KEY_SIZE,Config.KEY_DATE},
                null, null,null,null,
                Config.KEY_DATE + " DESC"
        );


//        String query = "SELECT * FROM " + Config.TABLE_NAME;
//        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            DateFormat dateFormat = DateFormat.getDateInstance();
            do{
                String dataFormatted = dateFormat.format(
                        new Date(
                                cursor.getLong(cursor.getColumnIndex(Config.KEY_DATE))
                        ).getTime()
                );
                BabyNeeds need = new BabyNeeds();
                need.setId(cursor.getInt(cursor.getColumnIndex(Config.KEY_ID)));
                need.setTitle(cursor.getString(cursor.getColumnIndex(Config.KEY_TITLE)));
                need.setQty(cursor.getInt(cursor.getColumnIndex(Config.KEY_QTY)));
                need.setColor(cursor.getString(cursor.getColumnIndex(Config.KEY_COLOR)));
                need.setSize(cursor.getInt(cursor.getColumnIndex(Config.KEY_SIZE)));
                need.setDateAdded(dataFormatted);
                babyNeeds.add(need);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return babyNeeds;
    }

    public int updateNeed(BabyNeeds need){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Config.KEY_TITLE, need.getTitle());
        values.put(Config.KEY_QTY,need.getQty());
        values.put(Config.KEY_COLOR,need.getColor());
        values.put(Config.KEY_SIZE,need.getSize());
        values.put(Config.KEY_DATE, java.lang.System.currentTimeMillis());

        return db.update(Config.TABLE_NAME,
                values,Config.KEY_ID + "=?",
                new String[]{String.valueOf(need.getId())});
    }

    public void deleteNeed(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.delete(Config.TABLE_NAME,Config.KEY_ID + "=?",
                    new String[]{String.valueOf(id)});

        }catch(Exception e){
            Log.d("Delete", "deleteNeed: " + e);
        }
        db.close();
    }

    public int getCount(){
        String query = "SELECT * FROM "+ Config.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}
