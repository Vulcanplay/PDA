package com.icar.inventory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.icar.inventory.adapter.PatrolAdapter;
import com.icar.inventory.adapter.SelectStoreAdapter;
import com.icar.inventory.model.Employee;
import com.icar.inventory.presenter.LoginPresenter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.victor.loading.newton.NewtonCradleLoading;

public class OptionShopActivity extends AppCompatActivity implements EditText.OnEditorActionListener, AdapterView.OnItemClickListener {

    //获取视图
    protected AbsListView selectStoreListView;
    protected SelectStoreAdapter selectStoreAdapter;
    protected SelectStoreAdapter queryStoreAdapter;

    //入口标记 1绑定.2盘库.3巡查
    protected int tag;

    protected TextView searchIcon,message;
    //搜索框
    protected EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_shop);

        //获取入口位置
        Bundle bundle = this.getIntent().getExtras();
        tag = bundle.getInt("tag");
        setSearchView();
        setListView();
    }

    //设置搜索
    protected void setListView() {
        selectStoreListView = (AbsListView) findViewById(R.id.select_store_list_view);
        selectStoreAdapter = new SelectStoreAdapter(getApplicationContext(),this);
        selectStoreListView.setAdapter(selectStoreAdapter);
        selectStoreListView.setOnItemClickListener(this);
    }

    //设置搜索框颜色
    private void setSearchView() {
        //ziti
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        searchIcon = (TextView) findViewById(R.id.search_icon);
        searchIcon.setTypeface(font);
        searchText = (EditText) findViewById(R.id.search_text);
        searchText.setOnEditorActionListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//ActionBar SearchView
//  MenuInflater mflater=new MenuInflater(this);
//        mflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 搜索
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String query = "";
        if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_SEARCH) {
            //do something;
            query = v.getText().toString();
            queryStoreAdapter = new SelectStoreAdapter(getApplicationContext(), query, this);
            selectStoreListView.setAdapter(queryStoreAdapter);
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("hahah ----",query);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String storeId = ((TextView) view.findViewById(R.id.store_id)).getText().toString();


        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Activity activity = null;
        switch (tag) {
            //绑定
            case NavigationActivity.BIND_TAG:
                activity = new BindActivity();
                break;
            //盘点
//            case NavigationActivity.INVENTORY_TAG:
//                activity = new InventoryActivity();
//                break;
            //巡查
            case NavigationActivity.PATROL_TAG:
                activity = new PatrolActivity();
                break;
        }
        if (activity != null) {
            intent.setClass(this, activity.getClass());
            bundle.putString("storeId", storeId);
            bundle.putInt("tag", tag);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public DialogPlus dialog;
    public NewtonCradleLoading newtonCradleLoading;

    //弹框
    public void setMessage(){
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //Toast.makeText(OptionShopActivity.this, "finish!", Toast.LENGTH_LONG).show();
            this.finish();
        }
        return false;
    }

}
