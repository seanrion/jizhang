package com.jizhang.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.jizhang.R;
import com.jizhang.activity.LoginActivity;
import com.jizhang.utils.EncryptUtils;
import com.jizhang.utils.ToastUtils;
import com.jizhang.view.GestureView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LoginSecondFragment extends BaseFragment {
    @BindView(R.id.GestureView)
    GestureView gestureView;
    @BindView(R.id.label_passwordsecondtip)
    TextView tip;
    private List<Integer> mList = new ArrayList();

    @Override
    protected int getResId() {
        return R.layout.fragment_login_second;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(((LoginActivity)getActivity()).getPasssecond()==null) {
            tip.setText("请注册密码");
        }else{
            tip.setText("请绘制密码");
        }

        view.findViewById(R.id.label_passwordfirst).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginSecondFragment.this)
                        .navigate(R.id.action_LoginSecondFragment_to_LoginFirstFragment);
            }
        });
        view.findViewById(R.id.label_passwordsecond_reset).setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View view){
                if(tip.getText().equals("请绘制密码")){
                    ToastUtils.showShort(getActivity(),"请绘制旧密码");
                    tip.setText("请绘制旧密码");
                }
            }
        });

        gestureView.setListener(new GestureView.GestureListener() {
            @Override
            public void onStart() {

            }
            @Override
            public void onDraw(int index) {

            }
            @Override
            public void onFinish(List<Integer> list) {
                String password = EncryptUtils.Encode(list.toString());
                if (tip.getText().equals("请注册密码")) {
                    ((LoginActivity)getActivity()).setPasssecond(password);
                    tip.setText("请绘制密码");
                    ToastUtils.showShort(getActivity(),"再次绘制相同解锁图案");
                }
                else if(tip.getText().equals("请绘制旧密码")) {
                    if (password.equals(((LoginActivity) getActivity()).getPasssecond())) {
                        ToastUtils.showShort(getActivity(), "请绘制新密码");
                        tip.setText("请绘制新密码");
                    }
                    else{
                        ToastUtils.showShort(getActivity(),"密码错误");
                        gestureView.showError();
                    }
                }
                else if(tip.getText().equals("请绘制新密码")){
                    ToastUtils.showShort(getActivity(), "修改成功");
                    ((LoginActivity)getActivity()).setPasssecond(password);
                    tip.setText("请绘制密码");
                }
                else {
                    if (password.equals(((LoginActivity)getActivity()).getPasssecond())) {
                        ToastUtils.showShort(getActivity(),"密码正确");
                        ((LoginActivity)getActivity()).login();
                    } else {
                        gestureView.showError();
                        ToastUtils.showShort(getActivity(),"密码错误");
                    }
                }
                if (!gestureView.IsErrorState())
                    gestureView.reset();
            }
            @Override
            public void onError() {
                Toast.makeText(getActivity(), "图案密码最少连续四个点",
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
