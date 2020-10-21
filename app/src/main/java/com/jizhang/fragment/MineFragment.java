package com.jizhang.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.jizhang.R;
import com.jizhang.activity.LoginActivity;
import com.jizhang.activity.MainActivity;
import com.jizhang.adapter.UserExpandableListAdapter;
import com.jizhang.dao.DBOpenHelper;
import com.jizhang.application.AccountApplication;

import java.util.ArrayList;

import com.jizhang.dao.ExpenseCatDao;
import com.jizhang.dao.IncomeCatDao;
import com.jizhang.dao.UserDao;
import com.jizhang.model.User;
import com.jizhang.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {
    @BindView(R.id.ll_me_user)
    ExpandableListView mUser;
    private ArrayList<String> gData = null;
    private ArrayList<ArrayList<User>> iData = null;
    private ArrayList<User> lData = null;
    private MainActivity mContext;
    private static final int UPDATE_USER = 0X1;
    private UserDao userDao;
    private UserExpandableListAdapter useradapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
        userDao = new UserDao(mContext);

    }



    public void initExpandlist(){
        gData = new ArrayList<String>();
        gData.add(new String("当前账户：")+AccountApplication.sUser.getName());
        iData = new ArrayList<ArrayList<User>>();
        lData = userDao.getAlluser();
        lData.remove(0);
        for(int i = 0;i < lData.size();i++){
            if (lData.get(i).getName().equals(AccountApplication.sUser.getName()))
                lData.remove(i);
        }
        lData.add(new User("新建账户",null,null));
        iData.add(lData);
        useradapter = new UserExpandableListAdapter(gData,iData,mContext);
        mUser.setGroupIndicator(null);
        mUser.setAdapter(useradapter);
        return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initExpandlist();
        mUser.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                if (iData.get(groupPosition).get(childPosition).getName().equals("新建账户")){
                    EditText input = new EditText(mContext);
                    input.setSingleLine();
                    builder.setTitle("新建账户").setView(input);
                    builder.setCancelable(true);
                    builder.setMessage("请输入新账户名称:");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String text = input.getText().toString();
                            ArrayList<User> all = null;
                            all= userDao.getAlluser();
                            for(int i = 0;i < lData.size();i++){
                                if (lData.get(i).getName().equals(text)){
                                    ToastUtils.showShort(mContext,"错误，账户名已存在");
                                    return;
                                }
                            }
                            User newuser = new User(text,null,null);
                            userDao.addUser(newuser);
                            IncomeCatDao incomeCatDao = new IncomeCatDao(mContext);
                            incomeCatDao.initIncomeCat(newuser);
                            ExpenseCatDao expenseCatDao = new ExpenseCatDao(mContext);
                            expenseCatDao.initExpensesCat(newuser);
                            lData.add(lData.size()-2,newuser);
                            iData.remove(0);
                            iData.add(lData);
                            useradapter.setData(gData,iData);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }else{
                    builder.setTitle("切换账户");
                    builder.setCancelable(true);
                    builder.setMessage("是否切换至另一个账户？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AccountApplication.sUser = iData.get(groupPosition).get(childPosition);
                            startActivity(new Intent(mContext, MainActivity.class));
                            mContext.finish();

                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.show();
                }
                return false;

            }
        });

        return view;
    }
    @OnClick({R.id.ll_me_init})
    public void mineClick(View view) {
        switch (view.getId()) {
            case R.id.ll_me_init: {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("警告");
                builder.setCancelable(true);
                builder.setMessage(" 初始化将删除所有的软件记录并恢复软件的最初设置，你确定这么做吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //在这里初始化所有数据表，并退出登录
                        DBOpenHelper.getInstance(mContext).dropTable();
                        AccountApplication.sUser = null;
                        startActivity(new Intent(mContext, LoginActivity.class));
                        mContext.finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
            break;
        }
    }

    @Override
    protected int getResId() {
        return R.layout.fragment_mine;
    }


}