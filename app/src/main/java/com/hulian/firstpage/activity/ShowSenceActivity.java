package com.hulian.firstpage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hulian.firstpage.R;
import com.hulian.firstpage.util.CommonUtils;

public class ShowSenceActivity extends Activity {
    WebView webView;
    TextView tv_show_finish;
    ImageView iv_share_btn;
    ImageView iv_edit_sence;
    String id;
    String url;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sence);
        init();

        url = CommonUtils.SENCE_VIEW_PATH +id ;
        Log.i("url",url);

          webView.loadUrl(url);
//        webView.loadUrl("http://cjl2015.sinaapp.com/scene/view/626ac8a1a7aa41d594ca4b900ea4058d");
        //设置加载全部后的监听
        webView.setWebViewClient(viewclient);
    }

    private WebViewClient viewclient = new WebViewClient() {
        //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);

            return true;
        }

    };

    private void init() {
        intent = getIntent();
       id =  intent.getStringExtra("senceId");
        webView = (WebView) findViewById(R.id.sence_web);
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);// 關鍵點

        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLoadWithOverviewMode(true);

        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        tv_show_finish = (TextView) findViewById(R.id.tv_show_finish);
        tv_show_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_share_btn = (ImageView) findViewById(R.id.iv_share_btn);
        iv_edit_sence = (ImageView) findViewById(R.id.iv_edit_sence);
        iv_edit_sence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowSenceActivity.this,SenceEditActivity.class);
                intent.putExtra("senceId", id);
                startActivity(intent);
                finish();
            }
        });
    }

}
