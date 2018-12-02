package org.foresee.learn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WebActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "URL";
    private static final String TAG = "WebActivity";
    private static final String JS_NAME = "android";

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }

    WebView mWebView;
    Button mCallJsBtn;
    Handler mMainHandler;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        String url = getIntent().getStringExtra(EXTRA_URL);
        Log.d(TAG, "onCreate: open url: "+url);
        mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d(TAG, "onReceivedTitle: title = " + title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient());
        //除了读取URL，还可以访问本地HTML文件，或者字符串HTML传进去。
        mWebView.loadUrl(url);
        mWebView.addJavascriptInterface(new JsInterface(), JS_NAME);

        mCallJsBtn =findViewById(R.id.call_js);
        mCallJsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:setText('web view')");
            }
        });
        mMainHandler=new Handler();
    }
    private class JsInterface{
        @JavascriptInterface
        public void notify(String msg){
            Log.d(TAG, "notify: msg = "+msg);
        }
        @JavascriptInterface
        public void javaMethod(String hello){
            Log.d(TAG, "javaMethod: arg = "+hello);
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.evaluateJavascript("getHello('foresee')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.d(TAG, "onReceiveValue: getHello = "+value);
                        }
                    });
                    mWebView.loadUrl("javascript:setRes('res')");
                }
            });
        }
        @JavascriptInterface
        public void show(){
            Log.d(TAG, "showMsg: called");
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    String msg="call setText(msg)";
                    mWebView.loadUrl("javascript:setText('"+msg+"')");
                }
            });
        }
        @JavascriptInterface
        public String getInfo(){
            Log.d(TAG, "getInfo: called");
            return "从Android来的信息";
        }
    }
}
