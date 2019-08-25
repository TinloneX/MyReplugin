package com.company.replugin.base;

import com.company.replugin.mvp.IView;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public interface IBasePresenter<V extends IView> {

    /**
     * 绑定V层
     * @param view view
     */
    void attachView(V view);

    /**
     * 解绑V
     */
    void dettachView();
}
