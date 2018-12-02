package org.foresee.learn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

public class JsBridgeActivity extends AppCompatActivity {
    private static final String EXTRA_URL = "URL";
    private static final String TAG = "JsBridgeActivity";
    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, JsBridgeActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }
    BridgeWebView mWebView;
    Button mCallJsBtn;
    Button mCallDefaultBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_web);
        String url = getIntent().getStringExtra(EXTRA_URL);
        Log.d(TAG, "onCreate: open url: "+url);
        mWebView = findViewById(R.id.bridge_web_view);
        mWebView.loadUrl(url);
        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data from Java");
            }
        });
        mWebView.setDefaultHandler(new DefaultHandler());

        mCallJsBtn =findViewById(R.id.call_js);
        mCallJsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.callHandler("functionInJs", "Hello from Java", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.d(TAG, "onCallBack: data = "+data);
                    }
                });
            }
        });
        mCallDefaultBtn=findViewById(R.id.call_default);
        mCallDefaultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.send("hello");
            }
        });
    }
}
