package com.jizhang.dao;

import android.content.Context;

import com.jizhang.model.IncomeCat;
import com.jizhang.model.User;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class IncomeCatDao {
    private Dao<IncomeCat, Integer> mDao;

    public IncomeCatDao(Context context) {
        mDao = DBOpenHelper.getInstance(context).getDao(IncomeCat.class);
    }

    //根据编号查询类别
    public List<IncomeCat> getIncomeCat(int userId) {
        List<IncomeCat> cats = null;
        try {
            cats = mDao.queryForEq("userId", userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cats;
    }

    //添加类别
    public boolean addCategory(IncomeCat incomeCat) {
        int row = 0;
        try {
            row = mDao.create(incomeCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    //修改类别
    public boolean update(IncomeCat incomeCat) {
        int row = 0;
        try {
            row = mDao.update(incomeCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    //删除类别
    public boolean delete(IncomeCat incomeCat) {
        int row = 0;
        try {
            row = mDao.delete(incomeCat);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }


    public void initIncomeCat(User user) {
        String labels[] = {"工资收入", "奖金收入","生活费用", "红包收入", "零花收入", "兼职收入", "投资收益", "借入他人",
                 "他人还款", "报销归还",  "退款所得", "其他"};
        List<IncomeCat> cats = new ArrayList<>(labels.length);
        IncomeCat incomeCat;
        for (int i = 0; i < labels.length; i++) {
            incomeCat = new IncomeCat();
            incomeCat.setUser(user);
            incomeCat.setName(labels[i]);
            cats.add(incomeCat);
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

