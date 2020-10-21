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
import com.jizhang.adapter.ListInCatAdapter;
import com.jizhang.application.AccountApplication;
import com.jizhang.dao.IncomeCatDao;
import com.jizhang.dao.IncomeDao;
import com.jizhang.model.Income;
import com.jizhang.model.IncomeCat;
import com.jizhang.utils.DateUtils;
import com.jizhang.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IncomeFragment extends BaseFragment implements ListView.OnItemClickListener {


    @BindView(R.id.label_income_cat)
    TextView mLabelIncomeCat;
    @BindView(R.id.et_income)
    EditText mEtIncome;
    @BindView(R.id.label_income_time)
    TextView mEtIncomeTime;
    @BindView(R.id.et_income_note)
    EditText mEtIncomeNote;
    @BindView(R.id.ll_income_cat)
    LinearLayout mLlIncomeCat;
    private boolean mIsUpdateCat;
    private IncomeCatDao mIncomeCatDao;
    private IncomeDao mIncomeDao;
    private Date mDate;
    private Income mIncome;
    private PopupWindow mPopupWindow;
    private onTimePickListener mOnTimePickListener;
    private boolean mIsUpdateIncome;
    private ListInCatAdapter mCatAdapter;
    private Context mContext;


    public static IncomeFragment getInstance(Income income) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constant.RECORD, income);
        IncomeFragment incomeFragment = new IncomeFragment();
        incomeFragment.setArguments(bundle);
        return incomeFragment;
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
        View view = super.onCreateView(inflater,container,savedInstanceState);
        mIncomeCatDao = new IncomeCatDao(mContext);
        mIncomeDao = new IncomeDao(mContext);
        mCatAdapter = new ListInCatAdapter(mContext, getCategory());
        Bundle arguments = getArguments();
        if (arguments != null) {
            mIsUpdateIncome = true;
            mIncome = arguments.getParcelable(Constant.RECORD);
            mEtIncome.setText(String.valueOf(mIncome.getAmount()));
            mLabelIncomeCat.setText(mIncome.getCategory().getName());
            mEtIncomeNote.setText(mIncome.getNote());
            mDate = mIncome.getDate();
        } else {
            mIsUpdateIncome = false;
            mIncome = new Income();
            mDate = new Date();
            mIncome.setDate(mDate);
            mIncome.setUser(AccountApplication.sUser);
            mIncome.setCategory((IncomeCat) mCatAdapter.getItem(0));
        }
        mEtIncomeTime.setText(DateUtils.date2Str(mDate));


        LinearLayout linearLayout = (LinearLayout) getActivity().getLayoutInflater().
                inflate(R.layout.pop_category, null);
        ListView ListIncomeCat = (ListView) linearLayout.findViewById(R.id.grid_category);
        ListIncomeCat.setAdapter(mCatAdapter);
        ListIncomeCat.setOnItemClickListener(this);

        //长按监听事件，长按的时候进入修改类别的界面
        ListIncomeCat.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                IncomeCat incomeCat = (IncomeCat) mCatAdapter.getItem(position);
                if(incomeCat.getName().equals("添加"))
                    return true;
                AlertDialog.Builder tipDialog =new AlertDialog.Builder(mContext);
                tipDialog.setTitle("删除类别").setMessage("是否删除该类别,同时还会删除该类别下的账目数据");
                tipDialog.setCancelable(true);
                tipDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mIncomeCatDao.delete(incomeCat)&&mIncomeDao.deleteIncomebyCat(incomeCat)) {
                            ToastUtils.showShort(mContext, "删除成功");
                            mCatAdapter.setData(getCategory());
                            EventBus.getDefault().post("income_deleted");
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
        //创建弹出式对话框，让用户管理收支类别
        mPopupWindow = new PopupWindow(linearLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        mPopupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        return view;
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_income;
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

    private List<IncomeCat> getCategory() {
        List<IncomeCat> cats = mIncomeCatDao.getIncomeCat(AccountApplication.sUser.getId());
        cats.add(new IncomeCat("添加", AccountApplication.sUser));
        return cats;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IncomeCat incomeCat = (IncomeCat) parent.getItemAtPosition(position);
        if(incomeCat.getName().equals("添加")){
            AlertDialog.Builder tipDialog =new AlertDialog.Builder(mContext);
            EditText input = new EditText(getActivity());
            input.setSingleLine();
            tipDialog.setTitle("添加类别").setMessage("输入新类别名").setView(input);
            tipDialog.setCancelable(true);
            tipDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String text = input.getText().toString();
                    if (mIncomeCatDao.addCategory(new IncomeCat(text,AccountApplication.sUser))) {
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
        mIncome.setCategory(incomeCat);
        mLabelIncomeCat.setText(incomeCat.getName());
        mPopupWindow.dismiss();

    }

    @OnClick({R.id.label_income_time, R.id.ll_income_cat, R.id.btn_income_save})
    public void incomeClick(View view) {
        switch (view.getId()) {
            case R.id.label_income_time: {
                if (mOnTimePickListener != null) {
                    mOnTimePickListener.DisplayDialog(mDate);
                }
            }
            break;
            case R.id.ll_income_cat:
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    mPopupWindow.showAsDropDown(mLlIncomeCat);
                }
                break;
            case R.id.btn_income_save: {
                saveIncome();
            }
            break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnTimePickListener = null;
    }

    private void saveIncome() {
        String trim = mEtIncome.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            Toast.makeText(mContext, "请输入金额", Toast.LENGTH_SHORT).show();
            return;
        }
        float amount = Float.parseFloat(trim);
        String note = mEtIncomeNote.getText().toString().trim();
        mIncome.setAmount(amount);
        mIncome.setNote(note);
        IncomeDao incomeDao = new IncomeDao(mContext);
        if (!mIsUpdateIncome) {
            if (incomeDao.addIncome(mIncome)) {
                ToastUtils.showShort(mContext, "保存成功");
                EventBus.getDefault().post("income_inserted");
                getActivity().finish();
            } else {
                ToastUtils.showShort(mContext, "保存失败");
            }
        } else {
            if (incomeDao.updateIncome(mIncome)) {
                ToastUtils.showShort(mContext, "修改成功");
                EventBus.getDefault().post("income_updated");
                getActivity().finish();
            } else {
                ToastUtils.showShort(mContext, "修改失败");
            }
        }
    }

    public void setDate(Date date) {
        mIncome.setDate(date);
        mEtIncomeTime.setText(DateUtils.date2Str(date));
    }

    public interface onTimePickListener {
        void DisplayDialog(Date date);
    }
}
