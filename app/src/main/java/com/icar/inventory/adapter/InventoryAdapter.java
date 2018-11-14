package com.icar.inventory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icar.inventory.InventoryActivity;
import com.icar.inventory.R;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.presenter.InventoryPresenter;
import com.icar.inventory.presenter.impl.ImplInventoryPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by light on 2016/3/15.
 */
public class InventoryAdapter extends BaseAdapter implements ImplInventoryPresenter {
    protected LayoutInflater inflater;
    List<PartsDetails> list = new ArrayList<>();
    protected InventoryActivity activity;
    protected InventoryPresenter inventoryPresenter;

    public static String maskId;

    public InventoryAdapter(InventoryActivity activity){
        this.activity = activity;
        //加载中
        activity.setMessage(InventoryActivity.LIST_LOADING);
            activity.setDialog();
            inventoryPresenter = new InventoryPresenter(this, activity);
            inventoryPresenter.setInventoryList();
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
        PartsDetails parts = list.get(position);
        TextView partsId = (TextView) convertView.findViewById(R.id.partsId);//配件名称
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);//配件名称
        TextView itemCode = (TextView) convertView.findViewById(R.id.itemCode);//配件编码
        TextView supplier = (TextView) convertView.findViewById(R.id.supplier);//供应商编码
        TextView inventoryFlag = (TextView) convertView.findViewById(R.id.inventoryFlag);//盘点状态
        TextView sysQuantity = (TextView) convertView.findViewById(R.id.bindText);//盘点状态
        TextView quantity = (TextView) convertView.findViewById(R.id.quantity);//盘点状态
        TextView flagTag = (TextView) convertView.findViewById(R.id.flagTag);//绑定域标记
        partsId.setText(String.valueOf(parts.getId()));
        itemName.setText(parts.getItemName());
        itemCode.setText(convertView.getResources().getString(R.string.itemCode) + parts.getItemCode());
        //supplier.setText(convertView.getResources().getString(R.string.supplier) + parts.getSupplier());
        //绑定标记
        flagTag.setText(String.valueOf(parts.getBindFlag()));
        //数量
        sysQuantity.setText("库存数" + parts.getSysQuantity());
        quantity.setText("已盘数" + parts.getQuantity());
        quantity.setTextColor(parts.getSysQuantity() != parts.getQuantity() ? convertView.getResources().getColor(R.color.nksTextColor1) : convertView.getResources().getColor(R.color.nksTextColorGreen));
        //inventoryFlag.setVisibility(View.GONE);
        //判断盘点状态
//        String invText = "";
//        int invColor = 0;
        //0未盘点 1未盘到 2已盘点
//        switch (parts.getInventoryFlag()){
//            case 0:
//                invText = convertView.getResources().getString(R.string.notInv);
//                invColor = convertView.getResources().getColor(R.color.nksTextColor1);
//                break;
//            case 1:
//                invText = convertView.getResources().getString(R.string.notInved);
//                invColor = convertView.getResources().getColor(R.color.white1);
//                break;
//            case 2:
//                invText = convertView.getResources().getString(R.string.onceInv);
//                invColor = convertView.getResources().getColor(R.color.nksTextColorGreen);
//                break;
//        }
//        inventoryFlag.setText(invText);
//        inventoryFlag.setTextColor(invColor);
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
    public void saveInventory() {

    }

    @Override
    public void queryBarcode() {

    }
}
