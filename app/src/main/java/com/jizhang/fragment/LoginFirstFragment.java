package com.jizhang.fragment;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.jizhang.R;
import com.jizhang.R.id;
import com.jizhang.activity.LoginActivity;
import com.jizhang.utils.EncryptUtils;
import com.jizhang.utils.ToastUtils;

import butterknife.BindView;

public class LoginFirstFragment extends BaseFragment {
    @BindView(R.id.et_login_pass)
    EditText passwordEdit;
    @BindView(R.id.btn_login)
    Button login;
    @BindView(id.label_passwordfirsttip)
    TextView tip;

    @Override
    protected int getResId() {
        return R.layout.fragment_login_first;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(((LoginActivity)getActivity()).getPassfirst()==null){
            tip.setText("请注册密码");
            login.setText("注册");
        }else{
            tip.setText("请输入密码");
        }

        view.findViewById(id.label_passwordsecond).setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFirstFragment.this)
                        .navigate(R.id.action_LoginFirstFragment_to_LoginSecondFragment);
            }
        });

        view.findViewById(id.label_passwordfirst_reset).setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view){
                if(tip.getText().equals("请输入密码")){
                    ToastUtils.showShort(getActivity(),"请输入旧密码");
                    passwordEdit.setText("");
                    tip.setText("请输入旧密码");
                    login.setText("确认");
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String password = EncryptUtils.Encode(passwordEdit.getText().toString());

                if(tip.getText().equals("请注册密码")){
                    ToastUtils.showShort(getActivity(),"注册成功");
                    passwordEdit.setText("");
                    tip.setText("请输入密码");
                    login.setText("登录");
                    ((LoginActivity)getActivity()).setPassfirst(password);
                }
                else if(tip.getText().equals("请输入旧密码")) {
                    if (password.equals(((LoginActivity) getActivity()).getPassfirst())) {
                        ToastUtils.showShort(getActivity(), "请输入新密码");
                        passwordEdit.setText("");
                        tip.setText("请输入新密码");
                    }
                    else{
                        ToastUtils.showShort(getActivity(),"密码错误");
                        passwordEdit.setText("");
                    }
                }
                else if(tip.getText().equals("请输入新密码")){
                    ToastUtils.showShort(getActivity(), "修改成功");
                    ((LoginActivity)getActivity()).setPassfirst(password);
                    passwordEdit.setText("");
                    tip.setText("请输入密码");
                    login.setText("登录");
                }
                else{
                    if(password.equals(((LoginActivity)getActivity()).getPassfirst())){
                        ToastUtils.showShort(getActivity(),"密码正确");
                        ((LoginActivity)getActivity()).login();
                    }else{
                        ToastUtils.showShort(getActivity(),"密码错误");
                        passwordEdit.setText("");
                    }
                }

            }
        });
    }

}
