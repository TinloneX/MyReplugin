package com.company.replugin;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.company.replugin.manager.AppLifecycleManager;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginConfig;

/**
 * @author EDZ
 * @date 2018/3/23.
 * I cannot choose the best. The best chooses me.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication mContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        RePlugin.App.onCreate();
        RePlugin.enableDebugger(this,BuildConfig.DEBUG);
        RePlugin.addCertSignature("4F:B9:67:FD:03:9D:B0:70:9B:36:01:35:3D:C7:7E:60".replace(":",""));
        mContext = this;
        AppExceptionHandler.getInstance().init(mContext);
        AppLifecycleManager.onAppStart();
    }

    public static MyApplication getAppContext() {
        return mContext;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //-------------- 开发的时候不验证签名 -----------
        RePlugin.App.attachBaseContext(this,
                new RePluginConfig()
                        .setVerifySign(!BuildConfig.DEBUG)
                        .setPrintDetailLog(true)
                        .setUseHostClassIfNotFound(true)
                        .setMoveFileWhenInstalling(false));
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onLowMemory();

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onTrimMemory(level);

    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        /* Not need to be called if your application's minSdkVersion > = 14 */
        RePlugin.App.onConfigurationChanged(config);

    }
}
