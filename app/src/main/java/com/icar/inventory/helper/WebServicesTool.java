package com.icar.inventory.helper;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by light on 2016/3/11.
 */
public class WebServicesTool {

    // 命名空间
    static String nameSpace = "http://tempuri.org/";
    //测试服务器
    static String endPoint = "http://180.76.158.206:8733/PdaAppService/basic";
    //正式服务器
    //static String endPoint = "http://180.76.168.224:8733/PdaAppService/basic";
    // SOAP Action
    //String soapAction = "http://tempuri.org/IService1/GetData";
    static String soapAction = "http://tempuri.org/IPdaAppService/";

    public static String Connect(String methodName, LinkedHashMap<String,String> map) throws RuntimeException,IOException,XmlPullParserException {

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(nameSpace, methodName);

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;

        if(map!=null){
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                PropertyInfo argument = new PropertyInfo();
                HashMap.Entry entry = (HashMap.Entry) iterator.next();
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                argument.setName(key);
                argument.setValue(val);
                rpc.addProperty(argument);
            }
        }
        // 等价于envelope.bodyOut = rpc;
        envelope.setOutputSoapObject(rpc);
        HttpTransportSE transport = new HttpTransportSE(endPoint);
        transport.debug=true;
            // 调用WebService
        transport.call(WebServicesTool.soapAction + methodName, envelope);


        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;


        // 获取返回的结果
        String result = object.getProperty(0).toString();

        return result;
    }
}
