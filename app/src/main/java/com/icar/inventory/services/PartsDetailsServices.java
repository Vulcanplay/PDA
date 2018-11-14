package com.icar.inventory.services;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.icar.inventory.NavigationActivity;
import com.icar.inventory.PartsDetailsActivity;
import com.icar.inventory.helper.JSONHelper;
import com.icar.inventory.helper.WebServicesTool;
import com.icar.inventory.model.Employee;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.presenter.impl.ImplPartsDetailsPresenter;

import org.json.JSONException;

import java.util.LinkedHashMap;

/**
 * Created by light on 2016/3/12.
 */
public class PartsDetailsServices {
    ImplPartsDetailsPresenter implPartsDetailsPresenter;

    public ImplPartsDetailsPresenter getImplPartsDetailsPresenter() {
        return implPartsDetailsPresenter;
    }

    public void setImplPartsDetailsPresenter(ImplPartsDetailsPresenter implPartsDetailsPresenter) {
        this.implPartsDetailsPresenter = implPartsDetailsPresenter;
    }

    protected String itemCode;
    protected PartsDetailsActivity activity;
    public Parts getPartsDetails(String itemCode,PartsDetailsActivity activity){
        this.itemCode = itemCode;
        this.activity = activity;
        PartsDetailsAsyncTask pda = new PartsDetailsAsyncTask();
        pda.execute();
        return null;
    }

    /**
     * 这里的Integer参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    private class PartsDetailsAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {

            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            map.put("barcode", itemCode);
            String result = "";
            try {
                result = WebServicesTool.Connect("GetPartDetail", map);
            } catch (Exception e) {
                Toast.makeText(activity, "网络连接超时，请检查网络！", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            PartsDetails partsDetails = null;
            try {
                partsDetails = JSONHelper.parseObject(result,PartsDetails.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            implPartsDetailsPresenter.setPartsDetails(partsDetails);
            Log.d("异步操作执行结束:", result);
        }


        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            Log.d("开始执行异步线程","");
        }
    }
}
