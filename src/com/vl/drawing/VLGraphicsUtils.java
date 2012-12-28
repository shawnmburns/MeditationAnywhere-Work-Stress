package com.vl.drawing;

import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.ctrls.VLCtrlsUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.FloatMath;

public class VLGraphicsUtils {
	
	public static void applyFontToPaint(Paint paint, VLFontInfo font) {
		float height = font.height;
		if(font.heightRatio > 0)
			height = (int) Math.ceil(VLCtrlsUtils.partOfDisplayMinSideRounded(font.heightRatio));
		paint.setTextSize(height);
		Typeface tf = null;
		if(font.isBold && font.isItalic)
			tf = Typeface.create(font.name, Typeface.BOLD_ITALIC);
		else if(font.isBold)
			tf = Typeface.create(font.name, Typeface.BOLD);
		else if(font.isItalic)
			tf = Typeface.create(font.name, Typeface.ITALIC);
		else
			tf = Typeface.create(font.name, Typeface.NORMAL);
		paint.setTypeface(tf);
	}
	
	public static VLSizeF getTextSize(String text, VLFontInfo font) {
		VLSizeF result = new VLSizeF();
		float height = font.height;
		if(font.heightRatio > 0)
			height = (int) Math.ceil(VLCtrlsUtils.partOfDisplayMinSideRounded(font.heightRatio));
		Paint paint = new Paint();
		applyFontToPaint(paint, font);
		paint.setAntiAlias(true);
		int width = (int) FloatMath.ceil(paint.measureText(text));
		result.width = width;
		result.height = (int) height;
		return result;
	}
	
	public static void drawText(String text, Canvas canvas, float x, float y, Paint paint, float textHeight) {
		paint.setTextAlign(Align.LEFT);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
		canvas.drawText(text, (float)x, (float)(y + textHeight/2 + textHeight/3), paint);
	}
	
	public static void drawText(String text, VLFontInfo font, Canvas canvas, float x, float y, Paint paint) {
		applyFontToPaint(paint, font);
		float textHeight = font.height;
		if(font.heightRatio > 0)
			textHeight = (int) Math.ceil(VLCtrlsUtils.partOfDisplayMinSideRounded(font.heightRatio));
		drawText(text, canvas, x, y, paint, textHeight);
	}
	
	public static void drawBitmap(Canvas canvas, Bitmap bitmap, VLRectF rect, VLPaint paint, boolean smoothly) {
		if(smoothly) {
			paint.setFilterBitmap(true);
			paint.setDither(true);
		}
		canvas.drawBitmap(bitmap,
			(new VLRectF(0, 0, bitmap.getWidth(), bitmap.getHeight())).toRect(),
			rect.toRectF(), paint);
	}
	
	public static void drawBitmap(Canvas canvas, Bitmap bitmap, VLRectF rect, boolean smoothly) {
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		drawBitmap(canvas, bitmap, rect, paint, smoothly);
	}
	
	public static void drawRoundedRect(Canvas canvas, VLPaint paint, VLRectF rect, float cornerRadius, VLColor colorBack) {
		paint.setColor(colorBack.toIntColor());
		canvas.drawRoundRect(rect.toRectF(), cornerRadius, cornerRadius, paint);
	}
	
	public static void drawBitmapTiledHorizontally(Bitmap bmp, Canvas canvas, VLRectF rect) {
		int bmpWidth = bmp.getWidth();
		Rect rcBmp = (new VLRectF(0, 0, bmp.getWidth(), bmp.getHeight())).toRect();
		VLRectF rcPart = new VLRectF(rect);
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.save();
		canvas.clipRect(rect.toRect());
		for(int x = (int) rect.left; x < rect.right; x += bmpWidth) {
			rcPart.left = x;
			rcPart.setWidth(bmpWidth);
			canvas.drawBitmap(bmp, rcBmp, rcPart.toRectF(), paint);
		}
		canvas.restore();
	}
	
	public static void drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY,
			VLColor lineColor, float lineWidth) {
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		paint.setColor(lineColor.toIntColor());
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(lineWidth);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}
}
