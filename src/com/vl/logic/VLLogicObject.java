package com.vl.logic;

import java.lang.ref.WeakReference;

public class VLLogicObject {
	
	private VLMessenger _msgrVersionChanged;
	private WeakReference<VLLogicObject> _parent;
	private long _version = 0;
	
	public long getVersion() {
		return _version;
	}
	
	public void modifyVersion() {
		_version++;
		if(_msgrVersionChanged != null)
			_msgrVersionChanged.postMessage();
		VLLogicObject parent = this.getParent();
		if(parent != null) {
			parent.modifyVersion();
		}
	}
	
	public VLMessenger getMsgrVersionChanged() {
		if(_msgrVersionChanged == null)
			_msgrVersionChanged = new VLMessenger();
		return _msgrVersionChanged;
	}
	
	public VLLogicObject getParent() {
		if(_parent == null)
			return null;
		return _parent.get();
	}
	
	public void setParent(VLLogicObject value) {
		if(value != this.getParent()) {
			_parent = null;
			if(value != null)
				_parent = new WeakReference<VLLogicObject>(value);
		}
	}
	
	public void resetParent(VLLogicObject valueToRemove) {
		if(valueToRemove == this.getParent())
		{
			this.setParent(null);
		}
	}
}
