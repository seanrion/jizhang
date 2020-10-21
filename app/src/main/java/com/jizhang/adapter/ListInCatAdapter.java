package com.jizhang.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jizhang.R;
import com.jizhang.model.IncomeCat;

import java.util.List;

public class ListInCatAdapter extends BaseAdapter {
    private List<IncomeCat> mIncomeCats;
    private Context mContext;


    public ListInCatAdapter(Context context, List<IncomeCat> incomeCats) {
        mContext = context;
        mIncomeCats = incomeCats;

    }

    @Override
    public int getCount() {
        return mIncomeCats != null ? mIncomeCats.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mIncomeCats != null ? mIncomeCats.get(position) : null;
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
        final IncomeCat incomeCat = mIncomeCats.get(position);
        String name = incomeCat.getName();
        viewHolder.tvCategory.setText(name);
        convertView.setClickable(false);
        return convertView;
    }



    public void setData(List<IncomeCat> incomeCats){
        mIncomeCats=incomeCats;
        notifyDataSetChanged();
    }

    static class ViewHolder {

        TextView tvCategory;
    }
}
