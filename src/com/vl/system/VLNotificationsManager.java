package com.vl.system;

import android.app.NotificationManager;
import android.content.Context;

import com.vl.logic.VLLogicObject;

public class VLNotificationsManager extends VLLogicObject {

	private static VLNotificationsManager _instance;
	
	public static VLNotificationsManager getInstance() {
		if(_instance == null)
			_instance = new VLNotificationsManager();
		return _instance;
	}
	
	public void cancelAllNotifications() {
		NotificationManager notificationManager = (NotificationManager)VLSystemMediator.getInstance().getMainActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}
}
