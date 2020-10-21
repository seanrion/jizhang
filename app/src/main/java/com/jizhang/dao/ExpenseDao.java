package com.jizhang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jizhang.model.Expense;
import com.jizhang.model.ExpenseCat;
import com.jizhang.model.ExpenseStatistics;
import com.jizhang.model.Income;
import com.jizhang.utils.DateUtils;
import com.jizhang.utils.FormatUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExpenseDao {
    private Dao<Expense, Income> mDao;
    private Context mContext;

    public ExpenseDao(Context context) {
        mContext = context;
        mDao = DBOpenHelper.getInstance(context).getDao(Expense.class);
    }

    public boolean addExpense(Expense expense) {
        int row = 0;
        try {
            row = mDao.create(expense);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public float getPeriodSumExpense(Date start, Date end, int userId) {
        List<Expense> expenses = getPeriodExpense(start, end, userId);
        float sum = 0;
        if (expenses != null && expenses.size() > 0) {
            for (int i = 0; i < expenses.size(); i++) {
                sum += expenses.get(i).getAmount();
            }
        }
        return sum;
    }

    public boolean deleteExpense(Expense expense) {
        int row = 0;
        try {
            row = mDao.delete(expense);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean deleteExpensebyCat(ExpenseCat expenseCat){
        int row = 0;
        try {
            row = mDao.delete(mDao.queryForEq("categoryId",expenseCat));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean updateExpense(Expense expense) {
        int row = 0;
        try {
            row = mDao.update(expense);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public List<ExpenseStatistics> getPeriodCatSumExpense(Date start, Date end, int userId) {
        List<ExpenseStatistics> expenseStatisticses = null;
        String sql = "select expenseCat.name, sum(amount) sumCatExpense from expense " +
                ", expenseCat where expense.categoryId = expenseCat.id and date between ? and ? and " +
                "categoryId in (select distinct(categoryId) from expense) and expense.userId=? group by categoryId;";
        SQLiteDatabase database = DBOpenHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, new String[]{DateUtils.date2Str(start), DateUtils.date2Str(end), String.valueOf(userId)});
        float sumExpense = getPeriodSumExpense(start, end, userId);
        if (cursor.moveToFirst()) {
            expenseStatisticses = new ArrayList<>(cursor.getCount());
            do {
                String categoryName = cursor.getString(0);
                float sumCat = cursor.getFloat(1);
                expenseStatisticses.add(new ExpenseStatistics(new ExpenseCat(categoryName),
                        sumCat, FormatUtils.formatFloat("#.0", sumCat / sumExpense * 100)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return expenseStatisticses;
    }

    public List<Expense> getPeriodExpense(Date start, Date end, int userId) {
        List<Expense> expenses = null;
        try {
            expenses = mDao.queryBuilder().where().between("date", start, end).and().eq("userId", userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expenses;
    }
}

