package com.jizhang.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.jizhang.R;
import com.jizhang.adapter.PagerAdapter;
import com.jizhang.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PieFragment extends BaseFragment {
    @BindView(R.id.chart_tab_strip)
    PagerSlidingTabStrip mChartTabStrip;
    @BindView(R.id.pager_chart)
    ViewPager mPagerChart;
    private PieExpenseFgt mPieExpenseFgt;
    private PieIncomeFgt mPieIncomeFgt;
    private PagerAdapter mPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =super.onCreateView(inflater,container,savedInstanceState);
        List<Fragment> fragments = new ArrayList<>(2);
        mPieExpenseFgt = new PieExpenseFgt();
        mPieIncomeFgt = new PieIncomeFgt();
        fragments.add(mPieIncomeFgt);
        fragments.add(mPieExpenseFgt);
        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        mPagerChart.setAdapter(mPagerAdapter);
        //mChartTabStrip.setViewPager(mPagerChart);
        //mChartTabStrip.setTextSize(ScreenUtils.dp2sp(getActivity(), 16));
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshChart(String message) {
        if (TextUtils.equals(message, "income_inserted") || TextUtils.equals(message, "income_updated")
                || TextUtils.equals(message, "income_deleted")) {
            mPieIncomeFgt.updateData(DateUtils.getMonthStart(), DateUtils.getMonthEnd());
            mPagerAdapter.setRefresh(new boolean[]{true, false});
        }
        if (TextUtils.equals(message, "expense_inserted") || TextUtils.equals(message, "expense_updated")
                || TextUtils.equals(message, "expense_deleted")) {
            mPieExpenseFgt.updateData(DateUtils.getMonthStart(), DateUtils.getMonthEnd());
            mPagerAdapter.setRefresh(new boolean[]{false, true});
        }
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_chart;
    }

}
