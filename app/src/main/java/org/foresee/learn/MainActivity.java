package org.foresee.learn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button mOpenWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOpenWeb=findViewById(R.id.open_web);
        mOpenWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=WebActivity.newIntent(MainActivity.this, "10.0.2.2:8088");
//                Intent intent=WebActivity.newIntent(MainActivity.this, "http://10.0.2.2:8088/");
                Intent intent=WebActivity.newIntent(MainActivity.this, "file:///android_asset/home.html");
                startActivity(intent);
            }
        });
    }
}
