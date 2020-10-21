package com.jizhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jizhang.R;
import com.jizhang.model.Expense;
import com.jizhang.utils.DateUtils;

import java.util.List;


public class ExpenseSwipeAdapter extends BaseAdapter {
    private Context mContext;
    private List<Expense> mExpenses;
    private boolean mToday;
    private int mDay;

    public ExpenseSwipeAdapter(Context context, List<Expense> expenses, boolean today) {
        mContext = context;
        mExpenses = expenses;
        mToday = today;
        if (mExpenses != null && mExpenses.size() > 0) {
            mDay = Integer.parseInt(DateUtils.date2Str(mExpenses.get(0).getDate(), "dd"));
        }
    }

    @Override
    public int getCount() {
        return mExpenses == null ? 0 : mExpenses.size();
    }

    @Override
    public Object getItem(int position) {
        return mExpenses == null ? null : mExpenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_show_expense, parent, false);
            viewHolder.categoryLabel = (TextView) convertView.findViewById(R.id.label_item_expense_category);
            viewHolder.amountLabel = (TextView) convertView.findViewById(R.id.label_item_expense_account);

            viewHolder.dateLabel = (TextView) convertView.findViewById(R.id.label_item_expense_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Expense expense = mExpenses.get(mExpenses.size()-1-position);
        String date = DateUtils.date2Str(expense.getDate(), "MM/dd");
        int day = Integer.parseInt(date.substring(date.indexOf("/") + 1));
        if ((!mToday && mDay != day) || position == 0) {
            viewHolder.dateLabel.setVisibility(View.VISIBLE);
            viewHolder.dateLabel.setText(date);
            mDay = day;
        }
        viewHolder.categoryLabel.setText(expense.getCategory().getName());
        viewHolder.amountLabel.setText(String.valueOf(expense.getAmount()));
        return convertView;
    }

    public void setData(List<Expense> expenses) {
        mExpenses = expenses;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView categoryLabel;
        TextView amountLabel;

        TextView dateLabel;
    }
}

