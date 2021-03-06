package com.company.replugin.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.company.replugin.config.Config;
import com.company.replugin.mvp.IView;

/**
 * @author EDZ
 * @date 2018/3/23.
 * The little flower lies in the dust. It sought the path of the butterfly.
 */

public abstract class BaseFragment<P extends IBasePresenter, DATA> extends Fragment implements IView<DATA> {
    protected P mPresenter;
    protected Context mContext;
    protected View mRootView;
    /**
     * 限制666ms内多次跳转同一界面
     */
    private long lastClick = 0L;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(layoutId(), container, false);
        }
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initView();
        initData();

        return mRootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.dettachView();
        }
        lastClick = 0L;
    }

    /**
     * 获取布局id
     *
     * @return id
     */
    public abstract int layoutId();

    /**
     * 获取presenter
     *
     * @return presenter
     */
    public P getPresenter() {
        return null;
    }

    /**
     * 提供初始化控件入口
     */
    protected void initView() {
    }

    /**
     * 提供初始化数据入口
     * （非必选）
     */
    protected void initData() {

    }

    /**
     * 打开新界面
     *
     * @param clazz 界面类
     */
    public void startActivity(Class<? extends BaseFragment> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 打开新界面
     *
     * @param clazz  界面类
     * @param bundle 数据
     */
    public void startActivity(Class<? extends BaseFragment> clazz, Bundle bundle) {
        if (System.currentTimeMillis() - lastClick < Config.Numbers.CLICK_LIMITED) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, clazz);
        if (bundle == null) {
            bundle = new Bundle();
        }
        intent.putExtras(bundle);
        lastClick = System.currentTimeMillis();
        startActivity(intent);
    }

    /**
     * 成功响应
     *
     * @param resultData 数据
     */
    @Override
    public void onLoadData(DATA resultData) {
        hideLoading();
    }

    /**
     * 失败响应
     *
     * @param resultMsg  失败返回信息
     * @param resultCode 失败返回码
     */
    @Override
    public void onLoadFail(String resultMsg, String resultCode) {
        hideLoading();
        ToastUtils.showShort(Config.Strings.SERVER_ERROR);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
