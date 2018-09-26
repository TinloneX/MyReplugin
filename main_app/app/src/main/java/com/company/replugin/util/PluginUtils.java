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
     * @param pluginName pluginName
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
     * @param pluginName pluginName
     * @return 卸载插件结果
     */
    public static boolean unInstallPluginByName(String pluginName) {
        TLog.i("tag", "卸载" + pluginName);
        return RePlugin.uninstall(pluginName);
    }
}
