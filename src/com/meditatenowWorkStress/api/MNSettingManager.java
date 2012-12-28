package com.meditatenowWorkStress.api;

import java.util.TimeZone;

import org.json.JSONException;

import android.util.Log;

import com.meditatenowWorkStress.MNAlarmReceiver;
import com.vl.common.VLDate;
import com.vl.logic.VLBinder;
import com.vl.logic.VLLogicObject;
import com.vl.logic.VLMessageListener;
import com.vl.system.VLAlarmsManager;
import com.vl.system.VLKeysValuesSerializable;
import com.vl.system.VLKeysValuesSerializer;
import com.vl.system.VLNotificationsManager;
import com.vl.system.VLPreferencesStorage;

public class MNSettingManager extends VLLogicObject implements VLKeysValuesSerializable {

	private final static String _storageKey = "MNSettingManager";
	private final static int _storageVersion = 2;
	private long _lastSavedDataVersion;
	
	private static MNSettingManager _instance;
	
	private boolean _isPlayInstructions = true;
	private boolean _isPlayIntro = true;
	private int _reminderType = MNReminderTypeInfo.REMINDER_TYPE_NONE;
	private int _reminderDateInDays = 0;
	private int _reminderTimeInSeconds = 12*3600;

	public static MNSettingManager getInstance() {
		if(_instance == null) {
			_instance = new MNSettingManager();
			VLPreferencesStorage storage = new VLPreferencesStorage(_storageKey, _storageVersion);
			_instance._lastSavedDataVersion = _instance.getVersion();
			if(!storage.readObject(_instance)) {
			}
		}
		return _instance;
	}
	
	public MNSettingManager() {
		this.getMsgrVersionChanged().addListener(_onVersionChanged);
	}
	
	private VLMessageListener _onVersionChanged = new VLMessageListener() {
		@Override
		public void onMessage(VLBinder sender, Object args) {
			if(_lastSavedDataVersion != MNSettingManager.this.getVersion())
			{
				Log.v(MNSettingManager.this.getClass().getCanonicalName(), "MNSettingManager: writeObject");
				VLPreferencesStorage storage = new VLPreferencesStorage(_storageKey, _storageVersion);
				storage.writeObject(MNSettingManager.this);
				_lastSavedDataVersion = MNSettingManager.this.getVersion();
			}
		}
	};
	
	@Override
	public void write(VLKeysValuesSerializer encoder) {
		try {
			encoder.writeBool("_isPlayInstructions", _isPlayInstructions);
			encoder.writeBool("_isPlayIntro", _isPlayIntro);
			encoder.writeInt("_reminderType", _reminderType);
			encoder.writeInt("_reminderDateInDays", _reminderDateInDays);
			encoder.writeInt("_reminderTimeInSeconds", _reminderTimeInSeconds);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void read(VLKeysValuesSerializer decoder) {
		try {
			_isPlayInstructions = decoder.readBool("_isPlayInstructions");
			_isPlayIntro = decoder.readBool("_isPlayIntro");
			_reminderType = decoder.readInt("_reminderType");
			_reminderDateInDays = decoder.readInt("_reminderDateInDays");
			if(decoder.containsKey("_reminderTimeInSeconds"))
				_reminderTimeInSeconds = decoder.readInt("_reminderTimeInSeconds");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isPlayInstructions() {
		return _isPlayInstructions;
	}
	
	public void setIsPlayInstructions(boolean isPlayInstructions) {
		if(_isPlayInstructions != isPlayInstructions) {
			_isPlayInstructions = isPlayInstructions;
			this.modifyVersion();
		}
	}
	
	public boolean isPlayIntro() {
		return _isPlayIntro;
	}
	
	public void setIsPlayIntro(boolean isPlayIntro) {
		if(_isPlayIntro != isPlayIntro) {
			_isPlayIntro = isPlayIntro;
			this.modifyVersion();
		}
	}

	public int reminderType() {
		return _reminderType;
	}

	public void setReminderType(int reminderType) {
		if(_reminderType != reminderType) {
			_reminderType = reminderType;
			updateReminderNotification();
			this.modifyVersion();
		}
	}
	
	public int reminderDateInDays() {
		return _reminderDateInDays;
	}

	public void setReminderDateInDays(int reminderDateInDays) {
		if(_reminderDateInDays != reminderDateInDays) {
			_reminderDateInDays = reminderDateInDays;
			updateReminderNotification();
			this.modifyVersion();
		}
	}

	public int reminderTimeInSeconds() {
		return _reminderTimeInSeconds;
	}

	public void setReminderTimeInSeconds(int reminderTimeInSeconds) {
		if(reminderTimeInSeconds >= 86400)
			reminderTimeInSeconds = 86400 - 1;
		if(reminderTimeInSeconds < 0)
			reminderTimeInSeconds = 0;
		if(_reminderTimeInSeconds != reminderTimeInSeconds) {
			_reminderTimeInSeconds = reminderTimeInSeconds;
			updateReminderNotification();
			this.modifyVersion();
		}
	}
	
	public void updateReminderNotification() {
		VLNotificationsManager.getInstance().cancelAllNotifications();
		if(_reminderType == MNReminderTypeInfo.REMINDER_TYPE_NONE || _reminderDateInDays <= 0) {
			VLAlarmsManager.getInstance().setAlarm(MNCommon.ReminderId, new VLDate(9999, 1, 1), MNAlarmReceiver.class);
			return;
		}
		VLDate dtCur = VLDate.getCurrent();
		TimeZone tz = TimeZone.getDefault();
		long offsetMS = tz.getOffset((dtCur.getTicks() -  VLDate.kTicksUntil1970) / VLDate.kTicksPerMillisecond);
		long secondsFromMidnight = (((dtCur.getTicks()/VLDate.kTicksPerMillisecond) + offsetMS) / 1000) % 86400;
		int curAllDays = (int)(((dtCur.getTicks()/VLDate.kTicksPerMillisecond) + offsetMS) / (86400 * 1000));
		int referenceAllDays = _reminderDateInDays;
		int targetAllDays = referenceAllDays;
		if(_reminderType == MNReminderTypeInfo.REMINDER_TYPE_DAILY) {
			targetAllDays = referenceAllDays;
			if(targetAllDays < curAllDays)
				targetAllDays = curAllDays;
			if(_reminderTimeInSeconds <= secondsFromMidnight)
				targetAllDays += 1;
		} else if(_reminderType == MNReminderTypeInfo.REMINDER_TYPE_EVERYOTHERDAY) {
			targetAllDays = referenceAllDays;
			while(targetAllDays < curAllDays)
				targetAllDays += 2;
			if(targetAllDays == curAllDays && (_reminderTimeInSeconds <= secondsFromMidnight))
				targetAllDays += 2;
		} else if(_reminderType == MNReminderTypeInfo.REMINDER_TYPE_WEEKLY) {
			targetAllDays = referenceAllDays;
			while(targetAllDays < curAllDays)
				targetAllDays += 7;
			if(targetAllDays == curAllDays && (_reminderTimeInSeconds <= secondsFromMidnight))
				targetAllDays += 7;
		}
		VLDate targetDate = new VLDate(
				targetAllDays * VLDate.kTicksPerDay
				- offsetMS * VLDate.kTicksPerMillisecond
				+ _reminderTimeInSeconds * VLDate.kTicksPerSecond);
		VLAlarmsManager.getInstance().setAlarm(MNCommon.ReminderId, targetDate, MNAlarmReceiver.class);
	}
}
