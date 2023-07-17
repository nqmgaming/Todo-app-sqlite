package com.nqmgaming.assignment_minhnqph31902.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseTodoAppHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "todoapp.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_USER = "user_table";
    public static final String TABLE_TODO = "todo_table";

    public DataBaseTodoAppHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String USER_TABLE = "CREATE TABLE " + TABLE_USER + " (id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL,\n" +
                "  username TEXT NOT NULL,\n" +
                "  email TEXT NOT NULL,\n" +
                "  password TEXT NOT NULL,\n" +
                "  firstname TEXT NOT NULL,\n" +
                "  lastname TEXT NOT NULL)";
        db.execSQL(USER_TABLE);

        String TODO_TABLE = "CREATE TABLE " + TABLE_TODO + " ( id INTEGER PRIMARY KEY,\n" +
                "  name TEXT NOT NULL,\n" +
                "  content TEXT NOT NULL,\n" +
                "  status TEXT NOT NULL,\n" +
                "  start_date TEXT NOT NULL,\n" +
                "  end_date TEXT NOT NULL,\n" +
                "  user_id INTEGER NOT NULL,\n" +
                "  FOREIGN KEY (user_id) REFERENCES user (id))";
        db.execSQL(TODO_TABLE);
    }

        @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
