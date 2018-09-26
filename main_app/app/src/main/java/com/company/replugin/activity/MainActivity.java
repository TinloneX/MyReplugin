package com.company.replugin.activity;

import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.company.replugin.R;
import com.company.replugin.base.BaseActivity;
import com.company.replugin.base.IBasePresenter;
import com.company.replugin.config.CollectionConfig;
import com.company.replugin.util.PluginUtils;
import com.company.replugin.util.TLog;
import com.qihoo360.replugin.RePlugin;

/**
 * @author EDZ
 * 未完成
 * We read the world wrong and say that it deceives us.
 */
public class MainActivity extends BaseActivity {

    private LinearLayout fragmentHolder;
    private RadioGroup rgBottom;
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
        fragmentHolder = findViewById(R.id.flFragmentHolder);
        tvInfo = findViewById(R.id.tv_info);
        rgBottom = findViewById(R.id.rgMainBottom);
        rgBottom.check(R.id.rb_home);
        tvInfo.setOnClickListener(v -> loadingPlugin());
        findViewById(R.id.btn_uninstall).setOnClickListener(v -> tvInfo.append(
                PluginUtils.isPluginInstalled(0) && PluginUtils.unInstallPluginByIndex(0)
                        ?
                        "\n插件卸载成功" : "\n未安装插件或卸载失败\n失败原因：当前插件正在运行\n操作：请(冷启动)重启程序")
        );
        tvInfo.append(
                "\n 插件replugin_plns" + (PluginUtils.isPluginInstalled(0) ? "已" : "未") + "安装"
        );
    }

    private void loadingPlugin() {
        tvInfo.append("\n点击了背景板");
        TLog.i("请求权限");

        if (!PluginUtils.isPluginInstalled(0)) {
            tvInfo.append("\n开始安装插件");
            install();
        } else {
            tvInfo.append("\n插件已安装");
            RePlugin.startActivity(mContext, RePlugin.createIntent(CollectionConfig.PLUGINS_NAME.get(0),
                    "com.company.plg1.plugin.PluginMainActivity"));
            tvInfo.append("\n启动 replugin_plns ");
        }

    }

    private void install(){
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        if (PluginUtils.installPluginByIndex(0)) {
                            tvInfo.append("\n插件安装成功");
                            RePlugin.startActivity(mContext, RePlugin.createIntent(CollectionConfig.PLUGINS_NAME.get(0),
                                    "com.company.plg1.plugin.PluginMainActivity"));
                        }
                    }

                    @Override
                    public void onDenied() {
                        tvInfo.append("\n无文件系统权限，无法安装插件");
                    }
                }).request();
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
