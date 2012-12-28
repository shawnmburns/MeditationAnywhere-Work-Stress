package com.vl.ctrls;

import com.vl.logic.VLErrorsHandler;
import com.vl.system.VLTimer;

public class VLUIErrorsHandler {
	
	private static VLUIErrorsHandler _instance;
	private VLTimer _timer;
	
	public static VLUIErrorsHandler getInstance() {
		if(_instance == null) {
			_instance = new VLUIErrorsHandler();
		}
		return _instance;
	}

	protected VLUIErrorsHandler() {
		
	}
	
	protected void processItem(VLErrorsHandler.Item item) {
		
	}
	
	public void start() {
		if(_timer == null) {
			_timer = new VLTimer(250);
			_timer.setListener(new VLTimer.Listener() {
				@Override
				public void onTimeEvent(VLTimer timer) {
					VLErrorsHandler source = VLErrorsHandler.getInstance();
					VLErrorsHandler.Item item = source.popItem();
					if(item != null) {
						processItem(item);
					}
				}
			});
			_timer.start();
		}
	}
}
