package com.jizhang.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.jizhang.R;
import com.jizhang.adapter.PagerAdapter;
import com.jizhang.fragment.ExpenseFragment;
import com.jizhang.fragment.IncomeFragment;
import com.jizhang.model.Expense;
import com.jizhang.model.Income;
import com.jizhang.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class RecordActivity extends BaseActivity implements IncomeFragment.onTimePickListener,
        ExpenseFragment.onTimePickListener {
    @BindView(R.id.record_tab_strip)
    PagerSlidingTabStrip mRecordTabStrip;
    @BindView(R.id.pager_record)
    ViewPager mPagerRecord;
    private FragmentManager mFragmentManager;
    private IncomeFragment mIncomeFragment;
    private ExpenseFragment mExpenseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        setTitle(getString(R.string.record_accout));
        showBackwardView(true);

        mFragmentManager = getSupportFragmentManager();
        Parcelable extra = getIntent().getParcelableExtra(Constant.RECORD);
        int index = 0;
        if (extra != null) {
            if (extra instanceof Income) {
                mIncomeFragment = IncomeFragment.getInstance((Income) extra);
                mExpenseFragment = new ExpenseFragment();
            } else if (extra instanceof Expense) {
                mExpenseFragment = ExpenseFragment.getInstance((Expense) extra);
                mIncomeFragment = new IncomeFragment();
                index = 1;
            }
        } else {
            mExpenseFragment = new ExpenseFragment();
            mIncomeFragment = new IncomeFragment();
        }
        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(mIncomeFragment);
        fragments.add(mExpenseFragment);
        mPagerRecord.setAdapter(new PagerAdapter(mFragmentManager, fragments));
        mPagerRecord.setCurrentItem(index);
        mRecordTabStrip.setViewPager(mPagerRecord);
        mRecordTabStrip.setTextSize(ScreenUtils.dp2sp(this, 16));
    }

    @Override
    protected Activity getSubActivity() {
        return this;
    }

    @Override
    public void DisplayDialog(Date date) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);

        Date dateset = new Date();
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateset.setYear(year - 1900);
                dateset.setMonth(month);
                dateset.setDate(dayOfMonth);
                if (mPagerRecord.getCurrentItem() == 1) {
                    mExpenseFragment.setDate(dateset);
                } else {
                    mIncomeFragment.setDate(dateset);
                }
            }
        });
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateset.setHours(hourOfDay);
                dateset.setMinutes(minute);
                if (mPagerRecord.getCurrentItem() == 1) {
                    mExpenseFragment.setDate(dateset);
                } else {
                    mIncomeFragment.setDate(dateset);
                }
            }
        }, 0, 0, true);
        datePickerDialog.show();
        timePickerDialog.show();
    }
}
