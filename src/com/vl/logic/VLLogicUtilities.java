package com.vl.logic;

import android.os.Handler;
import android.os.SystemClock;
import android.util.FloatMath;

public class VLLogicUtilities {
	
	public static class VLDurationProcessHelper {
		private double _valStart = 0;
		private double _valStop = 0;
		private long _durationMs = 0;
		private Runnable _runnable;
		private long _startTime = 0;
		private Handler _handler;
		private double _valCur = 0;
		private Listener _listener;
		
		public VLDurationProcessHelper() {	
		}
		
		public void start(double valStart, double valStop, int durationMs, Runnable runnable) {
			_valStart = valStart;
			_valStop = valStop;
			_durationMs = durationMs;
			_runnable = runnable;
			_startTime = SystemClock.elapsedRealtime();
			_valCur = _valStart;
			_handler = new Handler();
			_handler.postDelayed(_runnableInt, 1);
		}
		
		public void start(double valStart, double valStop, int durationMs, Listener listener) {
			_listener = listener;
			this.start(valStart, valStop, durationMs, (Runnable)null);
		}
		
		Runnable _runnableInt = new Runnable() {
			@Override
			public void run() {
				long curTime = SystemClock.elapsedRealtime();
				long dTime = curTime - _startTime;
				if(dTime > _durationMs)
					dTime = _durationMs;
				_valCur = VLDurationProcessHelper.this.calculateNewValue(_valStart, _valStop, _durationMs, dTime);
				if(_runnable != null)
					_runnable.run();
				double durationRatio = dTime / (double)_durationMs;
				if(_listener != null)
					_listener.onUpdate(_valCur, _valStart, _valStop, durationRatio);
				if(dTime < _durationMs) {
					_handler = new Handler();
					_handler.postDelayed(_runnableInt, 1);
				} else {
					if(_listener != null)
						_listener.onEnd();
				}
			}
		};
		
		protected double calculateNewValue(double valStart, double valStop, long durationMs, long dTime) {
			double ratio = dTime / (double)durationMs;
			double result = valStart + (valStop - valStart) * ratio;
			return result;
		}
		
		public double getCurValue() {
			return _valCur;
		}
		
		public double getEndValue() {
			return _valStop;
		}
		
		
		public static class VLCircularHelper extends VLDurationProcessHelper {
			
			private float _startRatio = 0;
			
			public VLCircularHelper() {
			}
			
			public VLCircularHelper(float startRatio) {
				_startRatio = Math.max(Math.min(startRatio, 1f), 0f);
			}
			
			@Override
			protected double calculateNewValue(double valStart, double valStop, long durationMs, long dTime) {
				float ratioTime = (float) (dTime / (double)durationMs);
				float dAngle = (float) Math.PI;
				float angle = dAngle * ratioTime;
				if(_startRatio > 0) {
					angle = (dAngle*_startRatio) + (dAngle - (dAngle*_startRatio)) * ratioTime;
				}
				float radius = 100f;
				float squareFull = (float) (Math.PI * radius * radius / 2);
				float squarePie = squareFull * angle / dAngle;
				float dx = (float) Math.abs(radius * FloatMath.cos(angle));
				float dy = (float) Math.abs(radius * FloatMath.sin(angle));
				float squareTriangle = dx * dy / 2;
				float square = 0f;
				if(angle < dAngle/2)
					square = squarePie - squareTriangle;
				else
					square = squareFull - ((squareFull - squarePie) - squareTriangle);
				float ratio = square / squareFull;
				double result = valStart + (valStop - valStart) * ratio;
				return result;
			}
		}
		
		public static abstract class Listener {
			
			public void onUpdate(double valCur, double valStart, double valStop, double durationRatio) {
			}
			
			public void onEnd() {
			}
		}
	}
	
}
