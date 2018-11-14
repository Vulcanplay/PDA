package com.icar.inventory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.icar.inventory.adapter.BindAdapter;
import com.icar.inventory.adapter.SelectStoreAdapter;
import com.icar.inventory.model.PartsParameters;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.victor.loading.newton.NewtonCradleLoading;
import com.zbar.lib.CaptureActivity;

import org.w3c.dom.Text;

public class BindActivity extends AppCompatActivity implements TextView.OnEditorActionListener{

    //tabs
    protected View bottomView;
    protected LinearLayout bottomBar;
    protected LinearLayout bottomBarLast;
    protected TextView bottomBarTextView;
    protected TextView bottomBarTextViewLast;

    //dangqiantab
    protected int type = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        setDefaultAdapter();
        settingBottomBar();

    }

    //适配器
    BindAdapter bindAdapter;
    ListView bindListView;
    TextView textViewIcon;
    String storeId = "99999";
    //搜索框
    protected EditText searchText;
    private void setDefaultAdapter() {
        Bundle bundle = getIntent().getExtras();
        //storeId = bundle.getString("storeId");
        //初始化适配器
        bindListView = (ListView) findViewById(R.id.bind_grid_view);
        bindAdapter = new BindAdapter(new PartsParameters(type,storeId,"",this));
        bindListView.setAdapter(bindAdapter);
        //设置搜索图标
        textViewIcon = (TextView) findViewById(R.id.search_icon);
        textViewIcon.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "fontawesome-webfont.ttf"));
        searchText = (EditText) findViewById(R.id.search_text);
        searchText.setOnEditorActionListener(this);
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
//    //刷新适配器
//    protected void refreshAdapter(int type, String query, BindActivity activity){
//        //bindGridView.setAdapter(new BindAdapter(getApplicationContext(), type, query,activity,storeId));
//        bindGridView.setAdapter(new BindAdapter(getApplicationContext(), type, query,activity,storeId));
//    }
    //刷新适配器
    protected void refreshAdapter(PartsParameters partsParameters){
        //bindGridView.setAdapter(new BindAdapter(getApplicationContext(), type, query,activity,storeId));
        bindListView.setAdapter(new BindAdapter(partsParameters));
    }


    protected View.OnClickListener bottomBarOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            LinearLayout ly = (LinearLayout) v;
            TextView tv = (TextView) ((LinearLayout) v).getChildAt(0);
            if (bottomBarLast != null && bottomBarTextViewLast != null) {
                bottomBarLast.setSelected(false);
                bottomBarTextViewLast.setSelected(false);
            }
            ly.setSelected(true);
            tv.setSelected(true);
            bottomBarLast = ly;
            bottomBarTextViewLast = tv;

            switch (v.getId()) {
                case R.id.bind_tabs_all:
                    //判断是不是在当前TAB
                    type = 1;
                    break;
                case R.id.bind_tabs_binded:
                    type = 2;
                    break;
                case R.id.bind_tabs_bind:
                    type = 3;
                    //判断是不是在当前TAB
                    break;
            }
            refreshAdapter(new PartsParameters(type,storeId,"",BindActivity.this));
        }
    };
    protected void settingBottomBar() {
        bottomBar = (LinearLayout) findViewById(R.id.bind_tabs);
        for (int i = 0; i < bottomBar.getChildCount(); i++) {
            bottomView = bottomBar.getChildAt(i);
            if (i == 1) {
                bottomView.setSelected(true);
                // 获取TextView
                bottomBarTextView = (TextView) ((LinearLayout) bottomView)
                        .getChildAt(0);
                // 设置TextView选中
                bottomBarTextView.setSelected(true);
                // 记录
                bottomBarLast = (LinearLayout) bottomView;
                bottomBarTextViewLast = bottomBarTextView;
            }
            bottomView.setOnClickListener(bottomBarOnClickListener);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String query = "";
        if (actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_SEARCH) {
            //do something;
            query = v.getText().toString();
            refreshAdapter(new PartsParameters(type,storeId,query,BindActivity.this));
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        Log.d("hahah ----",query);
        return false;
    }

}
