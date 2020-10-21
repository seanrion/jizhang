package com.jizhang.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jizhang.model.Income;
import com.jizhang.model.IncomeCat;
import com.jizhang.model.IncomeStatistics;
import com.jizhang.utils.DateUtils;
import com.jizhang.utils.FormatUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class IncomeDao {
    private Dao<Income, Integer> mDao;
    private Context mContext;

    public IncomeDao(Context context) {
        mContext = context;
        mDao = DBOpenHelper.getInstance(context).getDao(Income.class);
    }

    //添加收入信息
    public boolean addIncome(Income income) {
        int row = 0;
        try {
            row = mDao.create(income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    //修改收入信息
    public boolean updateIncome(Income income) {
        int row = 0;
        try {
            row = mDao.update(income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    //删除收入信息
    public boolean deleteIncome(Income income) {
        int row = 0;
        try {
            row = mDao.delete(income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    public boolean deleteIncomebyCat(IncomeCat incomeCat){
        int row = 0;
        try {
            row = mDao.delete(mDao.queryForEq("categoryId",incomeCat));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }
    //查询指定周期的收入总和
    public float getPeriodSumIncome(Date start, Date end, int userId) {
        List<Income> incomes = getPeriodIncomes(start, end, userId);
        float sum = 0;
        if (incomes != null && incomes.size() > 0) {
            for (int i = 0; i < incomes.size(); i++) {
                sum += incomes.get(i).getAmount();
            }
        }
        return sum;
    }

    //按类别统计指定周期的收入情况
    public List<IncomeStatistics> getPeriodCatSumExpense(Date start, Date end, int userId) {
        List<IncomeStatistics> incomeStatisticses = null;
        String sql = "select incomeCat.name, sum(amount) sumCatIncome from income " +
                ", incomeCat where income.categoryId = incomeCat.id and date between ? and ? and " +
                "categoryId in (select distinct(categoryId) from income) and income.userId=? group by categoryId;";
        SQLiteDatabase database = DBOpenHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = database.rawQuery(sql, new String[]{DateUtils.date2Str(start), DateUtils.date2Str(end), String.valueOf(userId)});
        float sumIncome = getPeriodSumIncome(start, end, userId);
        if (cursor.moveToFirst()) {
            incomeStatisticses = new ArrayList<>(cursor.getCount());
            do {
                String categoryName = cursor.getString(0);
                float sumCat = cursor.getFloat(1);
                incomeStatisticses.add(new IncomeStatistics(new IncomeCat(categoryName),
                        sumCat, FormatUtils.formatFloat("#.0", sumCat / sumIncome * 100)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return incomeStatisticses;
    }

    //查询指定周期的所有收入
    public List<Income> getPeriodIncomes(Date start, Date end, int userId) {
        List<Income> incomes = null;
        try {
            incomes = mDao.queryBuilder().where().between("date", start, end).and().eq("userId", userId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incomes;
    }
}

