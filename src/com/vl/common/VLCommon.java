package com.vl.common;

/*

Changes 2012/09/05 11:00

	VLSplashActivity, VLTabbarView, VLGraphicsUtils, VLSwitchView, VLRectF, VLTextView, VLDate, VLAlarmsManager, VLFileStorage,
	VLTimer
*/

public class VLCommon {
	@SuppressWarnings("unchecked")
	public static <T> T objectCast(Object obj, Class<T> classToCast) {
		if(obj == null)
			return null;
		Class<? extends Object> curClass = obj.getClass();
		if(classToCast.isAssignableFrom(curClass))
			return (T)obj;
		return null;
	}
}
