package com.jizhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jizhang.R;
import com.jizhang.model.ExpenseCat;

import java.util.List;


public class ListExpCatAdapter extends BaseAdapter {
    private List<ExpenseCat> mExpenseCatList;
    private Context mContext;


    public ListExpCatAdapter(Context context, List<ExpenseCat> expenseCats) {
        mContext = context;
        mExpenseCatList = expenseCats;

    }

    @Override
    public int getCount() {
        return mExpenseCatList != null ? mExpenseCatList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mExpenseCatList != null ? mExpenseCatList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_exlist_item, parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tvCategory = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ExpenseCat expenseCat = mExpenseCatList.get(position);
        String name = expenseCat.getName();
        viewHolder.tvCategory.setText(name);
        convertView.setClickable(false);
        return convertView;
    }


    public void setData(List<ExpenseCat> expenseCats) {
        mExpenseCatList = expenseCats;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvCategory;
    }
}
