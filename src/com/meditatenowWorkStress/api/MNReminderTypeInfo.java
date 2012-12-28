package com.meditatenowWorkStress.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MNReminderTypeInfo {
	
	public static final int REMINDER_TYPE_NONE = 0;
	public static final int REMINDER_TYPE_DAILY = 1;
	public static final int REMINDER_TYPE_EVERYOTHERDAY = 2;
	public static final int REMINDER_TYPE_WEEKLY = 3;
	
	private final int _type;
	private final String _name;
	private static ArrayList<MNReminderTypeInfo> _allTypes;
	
	public MNReminderTypeInfo(int type, String name) {
		_type = type;
		_name = name;
	}
	
	public int type() {
		return _type;
	}
	
	public String name() {
		return _name;
	}
	
	public static List<MNReminderTypeInfo> allTypes() {
		if(_allTypes == null) {
			_allTypes = new ArrayList<MNReminderTypeInfo>();
			_allTypes.add(new MNReminderTypeInfo(REMINDER_TYPE_NONE, "None"));
			_allTypes.add(new MNReminderTypeInfo(REMINDER_TYPE_DAILY, "Daily"));
			_allTypes.add(new MNReminderTypeInfo(REMINDER_TYPE_EVERYOTHERDAY, "Every Other Day"));
			_allTypes.add(new MNReminderTypeInfo(REMINDER_TYPE_WEEKLY, "Weekly"));
		}
		return Collections.unmodifiableList(_allTypes);
	}
	
	public static MNReminderTypeInfo infoByType(int type) {
		for(MNReminderTypeInfo info : allTypes())
			if(info.type() == type)
				return info;
		return null;
	}
}
