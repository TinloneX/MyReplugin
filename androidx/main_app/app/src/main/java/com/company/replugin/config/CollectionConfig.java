package com.company.replugin.config;


import android.os.Environment;

import com.company.replugin.R;

import java.io.File;
import java.util.ArrayList;

/**
 * @author EDZ
 * @date 2018/3/27.
 */

public interface CollectionConfig {

    ArrayList<Integer> GUIDE_IMAGES =
            new ArrayList<Integer>() {
                {
                    add(R.drawable.ic_guide_1);
                    add(R.drawable.ic_guide_2);
                    add(R.drawable.ic_guide_3);
                }
            };

    ArrayList<String> PLUGINS_NAME =
            new ArrayList<String>() {
                {
                    add("com.company.plg1.plugin");
                }
            };
    ArrayList<String> PLUGINS_PATH =
            new ArrayList<String>() {
                {
                    add(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "replugin_plns.apk");
                }
            };
}
