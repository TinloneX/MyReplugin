package com.company.replugin.http;

import com.company.replugin.base.BaseResponse;
import com.company.replugin.mvp.IModel;
import com.company.replugin.config.Config;
import com.company.replugin.util.TLog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author zjl
 * @date 2017/10/17 0017.
 */

public class HttpObserver<T> {
    private static HttpObserver mHttpDao = null;

    private HttpObserver() {

    }

    public static HttpObserver getInstance() {
        if (mHttpDao == null) {
            mHttpDao = new HttpObserver();
        }
        return mHttpDao;
    }

    public Observer<BaseResponse<T>> createObserver(final IModel.AsyncCallBack<BaseResponse<T>> callBack) {
        return new Observer<BaseResponse<T>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseResponse<T> response) {
                // 此处统一处理网络请求状态
                if (Config.Strings.RESPONSE_OK.equals(response.getResultCode())) {
                    callBack.onSuccess(response);
                } else {
                    callBack.onFailed(Config.Strings.SERVER_ERROR, response.getResultCode());
                }
                TLog.i("tag","(HttpObserver.java:45) ~ onNext:" + response.toString());
            }

            @Override
            public void onError(Throwable e) {
                callBack.onFailed(e.getMessage(), "-1");
            }

            @Override
            public void onComplete() {

            }
        };

    }

}
