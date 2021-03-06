package com.company.replugin.base;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.company.replugin.http.HttpObserver;
import com.company.replugin.mvp.IModel;
import com.company.replugin.http.HttpClient;
import com.company.replugin.http.HttpService;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author EDZ
 * @date 2018/3/23.
 * Life has become richer by the love that has been lost.
 */

public class BaseModel implements IModel {

    private SparseArray<Observable> queue = null;
    private int index = 0;
    protected HttpService mService = HttpClient.getInstance().getApiService();

    @SuppressLint("CheckResult")
    protected Observable bindObservable(@NonNull Observable call) {
        if (queue == null) {
            queue = new SparseArray<>();
        }
        queue.append(index, call);
        index++;
        return call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected void bindObservable(@NonNull Observable call,@NonNull AsyncCallBack callBack){
        if (queue == null) {
            queue = new SparseArray<>();
        }
        queue.append(index, call);
        index++;
        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(HttpObserver.getInstance().createObserver(callBack));
    }

    @Override
    @SuppressLint("CheckResult")
    public void cancelRequest() {
        if (queue != null && queue.size() > 0) {
            for (int i = 0; i < queue.size(); i++) {
                queue.get(i).unsubscribeOn(AndroidSchedulers.mainThread());
            }
            queue.clear();
            index = 0;
        }
    }
}
