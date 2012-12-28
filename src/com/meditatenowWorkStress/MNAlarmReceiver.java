package com.meditatenowWorkStress;

import com.meditatenowWorkStress.R;
import com.vl.system.VLAlarmsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MNAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int alarmId = VLAlarmsManager.getAlarmId(intent);
		Log.v(getClass().getCanonicalName(), "onReceive, alarmId = " + alarmId);

		String from = "Meditate Now";
		String message = "Don't forget to meditate";

		NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Intent mainActivityIntent = new Intent(Intent.ACTION_MAIN);
		mainActivityIntent.setClass(context.getApplicationContext(), MNNotificationActivity.class);

		PendingIntent contentIntent = PendingIntent.getActivity(context, alarmId, mainActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notif = new Notification(R.drawable.ic_launcher, message, System.currentTimeMillis() + 1000);
		
		notif.defaults = notif.defaults | Notification.DEFAULT_ALL;

		notif.setLatestEventInfo(context, from, message, contentIntent);
		notificationManager.notify(alarmId, notif);
	}
}
