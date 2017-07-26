package com.project.aidltocalculate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.project.aidltocalculate.bean.Person;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright (C) 2017,深圳市红鸟网络科技股份有限公司 All rights reserved.
 * 项目名称：AidlToCalculate
 * 类描述：Aidl使用实例2：用于获取客户端各种数据类型，并返回给客户端
 * 创建人：Administrator
 * 创建时间：2017/7/18 9:39
 * 修改人：Administrator
 * 修改时间：2017/7/18 9:39
 * 修改备注：
 * Version:  1.0.0
 */
public class IDataTypeService extends Service {

    private String TAG="IDataTypeService";
    private List<Person>  persons;
    /**
     * 当客户端绑定到该服务端执行
     * @param intent
     * @return
     */
    @Nullable
    @Override
        public IBinder onBind(Intent intent) {
        persons=new ArrayList<>();

        return iBinder;
    }
    private IBinder   iBinder=new BaseDataAidlInterface.Stub() {
        @Override
        public String basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString, List<String> list, CharSequence cs) throws RemoteException {
           String result="int类型："+anInt+"-->long类型:"+aLong+"-->boolean类型："+aBoolean+"--->float类型:"+aFloat+"--->double类型:"+aDouble+"-->String类型："
                   +aString+"--->List集合:"+list+"--->CharSequence类型:"+cs;
            Log.i(TAG,"获取到数据:"+result);
            return result;
        }

        @Override
        public List<Person> addPersonData(Person person) throws RemoteException {
            persons.add(person);
            return persons;
        }
    };

}
