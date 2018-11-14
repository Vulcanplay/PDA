package com.zbar.lib;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.icar.inventory.BindActivity;
import com.icar.inventory.InventoryActivity;
import com.icar.inventory.NavigationActivity;
import com.icar.inventory.PartsDetailsActivity;
import com.icar.inventory.PatrolActivity;
import com.icar.inventory.R;
import com.icar.inventory.adapter.InventoryAdapter;
import com.icar.inventory.adapter.PatrolAdapter;
import com.icar.inventory.adapter.SelectStoreAdapter;
import com.icar.inventory.model.BindResult;
import com.icar.inventory.model.Parts;
import com.icar.inventory.model.PartsDetails;
import com.icar.inventory.model.PartsParameters;
import com.icar.inventory.presenter.BindPresenter;
import com.icar.inventory.presenter.InventoryPresenter;
import com.icar.inventory.presenter.PatrolPresenter;
import com.icar.inventory.presenter.impl.ImplBindPresenter;
import com.icar.inventory.presenter.impl.ImplInventoryPresenter;
import com.icar.inventory.presenter.impl.ImplPatrolPresenter;
import com.zbar.lib.Activity.Test;
import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureActivityHandler;
import com.zbar.lib.decode.InactivityTimer;

import org.w3c.dom.Text;

/**
 * 作者: 陈涛(1076559197@qq.com)
 *
 * 时间: 2014年5月9日 下午12:25:31
 *
 * 版本: V_1.0.0
 *
 * 描述: 扫描界面
 */
public class CaptureActivity extends Activity implements Callback,View.OnClickListener,ImplBindPresenter,ImplInventoryPresenter,ImplPatrolPresenter {

	private static final int INVENTORY_SCANNING = 0;
	private static final int PATROL_SCANNING = 1;
	private CaptureActivityHandler handler;
	private boolean hasSurface;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private int x = 0;
	private int y = 0;
	private int cropWidth = 0;
	private int cropHeight = 0;
	private RelativeLayout mContainer = null;
	private RelativeLayout mCropLayout = null;
	protected EditText inputCode,inputQuantity;
	protected TextView inputCodePower,inputQuantityPower,inputTextQuantity,inputQuantityForIndex,lightPower,scanCount,scanTitleLeft,scanTitleRight,itmeCodeTextView,bindTagTextView;
	protected String storeId;
	protected String orderNo = "";
	protected String supplier = "";
	protected String itemCode = "";
	protected String maskId = "";
	protected String itemName = "";
	protected int bindNotCount = 0;
	protected int yiCount = 0;

	protected int tag = 0;
	protected LinearLayout linearLayoutCount;
	protected String result;
	protected Intent intent;
	protected Bundle bundle;
	private boolean isNeedCapture = false;

	public boolean isNeedCapture() {
		return isNeedCapture;
	}

	public void setNeedCapture(boolean isNeedCapture) {
		this.isNeedCapture = isNeedCapture;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qr_scan);
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		mContainer = (RelativeLayout) findViewById(R.id.capture_containter);
		mCropLayout = (RelativeLayout) findViewById(R.id.capture_crop_layout);

		//获取
		inputCode = (EditText) findViewById(R.id.inputCode);
		inputCodePower = (TextView) findViewById(R.id.inputCodePower);
		inputQuantity = (EditText) findViewById(R.id.inputQuantity);
		inputQuantityPower = (TextView) findViewById(R.id.inputQuantityPower);
		inputQuantityForIndex = (TextView) findViewById(R.id.inputQuantityForIndex);
		itmeCodeTextView = (TextView) findViewById(R.id.itemCode);
		bindTagTextView = (TextView) findViewById(R.id.bindTag);
		//请输入数量
		inputTextQuantity = (TextView) findViewById(R.id.inputTextQuantity);
		lightPower = (TextView) findViewById(R.id.lightPower);
		//隐藏输入框
		inputCodePower.setOnClickListener(this);
		lightPower.setOnClickListener(this);
		//盘点数
		linearLayoutCount = (LinearLayout) findViewById(R.id.linearLayoutCount);
		scanCount = (TextView) findViewById(R.id.scanCount);
		scanTitleLeft = (TextView) findViewById(R.id.scanTitleLeft);
		scanTitleRight = (TextView) findViewById(R.id.scanTitleRight);
		bindTagTextView.setText("已绑定：" + String.valueOf(yiCount));
		//跳转到主界面:HomeActivity
		intent = getIntent();
		//bundle传值到HomeActivity
		bundle = intent.getExtras();
		//配件ID
		orderNo = bundle.getString("orderNo");
		supplier = bundle.getString("supplier");
		itemCode = bundle.getString("itemCode");
		itemName = bundle.getString("itemName");
		bindNotCount = bundle.getInt("bindNotCount");
		maskId = bundle.getString("maskId");

