package com.icar.inventory.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.icar.inventory.BindActivity;
import com.icar.inventory.R;
import com.icar.inventory.helper.JSONHelper;
import com.icar.inventory.helper.WebServicesTool;
import com.icar.inventory.model.BindResult;
import com.icar.inventory.model.Employee;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.model.PartsParameters;
import com.icar.inventory.model.Store;
import com.icar.inventory.presenter.impl.ImplBindPresenter;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by light on 2016/3/10.
 */
public class BindServices {

    private ImplBindPresenter implBindPresenter;

    public void setImplBindPresenter(ImplBindPresenter implBindPresenter) {
        this.implBindPresenter = implBindPresenter;
    }
    List<PartsDetails> partsList = new ArrayList<>();
    //绑定返回的对象
    BindResult bindResult;
    PartsParameters partsParameters;
    BindActivity activity;
    //获取列表
    public List<PartsDetails> getParts(PartsParameters partsParameters){
        this.partsParameters = partsParameters;
        activity = partsParameters.getActivity();
        //异步实例 调用
        BindAsyncTask ba = new BindAsyncTask();
        ba.execute();
        return partsList;
    }


    //绑定配件
    public void setBind(PartsParameters partsParameters) {
        this.partsParameters = partsParameters;
        //异步实例 调用
        SetBindAsyncTask sba = new SetBindAsyncTask();
        sba.execute();
    }


    private class SetBindAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            map.put("barcode",partsParameters.getBarCode());
            map.put("itemCode",partsParameters.getItemCode());
            map.put("supplierName", partsParameters.getSupplier());
            map.put("orderNo", partsParameters.getOrderNo());
            map.put("total", partsParameters.getTotal());
            map.put("itemName", partsParameters.getItemName());
            String result = "";
            try {
                result = WebServicesTool.Connect("Binding", map);
            } catch (Exception e) {
                Toast.makeText(partsParameters.getActivity(), "网络连接超时，请检查网络！", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                bindResult = JSONHelper.parseObject(result, BindResult.class);
                if(bindResult.getResult().equals("success"))
                    bindResult.setTotal(Integer.parseInt(partsParameters.getTotal()));
                else
                    bindResult.setTotal(0);
                implBindPresenter.setBind(bindResult);
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("异步操作执行结束:", result);
        }


        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            Log.d("开始执行异步线程", "");
        }
    }

    private class BindAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {
            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            map.put("bindFlag", String.valueOf(partsParameters.getType()));//1 全部 2未绑定 3已绑定
            map.put("partsName",partsParameters.getQuery());
            String result = "";
            try {
                result = WebServicesTool.Connect("GetParts", map);
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
                        partsList = (List) JSONHelper.parseCollection(result,List.class,PartsDetails.class);
                        if (partsList.size() == 0){
                            //没有数据
                            Toast.makeText(activity, activity.getResources().getString(R.string.NullResult), Toast.LENGTH_SHORT).show();
                        }
                        implBindPresenter.setParts(partsList);
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
            //发送弹框
            activity.setMessage();
            Log.d("开始执行异步线程", "");
        }
    }
}
