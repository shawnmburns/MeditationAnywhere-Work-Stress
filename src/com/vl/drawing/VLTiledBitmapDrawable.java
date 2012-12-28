package com.vl.drawing;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;

public class VLTiledBitmapDrawable extends BitmapDrawable {
	
	private Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG);
    private boolean mRebuildShader = true;
    private Matrix mMatrix = new Matrix();

    public VLTiledBitmapDrawable(Resources res, Bitmap bitmap) {
    	super(res, bitmap);
	}
    
    public VLTiledBitmapDrawable(Resources res, int imageResourceId) {
    	this(res, VLImagesCache.getInstance().getImageByResource(imageResourceId, res));
	}

	@Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = getBitmap();
        if (bitmap == null)
            return;

        if (mRebuildShader)  {
            mPaint.setShader(new BitmapShader(bitmap, TileMode.REPEAT, TileMode.REPEAT));
            mRebuildShader = false;
        }

        // Translate down by the remainder
        mMatrix.setTranslate(0, getBounds().bottom % getIntrinsicHeight());
        canvas.save();
        canvas.setMatrix(mMatrix);
        canvas.drawRect(getBounds(), mPaint);
        canvas.restore();
    }
}
