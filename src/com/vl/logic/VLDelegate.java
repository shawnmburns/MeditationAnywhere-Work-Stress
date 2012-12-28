package com.vl.logic;

import java.util.ArrayList;

public class VLDelegate extends VLBinder {
	
	ArrayList<VLMessageListener> _listeners = new ArrayList<VLMessageListener>();
	
	public VLDelegate() {
		
	}
	
	public VLDelegate(Object owner) {
		super(owner);
	}
	
	public void subscribe(VLMessageListener listener) {
		if(!_listeners.contains(listener)) {
			_listeners.add(listener);
		}
	}
	
	public void unsubscribe(VLMessageListener listener) {
		if(_listeners.contains(listener)) {
			_listeners.remove(listener);
		}
	}
	
	public void sendMessage(Object args) {
		ArrayList<VLMessageListener> listeners = new ArrayList<VLMessageListener>();
		listeners.addAll(_listeners);
		for (VLMessageListener listener : listeners) {
			if(_listeners.contains(listener))
				listener.onMessage(VLDelegate.this, args);
		}
	}
}
