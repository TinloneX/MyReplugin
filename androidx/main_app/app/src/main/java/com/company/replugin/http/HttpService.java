package com.company.replugin.http;


import com.company.replugin.base.BaseResponse;
import com.company.replugin.bean.AdvertisementBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author tinlone
 * @date 2017/6/6 0006
 */

public interface HttpService {


    /**
     * 13、手机APP开屏页
     * from 	string 	是 	（安卓：android；水果：iOS） 		iOS
     * type 	string 	是 101-720*1280 102 1080*1920
     */
    @GET("openscreen/pic")
    Observable<BaseResponse<AdvertisementBean>> getAdvertisement(@QueryMap() Map<String, Object> params);

}
