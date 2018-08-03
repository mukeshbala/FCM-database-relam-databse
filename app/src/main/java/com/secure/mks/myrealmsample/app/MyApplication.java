package com.secure.mks.myrealmsample.app;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.secure.mks.myrealmsample.R;
import com.secure.mks.myrealmsample.realm.RealmConfigFactory;
import com.secure.mks.myrealmsample.utills.DeviceUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    public static MyApplication myApplication;
    private RealmConfiguration config;
    private DeviceUtils deviceUtils;


    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = new MyApplication();
        deviceUtils = new DeviceUtils(this);
        Realm.init(this);

        config = RealmConfigFactory.createAdminRealmRealmConfiguration();
//        Realm.setDefaultConfiguration(config);

    }


    public static MyApplication getMyApplication() {
        return myApplication;
    }

    public RealmConfiguration getConfig() {
        if (config == null) {
            config = RealmConfigFactory.createAdminRealmRealmConfiguration();
            Realm.setDefaultConfiguration(config);
        }
        return config;
    }

    public DeviceUtils getDeviceUtils() {
        if (deviceUtils == null) {
            deviceUtils = new DeviceUtils(this);
        }
        return deviceUtils;
    }


    public void getErrorDialog(final Context context) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_error_dialog);
        Button btOk = (Button) dialog.findViewById(R.id.bt_ok);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
