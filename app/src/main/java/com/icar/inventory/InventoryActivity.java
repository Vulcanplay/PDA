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
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.presenter.InventoryPresenter;
import com.icar.inventory.presenter.impl.ImplInventoryPresenter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.victor.loading.newton.NewtonCradleLoading;
import com.zbar.lib.CaptureActivity;

import java.util.List;

public class InventoryActivity extends AppCompatActivity implements View.OnClickListener,ImplInventoryPresenter {

    //定义控件
    Button invScanning,invSubmit;
    GridView inventoryGridView;
    TextView message;
    //适配器
    protected InventoryAdapter inventoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        getView();
    }

    //获取视图控件
    protected void getView(){
        invScanning = (Button) findViewById(R.id.invScanning);
        invSubmit = (Button) findViewById(R.id.invSubmit);
        inventoryGridView = (GridView) findViewById(R.id.inventoryGridView);
        message = (TextView)findViewById(R.id.loadingMessage);

        invScanning.setOnClickListener(this);
        invSubmit.setOnClickListener(this);
        setInventoryAdapter();
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

    //适配器
    protected void setInventoryAdapter(){
        inventoryAdapter = new InventoryAdapter(InventoryActivity.this);
        inventoryGridView.setAdapter(inventoryAdapter);
    }

    public DialogPlus dialogUpload;
    public NewtonCradleLoading newtonCradleLoadingUpload;
    InventoryPresenter inventoryPresenter;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invScanning:
                Bundle bundle = getIntent().getExtras();
                Intent intentCapture = new Intent(this, CaptureActivity.class);
                bundle.putString("maskId", InventoryAdapter.maskId);
                intentCapture.putExtras(bundle);
                startActivityForResult(intentCapture, 0);
                this.finish();
                break;
            case R.id.invSubmit:
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
                inventoryPresenter = new InventoryPresenter(this,InventoryActivity.this);
                inventoryPresenter.saveInventory(InventoryAdapter.maskId);
                break;
        }
    }

    @Override
    public void setInventory(List<PartsDetails> partsList) {

    }

    @Override
    public void saveInventory() {
        inventoryGridView.setAdapter(new InventoryAdapter(this));
        this.finish();
    }

    @Override
    public void queryBarcode() {

    }
}
