package com.jereh.talk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.dingtalk.extension.api.ApiCallback;
import com.alibaba.android.dingtalk.openapi.login.extensionpoint.EpLoginNode;
import com.alibaba.dingtalk.extension.annotation.Extension;

@Extension(id = "demo_login_node", target = "login_nodes")
public class DemoLoginNode extends EpLoginNode {
    private LoginContext loginContext;

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
        if (loginContext == null || loginContext.getActivity() == null) {
            callback.onException("100", "context invalid");
            return;
        }

        loginFlowCallback = callback;

        Intent intent = new Intent(loginContext.getActivity(), DemoActivity.class);
        loginContext.getActivity().startActivityForResult(intent, 10086);
        loginContext.registerActivityResult(10086, new EpLoginNode.OnActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                Toast.makeText(loginContext.getActivity(), "node get activity result", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


// 自定义activity节点
// This class was changed from 'public' to package-private to allow it to be in the same file as DemoLoginNode.
class DemoActivity extends Activity {
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
