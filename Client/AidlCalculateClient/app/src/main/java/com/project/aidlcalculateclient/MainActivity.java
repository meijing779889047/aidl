package com.project.aidlcalculateclient;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;




public class MainActivity extends AppCompatActivity   implements View.OnClickListener {


    private Button btnSample1;
    private Button btnSample2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化数据
        initView();


    }

    private void initView() {
        btnSample1 = (Button) findViewById(R.id.btn_sample1);
        btnSample1.setOnClickListener(this);
        btnSample1 = (Button) findViewById(R.id.btn_sample2);
        btnSample1.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sample1:
                startActivity(new Intent(MainActivity.this,AidlCommunicateToCalculateActivity.class));
                break;
            case R.id.btn_sample2:
                startActivity(new Intent(MainActivity.this,AidlCommunicateToDataTypeActivity.class));
                break;

        }
    }


}
