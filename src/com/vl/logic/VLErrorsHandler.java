package com.vl.logic;

import java.util.ArrayList;

public class VLErrorsHandler extends VLLogicObject {
	
	private static VLErrorsHandler _instance;
	private ArrayList<Item> _items = new ArrayList<VLErrorsHandler.Item>();
	
	public static class Item {
		private final String _message;
		
		public Item(String message) {
			_message = message;
		}
		
		public String mesage() {
			return _message;
		}
	}
	
	public static VLErrorsHandler getInstance() {
		if(_instance == null) {
			_instance = new VLErrorsHandler();
		}
		return _instance;
	}
	
	private void addItem(Item item) {
		synchronized (_items) {
			_items.add(item);
		}
		this.modifyVersion();
	}
	
	public void handleError(String message) {
		Item item = new Item(message);
		addItem(item);
	}
	
	public Item popItem() {
		Item item = null;
		synchronized (_items) {
			if(_items.size() > 0)
				item = _items.get(0);
		}
		return item;
	}
}
