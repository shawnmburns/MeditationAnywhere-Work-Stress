package com.vl.system;

import android.os.Handler;

public class VLMessageCenter {
	
	static VLMessageCenter _instance;
	int _ignoreTouchesCounter = 0;
	
	private VLMessageCenter() {
	}
	
	public static VLMessageCenter getInstance() {
		if(_instance == null)
			_instance = new VLMessageCenter();
		return _instance;
	}
	
	private boolean isIgnoringTouchesInt() {
		return (_ignoreTouchesCounter > 0);
	}
	
	private void startIgnoringTouchesInt() {
		_ignoreTouchesCounter++;
	}
	
	private void stopIgnoringTouchesInt() {
		if(_ignoreTouchesCounter > 0)
			_ignoreTouchesCounter = 0;
	}
	
	private void startIgnoringTouchesForDurationInt(int durationMs) {
		startIgnoringTouches();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				VLMessageCenter.this.stopIgnoringTouchesInt();
			}
		}, durationMs);
	}
	
	public static boolean isIgnoringTouches() {
		return getInstance().isIgnoringTouchesInt();
	}
	
	public static void startIgnoringTouches() {
		getInstance().startIgnoringTouchesInt();
	}
	
	public static void stopIgnoringTouches() {
		getInstance().stopIgnoringTouchesInt();
	}
	
	public static void startIgnoringTouchesForDuration(int durationMs) {
		getInstance().startIgnoringTouchesForDurationInt(durationMs);
	}
}


