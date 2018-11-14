package com.icar.inventory.services;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.icar.inventory.LoginActivity;
import com.icar.inventory.NavigationActivity;
import com.icar.inventory.R;
import com.icar.inventory.helper.HttpUtil;
import com.icar.inventory.helper.HttpUtilEvent;
import com.icar.inventory.helper.JSONHelper;
import com.icar.inventory.helper.WebServicesTool;
import com.icar.inventory.model.Employee;
import com.icar.inventory.presenter.LoginPresenter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.victor.loading.newton.NewtonCradleLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by light on 2016/3/6.
 */
public class LoginServices {

    //保存登陆XML
    private SharedPreferences sp;
    SharedPreferences.Editor editor;
    Employee employee = null;
    LoginActivity activity;

    public boolean checkLogin(Employee employee,LoginActivity activity) {
        this.employee = employee;
        this.activity = activity;
        //存储到 XML
        sp = this.activity.getSharedPreferences("userInfo", 0);
        editor = sp.edit();
        //异步实例 调用
        LoginAsyncTask la = new LoginAsyncTask();
        la.execute();
        return false;
    }
    /**
     * 这里的Integer参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    private class LoginAsyncTask extends AsyncTask<Integer, Integer, String>{

        @Override
        protected String doInBackground(Integer... params) {

            LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
            map.put("json", JSONHelper.toJSON(employee));
            String result = "";
            try {
                result = WebServicesTool.Connect("GetLoginUser", map);
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
            Employee employee1 = null;
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
                        employee1 = JSONHelper.parseObject(result,Employee.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (employee1 != null && employee1.getId() != 0) {
                        //登陆数据保存
                        editor.putInt("idKey",employee1.getId());
                        editor.putString("userName", employee1.getUserName());
                        editor.putString("userID", employee1.getUserID());
                        editor.putInt("userType",employee1.getUserType());
                        editor.putInt("shopId",employee1.getShopId());
                        editor.putString("buttonPower",employee1.getButtonPower());
                        //提交数据
                        editor.commit();
                        //登陆处理
                        //Toast.makeText(activity, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity,NavigationActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }else{
                        Toast.makeText(activity, activity.getResources().getString(R.string.NullResultLogin), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            activity.dialog.dismiss();

            Log.d("异步操作执行结束:", result);
        }


        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {
            Log.d("开始执行异步线程","");
        }
    }
    /**
     * 这里的Intege参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
//     */
//    @Override
//    protected void onProgressUpdate(Integer... values) {
//        int vlaue = values[0];
//        progressBar.setProgress(vlaue);
//    }
}
