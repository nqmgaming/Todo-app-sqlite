package com.nqmgaming.assignment_minhnqph31902.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.assignment_minhnqph31902.DTO.TodoDTO;
import com.nqmgaming.assignment_minhnqph31902.DataBase.DataBaseTodoAppHelper;

public class TodoDAO {
    DataBaseTodoAppHelper dataBaseTodoAppHelper;
    SQLiteDatabase sqLiteDatabase;

    public TodoDAO(Context context) {

        dataBaseTodoAppHelper = new DataBaseTodoAppHelper(context);
        sqLiteDatabase = dataBaseTodoAppHelper.getWritableDatabase();

    }

    //Insert new todo return long
    public long insertUser(TodoDTO todoDTO) {

        //create result variable
        long result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("name", todoDTO.getName());
        contentValues.put("content", todoDTO.getContent());
        contentValues.put("status", todoDTO.getStatus());
        contentValues.put("start_date", todoDTO.getStartDate());
        contentValues.put("end_date", todoDTO.getEndDate());
        contentValues.put("user_id", todoDTO.getUserId());

        //try catch
        try {

            //insert data to database
            result = sqLiteDatabase.insertOrThrow("todo_table", null, contentValues);

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Delete todo return int
    public int deleteUser(TodoDTO todoDTO) {

        //create result variable
        int result = -1;

        //try catch
        try {

            //delete data from database
            result = sqLiteDatabase.delete("todo_table", "id=?", new String[]{String.valueOf(todoDTO.getId())});

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }

    //Update todo return int
    public int updateUser(TodoDTO todoDTO) {

        //create result variable
        int result = -1;

        //create content values
        ContentValues contentValues = new ContentValues();

        //put data to content values
        contentValues.put("name", todoDTO.getName());
        contentValues.put("content", todoDTO.getContent());
        contentValues.put("status", todoDTO.getStatus());
        contentValues.put("start_date", todoDTO.getStartDate());
        contentValues.put("end_date", todoDTO.getEndDate());
        contentValues.put("user_id", todoDTO.getUserId());

        //try catch
        try {

            //update data to database
            result = sqLiteDatabase.update("todo_table", contentValues, "id=?", new String[]{String.valueOf(todoDTO.getId())});

        } catch (SQLException e) {

            //print error
            e.printStackTrace();

        }

        //return result
        return result;
    }
}
