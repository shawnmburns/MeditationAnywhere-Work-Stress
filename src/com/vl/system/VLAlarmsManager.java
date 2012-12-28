package com.vl.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.vl.common.VLDate;
import com.vl.logic.VLLogicObject;

public class VLAlarmsManager extends VLLogicObject {

	private static VLAlarmsManager _instance;
	private AlarmManager _alarmManager;
	private final static String _kAlarmId = "alarmId";
	
	public static VLAlarmsManager getInstance() {
		if(_instance == null)
			_instance = new VLAlarmsManager();
		return _instance;
	}
	
	private VLAlarmsManager() {
		Context context = VLSystemMediator.getInstance().getMainActivity();
		_alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
	
	public void setAlarm(int alarmId, VLDate fireDate, Class<?> alarmReceiverClass) {
		
		Context context = VLSystemMediator.getInstance().getMainActivity();
		
		Intent intent = new Intent(context, alarmReceiverClass);
		intent.putExtra(_kAlarmId, alarmId);
		
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		//PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		long ms = fireDate.getMilliSecondsSince1970();

		_alarmManager.set(AlarmManager.RTC_WAKEUP, ms, pendingIntent);
		
		Log.v(getClass().getCanonicalName(), "setAlarm, alarmId = " + alarmId + ", fireDate = " + fireDate.toString());
	}
	
	public static int getAlarmId(Intent intent) {
		int alarmId = intent.getIntExtra(_kAlarmId, -1);
		return alarmId;
	}
}
