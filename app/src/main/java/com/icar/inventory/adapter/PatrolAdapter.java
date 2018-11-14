package com.icar.inventory.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icar.inventory.InventoryActivity;
import com.icar.inventory.PatrolActivity;
import com.icar.inventory.R;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.presenter.PatrolPresenter;
import com.icar.inventory.presenter.impl.ImplPatrolPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by light on 2016/3/15.
 */
public class PatrolAdapter extends BaseAdapter implements ImplPatrolPresenter {
    protected LayoutInflater inflater;
    List<PartsDetails> list = new ArrayList<>();
    protected PatrolActivity activity;
    protected PatrolPresenter patrolPresenter;

    public static String maskId;

    public PatrolAdapter(PatrolActivity activity,String storeId){
        this.activity = activity;
        activity.setMessage(PatrolActivity.LIST_LOADING);
        activity.setDialog();
        patrolPresenter = new PatrolPresenter(this, activity);
        patrolPresenter.setPatrolList(storeId);
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
                    .inflate(R.layout.inventory_item, null);
        }
        TextView partsId = (TextView) convertView.findViewById(R.id.partsId);//配件名称
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);//配件名称
        TextView itemCode = (TextView) convertView.findViewById(R.id.itemCode);//配件编码
        TextView supplier = (TextView) convertView.findViewById(R.id.supplier);//供应商编码
        TextView inventoryFlag = (TextView) convertView.findViewById(R.id.inventoryFlag);//盘点状态
        TextView sysQuantity = (TextView) convertView.findViewById(R.id.bindText);//盘点状态
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);//盘点状态
        TextView flagTag = (TextView) convertView.findViewById(R.id.flagTag);//绑定域标记



        PartsDetails parts = list.get(position);
        partsId.setText(String.valueOf(parts.getId()));
        itemName.setText(parts.getItemName());
        itemCode.setText(convertView.getResources().getString(R.string.itemCode) + parts.getItemCode());
        //绑定标记
        flagTag.setText(String.valueOf(parts.getBindFlag()));
        //数量
        sysQuantity.setText(convertView.getResources().getString(R.string.sysQuantity) + parts.getSysQuantity());
        quantity.setText(convertView.getResources().getString(R.string.quantity) + parts.getQuantity());
        Log.e("巡查状态", String.valueOf(parts.getInventoryFlag()));
        //判断盘点状态
        String patText = "";
        int patColor = 0;
        //0未巡查 1已巡查
        switch (parts.getInventoryFlag()){
            case 0:
                patText = convertView.getResources().getString(R.string.notPat);
                patColor = convertView.getResources().getColor(R.color.white);
                break;
            case 1:
                patText = convertView.getResources().getString(R.string.pat);
                patColor = convertView.getResources().getColor(R.color.YELLOW);
                break;
            case 2:
                patText = convertView.getResources().getString(R.string.pated);
                patColor = convertView.getResources().getColor(R.color.nksTextColorGreen);
                break;
        }
        inventoryFlag.setText(patText);
        inventoryFlag.setTextColor(patColor);

        return convertView;
    }

    @Override
    public void setInventory(List<PartsDetails> partsList) {
        this.list = partsList;
        if (list.size() == 0){
            //没有数据
            activity.setMessage(InventoryActivity.LIST_NULL);
        }else{
            //加载完成
            activity.setMessage(InventoryActivity.LIST_LOAD);
            maskId = String.valueOf(list.get(0).getMasterId());
        }
        notifyDataSetChanged();
    }


    @Override
    public void savePatrol() {

    }

    @Override
    public void pQueryBarcode() {

    }

}
