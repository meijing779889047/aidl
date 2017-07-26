package com.project.aidltocalculate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：AidlToCalculate
 * 类描述：Aidl使用实例1：计算数据之和
 * 创建人：Administrator
 * 创建时间：2017/7/18 9:39
 * 修改人：Administrator
 * 修改时间：2017/7/18 9:39
 * 修改备注：
 * Version:  1.0.0
 */
public class ICalculateService  extends Service {

    private String TAG="ICalculateService";

    /**
     * 当客户端绑定到该服务端执行
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    private IBinder   iBinder=  new CalculateAidl.Stub() {
        @Override
        public int add(int num1, int num2) throws RemoteException {
            Log.i(TAG,"收到了远程的数据:   num1:"+(num1)+"--->num2:"+num2);
            return num1+num2;
        }
    };

}
