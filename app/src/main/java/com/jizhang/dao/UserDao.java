package com.jizhang.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jizhang.model.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDao {
    private Dao<User, Integer> mDao;
    private final DBOpenHelper mDbOpenHelper;


    public UserDao(Context context) {
        mDbOpenHelper = DBOpenHelper.getInstance(context);
        mDao = mDbOpenHelper.getDao(User.class);
    }

    public ArrayList<User> getAlluser() {
        ArrayList<User> users = new ArrayList<>();
        try {
            users.addAll(mDao.queryForAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    //获取当前用户
    public User getCurrentUser(String username) {
        List<User> users = null;
        try {
            users = mDao.queryForEq("name", username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users != null && users.size() > 0 ? users.get(0) : null;
    }

    //新用户登录
    public void addUser(User user) {
        try {
            mDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //修改口令密码
    public void updatePassfirst(String passfirst, int id) {
        SQLiteDatabase database = mDbOpenHelper.getWritableDatabase();
        String sql = "update user set passwordfirst=? where id=?;";
        database.execSQL(sql, new Object[]{passfirst, id});
    }

    //修改手势密码
    public void updatePasssecond(String passsecond, int id) {
        SQLiteDatabase database = mDbOpenHelper.getWritableDatabase();
        String sql = "update user set passwordsecond=? where id=?;";
        database.execSQL(sql, new Object[]{passsecond, id});
    }

    //修改用户名
    public void updateName(String name, int id) {
        SQLiteDatabase database = mDbOpenHelper.getWritableDatabase();
        String sql = "update user set name=? where id=?;";
        database.execSQL(sql, new Object[]{name, id});
    }

}