		//店ID
		storeId = bundle.getString("storeId");
		tag = bundle.getInt("tag",0);

		//如果是巡查
//		if (tag == NavigationActivity.PATROL_TAG){
//			scanCount.setText("0");
//			linearLayoutCount.setVisibility(View.VISIBLE);
//			scanTitleLeft.setText(getResources().getString(R.string.patScanLeft));
//			scanTitleRight.setText(getResources().getString(R.string.scanRight));
//			list = PatrolAdapter.partsList;
//		}//盘点
//		else if(tag == NavigationActivity.INVENTORY_TAG){
//			scanCount.setText("0");
//			linearLayoutCount.setVisibility(View.VISIBLE);
//			scanTitleLeft.setText(getResources().getString(R.string.invScanLeft));
//			scanTitleRight.setText(getResources().getString(R.string.scanRight));
//			list = InventoryAdapter.invList;
//		}
		//控制控件
		inputQuantityPower.setVisibility(View.GONE);
		if(tag == NavigationActivity.BIND_TAG || tag == NavigationActivity.PATROL_TAG || tag == NavigationActivity.INVENTORY_TAG){
			inputCode.setVisibility(View.GONE);
			inputCodePower.setVisibility(View.GONE);
			inputQuantity.setVisibility(View.VISIBLE);
			inputTextQuantity.setVisibility(View.VISIBLE);
			inputQuantityPower.setOnClickListener(this);
		}
		if(tag == NavigationActivity.PARTS_TAG){
			inputCode.setVisibility(View.GONE);
			inputCodePower.setVisibility(View.GONE);
			bindTagTextView.setVisibility(View.GONE);
		}
		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
				TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.9f);
		mAnimation.setDuration(1500);
		mAnimation.setRepeatCount(-1);
		mAnimation.setRepeatMode(Animation.REVERSE);
		mAnimation.setInterpolator(new LinearInterpolator());
		mQrLineView.setAnimation(mAnimation);
	}

	boolean flag = true;

	protected void light() {
		if (flag == true) {
			flag = false;
			// 开闪光灯
			CameraManager.get().openLight();
		} else {
			flag = true;
			// 关闪光灯
			CameraManager.get().offLight();
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}
	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	int i = 1;
	int p = 1;
	public String handleDecode(String result) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		this.result = result;
		itmeCodeTextView.setText(result);
		Log.i("扫描的码！",result);
		//判断去哪里
 		switch (tag){
			case NavigationActivity.PARTS_TAG:
				intent.setClass(CaptureActivity.this,PartsDetailsActivity.class);
				bundle.putString("itemCode", result);
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case NavigationActivity.BIND_TAG:
				setQuantity();
				//实例化主导者
//				BindPresenter bindPresenter = new BindPresenter(this);
//				bindPresenter.setBind(new PartsParameters(orderNo,storeId,result,CaptureActivity.this));
				break;
			case NavigationActivity.INVENTORY_TAG:
				//InvAndPatrolScanning(result,INVENTORY_SCANNING);
				//因为巡查和盘点需要连续扫描

				setQuantity();
//				if(i == list.size() + 1){
//					Toast.makeText(CaptureActivity.this, "扫描完成...", Toast.LENGTH_SHORT).show();
//					intent.setClass(CaptureActivity.this, InventoryActivity.class);//去哪儿
//					startActivity(intent);
//					this.finish();
//				}else{
//					// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
//					handler.sendEmptyMessageDelayed(R.id.restart_preview,2000);
//
//				}
				break;
			case NavigationActivity.PATROL_TAG:
				//InvAndPatrolScanning(result,PATROL_SCANNING);

				//因为巡查和盘点需要连续扫描
				setQuantity();
//				if(p == list.size() + 1){
//					Toast.makeText(CaptureActivity.this, "扫描完成...", Toast.LENGTH_SHORT).show();
//					intent.setClass(CaptureActivity.this, PatrolActivity.class);//去哪儿
//					startActivity(intent);
//					this.finish();
//				}else{
//					// 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
//					handler.sendEmptyMessageDelayed(R.id.restart_preview,2000);
//				}
				break;
		}
		return result;
	}

	//存储静态集合
	List<PartsDetails> list = null;
	//巡查 和 盘点的方法
	protected void InvAndPatrolScanning(String result,int tag){
//		int index = searchLoop(result,tag);
//		inputQuantityForIndex.setText(String.valueOf(index));
//		//提示消息
//		String message = "";
//		//统一索引
//		int j = 0;
//		//判断
//		if(tag == INVENTORY_SCANNING){
//			j = i;
//			message = getResources().getString(R.string.invScanLeft);
//			list = InventoryAdapter.invList;
//		}else{
//			j = p;
//			message = getResources().getString(R.string.patScanLeft);
//			list = PatrolAdapter.partsList;
//		}
////		if((tag == INVENTORY_SCANNING && index != -1 && list.get(index).getInventoryFlag() != 2) || (tag == PATROL_SCANNING && index != -1 && list.get(index).getInventoryFlag() != 2)){
//			if(tag == INVENTORY_SCANNING){
//				list.get(index).setInventoryFlag(2);//已盘点
//				i++;
//			}else{
//				list.get(index).setInventoryFlag(2);//已巡查
//				p++;
//			}
//			scanCount.setText(String.valueOf(j));
//			//显示数量
//			inputQuantity.setText(String.valueOf(list.get(index).getSysQuantity()));
//		}else if((tag == INVENTORY_SCANNING && index != -1 && list.get(index).getInventoryFlag() == 2) || (tag == PATROL_SCANNING && index != -1 && list.get(index).getInventoryFlag() == 2)){// || (index != -1 && list.get(index).getInventoryFlag() == 1)
//			Toast.makeText(CaptureActivity.this, message, Toast.LENGTH_SHORT).show();
//			handler.sendEmptyMessageDelayed(R.id.restart_preview,2000);
//		}else{
//			Toast.makeText(CaptureActivity.this, getResources().getString(R.string.notMatch), Toast.LENGTH_SHORT).show();
//			handler.sendEmptyMessageDelayed(R.id.restart_preview,2000);
//		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Point point = CameraManager.get().getCameraResolution();
			int width = point.y;
			int height = point.x;

			int x = mCropLayout.getLeft() * width / mContainer.getWidth();
			int y = mCropLayout.getTop() * height / mContainer.getHeight();

			int cropWidth = mCropLayout.getWidth() * width / mContainer.getWidth();
			int cropHeight = mCropLayout.getHeight() * height / mContainer.getHeight();

			setX(x);
			setY(y);
			setCropWidth(cropWidth);
			setCropHeight(cropHeight);
			// 设置是否需要截图
			setNeedCapture(true);


		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(CaptureActivity.this);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public Handler getHandler() {
		return handler;
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	//不能为空
	protected void itemCodeToastNotNull(){
		Toast.makeText(CaptureActivity.this, "编号不能为空", Toast.LENGTH_SHORT).show();
	}
	//提交查询
	protected void itemCodeSubmit(){
		//跳转到主界面:HomeActivity
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		//码
		String itemCode = inputCode.getText().toString();
		switch (bundle.getInt("tag",0)){
			//配件识别
			case NavigationActivity.PARTS_TAG:
				intent.setClass(CaptureActivity.this,PartsDetailsActivity.class);
				bundle.putString("itemCode", itemCode);
				intent.putExtras(bundle);
				break;
			//绑定
			case NavigationActivity.BIND_TAG:
				//实例化主导者
//				BindPresenter bindPresenter = new BindPresenter(this);
//				bindPresenter.setBind(new PartsParameters(orderNo,storeId,itemCode,CaptureActivity.this));
				break;
			//盘点
			case NavigationActivity.INVENTORY_TAG:

				break;
			//巡查
			case NavigationActivity.PATROL_TAG:

				break;
		}
		//bundle传值到HomeActivity
		startActivity(intent);
		CaptureActivity.this.finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.inputCodePower:
				if (inputCode.getText().toString().equals("") || inputCode.getText() == null){
					itemCodeToastNotNull();
				}else{
					itemCodeSubmit();
				}
				break;
			case R.id.lightPower:
				light();
				break;
			case R.id.inputQuantityPower:
				//setQuantity();
				break;
		}
	}

	/**
	 * 修改数量
	 * */
	private void setQuantity() {
		String total = inputQuantity.getText().toString();
		if("".equals(total)){
			Toast.makeText(CaptureActivity.this, "请输入有效数量", Toast.LENGTH_SHORT).show();
		}else if(result == null){
			Toast.makeText(CaptureActivity.this, "请先扫描", Toast.LENGTH_SHORT).show();
		}else{
			if(tag == NavigationActivity.BIND_TAG){
				if(bindNotCount >= Integer.parseInt(total)){
					BindPresenter bindPresenter = new BindPresenter(this);
					//String orderNo,String barCode,String itemCode, String supplier,String total
					bindPresenter.setBind(new PartsParameters(orderNo, result, itemCode, supplier, total, itemName, CaptureActivity.this));
				}else {
					Toast.makeText(CaptureActivity.this, "超出最大数量，最大数量为" + bindNotCount, Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessageDelayed(R.id.restart_preview, 2000);
				}
			}else if(tag == NavigationActivity.INVENTORY_TAG){
				InventoryPresenter inventoryPresenter = new InventoryPresenter(this,CaptureActivity.this);
				inventoryPresenter.queryBarcode(new PartsParameters(maskId, result, total));
			}else if(tag == NavigationActivity.PATROL_TAG){
				PatrolPresenter patrolPresenter = new PatrolPresenter(this,CaptureActivity.this);
				patrolPresenter.pQueryBarcode(new PartsParameters(maskId, result, total));
			}
		}
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			//Toast.makeText(OptionShopActivity.this, "finish!", Toast.LENGTH_SHORT).show();
//			this.finish();
//		}
//		return false;
//	}

	@Override
	public void setParts(List<PartsDetails> partsList) {

	}

//	public int searchLoop(String barCode,int tag)
//	{
//		List<PartsDetails> list = tag == INVENTORY_SCANNING ? InventoryAdapter.invList : PatrolAdapter.partsList;
//		int start=0;
//		int end = list.size()-1;
//
//		while(start<=end)
//		{
//			//中间位置
//
//		int middle= start + (end-start) / 2;    //相当于(start+end)/2
//			//中值
//			String middleValue = list.get(middle).getBarcode();
//
//			if(barCode.equals(middleValue))
//			{
//				//等于中值直接返回
//				return middle;
//			}
//			else if(barCode.compareTo(middleValue) < 0)
//			{
//				//小于中值时在中值前面找
//				end=middle-1;
//			}
//			else
//			{
//				//大于中值在中值后面找
//				start=middle+1;
//			}
//		}
//		//找不到
//		return -1;
//	}
	@Override
	public void setBind(BindResult bindResult) {
		bindNotCount = bindNotCount - bindResult.getTotal();
		yiCount = yiCount + bindResult.getTotal();
		if(bindNotCount > 0 || bindResult.getResult().equals("fail")){
			bindTagTextView.setText("已绑定：" + String.valueOf(yiCount));
			handler.sendEmptyMessageDelayed(R.id.restart_preview, 2000);
			//获取焦点
			inputQuantity.requestFocus();
			Toast.makeText(CaptureActivity.this, bindResult.getMessage(), Toast.LENGTH_SHORT).show();
		}else {
			Intent intent = getIntent();
			intent.setClass(this, BindActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

	@Override
	public void onBackPressed() {
		if(tag != NavigationActivity.PARTS_TAG){
			Intent intent = getIntent();
			if(tag == NavigationActivity.INVENTORY_TAG){
				intent.setClass(this, InventoryActivity.class);
			}else if(tag == NavigationActivity.PATROL_TAG){
				intent.setClass(this, PatrolActivity.class);
			}else{
				intent.setClass(this, BindActivity.class);
			}
			startActivity(intent);
			this.finish();
		}else {
			super.onBackPressed();
		}
	}


	@Override
	public void setInventory(List<PartsDetails> partsList) {

	}

	@Override
	public void savePatrol() {

	}

	@Override
	public void pQueryBarcode() {
		handler.sendEmptyMessageDelayed(R.id.restart_preview,2000);
	}

	@Override
	public void saveInventory() {

	}

	@Override
	public void queryBarcode() {
		handler.sendEmptyMessageDelayed(R.id.restart_preview,2000);
	}
}