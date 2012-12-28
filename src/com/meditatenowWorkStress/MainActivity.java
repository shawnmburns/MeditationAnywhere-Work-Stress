package com.meditatenowWorkStress;

import com.meditatenowWorkStress.R;
import com.vl.ctrls.VLSplashActivity;
import com.vl.drawing.VLColor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends VLSplashActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setSplashImageResId(R.drawable.splash_screen, getResources(), VLColor.ColorsCommon.Green, false, false);

    	Handler handler = new Handler();
    	handler.postDelayed (
        	new Runnable() {
				public void run() {
					startActivity(new Intent(MainActivity.this, MNHomeActivity.class));
					MainActivity.this.finish();
				}
			}, getResources().getInteger(R.integer.splash_delay)
        );
	}
}
