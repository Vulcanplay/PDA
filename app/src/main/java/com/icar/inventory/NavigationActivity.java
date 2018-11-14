package com.icar.inventory;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.yoojia.anyversion.AnyVersion;
import com.github.yoojia.anyversion.NotifyStyle;
import com.github.yoojia.anyversion.Version;
import com.icar.inventory.application.OurApplication;
import com.icar.inventory.helper.PermissionsChecker;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zbar.lib.CaptureActivity;


public class NavigationActivity extends Activity implements View.OnClickListener {

    //读取XML
    //UI
    protected ImageButton bindSelectionStore, inventorySelectStore, partsRecognition, patrol;
    protected Button logOut;
    //1绑定.2盘库.3巡查 4.配件识别
    public final static int BIND_TAG = 1;
    public final static int INVENTORY_TAG = 2;
    public final static int PATROL_TAG = 3;
    public final static int  PARTS_TAG = 4;
    public final static int LOG_OUT = 5;

    //获取用户信息
    protected SharedPreferences sf;

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.SYSTEM_ALERT_WINDOW,
    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        // 缺少权限时, 进入权限配置页面
//        mPermissionsChecker = new PermissionsChecker(this);
//        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
//            startPermissionsActivity();
//        }
        getView();
        setButtonPower();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case R.id.loginOut:
                getSharedPreferences("userInfo",0).edit().clear().commit();
                startActivity(new Intent(this,LoginActivity.class));
                this.finish();
                break;
            case R.id.checkUpdate:
                AnyVersion version = AnyVersion.getInstance();
                try {
                    PackageManager manager = getPackageManager();
                    PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
                    int nowVCode = info.versionCode;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                version.check(NotifyStyle.Dialog);
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /*
            * 控制按钮权限
            * */
    private void setButtonPower() {
        //加载XML数据
        sf = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        bindSelectionStore.setOnClickListener(this);//绑定
        inventorySelectStore.setOnClickListener(this);//盘点
        partsRecognition.setOnClickListener(this);//配件识别
        patrol.setOnClickListener(this);//巡查
    }

    private boolean getButtonPower(String type) {
        boolean r = false;
        for (String str : sf.getString("buttonPower", "").split(",")) {
            Log.d("str:", str);
            if(!str.equals(type))
                r = false;
            else{
                r = true;
                break;
            }
        }
        return r;
    }

    private void getView() {
        bindSelectionStore = (ImageButton) findViewById(R.id.bindText);
        inventorySelectStore = (ImageButton) findViewById(R.id.inventory);
        partsRecognition = (ImageButton) findViewById(R.id.parts);
        patrol = (ImageButton) findViewById(R.id.patrol);
        logOut = (Button) findViewById(R.id.logout);

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        //传点击标示
        Intent intent = new Intent(NavigationActivity.this, OptionShopActivity.class);
        //要去的Activity
        int tag = 0;
        switch (v.getId()) {
            case R.id.logout:
                //清除
                tag = LOG_OUT;
                break;
            case R.id.bindText:
                if(getButtonPower("1")){
                    tag = BIND_TAG;
                    Intent bindInventory = new Intent(this, BindActivity.class);
                    bundle.putInt("tag",BIND_TAG);
                    bindInventory.putExtras(bundle);
                    startActivity(bindInventory);
                }else{
                    Toast.makeText(NavigationActivity.this, "未拥有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.inventory:
                if(getButtonPower("2")){
                    tag = INVENTORY_TAG;
                    Intent intentInventory = new Intent(this, InventoryActivity.class);
                    bundle.putInt("tag",INVENTORY_TAG);
                    intentInventory.putExtras(bundle);
                    startActivity(intentInventory);
                }else{
                    Toast.makeText(NavigationActivity.this, "未拥有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.parts:
                if(getButtonPower("3")){
                    //配件识别
                    Intent intentCapture = new Intent(this, CaptureActivity.class);
                    bundle.putInt("tag",PARTS_TAG);
                    intentCapture.putExtras(bundle);
                    startActivityForResult(intentCapture, 0);
                    tag = PARTS_TAG;
                }else{
                    Toast.makeText(NavigationActivity.this, "未拥有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.patrol:
                if(getButtonPower("4")){
                    tag = PATROL_TAG;
                    bundle.putInt("tag", tag);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(NavigationActivity.this, "未拥有权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        if(tag == LOG_OUT){
            getSharedPreferences("userInfo",0).edit().clear().commit();
            startActivity(new Intent(this,LoginActivity.class));
            this.finish();
        }
        //this.finish();
    }
}
