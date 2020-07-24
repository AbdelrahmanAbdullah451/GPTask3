package com.example.gptask3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private boolean callOnce = false;

    public static final String POINTS_TABLE = "POINTS_TABLE";
    public static final String X_POINTS_COLUMN = "X_POINTS";
    public static final String Y_POINTS_COLUMN = "Y_POINTS";
    public static final String Z_POINTS_COLUMN = "Z_POINTS";
    public static final String STREAM_FLAG_COLUMN = "STREAM_FLAG";
    public static final String ID_COLUMN = "ID";
    public static final String HAS_ARRIVED_TABLE = "HAS_ARRIVED";
    public static final String COLUMN_ARRIVAL_ID = "ARRIVAL_ID";
    public static final String COLUMN_ARRIVAL_FLAG = "ARRIVAL_FLAG";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "FinalAccDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      String firstTable = "CREATE TABLE " + POINTS_TABLE + " ( " + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT , " + X_POINTS_COLUMN + " REAL , " + Y_POINTS_COLUMN + " REAL , " + Z_POINTS_COLUMN + " REAL , " + STREAM_FLAG_COLUMN + " BOOL ) ";
      sqLiteDatabase.execSQL(firstTable);
      String flagTable = "CREATE TABLE " + HAS_ARRIVED_TABLE + "(" + COLUMN_ARRIVAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + COLUMN_ARRIVAL_FLAG + " BOOL )";
      sqLiteDatabase.execSQL(flagTable);
      String initFlag = "INSERT INTO " + HAS_ARRIVED_TABLE + "(ARRIVAL_FLAG) VALUES (0)";
      sqLiteDatabase.execSQL(initFlag);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HAS_ARRIVED_TABLE);
        onCreate(sqLiteDatabase);

    }

    public boolean addRecord(Point point){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(X_POINTS_COLUMN,point.getX());
        cv.put(Y_POINTS_COLUMN,point.getY());
        cv.put(Z_POINTS_COLUMN,point.getZ());
        cv.put(STREAM_FLAG_COLUMN,point.isStreamFlag());

        long insert= db.insert(POINTS_TABLE,null,cv);
        if (insert == -1){
            return false;
        }
        else
        {
            return true;
        }

    }

    public boolean streamStandBy(){

        boolean dummyTemp ;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ARRIVAL_FLAG,0);

        long update = db.update(HAS_ARRIVED_TABLE,contentValues,"ARRIVAL_ID = 1" , null);

        if (update == 1){
            return true;
        }
        else{
            return false;
        }

    }

    public boolean steamArrived(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ARRIVAL_FLAG,1);
        long arrived = db.update(HAS_ARRIVED_TABLE,contentValues,"ARRIVAL_ID = 1" , null);
        if (arrived == 1){
            return true;
        }
        else{
            return false;
        }
    }


}
