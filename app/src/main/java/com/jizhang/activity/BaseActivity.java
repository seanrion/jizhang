package com.jizhang.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jizhang.R;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout mContentLayout;
    private boolean menuShow;
    private int menuitemId;
    private int iconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContentLayout = findViewById(R.id.layout_content);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.btn_top_backward:
                onBackward();
                break;
            case R.id.btn_top_forward:
                onForward();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void onBackward() {
        finish();
    }

    protected void onForward() {
    }

    @Override
    public void setContentView(int layoutResId) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResId, mContentLayout);
        onContentChanged();
        ButterKnife.bind(getSubActivity());
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
        ButterKnife.bind(getSubActivity());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
        ButterKnife.bind(getSubActivity());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try{
            MenuItem item = menu.findItem(menuitemId);
            item.setVisible(menuShow);
            item.setIcon(iconId);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return super.onPrepareOptionsMenu(menu);
        }
    }

    protected void showBackwardView(boolean show, int resId) {
        this.menuitemId = R.id.btn_top_backward;
        this.iconId = resId;
        this.menuShow = show;
        invalidateOptionsMenu();
    }

    protected void showBackwardView(boolean show) {
        this.menuitemId = R.id.btn_top_backward;
        this.iconId = R.mipmap.ic_arrow_back;
        this.menuShow = show;
        invalidateOptionsMenu();
    }

    protected void showForwardView(boolean show, int resId) {
        this.menuitemId = R.id.btn_top_forward;
        this.iconId = resId;
        this.menuShow = show;
        invalidateOptionsMenu();
    }

    protected void showForwardView(boolean show) {
        this.menuitemId = R.id.btn_top_forward;
        this.menuShow = show;
        invalidateOptionsMenu();
    }

    @Override
    public void onClick(View view) {

    }
    protected Activity getSubActivity(){return this;}

}