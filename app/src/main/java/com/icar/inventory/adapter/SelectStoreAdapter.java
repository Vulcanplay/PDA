package com.icar.inventory.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.icar.inventory.InventoryActivity;
import com.icar.inventory.OptionShopActivity;
import com.icar.inventory.PatrolActivity;
import com.icar.inventory.R;
import com.icar.inventory.model.Store;
import com.icar.inventory.presenter.StorePresenter;
import com.icar.inventory.presenter.impl.ImplStorePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by light on 2016/3/8.
 */
public class SelectStoreAdapter extends BaseAdapter implements ImplStorePresenter {
    protected LayoutInflater inflater;
    protected StorePresenter storePresenter;
    protected String query = "";
    protected Context context;
    List<Store> list = new ArrayList<>();
    OptionShopActivity activity;

    public SelectStoreAdapter(Context context, OptionShopActivity activity){
        init(context,activity);
    }

    public SelectStoreAdapter(Context context, String query,OptionShopActivity activity) {
        this.query = query;
        init(context,activity);
    }
    public void init(Context context,OptionShopActivity activity){
        this.context = context;
        this.activity = activity;
        Log.d("QUERY---",query);
        //加载中
        activity.setMessage();
        storePresenter = new StorePresenter(this,query,activity);
        storePresenter.getStore();
        inflater = (LayoutInflater) context
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
                    .inflate(R.layout.select_store_list_view_item, null);
        }
        Store store = list.get(position);
        TextView storeId = (TextView) convertView.findViewById(R.id.store_id);
        TextView storeName = (TextView) convertView.findViewById(R.id.store_text);
        //设置右边图标的字体
        TextView storeItemRight = (TextView) convertView.findViewById(R.id.store_item_rigth);
        storeItemRight.setTypeface(Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf"));
        storeId.setText(String.valueOf(store.getShopId()));
        storeName.setText(store.getShopName());
        return convertView;
    }

    @Override
    public void setStore(List<Store> storeList) {
        this.list = storeList;
        this.notifyDataSetChanged();
    }
}
