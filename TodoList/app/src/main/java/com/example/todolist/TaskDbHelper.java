package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.todolist.Tasks.DATABASE_NAME;

public class TaskDbHelper extends SQLiteOpenHelper {

    public TaskDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, Tasks.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable=" CREATE TABLE " + Tasks.TaskEntry.TABLE_NAME + " ( " + Tasks.TaskEntry._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT , " + Tasks.TaskEntry.COL_TITLE  +  " TEXT NOT NULL );";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tasks.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
