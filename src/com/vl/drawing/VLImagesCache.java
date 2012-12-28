package com.vl.drawing;

import java.util.Hashtable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class VLImagesCache {
	
	private static VLImagesCache _instance;
	private Hashtable<Integer, Bitmap> _imagesByResId = new Hashtable<Integer, Bitmap>();
	
	public VLImagesCache() {
		
	}
	
	public static VLImagesCache getInstance() {
		if(_instance == null) {
			_instance = new VLImagesCache();
		}
		return _instance;
	}
	
	public Bitmap getImageByResource(int resourceId, Resources resources) {
		Integer num = Integer.valueOf(resourceId);
		Bitmap res = _imagesByResId.get(num);
		if(res != null)
			return res;
		res = BitmapFactory.decodeResource(resources, resourceId);
		if(res != null)
			_imagesByResId.put(num, res);
		return res;
	}
}

