package com.vl.system;

public interface VLKeysValuesSerializable {

	void write(VLKeysValuesSerializer encoder);
	void read(VLKeysValuesSerializer decoder);
	
}
