package com.company.replugin.mvp.model;

import com.company.replugin.base.BaseModel;
import com.company.replugin.base.BaseResponse;
import com.company.replugin.bean.AdvertisementBean;
import com.company.replugin.mvp.contract.AdvertisementContract;
import com.company.replugin.util.TLog;

import java.util.HashMap;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public class AdvertisementModel extends BaseModel implements AdvertisementContract.IAdvertisementModel {

    @Override
    public void getAdvertisement(HashMap params, AsyncCallBack<BaseResponse<AdvertisementBean>> callBack) {
        TLog.i("(AdvertisementModel.java:20) -> getAdvertisement" + mService);
        bindObservable(mService.getAdvertisement(params), callBack);
//        bindObservable(mService.getAdvertisement(params))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(HttpObserver.getInstance().createObserver(callBack));
    }
}
