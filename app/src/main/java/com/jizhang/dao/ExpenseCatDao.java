package com.jizhang.dao;

import android.content.Context;

import com.jizhang.model.ExpenseCat;
import com.jizhang.model.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ExpenseCatDao {
    private Dao<ExpenseCat, Integer> mDao;

    public ExpenseCatDao(Context context) {
        mDao = DBOpenHelper.getInstance(context).getDao(ExpenseCat.class);
    }

    public List<ExpenseCat> getExpenseCat(int userId) {
        List<ExpenseCat> cats = null;
        try {
            cats = mDao.queryForEq("userId", userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cats;
    }

    public boolean addCategory(ExpenseCat expenseCat) {
        int row = 0;
        try {
            row = mDao.create(expenseCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean delete(ExpenseCat expenseCat) {
        int row = 0;
        try {
            row = mDao.delete(expenseCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean update(ExpenseCat expenseCat) {
        int row = 0;
        try {
            row = mDao.update(expenseCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }
    public void initExpensesCat(User user) {

        String labels[] = {"一般支出","早午晚餐", "烟酒茶饮", "交通出行", "酒水饮料", "水果零食",  "食材购买", "衣服鞋包", "生活用品",
                "护肤彩妆", "房租水电","运动健身", "书报杂志"};
        List<ExpenseCat> cats = new ArrayList<>(labels.length);
        ExpenseCat expenseCat;
        for (int i = 0; i < labels.length; i++) {
            expenseCat = new ExpenseCat();
            expenseCat.setUser(user);
            expenseCat.setName(labels[i]);
            cats.add(expenseCat);
        }
        try {
            for (int i = 0, j = cats.size(); i < j; i++) {
                mDao.create(cats.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}