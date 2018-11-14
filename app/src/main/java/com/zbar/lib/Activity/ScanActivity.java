package com.zbar.lib.Activity;

import com.icar.inventory.R;
import com.zbar.lib.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class ScanActivity extends Activity {

	private Button btn_scan;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanning);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		
		btn_scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(ScanActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
