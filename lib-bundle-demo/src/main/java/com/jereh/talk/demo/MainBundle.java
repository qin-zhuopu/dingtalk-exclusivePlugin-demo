package com.jereh.talk.demo;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.dingtalk.bundle.BundleApplication;
import com.alibaba.android.dingtalk.bundle.BundleContext;
import com.alibaba.dingtalk.extension.annotation.Bundle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Bundle
public class MainBundle extends BundleApplication {

    // 该值需要同钉钉方提前约定，务必与bundle.xml保持相同，请不要随意修改
    public static final String BUNDLE_ID = "JerehTalkDemo";
    private static final String TAG = "MainBundle";

    private static BundleContext bundleContext;

    public static BundleContext getBundleContext() {
        return bundleContext;
    }

    @NonNull
    @Override
    public String getBundleId() {
        return BUNDLE_ID;
    }

    @Override
    public void onApplicationCreate(@NonNull BundleContext context) {
        super.onApplicationCreate(context);
        bundleContext = context;   // 该行不要删除

        Toast.makeText(context.getApplication(), "烟台杰瑞onApplicationCreate", Toast.LENGTH_SHORT).show();

        // Network requests must be on a background thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 格式化时间戳为 yyyyMMddHHmmss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                    String timestamp = sdf.format(new Date());

                    String urlString = "https://iam.us.zhuopu.net:8443/openid-configuration.json?ts=onApplicationCreate" + timestamp;
                    URL url = new URL(urlString);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        // Log the result, you can see it in Logcat with the tag "MainBundle"
                        Log.d(TAG, "Request successful: " + result.toString());
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Request failed", e);
                }
            }
        }).start();
    }
}
