package com.project.aidlcalculateclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.aidltocalculate.CalculateAidl;

/**
 * Aidl通信实例1：通过aidl实现数据计算
 */
public class AidlCommunicateToCalculateActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnAdd;
    private EditText etNum1;
    private EditText etNum2;
    private TextView tvData;

    private String TAG="AidlCalculate";
    private CalculateAidl mCalculateAidl;
    private ServiceConnection connectionm=new ServiceConnection() {

        /**
         * 当服务连接后调用
         * @param componentName
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(AidlCommunicateToCalculateActivity.this,"连接到远程服务",Toast.LENGTH_SHORT);
            Log.i(TAG,"连接到远程服务");
            mCalculateAidl= CalculateAidl.Stub.asInterface(iBinder);
            //在客户端绑定远程服务之后，给Binder设置死亡代理：
            try {
                iBinder.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.i(TAG,"给Binder设置死亡代理失败:"+e.getMessage());
            }
        }

        /**
         * 当服务端开后实现
         * @param componentName
         */
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Toast.makeText(AidlCommunicateToCalculateActivity.this,"与远程服务断开连接",Toast.LENGTH_SHORT);
            Log.i(TAG,"与远程服务断开连接");
            //资源回收
            mCalculateAidl=null;

        }
    };

   /*
   * 当远程服务挂掉后调用
   *
   */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            // TODO: 这里重新绑定远程Service。
            if(mCalculateAidl == null)
                return;
            mCalculateAidl.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mCalculateAidl = null;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_communicate_to_calculate);
        //初始化数据
        initView();
        //绑定服务
        bindService();

    }

    private void initView() {
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        etNum1 = (EditText) findViewById(R.id.et_num1);
        etNum2 = (EditText) findViewById(R.id.et_num2);
        tvData = (TextView) findViewById(R.id.tv_data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                String result1=etNum1.getText().toString();
                String result2=etNum2.getText().toString();
                if(TextUtils.isEmpty(result1)){
                    result1="0";
                }
                if(TextUtils.isEmpty(result2)){
                    result2="0";
                }
                int num1=Integer.valueOf(result1);
                int num2=Integer.valueOf(result2);
                try {
                    if(mCalculateAidl!=null) {
                        int data = mCalculateAidl.add(num1, num2);
                        tvData.setText("数据之和:" + data);
                    }else {
                        Toast.makeText(AidlCommunicateToCalculateActivity.this," 与远程服务断开连接",Toast.LENGTH_SHORT);
                    }
                } catch (RemoteException e) {
                    Log.i(TAG,"没有连接到服务:"+e.getMessage());
                    Toast.makeText(AidlCommunicateToCalculateActivity.this,"没找到该服务",Toast.LENGTH_SHORT);
                }

                break;
        }
    }

    /**
     * 绑定服务
     * 采用显示意图调用   需要  包名    包名+类名
     */
    private void bindService() {
        Intent intent=new Intent();
        intent.setComponent(new ComponentName("com.project.aidltocalculate","com.project.aidltocalculate.ICalculateService"));
        bindService(intent,connectionm, Service.BIND_AUTO_CREATE);
        Log.i(TAG,"绑定服务");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connectionm);
    }
}
