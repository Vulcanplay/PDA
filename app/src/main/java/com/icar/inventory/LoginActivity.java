package com.icar.inventory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.icar.inventory.model.Employee;
import com.icar.inventory.presenter.LoginPresenter;
import com.icar.inventory.presenter.impl.ImplLoginPresenter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.victor.loading.newton.NewtonCradleLoading;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements ImplLoginPresenter,View.OnClickListener {
    //主导者的引用
    protected LoginPresenter loginPresenter;
    // UI references.
    private TextView userNameLeft;
    private TextView userPassWordLeft;
    //编辑框
    private EditText userNameText;
    private EditText userPasswordText;

    private Button logInButton;

    //获取用户信息
    protected SharedPreferences sf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //加载XML数据
        sf = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        if(sf.getInt("idKey", 0) != 0){
            //已登陆过 直接进入导航视图
            Intent intent = new Intent(this,NavigationActivity.class);
            startActivity(intent);
            //销毁
            this.finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setFontAwesom();
        getViewAndSetListener();
    }

    public DialogPlus dialog;
    public NewtonCradleLoading newtonCradleLoading;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                dialog = DialogPlus.newDialog(this)
                        //.setAdapter()//设置适配器
                        .setExpanded(true)//Set expand animation default height 设置动画 以及高度
                        .setContentHolder(new ViewHolder(R.layout.dialog_login))
                        .setGravity(Gravity.BOTTOM)//设置显示的位置
                        .setCancelable(false)//设置点击背景区域是否可以被关闭
                        .setContentBackgroundResource(R.color.black_overlay)//设置背景颜色
                        .create();  // This will enable the expand feature, (similar to android L share dialog)
                dialog.show();

                newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
                newtonCradleLoading.start();
                newtonCradleLoading.setLoadingColor(getResources().getColor(R.color.nksTextColor1));
                loginPresenter = new LoginPresenter(this,LoginActivity.this);
                loginPresenter.setLogin(new Employee(userNameText.getText().toString(),userPasswordText.getText().toString()));
                break;
            default:
                break;
        }
    }

    @Override
    public void logIn(boolean isLogIn) {
        Log.d("OK了","0.0");
    }

    private void getViewAndSetListener(){
        userNameText = (EditText)findViewById(R.id.usernameEdit);
        userPasswordText = (EditText)findViewById(R.id.userPasswordEdit);
        logInButton = (Button)findViewById(R.id.loginButton);
        logInButton.setOnClickListener(this);
    }

    //设置字体图标
    private void setFontAwesom(){
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        userNameLeft=(TextView) findViewById(R.id.userNameLeft);
        userPassWordLeft=(TextView) findViewById(R.id.userPassWordLeft);
        userNameLeft.setTypeface(font);
        userPassWordLeft.setTypeface(font);
    }

//    private class DomeAsyncTask extends AsyncTask<Void, String, String> {
//        @Override
//        protected String doInBackground(Void... params) {
//            //publishProgress(); // 提交进度: 每5%进度时,更新一次UI
////                if (isCancelled())
////                    break; // 异步任务中途被取消
//            //return wsh.GetResponse("GetLoginUser1"); // 这个值会传入给onPostExecute()方法
//            return "11";
//        }
//
//        // 更新UI
//        @Override
//        protected void onProgressUpdate(String... values) {
//            //testView.setText("已经完成了" + values[0] + "%");
//        }
//
//        // 当异步处理结束后
//        @Override
//        protected void onPostExecute(String integer) {
//            Log.d("Test",integer);
//            //testView.setText(integer);
//        }
//    }
}

