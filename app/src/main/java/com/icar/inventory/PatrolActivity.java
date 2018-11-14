package com.icar.inventory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.icar.inventory.adapter.InventoryAdapter;
import com.icar.inventory.adapter.PatrolAdapter;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.presenter.InventoryPresenter;
import com.icar.inventory.presenter.PatrolPresenter;
import com.icar.inventory.presenter.impl.ImplPatrolPresenter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.victor.loading.newton.NewtonCradleLoading;
import com.zbar.lib.CaptureActivity;

import java.util.List;

public class PatrolActivity extends AppCompatActivity implements View.OnClickListener,ImplPatrolPresenter {

    //定义控件
    Button patScanning,patSubmit;
    GridView patrolGridView;
    TextView message;
    //适配器
    protected PatrolAdapter patAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol);
        getView();
    }

    //获取视图控件
    protected void getView(){
        patScanning = (Button) findViewById(R.id.patScanning);
        patSubmit = (Button) findViewById(R.id.patSubmit);
        patrolGridView = (GridView) findViewById(R.id.patrolGridView);
        message = (TextView)findViewById(R.id.loadingMessage);

        patScanning.setOnClickListener(this);
        patSubmit.setOnClickListener(this);
        setPatrolAdapter();
    }


    public DialogPlus dialog;
    public NewtonCradleLoading newtonCradleLoading;

    //弹框
    public void setDialog(){
        dialog = DialogPlus.newDialog(this)
                //.setAdapter()//设置适配器
                .setExpanded(true, ViewGroup.LayoutParams.MATCH_PARENT)//Set expand animation default height 设置动画 以及高度
                .setContentHolder(new ViewHolder(R.layout.dialog_loading))
                .setGravity(Gravity.TOP)//设置显示的位置
                .setCancelable(false)//设置点击背景区域是否可以被关闭
                .setContentBackgroundResource(R.color.black_overlay)//设置背景颜色
                .create();  // This will enable the expand feature, (similar to android L share dialog)
        dialog.show();
        newtonCradleLoading = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
        newtonCradleLoading.start();
        newtonCradleLoading.setLoadingColor(getResources().getColor(R.color.nksTextColor1));
    }
    public static final int LIST_LOADING = 1;//加载中
    public static final int LIST_LOAD = 2;//已加载
    public static final int LIST_NULL = 3;//无数据

    public void setMessage(int status){
        switch (status){
            case LIST_LOADING:
                message.setVisibility(View.VISIBLE);
                message.setText(getResources().getString(R.string.onLoading));
                break;
            case LIST_NULL:
                message.setVisibility(View.VISIBLE);
                message.setText(getResources().getString(R.string.notResult));
                break;
            case LIST_LOAD:
                message.setVisibility(View.GONE);
                break;
        }
    }
    String storeId = "";
    //适配器
    protected void setPatrolAdapter(){
        storeId = getIntent().getExtras().getString("storeId");
        patAdapter = new PatrolAdapter(this,storeId);
        patrolGridView.setAdapter(patAdapter);
    }
    public DialogPlus dialogUpload;
    public NewtonCradleLoading newtonCradleLoadingUpload;
    PatrolPresenter patrolPresenter;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.patScanning:
                Bundle bundle = getIntent().getExtras();
                Intent intentCapture = new Intent(this, CaptureActivity.class);
                bundle.putString("maskId",PatrolAdapter.maskId);
                intentCapture.putExtras(bundle);
                startActivityForResult(intentCapture, 0);
                this.finish();
                break;
            case R.id.patSubmit:
                //上传
                dialogUpload = DialogPlus.newDialog(this)
                        //.setAdapter()//设置适配器
                        .setExpanded(true, ViewGroup.LayoutParams.MATCH_PARENT)//Set expand animation default height 设置动画 以及高度
                        .setContentHolder(new ViewHolder(R.layout.dialog_submit))
                        //.setGravity(Gravity)//设置显示的位置
                        .setCancelable(false)//设置点击背景区域是否可以被关闭
                        .setContentBackgroundResource(R.color.black_overlay)//设置背景颜色
                        .create();  // This will enable the expand feature, (similar to android L share dialog)
                dialogUpload.show();
                newtonCradleLoadingUpload = (NewtonCradleLoading) findViewById(R.id.newton_cradle_loading);
                newtonCradleLoadingUpload.start();
                newtonCradleLoadingUpload.setLoadingColor(getResources().getColor(R.color.nksTextColor1));

                //提交
                patrolPresenter = new PatrolPresenter(this,PatrolActivity.this);
                patrolPresenter.saveInventory(PatrolAdapter.maskId);
                break;
        }
    }

    /*会掉*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        patrolGridView.setAdapter(new PatrolAdapter(this,storeId));
    }

    @Override
    public void setInventory(List<PartsDetails> partsList) {

    }

    @Override
    public void savePatrol() {
        patrolGridView.setAdapter(new PatrolAdapter(this, getIntent().getExtras().getString("storeId")));
    }

    @Override
    public void pQueryBarcode() {

    }

}
