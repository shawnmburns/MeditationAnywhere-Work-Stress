package com.vl.system;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class VLKeysValuesSerializer {

	private JSONObject _json = null;
	
	public VLKeysValuesSerializer() {
	}
	
	public VLKeysValuesSerializer(String sJson) throws JSONException {
		_json = new JSONObject(sJson);
	}
	
	public void start() {
		_json = new JSONObject();
	}
	
	public Iterator<?> keys() {
		return _json.keys();
	}
	
	public boolean containsKey(String key) {
		return _json.has(key);
	}
	
	public void writeSerializable(String key, VLKeysValuesSerializable value) throws JSONException {
		VLKeysValuesSerializer encoder = new VLKeysValuesSerializer();
		encoder.start();
		value.write(encoder);
		String sJson = encoder.end();
		_json.put(key, sJson);
	}
	
	public void readSerializable(String key, VLKeysValuesSerializable value) throws JSONException {
		if(!_json.has(key))
			return;
		String sJson = _json.getString(key);
		VLKeysValuesSerializer decoder = new VLKeysValuesSerializer(sJson);
		value.read(decoder);
	}

	public void writeString(String key, String value) throws JSONException {
		_json.put(key, value);
	}
	
	public String readString(String key, String defaultValue) throws JSONException {
		if(!_json.has(key))
			return defaultValue;
		String result = _json.getString(key);
		if(result == null)
			return defaultValue;
		return result;
	}
	
	public void writeInt(String key, int value) throws JSONException {
		_json.put(key, value);
	}
	
	public int readInt(String key, int defaultValue) throws JSONException {
		if(!_json.has(key))
			return defaultValue;
		int result = _json.getInt(key);
		return result;
	}
	public int readInt(String key) throws JSONException {
		return readInt(key, 0);
	}
	
	public void writeBool(String key, boolean value) throws JSONException {
		this.writeInt(key, value ? 1 : 0);
	}
	
	public boolean readBool(String key) throws JSONException {
		return this.readInt(key) != 0;
	}
	
	public void writeDouble(String key, double value) throws JSONException {
		_json.put(key, value);
	}
	
	public double readDouble(String key, double defaultVal) throws JSONException {
		if(!_json.has(key))
			return defaultVal;
		double result = _json.getDouble(key);
		return result;
	}
	public double readDouble(String key) throws JSONException {
		return readDouble(key, 0);
	}
	
	public void writeArray(String key, ArrayList<? extends VLKeysValuesSerializable> array) throws JSONException {
		VLKeysValuesSerializer encoder = new VLKeysValuesSerializer();
		encoder.start();
		for(int i = 0; i < array.size(); i++) {
			VLKeysValuesSerializable item = array.get(i);
			String itemKey = "" + i;
			encoder.writeSerializable(itemKey, item);
		}
		String sJsonArray = encoder.end();
		_json.put(key, sJsonArray);
	}
	
	@SuppressWarnings("unchecked")
	public <Type extends VLKeysValuesSerializable> void readArray(String key, ArrayList<Type> array, Class<? extends VLKeysValuesSerializable> itemClass) throws JSONException {
		array.clear();
		if(!_json.has(key))
			return;
		String sJsonArray = _json.getString(key);
		VLKeysValuesSerializer decoder = new VLKeysValuesSerializer(sJsonArray);
		Iterator<?> keys = decoder.keys();
		while(keys.hasNext()) {
			String itemKey = (String)keys.next();
			Type item = null;
			try {
				item = (Type)itemClass.newInstance();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			decoder.readSerializable(itemKey, item);
			array.add(item);
		}
	}
	
	
	public String end() throws JSONException {
		String sJson = _json.toString(4);
		return sJson;
	}
}
