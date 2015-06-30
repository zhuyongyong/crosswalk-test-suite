// Copyright (c) 2014 Intel Corporation. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.xwalkview.stability.app;


import org.xwalkview.stability.base.XWalkBaseTabImageActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class TabImageWebViewsActivity extends XWalkBaseTabImageActivity {

    @Override
    protected void onXWalkReady() {
        textDes.setText("This sample demonstrates the feasibility to create and destroy WebViews in many tabs (1 webview per tab) at same time to load webpages with image contents.");
        mAddViewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int add_num = 0;
                int max_num = view_num;
                if(!TextUtils.isEmpty(views_num_text.getText())){
                    add_num = Integer.valueOf(views_num_text.getText().toString());
                    max_num = max_num + add_num;
                    int len = checkBoxList.size();
                    for(int i = view_num; i < max_num; i++) {
                        if (url_index >= len) {
                            url_index = 0;
                        }
                        String name = "Tab " + (i + 1);
                        TabSpec tab1 = tabHost.newTabSpec(name);
                        tab1.setIndicator(name);

                        tab1.setContent(new TabContentFactory(){

                            @Override
                            public View createTabContent(String tag) {
                                WebView webView = new WebView(TabImageWebViewsActivity.this);
                                webView.setId(i_index);
                                i_index++;
                                webView.setWebViewClient(new TestWebViewClientBase());
                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.loadUrl(checkBoxList.get(url_index).getText().toString());
                                return webView;
                            }
                        });
                        tabHost.addTab(tab1);
                        tabHost.setCurrentTab(i);
                        tabHost.performClick();
                        url_index++;
                        mAddViewsButton.setClickable(false);
                    }
                    view_num = view_num + add_num;
                }
            }
        });

        mExitViewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        setContentView(root);
    }

    class TestWebViewClientBase extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            String idStr = String.valueOf(view.getId());
            if(!idList.contains(idStr)){
                idList.add(idStr);
                count_num++;
                if(count_num == view_num) {
                    mAddViewsButton.setClickable(true);
                }
                textResultTextView.setText(String.valueOf(count_num));
            }
            super.onPageFinished(view, url);
        }
    }
}
