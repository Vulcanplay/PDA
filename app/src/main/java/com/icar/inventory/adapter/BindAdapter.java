package com.icar.inventory.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icar.inventory.BindActivity;
import com.icar.inventory.NavigationActivity;
import com.icar.inventory.R;
import com.icar.inventory.model.BindResult;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.model.PartsParameters;
import com.icar.inventory.presenter.BindPresenter;
import com.icar.inventory.presenter.impl.ImplBindPresenter;
import com.zbar.lib.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by light on 2016/3/10.
 */
public class BindAdapter extends BaseAdapter implements ImplBindPresenter,View.OnClickListener {

    protected LayoutInflater inflater;

    protected BindPresenter bindPresenter;
    //类型
    protected String storeId = null;
    protected Context context;
    List<PartsDetails> list = new ArrayList<>();
    //canshuduixiang
    BindActivity activity;
    public BindAdapter(PartsParameters partsParameters){
        init(partsParameters);
    }
    //初始化
    protected void init(PartsParameters partsParameters){
        this.activity = partsParameters.getActivity();
        this.storeId = partsParameters.getStoreId();
        bindPresenter = new BindPresenter(this);
        bindPresenter.getParts(partsParameters);
        inflater = (LayoutInflater) activity.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取每一项的布局
        if (convertView == null) {
            convertView = (ViewGroup) inflater
                    .inflate(R.layout.parts_item, null);
        }
        TextView orderNo = (TextView) convertView.findViewById(R.id.orderNo);//配件名称
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);//配件名称
        TextView itemCode = (TextView) convertView.findViewById(R.id.itemCode);//配件编码
        TextView supplier = (TextView) convertView.findViewById(R.id.supplier);//供应商编码

        TextView bindNotVal = (TextView) convertView.findViewById(R.id.bindNotVal);//未绑定
        TextView bindNot = (TextView) convertView.findViewById(R.id.bindNot);//已绑定

        TextView bind = (TextView) convertView.findViewById(R.id.bindText);//未绑定
        TextView bindVal = (TextView) convertView.findViewById(R.id.bindVal);//已绑定

        TextView location = (TextView) convertView.findViewById(R.id.locationVal);//库位Val

        TextView flagTag = (TextView) convertView.findViewById(R.id.flagTag);//绑定域标记
        Button bindScanning = (Button) convertView.findViewById(R.id.bindScanning);


        TextView orderNoVal = (TextView) convertView.findViewById(R.id.orderNoVal);//配件名称
        TextView itemCodeVal = (TextView) convertView.findViewById(R.id.itemCodeVal);//配件编码
        TextView supplierVal = (TextView) convertView.findViewById(R.id.supplierVal);//供应商编码
        bindScanning.setOnClickListener(this);
        PartsDetails parts = list.get(position);
        itemName.setText(parts.getItemName());
        orderNo.setText(convertView.getResources().getString(R.string.orderNo));
        itemCode.setText(convertView.getResources().getString(R.string.itemCode));
        supplier.setText(convertView.getResources().getString(R.string.supplier));

        orderNoVal.setText(String.valueOf(parts.getOrderNo()));
        itemCodeVal.setText(parts.getItemCode());
        supplierVal.setText(parts.getSupplier());
        //绑定标记
        flagTag.setText(String.valueOf(parts.getBindFlag()));
        //未绑定
        bindNot.setText(convertView.getResources().getString(R.string.bindNot) + ":");
        bindNotVal.setText(String.valueOf(parts.getSysQuantity()));
        //已绑定
        bind.setText(convertView.getResources().getString(R.string.bind) + ":");
        bindVal.setText(String.valueOf(parts.getQuantity()));
        //库位
        location.setText(parts.getLocation());
        //判断绑定数量
        bind.setTextColor(parts.getSysQuantity() != 0 ? convertView.getResources().getColor(R.color.nksTextColor1) : convertView.getResources().getColor(R.color.nksTextColorGreen));
        bindVal.setTextColor(parts.getSysQuantity() != 0 ? convertView.getResources().getColor(R.color.nksTextColor1) : convertView.getResources().getColor(R.color.nksTextColorGreen));

        return convertView;
    }

    @Override
    public void setParts(List<PartsDetails> partsList) {
        this.list = partsList;
        this.notifyDataSetChanged();
    }

    @Override
    public void setBind(BindResult bindResult) {

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = activity.getIntent().getExtras();
        switch (v.getId()){
            case R.id.bindScanning:
                Intent intentCapture = new Intent(activity, CaptureActivity.class);
                // =0就是完了不让点了
                TextView bindNotVal = (TextView) ((RelativeLayout)v.getParent()).findViewById(R.id.bindNotVal);
                int bindNotCount = Integer.parseInt(bindNotVal.getText().toString());
                if(bindNotCount != 0) {
                    TextView orderNo = (TextView) ((RelativeLayout) v.getParent()).findViewById(R.id.orderNoVal);
                    TextView supplier = (TextView) ((RelativeLayout) v.getParent()).findViewById(R.id.supplierVal);
                    TextView itemCode = (TextView) ((RelativeLayout) v.getParent()).findViewById(R.id.itemCodeVal);
                    TextView itemName = (TextView) ((RelativeLayout) v.getParent()).findViewById(R.id.itemName);
                    Log.d("ItemName", itemName.getText().toString());
                    bundle.putInt("tag", NavigationActivity.BIND_TAG);
                    bundle.putString("orderNo", orderNo.getText().toString());
                    bundle.putString("supplier", supplier.getText().toString());
                    bundle.putString("itemCode", itemCode.getText().toString());
                    bundle.putString("itemName", itemName.getText().toString());
                    //剩余数量
                    bundle.putInt("bindNotCount", bindNotCount);
                    //bundle.putString("storeId",storeId);
                    intentCapture.putExtras(bundle);
                    activity.startActivity(intentCapture);
                    activity.finish();
                }else {
                    Toast.makeText(activity , "已绑定完成！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
