package com.vl.logic;

import java.util.ArrayList;

public abstract class VLMessageListener {
	
	public VLMessageListener() {
		
	}
	
	public VLMessageListener(References references) { // To avoid garbage collection of inner class while outer still exists
		references.Add(this);
	}
	
	public abstract void onMessage(VLBinder sender, Object args);
	
	public static class References {
		
		ArrayList<VLMessageListener> _list = new ArrayList<VLMessageListener>();
		
		public void Add(VLMessageListener listener) {
			_list.add(listener);
		}
	}
}
