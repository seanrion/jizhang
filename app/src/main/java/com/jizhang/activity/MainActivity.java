package com.jizhang.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jizhang.R;
import com.jizhang.fragment.DetailFragment;
import com.jizhang.fragment.LineFragment;
import com.jizhang.fragment.MineFragment;
import com.jizhang.fragment.PieFragment;
import com.jizhang.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener{
    @BindView(R.id.radio_gp_tab)
    RadioGroup mRadioGpTab;
    private long exitTime;
    private FragmentManager mFragmentManager;
    private DetailFragment mDetailFragment;
    private MineFragment mMineFragment;
    private PieFragment mPieFragment;
    private LineFragment mLineFragment;
    private int mChartType;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        mRadioGpTab.setOnCheckedChangeListener(this);

        mFragmentManager = getSupportFragmentManager();
        ((RadioButton) (mRadioGpTab.getChildAt(0))).setChecked(true);
        mChartType = R.id.radio_pop_pie;

    }

    private void switchFragment(Fragment newFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!newFragment.isAdded()) {
            transaction.add(R.id.main_fragment, newFragment);
        }
        if (mDetailFragment != null && mDetailFragment.isVisible()) {
            transaction.hide(mDetailFragment);
        }
        if (mMineFragment != null && mMineFragment.isVisible()) {
            transaction.hide(mMineFragment);
        }
        if (mPieFragment != null && mPieFragment.isVisible()) {
            transaction.hide(mPieFragment);
        }
        if (mLineFragment != null && mLineFragment.isVisible()) {
            transaction.hide(mLineFragment);
        }
        transaction.show(newFragment);
        transaction.commit();
    }

    private void switchTitleBar(int title, boolean showBackward, boolean showForward, int forwardResId) {
        setTitle(title);
        if (showBackward) {
            showBackwardView(true);
        } else {
            showBackwardView(false);
        }
        if (showForward) {
            if (forwardResId == 0) {
                showForwardView(true);
            } else {
                showForwardView(true, forwardResId);
            }
        } else {
            showForwardView(false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.radio_gp_tab) {
            switch (checkedId) {
                case R.id.radio_btn_detail:
                    if (mDetailFragment == null) {
                        mDetailFragment = new DetailFragment();
                    }
                    switchTitleBar(R.string.tab_detail, false, false, 0);
                    switchFragment(mDetailFragment);
                    break;
                case R.id.radio_btn_chart:
                    switchChart(mChartType);
                    switchTitleBar(R.string.tab_chart, false, true, R.mipmap.ic_expand_more);
                    break;
                case R.id.radio_btn_me:
                    if (mMineFragment == null) {
                        mMineFragment = new MineFragment();
                    }
                    switchTitleBar(R.string.tab_mine, false, false, 0);
                    switchFragment(mMineFragment);
                    break;
            }
        } else {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            mChartType = checkedId;
            switchChart(checkedId);
        }
    }

    @Override
    protected void onForward() {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(mRadioGpTab, Gravity.BOTTOM, 0, 0);
            showForwardView(true, R.mipmap.ic_arrow_up);
        }
    }

    @Override
    protected Activity getSubActivity() {
        return this;
    }

    private void initPopWindow() {
        View popView = LayoutInflater.from(this).inflate(R.layout.pop_chart_type, null);
        RadioGroup radioGroup = (RadioGroup) popView.findViewById(R.id.radio_group_chart);
        radioGroup.setOnCheckedChangeListener(this);
        mPopupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                showForwardView(true, R.mipmap.ic_expand_more);
            }
        });
    }

    private void switchChart(int checkId) {
        if (checkId == R.id.radio_pop_pie) {
            if (mPieFragment == null) {
                mPieFragment = new PieFragment();
                initPopWindow();
            }
            switchFragment(mPieFragment);
        } else {
            if (mLineFragment == null) {
                mLineFragment = new LineFragment();
            }
            switchFragment(mLineFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > Constant.DELAY_TIME) {
                ToastUtils.showShort(getApplicationContext(), R.string.text_exit);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
