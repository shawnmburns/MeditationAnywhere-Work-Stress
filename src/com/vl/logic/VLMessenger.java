package com.vl.logic;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.os.Handler;

public class VLMessenger extends VLBinder {
	
	static class ListenerReference extends WeakReference<VLMessageListener> {
		
		public ListenerReference(VLMessageListener r) {
			super(r);
		}
	}
	
	ArrayList<ListenerReference> _listeners = new ArrayList<ListenerReference>();
	Handler _handler;
	boolean _msgPosted;
	boolean _postCanceled;
	Object _args;
	
	public VLMessenger() {
		
	}
	
	public VLMessenger(Object owner) {
		super(owner);
	}
	
	void correctListenersList() {
		for(int i = _listeners.size()-1; i >= 0; i--) {
			ListenerReference ref = _listeners.get(i);
			VLMessageListener listr = ref.get();
			if(listr == null)
				_listeners.remove(i);
		}
	}
	
	public boolean containsListener(VLMessageListener listener) {
		if(listener == null)
			return false;
		for(int i = _listeners.size()-1; i >= 0; i--) {
			ListenerReference ref = _listeners.get(i);
			VLMessageListener listr = ref.get();
			if(listr == null) {
				_listeners.remove(i);
				continue;
			}
			if(listr == listener)
				return true;
		}
		return false;
	}
	
	public void addListener(VLMessageListener listener) {
		this.correctListenersList();
		if(listener == null)
			return;
		if(!this.containsListener(listener)) {
			_listeners.add(new ListenerReference(listener));
		}
	}
	
	public void removeListener(VLMessageListener listener) {
		this.correctListenersList();
		if(listener == null)
			return;
		//if(this.ContainsListener(listener)) {
			for(int i = _listeners.size()-1; i >= 0; i--) {
				ListenerReference ref = _listeners.get(i);
				VLMessageListener listr = ref.get();
				if(listr == listener)
					_listeners.remove(i);
			}
		//}
	}
	
	public void postMessage(Object args) {
		_args = args;
		if(_msgPosted)
			return;
		_msgPosted = true;
		_postCanceled = false;
		if(_handler == null)
			_handler = new Handler();

		_handler.post(
			new Runnable() {
				@Override
				public void run() {
					_msgPosted = false;
					Object args = _args;
					_args = null;
					if(_postCanceled) {
						_postCanceled = false;
						return;
					}
					VLMessenger.this.correctListenersList();
					ArrayList<ListenerReference> listeners = new ArrayList<ListenerReference>();
					listeners.addAll(_listeners);
					for (ListenerReference ref : listeners) {
						VLMessageListener listener = ref.get();
						if(VLMessenger.this.containsListener(listener))
							listener.onMessage(VLMessenger.this, args);
					}
				}
			}
		);
	}
	
	public void postMessage() {
		this.postMessage(null);
	}
	
	public void cancelPostMessage() {
		_postCanceled = true;
	}
}


