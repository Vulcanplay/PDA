package com.icar.inventory.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.icar.inventory.BindActivity;
import com.icar.inventory.InventoryActivity;
import com.icar.inventory.R;
import com.icar.inventory.adapter.InventoryAdapter;
import com.icar.inventory.helper.JSONHelper;
import com.icar.inventory.helper.WebServicesTool;
import com.icar.inventory.model.BindResult;
import com.icar.inventory.model.Employee;
import com.icar.inventory.model.InventoryResult;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.model.PartsParameters;
import com.icar.inventory.presenter.impl.ImplBindPresenter;
import com.icar.inventory.presenter.impl.ImplInventoryPresenter;
import com.zbar.lib.CaptureActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by light on 2016/3/10.
 */
public class InventoryServices {
    ImplInventoryPresenter implInventoryPresenter;

    public ImplInventoryPresenter getImplInventoryPresenter() {
        return implInventoryPresenter;
    }

    public void setImplInventoryPresenter(ImplInventoryPresenter implInventoryPresenter) {
        this.implInventoryPresenter = implInventoryPresenter;
    }
    //实体
    InventoryActivity activity;
    List<PartsDetails> partsList = new ArrayList<>();
    String id;
    PartsParameters queryPartsParameters;
    CaptureActivity captureActivity;
    //获取列表
    public List<PartsDetails> getPartsForInventory(InventoryActivity activity){
        this.activity = activity;
        //异步实例 调用
        InventoryAsyncTask ba = new InventoryAsyncTask();
        ba.execute();
        return partsList;
    }


    public void setPartsForInventory(String id, InventoryActivity activity){
        this.activity = activity;
        this.id = id;
        SetInventoryAsyncTask sita = new SetInventoryAsyncTask();
        sita.execute();
    }

    public void queryBarcode(PartsParameters partsParameters, Activity activity){
        this.captureActivity = (CaptureActivity) activity;
        this.queryPartsParameters = partsParameters;
        QueryBarcodeAsyncTask ba = new QueryBarcodeAsyncTask();
        ba.execute();
    }
    private class QueryBarcodeAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
            map.put("id", queryPartsParameters.getScanningId());
            map.put("barcode", queryPartsParameters.getScanningCode());
            map.put("quantity", queryPartsParameters.getScanningTotal());
            String result = "";
            try {
                result = WebServicesTool.Connect("BindBarcode", map);
            } catch (RuntimeException e) {
                return "RuntimeException";
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException";
            } catch (XmlPullParserException e) {
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
            switch (result) {
                case "RuntimeException":
                    Toast.makeText(captureActivity, captureActivity.getResources().getString(R.string.RuntimeException), Toast.LENGTH_SHORT).show();
                    break;
                case "IOException":
                    Toast.makeText(captureActivity, captureActivity.getResources().getString(R.string.IOException), Toast.LENGTH_SHORT).show();
                    break;
                case "XmlPullParserException":
                    Toast.makeText(captureActivity, captureActivity.getResources().getString(R.string.XmlPullParserException), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    try {
                        InventoryResult inventoryResult = JSONHelper.parseObject(result, InventoryResult.class);
                        if(inventoryResult.getResult().equals("success")){
                            Toast.makeText(captureActivity, inventoryResult.getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(captureActivity, inventoryResult.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("异步操作执行结束:", result);
                    break;
            }
            implInventoryPresenter.queryBarcode();
        }
    }

    private class InventoryAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            map.put("shopId",String.valueOf(Employee.getEmployee(activity).getShopId()));
            String result = "";
            try {
                result = WebServicesTool.Connect("GetInventoryParts", map);
            }catch (RuntimeException e) {
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
                        partsList = (List) JSONHelper.parseCollection(result,List.class,PartsDetails.class);
                        if (partsList.size() == 0){
                            //没有数据
                            Toast.makeText(activity, activity.getResources().getString(R.string.NullResult), Toast.LENGTH_SHORT).show();
                        }
                        implInventoryPresenter.setInventory(partsList);
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

    private class SetInventoryAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            map.put("id", id);
            String result = "";
            try {
                result = WebServicesTool.Connect("UpdateInventory", map);
            }catch (RuntimeException e) {
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
                        InventoryResult inventoryResult = JSONHelper.parseObject(result, InventoryResult.class);
                        if (inventoryResult.getResult().equals("success")){
                            Toast.makeText(activity, activity.getResources().getString(R.string.uploadSuccess), Toast.LENGTH_SHORT).show();
                            implInventoryPresenter.saveInventory();
                        }
                        else{
                            Toast.makeText(activity, activity.getResources().getString(R.string.uploadError), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d("异步操作执行结束:", result);
                    break;
            }
            activity.dialogUpload.dismiss();
        }


        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            Log.d("开始执行异步线程", "");
        }
    }
}
