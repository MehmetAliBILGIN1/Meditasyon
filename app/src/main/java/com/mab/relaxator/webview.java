package com.mab.relaxator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class webview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        android.webkit.WebView mWebView = null;
        mWebView = (android.webkit.WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/privacypolicy.html");
    }
}
