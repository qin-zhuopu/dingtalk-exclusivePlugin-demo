package com.jereh.talk.demo;

import android.os.Bundle;
import android.view.View;
import com.alibaba.android.dingtalk.openui.activity.DUIBaseActivity;

public class DemoActivity extends DUIBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity);

        findViewById(R.id.btn_success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DemoLoginNode.loginFlowCallback != null) {
                    DemoLoginNode.loginFlowCallback.onSuccess(null);
                }
                finish();
            }
        });

        findViewById(R.id.btn_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DemoLoginNode.loginFlowCallback != null) {
                    DemoLoginNode.loginFlowCallback.onException("10", "prepare失败！");
                }
                finish();
            }
        });
    }
}
