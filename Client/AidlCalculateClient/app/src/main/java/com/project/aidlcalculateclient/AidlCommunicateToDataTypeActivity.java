package com.project.aidlcalculateclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.aidltocalculate.BaseDataAidlInterface;
import com.project.aidltocalculate.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Aidl通信实例2：通过aidl实现s所支持的数据类型的数据传递
 */
public class AidlCommunicateToDataTypeActivity extends AppCompatActivity  implements View.OnClickListener {


    private Button   btnGetWay1;
    private Button   btnGetWay2;
    private TextView tvWay1;
    private TextView tvWay2;

    private String TAG="DataType";
    private BaseDataAidlInterface mCalculateAidl;
    private ServiceConnection connectionm=new ServiceConnection() {

        /**
         * 当服务连接后调用
         * @param componentName
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Toast.makeText(AidlCommunicateToDataTypeActivity.this,"连接到远程服务",Toast.LENGTH_SHORT);
            Log.i(TAG,"连接到远程服务");
            mCalculateAidl= BaseDataAidlInterface.Stub.asInterface(iBinder);
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
            Toast.makeText(AidlCommunicateToDataTypeActivity.this,"与远程服务断开连接",Toast.LENGTH_SHORT);
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
        setContentView(R.layout.activity_aidl_communicate_to_data_type);
        //初始化数据
        initView();
        bindService();

    }

    private void initView() {
        tvWay1= (TextView) findViewById(R.id.tv_get_remote_data_way1);
        tvWay2= (TextView) findViewById(R.id.tv_get_remote_data_way2);
        btnGetWay1= (Button) findViewById(R.id.btn_get_remote_data_way1);
        btnGetWay1.setOnClickListener(this);
        btnGetWay2= (Button) findViewById(R.id.btn_get_remote_data_way2);
        btnGetWay2.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_get_remote_data_way1:
                try {
                    if(mCalculateAidl!=null) {
                        List<String>  list=new ArrayList<>();
                        list.add("list的集合");
                        String data = mCalculateAidl.basicTypes(1,99999,true,3.14f,0.9999999,"字符串数据",list,"aa");
                        tvWay1.setText("传递基本数据后服务端返回的数据：" + data);
                    }else {
                        Toast.makeText(AidlCommunicateToDataTypeActivity.this," 与远程服务断开连接",Toast.LENGTH_SHORT);
                    }
                } catch (RemoteException e) {
                    Log.i(TAG,"没有连接到服务:"+e.getMessage());
                    Toast.makeText(AidlCommunicateToDataTypeActivity.this,"没找到该服务",Toast.LENGTH_SHORT);
                }

                break;
            case R.id.btn_get_remote_data_way2:
                try {
                if(mCalculateAidl!=null) {
                    Person person=new Person();
                    person.setAge(21);
                    person.setName("张三");
                    List<Person> result = mCalculateAidl.addPersonData(person);
                    tvWay2.setText("传递实体数据后服务端返回的数据：" + result.toString());
                }else {
                    Toast.makeText(AidlCommunicateToDataTypeActivity.this," 与远程服务断开连接",Toast.LENGTH_SHORT);
                }
        } catch (RemoteException e) {
            Log.i(TAG,"没有连接到服务:"+e.getMessage());
            Toast.makeText(AidlCommunicateToDataTypeActivity.this,"没找到该服务",Toast.LENGTH_SHORT);
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
        intent.setComponent(new ComponentName("com.project.aidltocalculate","com.project.aidltocalculate.IDataTypeService"));
        bindService(intent,connectionm, Service.BIND_AUTO_CREATE);
        Log.i(TAG,"绑定服务");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connectionm);
    }



}
