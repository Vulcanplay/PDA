package com.zbar.lib.Activity;


import android.app.Activity;

import android.os.Bundle;

import android.widget.TextView;

import com.icar.inventory.R;

public class Test extends Activity{


	private TextView tv_scan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scanning);
		
		tv_scan = (TextView) findViewById(R.id.tv_scan);
		Bundle bundle=this.getIntent().getExtras();
		String re=bundle.getString("QWE");
		tv_scan.setText(re);
		
	}
}
