package com.vl.system;

import org.json.JSONException;

// Not completed

public class VLFileStorage {

	private String _key = "";
	private int _version = 1;
	private static final String _keyForStoredVersion = "stored_version";
	
	public VLFileStorage(String key, int version) {
		_key = key;
		_version = version;
	}
	
	@SuppressWarnings("unused")
	private String getFileNameForStore() {
		return String.format("%s_%s.dat", "VLFileStorage", _key);
	}
	
	public void writeObject(VLKeysValuesSerializable obj) {
		VLKeysValuesSerializer encoder = new VLKeysValuesSerializer();
		encoder.start();
		try {
			encoder.writeInt(_keyForStoredVersion, _version);
		} catch (JSONException e1) {
			e1.printStackTrace();
			return;
		}
		obj.write(encoder);
		@SuppressWarnings("unused")
		String sData = "";
		try {
			sData = encoder.end();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
