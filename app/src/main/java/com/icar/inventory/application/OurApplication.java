package com.icar.inventory.application;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.Callback;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;
import com.github.yoojia.anyversion.VersionParser;
import com.icar.inventory.NavigationActivity;
import com.icar.inventory.helper.CrashHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by light on 2016/3/17.
 */
public class OurApplication extends Application {
    public static int JSON_CODE;
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler handler = CrashHandler.getInstance();
//        handler.init(getApplicationContext()); //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        AnyVersion.init(this, new VersionParser() {
            @Override
            public Version onParse(String response) {
                final JSONTokener tokener = new JSONTokener(response);
                try {
                    JSONObject json = (JSONObject) tokener.nextValue();
                    Version version = new Version(
                            json.getString("name"),
                            json.getString("note"),
                            json.getString("url"),
                            json.getInt("code")
                    );
                    return version;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        AnyVersion version = AnyVersion.getInstance();
        version.setURL("http://180.76.181.115/icar/version.json");
    }
}
