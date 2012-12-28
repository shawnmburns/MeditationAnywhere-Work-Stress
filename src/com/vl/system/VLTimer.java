package com.vl.system;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.vl.logic.VLMessenger;

import android.os.Handler;
import android.os.SystemClock;

public class VLTimer {
	
	private static class TimerReference extends WeakReference<VLTimer> {
		public TimerReference(VLTimer r) {
			super(r);
		}
	}
	
	static public class TimersManager {
		static TimersManager _instance;
		ArrayList<TimerReference> _timers = new ArrayList<VLTimer.TimerReference>();
		
		public static TimersManager getInstance() {
			if(_instance == null)
				_instance = new TimersManager();
			return _instance;
		}
		
		private void correctTimersList() {
			for(int i = _timers.size()-1; i >= 0; i--) {
				TimerReference ref = _timers.get(i);
				VLTimer timer = ref.get();
				if(timer == null)
					_timers.remove(i);
			}
		}
		
		public boolean containsTimer(VLTimer timer) {
			if(timer == null)
				return false;
			for(int i = _timers.size()-1; i >= 0; i--) {
				TimerReference ref = _timers.get(i);
				VLTimer tmr = ref.get();
				if(tmr == null) {
					_timers.remove(i);
					continue;
				}
				if(tmr == timer)
					return true;
			}
			return false;
		}
		
		public void subscribe(VLTimer timer) {
			this.correctTimersList();
			if(timer == null)
				return;
			if(!this.containsTimer(timer)) {
				_timers.add(new TimerReference(timer));
			}
		}
		
		public void unsubscribe(VLTimer timer) {
			this.correctTimersList();
			if(timer == null)
				return;
			for(int i = _timers.size()-1; i >= 0; i--) {
				TimerReference ref = _timers.get(i);
				VLTimer tmr = ref.get();
				if(tmr == timer)
					_timers.remove(i);
			}
		}
		
		public ArrayList<VLTimer> getAllTimers() {
			ArrayList<VLTimer> result = new ArrayList<VLTimer>();
			this.correctTimersList();
			for(TimerReference ref : _timers) {
				VLTimer tmr = ref.get();
				if(tmr != null)
					result.add(tmr);
			}
			return result;
		}
		
		public void suspendTimers() {
			ArrayList<VLTimer> timers = this.getAllTimers();
			for(VLTimer timer : timers)
				timer.suspend();
		}
		
		public void resumeTimers() {
			ArrayList<VLTimer> timers = this.getAllTimers();
			for(VLTimer timer : timers)
				timer.resume();
		}
	}

	private boolean _started = false;
	private boolean _suspended = false;
	private Handler _handler;
	private int _interval = 1000; // Milliseconds
	private VLMessenger _msgrTimerEvent;
	private long _lastFireTime;
	private Listener _listener;
	private VLRunnable _runnable;
	
	public VLTimer() {
		_lastFireTime = -1;
	}
	
	public VLTimer(int intervalMilliseconds) {
		this();
		_interval = intervalMilliseconds;
	}
	
	private void startRunnable() {
		if(_handler == null)
			_handler = new Handler();
		_runnable = new VLRunnable();
		long curTime = SystemClock.elapsedRealtime();
		long interval = _interval;
		if(_lastFireTime < 0)
			_lastFireTime = curTime;
		if((curTime - _lastFireTime) >= _interval)
			interval = 1;
		else if((curTime - _lastFireTime) < _interval)
			interval = _interval - (curTime - _lastFireTime);
		if(interval < 1)
			interval = 1;
		if(interval > _interval)
			interval = _interval;
		_handler.postDelayed(_runnable, interval);
	}
	
	private void checkFireTime() {
		if(!_started || _suspended || (_runnable == null))
			return;
		long curTime = SystemClock.elapsedRealtime();
		if(_lastFireTime >= 0 && curTime < (_lastFireTime + _interval)) {
			startRunnable();
			return;
		}
		if(_lastFireTime < 0) {
			_lastFireTime = curTime;
		} else {
			for(int i = 0; i < 100; i++)
				if(_lastFireTime < curTime - _interval)
					_lastFireTime += _interval;
			if(_lastFireTime < curTime - _interval)
				_lastFireTime = curTime;
		}
		
		if(_msgrTimerEvent != null)
			_msgrTimerEvent.postMessage();
		if(_listener != null)
			_listener.onTimeEvent(VLTimer.this);
		
		curTime = SystemClock.elapsedRealtime();
		long interval = _interval - (curTime - _lastFireTime);
		if(interval > _interval)
			interval = _interval;
		if(interval < 1)
			interval = 1;
		if(_handler != null)
			_handler.postDelayed(_runnable, interval);
	}
	
	private class VLRunnable implements Runnable {

		@Override
		public void run() {
			if(_runnable != this)
				return;
			checkFireTime();
		}
	}
	
	public void start() {
		if(_started)
			return;
		_started = true;
		_suspended = false;
		_lastFireTime = -1;
		TimersManager.getInstance().subscribe(this);
		startRunnable();
	}
	
	public void stop() {
		TimersManager.getInstance().unsubscribe(this);
		if(!_started)
			return;
		_started = false;
		_suspended = false;
		_runnable = null;
		_handler = null;
		_lastFireTime = -1;
	}
	
	public void suspend() {
		if(_started) {
			if(!_suspended) {
				_suspended = true;
				_handler = null;
				_runnable = null;
			}
		}
	}
	
	public void resume() {
		if(_started) {
			if(_suspended) {
				_suspended = false;
				startRunnable();
			}
		}
	}
	
	public VLMessenger getMsgrTimerEvent() {
		if(_msgrTimerEvent == null) {
			_msgrTimerEvent = new VLMessenger(this);
		}
		return _msgrTimerEvent;
	}

	@Override
	protected void finalize() throws Throwable {
		this.stop();
		TimersManager.getInstance().unsubscribe(this);
		super.finalize();
	}
	
	public static abstract class Listener {
		public abstract void onTimeEvent(VLTimer timer);
	}

	public Listener getListener() {
		return _listener;
	}
	
	public void setListener(Listener listener) {
		_listener = listener;
	}
	
	public int interval() {
		return _interval;
	}
	
	public void setInterval(int interval) {
		if(interval < 1)
			interval = 1;
		if(_interval != interval) {
			_interval = interval;
			_runnable = null;
			if(_started && !_suspended)
				startRunnable();
		}
	}
}


