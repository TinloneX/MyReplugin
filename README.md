## 写在前面
最近公司想做AB面的APP，考虑想用插件化方案实现动态接入功能，多(bai)方(du)考(yi)虑(xia)发现[360的RePlugin](https://github.com/Qihoo360/RePlugin)比(hen)较(jian)火(dan)，故尝试一下Replugin[手动smile],虽然最终不适合AB面的最初设想，但是对正经项目还是挺合适的，过程曲折离奇，聊做记录！
## 主程序配置：
由于本人**比(fei)较(chang)莽**，如果参考本文失败，请参见官方文档：[Replugin-wiki](https://github.com/Qihoo360/RePlugin/wiki)
##### gradle相关配置
懒人直接PO代码，[案例详见GitHub](https://github.com/TinloneX/MyReplugin)！
- 根目录*build.gradle* 中代码：

```
dependencies {
        // 由于replugin:2.3.0尚未兼容gradle:3.2.0,故建议不使用高版本gradle
        // 使用3.2.0版本gradle编译无法通过，3.1.4版本正常
        classpath 'com.android.tools.build:gradle:3.1.4'
        classpath 'com.qihoo360.replugin:replugin-host-gradle:2.3.0'
    }
```
以下是app目录下*build.gradle* 相关代码：
- android{}中 *defaultConfig* :

```
// gradle:2.x需要指定buildToolsVersion，版本按各自本地环境填写
buildToolsVersion "27.0.3"
 defaultConfig {
        // 注意必须申明 applicationId
        applicationId "com.company.replugin"
        // 其他配置按各自项目要求配置，与replugin导入无明显关系
        minSdkVersion 19
        targetSdkVersion 27
        versionCode 100
        versionName "1.0.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
```
- *dependencies* 中导入**replugin-host** 框架

```
implementation 'com.qihoo360.replugin:replugin-host-lib:2.3.0'
```

- 在app目录下*build.gradle* **末尾**加入以下代码：

```
apply plugin: 'replugin-host-gradle'
repluginHostConfig {
    useAppCompat = true
    // 可以在这里自定义常驻进程的名字
    // persistentName = ":XXXXService"
}
```
##### 自定义Application配置
若自定义Application无继承关系，可使用继承式继承**RePluginApplication**类(官方推荐)。
由于本案例项目集成MultiDex，故采用非继承式接入RePlugin,代码如下：

```
package com.company.replugin;

import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDexApplication;

import com.company.replugin.manager.AppLifecycleManager;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.RePluginConfig;

/**
 * @author Tinlone
 * @date 2018/3/23.
 * I cannot choose the best. The best chooses me.
 */
public class MyApplication extends MultiDexApplication {

    private static MyApplication mContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        // 官方建议紧接着super调用
        RePlugin.App.onCreate();
        // 设置debug
        RePlugin.enableDebugger(this,BuildConfig.DEBUG);
        // 安全起见，建议生产应用应添加插件签名白名单
        // 实际生产中，外部插件大多通过下载安装，可在安装前加入白名单
        // 签名信息生成方法：keytool -list -v -keystore 你的签名路径
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
```
重要的：请在*AndroidManifest.xml* 中为你的Application配置name:

```
android:name=".MyApplication"
```
并且添加以下权限：

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
此时，你的主程序配置已经基本完成了，为了方便安装和使用插件的方便，此时，先介绍插件开发项目相关配置，稍后再回主程序介绍如何安装和使用插件！

## 插件程序配置
##### gradle 配置
- 根目录下*build.gradle* 代码：

```
dependencies {
        // (仅)建议gradle版本与主程序一致，显然还是要低于3.2.0版本(高于3.2.0未尝试)
        classpath 'com.android.tools.build:gradle:3.1.4'
        classpath 'com.qihoo360.replugin:replugin-plugin-gradle:2.2.4'
    }
```
- app目录下*build.gradle* 导入replugin-plugin框架：

```
implementation 'com.qihoo360.replugin:replugin-plugin-lib:2.2.4'
```
- app目录下*build.gradle* 文末加入：

```
apply plugin: 'replugin-plugin-gradle'
```
##### AndroidManifest.xml配置

在application标签下加入以下节点代码：

```
   <!-- name 填写插件别名，使用唯一值，一般的使用:包名+你想要的文字 --> 
    <meta-data
        android:name="com.company.plg1.plugin"
        android:value="replugin_plns" />
    <!-- 版本号，用于更新插件版本比较,注意replugin只支持同级覆盖和升级，不支持降级操作 -->
    <meta-data
        android:name="com.qihoo360.plugin.version.ver"
        android:value="100" />
```
此时，你开发的这个项目打包即可作为插件安装在主程序中了！
现在，我们打包好这个项目的apk,将它放到手机的根目录中备用，回到主程序编写安装和调用代码。
## 主程序安装和使用插件
给定通过按键触发安装及卸载操作：

```
// 初始化控件省略，详见demo
// tvInfo用以展示流程信息及点击触发安装插件/跳转插件界面
tvInfo.setOnClickListener(v -> loadingPlugin());
// 卸载插件的触发按钮，PluginUtils为个人封(xia)装(xie)的插件相关工具类
// PluginUtils.unInstallPluginByIndex(0) 实际调用Replugin.unInstall(pluginName)
findViewById(R.id.btn_uninstall).setOnClickListener(v -> tvInfo.append(
                PluginUtils.isPluginInstalled(0) && PluginUtils.unInstallPluginByIndex(0)
                        ?
                        "\n插件卸载成功" : "\n未安装插件或卸载失败\n失败原因：当前插件正在运行\n操作：请(冷启动)重启程序")
        );
        tvInfo.append(
                "\n 插件replugin_plns" + (PluginUtils.isPluginInstalled(0) ? "已" : "未") + "安装"
        );
```
loadPlugin()方法内容：

```
    private void loadingPlugin() {
        tvInfo.append("\n点击了信息面板");
        TLog.i("请求权限");
        // 检查是否安装第1个插件,否则安装插件,实际调用Replugin.isPluginInstalled(pluginName)
        if (!PluginUtils.isPluginInstalled(0)) {
            tvInfo.append("\n开始安装插件");
            install();
        } else {
            tvInfo.append("\n插件已安装");
            gotoPluginMainActivity();
        }
    }
    /**
     * 安装插件1
     **/
    private void install(){
       // 重要：动态判断/获取文件权限，没有权限则安装不成功
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        // 调用安装程序，若安装成功，则跳转页面 实际调用Replugin.install(pluginName)
                        if (PluginUtils.installPluginByIndex(0)) {
                            tvInfo.append("\n插件安装成功");
                            gotoPluginMainActivity();
                        }
                    }

                    @Override
                    public void onDenied() {
                        tvInfo.append("\n无文件系统权限，无法安装插件");
                    }
                }).request();
    }
    /**
     * 跳转插件1页面
     **/
    private void gotoPluginMainActivity() {
        RePlugin.startActivity(mContext, RePlugin.createIntent(CollectionConfig.PLUGINS_NAME.get(0),
                "com.company.plg1.plugin.PluginMainActivity"));
        tvInfo.append("\n启动 replugin_plns ");
    }

```
PluginUtils代码：

```
package com.company.replugin.util;

import com.company.replugin.config.CollectionConfig;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.io.File;

public class PluginUtils {
    /**
     * 检查插件是否已安装
     *
     * @param pluginName 插件AndroidManifest.xml中 meta-data 的 name 
     * @return isPluginInstalled
     */
    public static boolean isPluginInstalled(String pluginName) {
        return RePlugin.isPluginInstalled(pluginName);
    }

    /**
     * 检查插件是否已安装
     *
     * @param pluginIndex pluginIndex
     * @return isPluginInstalled
     */
    public static boolean isPluginInstalled(int pluginIndex) {
        // CollectionConfig.PLUGINS_NAME 用于存放 插件AndroidManifest.xml中 meta-data 的 name集合 
        return RePlugin.isPluginInstalled(CollectionConfig.PLUGINS_NAME.get(pluginIndex));
    }

    /**
     * 安装外部插件
     *
     * @param pluginIndex pluginIndex
     * @return 是否安装成功
     */
    public static boolean installPluginByIndex(int pluginIndex) {
        // CollectionConfig.PLUGINS_PATH 用于存放 插件的 存放路径集合 
        return installPluginByName(
                CollectionConfig.PLUGINS_NAME.get(pluginIndex),
                CollectionConfig.PLUGINS_PATH.get(pluginIndex));
    }

    /**
     * 安装外部插件
     *
     * @param pluginName 插件AndroidManifest.xml中 meta-data 的 name 
     * @param filePath   filePath
     * @return 是否安装成功
     */
    public static boolean installPluginByName(String pluginName, String filePath) {

        TLog.i("开始安装插件");
        if (!isPluginInstalled(pluginName)) {
            TLog.i("查找插件 %s 安装文件 %s", pluginName, filePath);
            TLog.i("检查 " + filePath);
            File pluginFile = new File(filePath);
            //文件不存在就返回
            if (!pluginFile.exists()) {
                TLog.i("%s %s不存在", pluginName, filePath);
                return false;
            }
            PluginInfo info = null;
            if (pluginFile.exists()) {
                info = RePlugin.install(filePath);
                TLog.i("%s存在 + info = %s", filePath, TLog.valueOf(info));
            }
            if (info != null) {
                //预先加载
                TLog.i("%s %s预安装", pluginName, filePath);
                RePlugin.preload(info);
                TLog.i("安装插件 %s 成功 - %s", pluginName, info.getName());
            } else {
                return false;
            }
        } else {
            TLog.i("plg1已安装");
        }
        return true;
    }

    /**
     * 卸载插件
     *
     * @param pluginIndex pluginIndex
     * @return 卸载插件结果
     */
    public static boolean unInstallPluginByIndex(int pluginIndex) {
        return unInstallPluginByName(CollectionConfig.PLUGINS_NAME.get(pluginIndex));
    }

    /**
     * 卸载插件
     *
     * @param pluginName 插件AndroidManifest.xml中 meta-data 的 name 
     * @return 卸载插件结果
     */
    public static boolean unInstallPluginByName(String pluginName) {
        TLog.i("tag", "卸载" + pluginName);
        return RePlugin.uninstall(pluginName);
    }
}

```
#### 注意事项：
- 配置gradle 一定要注意版本兼容问题,不然可能会一直卡在Gralde Sync上(比如我)
- pluginName 一定要写对，最好复制粘贴，否则啥都不对(比如我)，生产环境中基本都是后台返回，理论上不会有这个问题
- 安装插件前==一定需要获取文件系统权限==，否则 *RePlugin.install(filePath)* 返回 null，插件安装失败(还是我)
- 卸载不一定是及时行为，如果你正在使用插件，那么卸载插件仅先记载卸载行为，当(冷启动)重启时执行卸载操作
- 案例中代码没有生产环境代码一般严谨，PlugName 及 PluginPath请按个人想法处理, 案例中签名文件已放在项目中，如要使用release编译需要自行更改密钥路径。
- 此案例仅介绍外置插件的安装和卸载，内置插件看官方文档吧~~
