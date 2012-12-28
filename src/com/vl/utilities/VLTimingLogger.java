package com.vl.utilities;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.os.SystemClock;
import android.util.Log;

public class VLTimingLogger {
	
	private class LogItem {
		public long uptime;
		public String info = "";
	}
	
	private Object _owner;
	private ArrayList<LogItem> _items = new ArrayList<VLTimingLogger.LogItem>();
	
	public VLTimingLogger(Object owner) {
		_owner = owner;
	}
	
	public void addItem(String info) {
		LogItem item = new LogItem();
		item.uptime = SystemClock.uptimeMillis();
		item.info = info;
		_items.add(item);
	}
	
	public void addItem() {
		addItem("");
	}
	
	public void writeAllToLog() {
		Method enclosingMethod = _owner.getClass().getEnclosingMethod();
		String methodName = "";
		if(enclosingMethod != null)
			methodName = enclosingMethod.getName();
		StringBuffer message = new StringBuffer();
		message.append(methodName);
		message.append(": ");
		if(_items.size() > 1)
		{
			for(int i = 1; i < _items.size(); i++) {
				LogItem item = _items.get(i);
				LogItem itemPrev = _items.get(i-1);
				if(itemPrev != null) {
					if(i > 1 && _items.size() > 2)
						message.append(", ");
					if(item.info != null && item.info.length() > 0) {
						message.append(item.info);
						message.append(" ");
					}
					message.append((item.uptime - itemPrev.uptime));
					if(i == _items.size()-1) {
						LogItem item0 = _items.get(0);
						if(_items.size() > 2)
							message.append(", all: ");
						message.append((item.uptime - item0.uptime));
						message.append(" ms");
					}
				}
			}
		}
		else
			message.append("no items");
		Log.v(_owner.getClass().getCanonicalName(), message.toString());
	}
}
