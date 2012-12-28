package com.vl.system;

import java.util.ArrayList;

import com.vl.common.VLSizeF;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class VLSystemMediator {
	
	private static VLSystemMediator _instance;
	private Activity _mainActivity;
	private ArrayList<Activity> _curActivitiesStack = new ArrayList<Activity>();
	
	public static VLSystemMediator getInstance() {
		if(_instance == null) {
			_instance = new VLSystemMediator();
		}
		return _instance;
	}
	
	public void Initialize(Activity mainActivity) {
		_mainActivity = mainActivity;
	}
	
	public Activity getMainActivity() {
		return _mainActivity;
	}
	
	public Context getCurrentContext() {
		return this.getCurrentActivity();
	}
	
	public Activity getCurrentActivity() {
		if(_curActivitiesStack.size() == 0)
			return null;
		return _curActivitiesStack.get(_curActivitiesStack.size() - 1);
	}
	
	public void pushCurrentActivity(Activity curActivity) {
		if(getCurrentActivity() == curActivity) {
			Log.e(getClass().getCanonicalName(), "pushCurrentActivity: (getCurrentActivity() == curActivity)");
			return;
		}
		_curActivitiesStack.add(curActivity);
	}
	
	public void popCurrentActivity(Activity curActivity) {
		if(getCurrentActivity() != curActivity) {
			Log.e(getClass().getCanonicalName(), "popCurrentActivity: (getCurrentActivity() != curActivity)");
			return;
		}
		_curActivitiesStack.remove(_curActivitiesStack.size() - 1);
	}
	
	public Display getDefaultDisplay() {
		Context context = this.getMainActivity();
		Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		return display;
	}
	
	public VLSizeF getDisplaySize() {
		Display display = this.getDefaultDisplay();
		//Point size = new Point();
		//display.getSize(size);
		VLSizeF result = new VLSizeF();
		result.width = display.getWidth();
		result.height = display.getHeight();
		return result;
	}
	
	public VLSizeF getDisplaySizeDP() {
		Display display = this.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		float density = metrics.density; 
		VLSizeF result = new VLSizeF();
		result.width = (int)(metrics.widthPixels / density);
		result.height = (int)(metrics.heightPixels / density);
		return result;
	}
	
	public float getPixelsByPoint() {
		Display display = this.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		float density = metrics.density;
		return density;
	}
	
	public boolean isInternetConnectionAvailable() {
		Context context = this.getMainActivity();
		boolean connected = false;
        try {
        	ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        	NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        	connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        	return connected;
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
	
	public boolean isWiFiConnected() {
		Context context = this.getMainActivity();
		boolean connected = false;
        try {
        	ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        	NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        	connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        	return connected;
        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
	}
}
