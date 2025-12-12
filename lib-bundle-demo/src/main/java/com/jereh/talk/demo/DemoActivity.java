package com.jereh.talk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Note: You need a layout file named 'demo_activity.xml' in your 'res/layout' folder
        // with two buttons with ids 'btn_success' and 'btn_error' for the R.layout and R.id references to work.
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
