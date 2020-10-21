package com.jizhang.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.jizhang.model.Expense;
import com.jizhang.model.ExpenseCat;
import com.jizhang.model.Income;
import com.jizhang.model.IncomeCat;
import com.jizhang.model.User;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class DBOpenHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "JiZhang.db";
    private static final int DB_VERSION = 1;
    private static DBOpenHelper sDBOpenHelper;
    private Map<String, Dao> mDaoMap;

    private DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mDaoMap = new HashMap<>();
    }

    //获取数据库连接，采用单例模式
    public static synchronized DBOpenHelper getInstance(Context context) {
        if (sDBOpenHelper == null) {
            sDBOpenHelper = new DBOpenHelper(context.getApplicationContext());
        }
        return sDBOpenHelper;
    }

    //清空数据表
    public void dropTable() {
        SQLiteDatabase database = sDBOpenHelper.getWritableDatabase();
        database.execSQL("delete from user;");
        database.execSQL("delete from incomeCat;");
        database.execSQL("delete from expenseCat;");
        database.execSQL("delete from income;");
        database.execSQL("delete from expense;");
    }

    //创数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, IncomeCat.class);
            TableUtils.createTable(connectionSource, ExpenseCat.class);
            TableUtils.createTable(connectionSource, Income.class);
            TableUtils.createTable(connectionSource, Expense.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //数据库升级
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            sqLiteDatabase.execSQL("drop table if exists JiZhang;");
            onCreate(sqLiteDatabase, connectionSource);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized Dao getDao(Class clazz) {
        String className = clazz.getSimpleName();
        Dao dao = null;
        if (mDaoMap.containsKey(className)) {
            dao = mDaoMap.get(className);
        } else {
            try {
                dao = super.getDao(clazz);
                mDaoMap.put(className, dao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dao;
    }

    @Override
    public void close() {
        super.close();
        mDaoMap.clear();
    }
}