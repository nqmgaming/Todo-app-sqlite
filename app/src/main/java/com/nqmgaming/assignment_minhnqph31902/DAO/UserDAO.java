package com.nqmgaming.assignment_minhnqph31902.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nqmgaming.assignment_minhnqph31902.DTO.UserDTO;
import com.nqmgaming.assignment_minhnqph31902.DataBase.DataBaseTodoAppHelper;

public class UserDAO {

    DataBaseTodoAppHelper dataBaseTodoAppHelper;
    SQLiteDatabase sqLiteDatabase;

    public UserDAO(Context context) {
        dataBaseTodoAppHelper = new DataBaseTodoAppHelper(context);
        sqLiteDatabase = dataBaseTodoAppHelper.getWritableDatabase();
    }

    //Insert new user return long
    public long insertUser(UserDTO userDTO) {
        long result = -1;

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", userDTO.getUsername());
        contentValues.put("email", userDTO.getEmail());
        contentValues.put("password", userDTO.getPassword());
        contentValues.put("firstname", userDTO.getFirstname());
        contentValues.put("lastname", userDTO.getLastname());

        try {
            result = sqLiteDatabase.insertOrThrow("user_table", null, contentValues);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Delete user return int
    public int deleteUser(UserDTO userDTO) {
        int result = -1;
        String[] condition = new String[]{
                String.valueOf(userDTO.getId())
        };

        try {
            result = sqLiteDatabase.delete("user_table", "id = ?", condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    //Update user return int
    public int updateUser(UserDTO userDTO) {
        int result = -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", userDTO.getUsername());
        contentValues.put("email", userDTO.getEmail());
        contentValues.put("password", userDTO.getPassword());
        contentValues.put("firstname", userDTO.getFirstname());
        contentValues.put("lastname", userDTO.getLastname());

        String[] condition = new String[]{String.valueOf(userDTO.getId())};

        try {
            result = sqLiteDatabase.update("user_table", contentValues, "id = ?", condition);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Check user exist return boolean
    public boolean checkUserExist(String username) {
        boolean result = false;
        String[] condition = new String[]{
                username
        };

        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE username = ?", condition);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Check email exist return boolean
    public boolean checkEmailExist(String email) {
        boolean result = false;
        String[] condition = new String[]{
                email
        };

        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE email = ?", condition);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //check account match return boolean
    public boolean checkAccount(String email, String password){
        boolean result = false;
        String[] condition = new String[]{
                email, password
        };
      try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE email = ? AND password = ?", condition);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
      }catch (SQLException e){
          e.printStackTrace();
      }
        return result;
    }

    //check username and email match return boolean
    public boolean checkUsernameAndEmail(String username, String email){
        boolean result = false;
        String[] condition = new String[]{
                username, email
        };
      try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE username = ? AND email = ?", condition);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
      }catch (SQLException e){
          e.printStackTrace();
      }
        return result;
    }

    //check username and email after return UserDTO
    public UserDTO checkUsernameAndEmailAfter(String username, String email){
        UserDTO userDTO = null;
        String[] condition = new String[]{
                username, email
        };
      try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE username = ? AND email = ?", condition);
            if (cursor.moveToFirst()) {
                userDTO = new UserDTO();
                userDTO.setId(cursor.getInt(0));
                userDTO.setUsername(cursor.getString(1));
                userDTO.setEmail(cursor.getString(2));
                userDTO.setPassword(cursor.getString(3));
                userDTO.setFirstname(cursor.getString(4));
                userDTO.setLastname(cursor.getString(5));
            }
            cursor.close();
      }catch (SQLException e){
          e.printStackTrace();
      }
        return userDTO;
    }

    //check if password match return boolean
    public boolean checkPassword(String password){
        boolean result = false;
        String[] condition = new String[]{
                password
        };
      try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE password = ?", condition);
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
      }catch (SQLException e){
          e.printStackTrace();
      }
        return result;
    }

    //Get id by username and email return int
    public int getIdByUsernameAndEmail(String username, String email) {
        int result = -1;
        String[] condition = new String[]{username, email};
        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM user_table WHERE username = ? AND email = ?", condition);
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
            cursor.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}
