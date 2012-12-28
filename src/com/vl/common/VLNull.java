package com.vl.common;

public class VLNull {
	
	public static VLNull make() {
		return new VLNull();
	}
	
	public static boolean isNull(Object obj) {
		if(obj == null)
			return false;
		if(VLNull.class.isInstance(obj))
			return true;
		return false;
	}
}
