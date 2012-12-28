package com.vl.system;

import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class VLPreferencesStorage {
	
	private String _key = "";
	private int _version = 1;
	private static final String _keyForStoredVersion = "stored_version";
	
	public VLPreferencesStorage(String key, int version) {
		_key = key;
		_version = version;
	}
	
	String getKeyForStore() {
		return String.format("%s_%s", "VLPreferencesStorage", _key);
	}
	
	public void writeObject(VLKeysValuesSerializable obj) {
		Context context = VLSystemMediator.getInstance().getMainActivity();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		VLKeysValuesSerializer encoder = new VLKeysValuesSerializer();
		encoder.start();
		try {
			encoder.writeInt(_keyForStoredVersion, _version);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return;
		}
		obj.write(encoder);
		String sData = "";
		try {
			sData = encoder.end();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String key = getKeyForStore();
		SharedPreferences.Editor edit = prefs.edit();
		edit.putString(key, sData);
		edit.commit();
	}
	
	public boolean readObject(VLKeysValuesSerializable obj) {
		String key = getKeyForStore();
		Context context = VLSystemMediator.getInstance().getMainActivity();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String sData = prefs.getString(key, "");
		if(sData == null || sData.equals(""))
			return false;
		
		VLKeysValuesSerializer decoder;
		try {
			decoder = new VLKeysValuesSerializer(sData);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return false;
		}

		int version;
		try {
			version = decoder.readInt(_keyForStoredVersion);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		if(version != _version)
			return false;
		
		obj.read(decoder);
		
		return true;
	}
}


