package com.icar.inventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.presenter.PartsDetailsPresenter;
import com.icar.inventory.presenter.impl.ImplPartsDetailsPresenter;

import org.w3c.dom.Text;

public class PartsDetailsActivity extends AppCompatActivity implements ImplPartsDetailsPresenter {

    private TextView partsDetailsInfo;
    protected PartsDetailsPresenter partsDetailsPresenter;
    protected String itemCode;
    protected TextView solid,partsCode,partsName,partsBatch,partsPriceWithTax,partsWareHouse,partsLocation,partsWarehouseAge,partsSupplierType,partsSupplier,partsProductLine1,partsProductLine2,partsProductLine3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_details);

        Bundle bundle=this.getIntent().getExtras();
        itemCode = bundle.getString("itemCode");
        partsDetailsPresenter = new PartsDetailsPresenter(this, itemCode,PartsDetailsActivity.this);
        partsDetailsPresenter.setPartsDetails();
        //获取控件
        getView();
    }

    public void getView(){
        //边线
        solid = (TextView) findViewById(R.id.solid);
        solid.setVisibility(View.GONE);
        //加载中
        partsDetailsInfo = (TextView) findViewById(R.id.partsDetailsInfo);
        //商品编号
        partsCode = (TextView) findViewById(R.id.parts_details_itemCode);
        //商品名称
        partsName = (TextView) findViewById(R.id.parts_details_itemName);
        //批次号
        partsBatch = (TextView) findViewById(R.id.parts_details_batch);
        //价格
        partsPriceWithTax = (TextView) findViewById(R.id.parts_details_priceWithTax);
        //仓库
        partsWareHouse = (TextView) findViewById(R.id.parts_details_warehouse);
        //库位
        partsLocation = (TextView) findViewById(R.id.parts_details_location);
        //库龄
        partsWarehouseAge = (TextView) findViewById(R.id.parts_details_warehouseAge);
        //供应商类别
        partsSupplierType = (TextView) findViewById(R.id.parts_details_supplierType);
        //供应商名称
        partsSupplier = (TextView) findViewById(R.id.parts_details_supplier);
        //商品一级类别
        partsProductLine1 = (TextView) findViewById(R.id.parts_details_productLine1);
        //商品二级类别
        partsProductLine2 = (TextView) findViewById(R.id.parts_details_productLine2);
        //商品三级类别
        partsProductLine3 = (TextView) findViewById(R.id.parts_details_productLine3);

    }
    @Override
    public void setPartsDetails(PartsDetails partsDetails) {
        if (partsDetails.getBatch() == ""){
            partsDetailsInfo.setText("没有找到数据！");
        }else{
            //如果有数据就隐藏显示信息的地方了
            partsDetailsInfo.setVisibility(View.GONE);
            solid.setVisibility(View.INVISIBLE);
            //处理返回的数据放到UI中
            if(partsDetails.getItemName() == "null")
                partsName.setVisibility(View.GONE);
            else
                partsName.setText(getResources().getString(R.string.parts_details_itemName) + partsDetails.getItemName());

            if(partsDetails.getItemCode() == "null")
                partsCode.setVisibility(View.GONE);
            else
                partsCode.setText(getResources().getString(R.string.parts_details_itemCode) + partsDetails.getItemCode());

            if(partsDetails.getBatch() == "null")
                partsBatch.setVisibility(View.GONE);
            else
                partsBatch.setText(getResources().getString(R.string.parts_details_batch) + partsDetails.getBatch());

            if(partsDetails.getWarehouse() == "null")
                partsWareHouse.setVisibility(View.GONE);
            else
                partsWareHouse.setText(getResources().getString(R.string.parts_details_warehouse) + partsDetails.getWarehouse());

            if(partsDetails.getLocation() == "null")
                partsLocation.setVisibility(View.GONE);
            else
                partsLocation.setText(getResources().getString(R.string.parts_details_location) + partsDetails.getLocation());

            if(partsDetails.getWarehouseAge() == "null")
                partsWarehouseAge.setVisibility(View.GONE);
            else
                partsWarehouseAge.setText(getResources().getString(R.string.parts_details_warehouseAge) + partsDetails.getWarehouseAge());

            if(partsDetails.getSupplierType() == "null")
                partsSupplierType.setVisibility(View.GONE);
            else
                partsSupplierType.setText(getResources().getString(R.string.parts_details_supplierType) + partsDetails.getSupplierType());


            if(partsDetails.getSupplier() == "null")
                partsSupplier.setVisibility(View.GONE);
            else
                partsSupplier.setText(getResources().getString(R.string.parts_details_supplier) + partsDetails.getSupplier());

            if(partsDetails.getProductLine1() == "null")
                partsProductLine1.setVisibility(View.GONE);
            else
                partsProductLine1.setText(getResources().getString(R.string.parts_details_productLine1) + partsDetails.getProductLine1());

            if(partsDetails.getProductLine2() == "null")
                partsProductLine2.setVisibility(View.GONE);
            else
                partsProductLine2.setText(getResources().getString(R.string.parts_details_productLine2) + partsDetails.getProductLine2());

            partsProductLine3.setText("数量：" + partsDetails.getSysQuantity());
        }
    }
}
