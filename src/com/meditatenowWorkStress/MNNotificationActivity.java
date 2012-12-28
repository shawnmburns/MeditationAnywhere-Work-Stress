package com.meditatenowWorkStress;

import com.meditatenowWorkStress.api.MNCommon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MNNotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent(this, MNHomeActivity.class);
    	startActivity(intent);
    	
    	Intent intent1 = new Intent();
    	intent1.setAction(MNCommon.ACTION_KEY_START_PLAYING_FROM_BEGIN);
		sendBroadcast(intent1);
    	
    	finish();
	}
}
