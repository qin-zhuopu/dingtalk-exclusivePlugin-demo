package com.jereh.talk.demo;

import android.content.Intent;
import android.widget.Toast;

import com.alibaba.android.dingtalk.extension.api.ApiCallback;
import com.alibaba.android.dingtalk.openapi.login.extensionpoint.EpLoginNode;
import com.alibaba.dingtalk.extension.annotation.Extension;


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

//        Toast.makeText(loginContext.getActivity(), "杰瑞登录扩展点 Prepare", Toast.LENGTH_SHORT).show();
//        callback.onSuccess(null);

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
