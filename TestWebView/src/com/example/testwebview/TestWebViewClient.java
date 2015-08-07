/**
 * 
 */
package com.example.testwebview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author wanghaiming
 *
 */
public class TestWebViewClient extends WebViewClient {
	private static final String TAG = "TestWebViewClient";
	private  View mAlphaView;
	public TestWebViewClient(View view){
	  mAlphaView = view;	
	}
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		Log.i(TAG,"shouldOverrideUrlLoading-----called. url is: " + url);
		return super.shouldOverrideUrlLoading(view, url);
	}

	@Override
	public void doUpdateVisitedHistory(WebView view, String url,
			boolean isReload) {
		// TODO Auto-generated method stub
		Log.i(TAG,"doUpdateVisitedHistory-----called. url is: "+url);
		super.doUpdateVisitedHistory(view, url, isReload);
	}

	@Override
	public void onFormResubmission(WebView view, Message dontResend,
			Message resend) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onFormResubmission-----called.");
		super.onFormResubmission(view, dontResend, resend);
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onLoadResource-----called. url is: " + url);
		super.onLoadResource(view, url);
	}

	@SuppressLint("NewApi")
	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onPageFinished-----called. url is: "+url);
		super.onPageFinished(view, url);
//		if(mAlphaView != null){
//			mAlphaView.setAlpha(1.0f);
//		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onPageStarted-----called. url is: "+url);
		super.onPageStarted(view, url, favicon);
//		if(mAlphaView != null){
//			mAlphaView.setAlpha(0.0f);
//		}
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onReceivedError-----called.");
		super.onReceivedError(view, errorCode, description, failingUrl);
	}

	@Override
	public void onReceivedHttpAuthRequest(WebView view,
			HttpAuthHandler handler, String host, String realm) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onReceivedHttpAuthRequest-----called.");
		super.onReceivedHttpAuthRequest(view, handler, host, realm);
	}

	@SuppressLint("NewApi")
	@Override
	public void onReceivedLoginRequest(WebView view, String realm,
			String account, String args) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onReceivedLoginRequest-----called.");
		super.onReceivedLoginRequest(view, realm, account, args);
	}

	@Override
	public void onReceivedSslError(WebView view, SslErrorHandler handler,
			SslError error) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onReceivedSslError-----called.");
		super.onReceivedSslError(view, handler, error);
	}

	@Override
	public void onScaleChanged(WebView view, float oldScale, float newScale) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onScaleChanged-----called.");
		super.onScaleChanged(view, oldScale, newScale);
	}

	@Override
	@Deprecated
	public void onTooManyRedirects(WebView view, Message cancelMsg,
			Message continueMsg) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onTooManyRedirects-----called.");
		super.onTooManyRedirects(view, cancelMsg, continueMsg);
	}

	@Override
	public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG,"onUnhandledKeyEvent-----called.");
		super.onUnhandledKeyEvent(view, event);
	}

	@SuppressLint("NewApi")
	@Override
	public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
		// TODO Auto-generated method stub
		Log.i(TAG,"shouldInterceptRequest-----called. url is: "+url);
//		if(url.indexOf("jpg") > 0) {
//			  try {  
//				  InputStream is = view.getContext().getResources().getAssets().open("test.jpg");  
//				  WebResourceResponse response = new WebResourceResponse("image/jpg", "utf-8", is);  
//				  return response;  
//			  	} catch (IOException e) {  
//                // TODO Auto-generated catch block  
//                e.printStackTrace();  
//			  	}
//		}
//		
		return super.shouldInterceptRequest(view, url);	
		
	}

	
	@Override
	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i(TAG,"shouldOverrideKeyEvent-----called.");
		return super.shouldOverrideKeyEvent(view, event);
	}
	
}
