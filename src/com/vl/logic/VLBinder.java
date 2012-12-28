package com.vl.logic;

public class VLBinder {
	
	Object _owner;
	
	public VLBinder() {
		
	}
	
	public VLBinder(Object owner) {
		_owner = owner;
	}
	
	public static class EventArgs {
		
	}
	
	public static class CancelEventArgs extends EventArgs {
		public boolean Canceled;
		
		public CancelEventArgs() {
			Canceled = false;
		}
	}
	
	public static class IntegerEventArgs extends CancelEventArgs {
		public int IntegetValue;
		
		public IntegerEventArgs() {
			IntegetValue = 0;
		}
		
		public IntegerEventArgs(int integetValue) {
			IntegetValue = integetValue;
		}
	}
}

