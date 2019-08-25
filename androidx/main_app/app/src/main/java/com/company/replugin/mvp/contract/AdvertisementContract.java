package com.company.replugin.mvp.contract;

import com.company.replugin.base.BaseResponse;
import com.company.replugin.base.IBasePresenter;
import com.company.replugin.mvp.IModel;
import com.company.replugin.mvp.IView;
import com.company.replugin.bean.AdvertisementBean;

import java.util.HashMap;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public interface AdvertisementContract {

    interface IAdvertisementView extends IView<AdvertisementBean> {

    }

    interface IAdvertisementPresenter extends IBasePresenter<IAdvertisementView> {
        /**
         * 获取启动页广告
         */
        void getAdvertisement();
    }
    interface IAdvertisementModel extends IModel {

        /**
         * 获取广告
         *
         * @param params   参数
         * @param callBack 回调
         */
        void getAdvertisement(HashMap params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack);
    }
}
