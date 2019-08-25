package com.company.replugin.activity;

import android.support.v4.view.ViewPager;

import com.company.replugin.R;
import com.company.replugin.base.BaseActivity;
import com.company.replugin.base.IBasePresenter;
import com.company.replugin.adapter.GuideAdapter;

/**
 * @author EDZ
 * Let this be my last word, that I trust in thy love.
 */
public class GuideActivity extends BaseActivity {

    private ViewPager vpGuide;
    private GuideAdapter guideAdapter;

    @Override
    public int layoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public IBasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initData() {
        super.initData();
        guideAdapter = new GuideAdapter(this);
        vpGuide.setAdapter(guideAdapter);
    }

    @Override
    public void onLoadData(Object resultData) {

    }

    @Override
    public void onLoadFail(String resultMsg, String resultCode) {

    }

    @Override
    protected void initView() {
        super.initView();
        vpGuide = findViewById(R.id.vpGuide);
    }
}
