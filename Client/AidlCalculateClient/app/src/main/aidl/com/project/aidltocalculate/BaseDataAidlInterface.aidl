// BaseDataAidlInterface.aidl
package com.project.aidltocalculate;

import com.project.aidltocalculate.bean.Person;
// Declare any non-default types here with import statements

interface BaseDataAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString,in List<String> list,CharSequence cs);

     //对于实例类数据，我们必须实现序列表接口，同时在aidl中声明该实例类是一个序列化数据，该实体类需要导包
    List<Person>   addPersonData(in Person person);


}
