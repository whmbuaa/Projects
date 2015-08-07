package com.example.testwebview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
	private WebView mWebView;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		mWebView = (WebView)findViewById(R.id.web_view);
		mWebView.setWebViewClient(new TestWebViewClient(getWindow().getDecorView()));
//		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//		mWebView.loadUrl("http://192.168.132.91:8080/whm/");
//		getWindow().getDecorView().setAlpha(0.0f);
		mWebView.loadUrl("file:///android_asset/index_asset.html"); 
	}
}
