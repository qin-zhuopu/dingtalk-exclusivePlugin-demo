package com.jereh.talk.demo;

import com.alibaba.android.dingtalk.bundle.BundleApplication;
import com.alibaba.android.dingtalk.bundle.BundleContext;
import com.alibaba.dingtalk.extension.annotation.Bundle;

@Bundle
public class MainBundle extends BundleApplication {

    // 该值需要同钉钉方提前约定，务必与bundle.xml保持相同，请不要随意修改
    public static final String BUNDLE_ID = "JerehTalkDemo";

    private static BundleContext bundleContext;

    public static BundleContext getBundleContext() {
        return bundleContext;
    }

    @Override
    public String getBundleId() {
        return BUNDLE_ID;
    }

    @Override
    public void onApplicationCreate(BundleContext context) {
        super.onApplicationCreate(context);
        this.bundleContext = context;   // 该行不要删除
    }
}

