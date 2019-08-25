package com.company.replugin.activity;

import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.company.replugin.R;
import com.company.replugin.base.BaseActivity;
import com.company.replugin.base.IBasePresenter;
import com.company.replugin.config.CollectionConfig;
import com.company.replugin.util.PluginUtils;
import com.qihoo360.replugin.RePlugin;

/**
 * @author EDZ
 * 未完成
 * We read the world wrong and say that it deceives us.
 */
public class MainActivity extends BaseActivity {

    private TextView tvInfo;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public IBasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        tvInfo = findViewById(R.id.tv_info);
        findViewById(R.id.btn_install).setOnClickListener(v -> loadingPlugin());
        findViewById(R.id.btn_uninstall).setOnClickListener(v -> tvInfo.append(
                PluginUtils.isPluginInstalled(0) && PluginUtils.unInstallPluginByIndex(0)
                        ?
                        "\n插件卸载成功 plugin uninstall success"
                        :
                        "\n未安装插件或卸载失败\n失败原因：当前插件正在运行\n操作：请(冷启动)重启程序\n " +
                                "uninstall failed or has no plugin to uninstall. error: this plugin is still running,you need kill this app and restart it after you uninstall it")
        );
        tvInfo.append(
                "\n 插件replugin_plns" + (PluginUtils.isPluginInstalled(0) ? "已" : "未") + "安装"
        );
    }

    private void loadingPlugin() {
        tvInfo.append("\n请求权限 request file permission");
        // 检查是否安装第1个插件,否则安装并
        if (!PluginUtils.isPluginInstalled(0)) {
            tvInfo.append("\n开始安装插件 install plugin");
            install();
        } else {
            tvInfo.append("\n插件已安装 plugin has installed");
            gotoPluginMainActivity();
        }
    }

    private void install() {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        if (PluginUtils.installPluginByIndex(0)) {
                            tvInfo.append("\n插件安装成功  plugin installed success");
                            gotoPluginMainActivity();
                        }
                    }

                    @Override
                    public void onDenied() {
                        tvInfo.append("\n无文件系统权限，无法安装插件 no file system permission,could not install plugin");
                    }
                }).request();
    }

    private void gotoPluginMainActivity() {
        RePlugin.startActivity(mContext, RePlugin.createIntent(CollectionConfig.PLUGINS_NAME.get(0),
                "com.company.plg1.plugin.PluginMainActivity"));
        tvInfo.append("\n启动 start replugin_plns ");
    }

    @Override
    protected void initData() {
//        showLoading();
    }

    @Override
    public void onLoadData(Object resultData) {
        hideLoading();
    }

    @Override
    public void onLoadFail(String resultMsg, String resultCode) {
        hideLoading();
    }
}
