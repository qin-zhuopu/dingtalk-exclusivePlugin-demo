package com.jereh.talk.demo;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.dingtalk.extension.api.ApiCallback;
import com.alibaba.android.dingtalk.openapi.login.extensionpoint.EpLoginNode;
import com.alibaba.dingtalk.extension.annotation.Extension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Extension(id = "demo_login_node", target = "login_nodes")
public class DemoLoginNode extends EpLoginNode {
    private LoginContext loginContext;
    private static final String TAG = "DemoLoginNode";

    // 简化演示记录登录回调，activity可回调返回钉钉登录流程
    // 实际使用中建议定义Manager等做更优雅的封装
    public static ApiCallback<Void> loginFlowCallback;

    @Override
    public void setLoginContext(LoginContext context) {
        this.loginContext = context;
    }

    @Override
    public NodeType getNodeType() {
        return NodeType.Prepare;
    }

    @Override
    public void bindData(LoginData data) {
    }

    @Override
    public void execute(ApiCallback<Void> callback) {
        if (loginContext == null) {
            // Context is null, so we cannot show a Toast here.
            callback.onException("100", "context invalid");
            return;
        }

        if (loginContext.getActivity() == null) {
            // Activity is null, so we cannot show a Toast here either.
            // Calling the onException callback is the correct way to handle this error.
            callback.onException("100", "context invalid: activity is null");
            return;
        }

        loginFlowCallback = callback;

        Toast.makeText(loginContext.getActivity(), "杰瑞登录扩展点 Prepare", Toast.LENGTH_SHORT).show();

        // Network requests must be on a background thread.
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 格式化时间戳为 yyyyMMddHHmmss
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
                    String timestamp = sdf.format(new Date());

                    String urlString = "https://iam.us.zhuopu.net:8443/openid-configuration.json?ts=" + timestamp;
                    URL url = new URL(urlString);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        // Log the result, you can see it in Logcat with the tag "DemoLoginNode"
                        Log.d(TAG, "Request successful: " + result.toString());
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Request failed", e);
                }
            }
        }).start();

        callback.onSuccess(null);


//        Intent intent = new Intent(loginContext.getActivity(), DemoActivity.class);
//        loginContext.getActivity().startActivityForResult(intent, 10086);
//        loginContext.registerActivityResult(10086, new EpLoginNode.OnActivityResultListener() {
//            @Override
//            public void onActivityResult(int requestCode, int resultCode, Intent data) {
//                Toast.makeText(loginContext.getActivity(), "node get activity result", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
