package com.icar.inventory.services;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.icar.inventory.NavigationActivity;
import com.icar.inventory.OptionShopActivity;
import com.icar.inventory.R;
import com.icar.inventory.helper.JSONHelper;
import com.icar.inventory.helper.WebServicesTool;
import com.icar.inventory.model.Employee;
import com.icar.inventory.model.Store;
import com.icar.inventory.presenter.StorePresenter;
import com.icar.inventory.presenter.impl.ImplStorePresenter;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by light on 2016/3/8.
 */
public class StoreServices {

    public ImplStorePresenter getImplStorePresenter() {
        return implStorePresenter;
    }

    public void setImplStorePresenter(ImplStorePresenter implStorePresenter) {
        this.implStorePresenter = implStorePresenter;
    }

    private ImplStorePresenter implStorePresenter;
    List<Store> list = new ArrayList<>();
    OptionShopActivity activity;
    String query = "";
    public List<Store> getStore(String query, OptionShopActivity activity) {
        this.activity = activity;
        this.query = query;
        //异步实例 调用
        StoreAsyncTask sa = new StoreAsyncTask();
        sa.execute();
//        try {
//            sa.execute().get(2000, TimeUnit.MILLISECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//
//        }
        return list;
    }


    /**
     * 这里的Integer参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    private class StoreAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            map.put("json",JSONHelper.toJSON(Employee.getEmployee(activity)));
            map.put("shopName",query == null ? "" : query );
            String result = "";
            try {
                result = WebServicesTool.Connect("GetShopList",map);
            } catch (RuntimeException e) {
                return "RuntimeException";
            }catch (IOException e){
                e.printStackTrace();
                return "IOException";
            }catch (XmlPullParserException e){
                return "XmlPullParserException";
            }
            return result;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            switch (result){
                case "RuntimeException":
                    Toast.makeText(activity, activity.getResources().getString(R.string.RuntimeException), Toast.LENGTH_SHORT).show();
                    break;
                case "IOException":
                    Toast.makeText(activity, activity.getResources().getString(R.string.IOException), Toast.LENGTH_SHORT).show();
                    break;
                case "XmlPullParserException":
                    Toast.makeText(activity, activity.getResources().getString(R.string.XmlPullParserException), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    try {
                        list = (List) JSONHelper.parseCollection(result,List.class,Store.class);
                        if (list.size() == 0){
                            //没有数据
                            Toast.makeText(activity, activity.getResources().getString(R.string.NullResult), Toast.LENGTH_SHORT).show();
                        }
                        implStorePresenter.setStore(list);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d("异步操作执行结束:", result);
                    break;
            }
            activity.dialog.dismiss();
        }


        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            Log.d("开始执行异步线程", "");
        }
    }
}
