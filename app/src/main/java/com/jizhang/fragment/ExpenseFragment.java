package com.jizhang.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.jizhang.R;
import com.jizhang.activity.Constant;
import com.jizhang.adapter.ListExpCatAdapter;
import com.jizhang.application.AccountApplication;
import com.jizhang.dao.ExpenseCatDao;
import com.jizhang.dao.ExpenseDao;
import com.jizhang.model.Expense;
import com.jizhang.model.ExpenseCat;
import com.jizhang.utils.DateUtils;
import com.jizhang.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class ExpenseFragment extends BaseFragment implements ListView.OnItemClickListener {


    @BindView(R.id.label_expense_cat)
    TextView mLabelExpenseCat;
    @BindView(R.id.et_expense)
    EditText mEtExpense;
    @BindView(R.id.label_expense_time)
    TextView mLabelExpenseTime;
    @BindView(R.id.et_expense_note)
    EditText mEtExpenseNote;
    @BindView(R.id.ll_expense_cat)
    LinearLayout mLlExpenseCat;
    private ExpenseCatDao mExpenseCatDao;
    private ExpenseDao mExpenseDao;
    private static final int REQUEST_ADD_CATEGORY = 0x201;
    private static final int REQUEST_UPDATE_CATEGORY = 0x202;
    private boolean mIsUpdateExpense;
    private boolean mIsUpdateCat;
    private Expense mExpense;
    private Date mDate;
    private ListExpCatAdapter mCatAdapter;
    private PopupWindow mPopupWindow;
    private onTimePickListener mOnTimePickListener;
    private Context mContext;

    public static ExpenseFragment getInstance(Expense expense) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.RECORD, expense);
        ExpenseFragment expenseFragment = new ExpenseFragment();
        expenseFragment.setArguments(bundle);
        return expenseFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onTimePickListener) {
            mOnTimePickListener = (onTimePickListener) activity;
        }
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mExpenseCatDao = new ExpenseCatDao(mContext);
        mCatAdapter = new ListExpCatAdapter(mContext, getCategory());
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsUpdateExpense = true;
            mExpense = arguments.getParcelable(Constant.RECORD);
            mEtExpenseNote.setText(mExpense.getNote());
            mEtExpense.setText(String.valueOf(mExpense.getAmount()));

            mLabelExpenseCat.setText(mExpense.getCategory().getName());
            mDate = mExpense.getDate();
        } else {
            mIsUpdateExpense = false;
            mDate = new Date();
            mExpense = new Expense();
            mExpense.setUser(AccountApplication.sUser);
            mExpense.setDate(mDate);
            mExpense.setCategory((ExpenseCat) mCatAdapter.getItem(0));
        }
        mLabelExpenseTime.setText(DateUtils.date2Str(mDate));

        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.pop_category, null);
        ListView ListExpenseCat = (ListView) linearLayout.findViewById(R.id.grid_category);
        ListExpenseCat.setOnItemClickListener(this);
        ListExpenseCat.setAdapter(mCatAdapter);


        ListExpenseCat.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ExpenseCat expenseCat = (ExpenseCat) mCatAdapter.getItem(position);
                if(expenseCat.getName().equals("添加"))
                    return true;
                AlertDialog.Builder tipDialog =new AlertDialog.Builder(mContext);
                tipDialog.setTitle("删除类别").setMessage("是否删除该类别,同时还会删除该类别下的账目数据");
                tipDialog.setCancelable(true);
                tipDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mExpenseCatDao.delete(expenseCat)&&mExpenseDao.deleteExpensebyCat(expenseCat)) {
                            ToastUtils.showShort(mContext, "删除成功");
                            mCatAdapter.setData(getCategory());
                            EventBus.getDefault().post("expense_deleted");

                        } else {
                            ToastUtils.showShort(mContext, "删除失败");
                        }
                    }
                });
                tipDialog.setNegativeButton("取消", null);
                tipDialog.show();

                return true;
            }
        });
        mPopupWindow = new PopupWindow(linearLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        return view;
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_expense;
    }

    @Override
    protected Fragment getSubFragment() {
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsUpdateCat && mCatAdapter != null) {
            mCatAdapter.setData(getCategory());
        }
    }

    private List<ExpenseCat> getCategory() {
        List<ExpenseCat> cats = mExpenseCatDao.getExpenseCat((AccountApplication.sUser.getId()));
        cats.add(new ExpenseCat("添加", AccountApplication.sUser));
        return cats;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ExpenseCat expenseCat = (ExpenseCat) parent.getItemAtPosition(position);
        if(expenseCat.getName().equals("添加")){
            AlertDialog.Builder tipDialog =new AlertDialog.Builder(mContext);
            EditText input = new EditText(getActivity());
            input.setSingleLine();
            tipDialog.setTitle("添加类别").setMessage("输入新类别名").setView(input);
            tipDialog.setCancelable(true);
            tipDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();
                    if (mExpenseCatDao.addCategory(new ExpenseCat(text,AccountApplication.sUser))) {
                        ToastUtils.showShort(mContext, "添加成功");
                        mCatAdapter.setData(getCategory());
                    } else {
                        ToastUtils.showShort(mContext, "添加失败");
                    }
                }
            });
            tipDialog.setNegativeButton("取消", null);
            tipDialog.show();
            return;
        }
        mExpense.setCategory(expenseCat);
        mLabelExpenseCat.setText(expenseCat.getName());
        mPopupWindow.dismiss();

    }

    @OnClick({R.id.ll_expense_cat, R.id.label_expense_time, R.id.btn_expense_save})
    public void expenseClick(View view) {
        switch (view.getId()) {
            case R.id.ll_expense_cat: {
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(mLlExpenseCat);
                }
            }
            break;
            case R.id.label_expense_time: {
                if (mOnTimePickListener != null) {
                    mOnTimePickListener.DisplayDialog(mDate);
                }
            }
            break;
            case R.id.btn_expense_save:
                saveExpense();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnTimePickListener = null;
    }

    public void setDate(Date date) {
        mExpense.setDate(date);
        mLabelExpenseTime.setText(DateUtils.date2Str(date));
    }

    private void saveExpense() {
        String trim = mEtExpense.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        float amount = Float.parseFloat(trim);
        String note = mEtExpenseNote.getText().toString().trim();
        mExpense.setAmount(amount);
        mExpense.setNote(note);
        ExpenseDao expenseDao = new ExpenseDao(mContext);
        if (mIsUpdateExpense) {
            if (expenseDao.updateExpense(mExpense)) {
                ToastUtils.showShort(mContext, "修改成功");
                EventBus.getDefault().post("expense_updated");
                getActivity().finish();
            } else {
                ToastUtils.showShort(mContext, "修改失败");
            }
        } else {
            if (expenseDao.addExpense(mExpense)) {
                ToastUtils.showShort(mContext, "保存成功");
                EventBus.getDefault().post("expense_inserted");
                getActivity().finish();
            } else {
                ToastUtils.showShort(mContext, "保存失败");
            }
        }
    }

    public interface onTimePickListener {
        void DisplayDialog(Date date);
    }
}
