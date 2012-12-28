package com.vl.common;

public class VLGeometry {
	public static VLRectF roundRect(VLRectF rect) {
		VLRectF result = new VLRectF();
		result.left = Math.round(rect.left);
		result.top = Math.round(rect.top);
		result.right = Math.round(rect.right);
		result.bottom = Math.round(rect.bottom);
		return result;
	}
	
	public static VLSizeF roundSize(VLSizeF size) {
		VLSizeF result = new VLSizeF();
		result.width = Math.round(size.width);
		result.height = Math.round(size.height);
		return result;
	}
	
	public static VLRectF scaleRectToFitWidth(VLRectF rect, float width) {
		VLRectF result = new VLRectF(rect);
		if(result.getWidth() == 0)
			return result;
		float scale = width / result.getWidth();
		result.setWidth(width);
		result.setHeight(result.getHeight() * scale);
		return result;
	}
	
	public static VLRectF rectOfFitToRectWithSize(VLRectF rect, VLSizeF size) {
		VLRectF res = new VLRectF(rect);
		if(rect.getWidth() == 0 || rect.getHeight() == 0 || size.width == 0 || size.height == 0)
			return res;
		float rectScale = rect.getWidth() / rect.getHeight();
		float sizeScale = size.width / size.height;
		if(sizeScale > rectScale) {
			res.setHeight(rect.getWidth() / sizeScale);
			res.moveToY(rect.top + rect.getHeight() / 2 - res.getHeight() / 2);
		} else if(sizeScale < rectScale) {
			res.setWidth(rect.getHeight() * sizeScale);
			res.moveToX(rect.left + rect.getWidth() / 2 - res.getWidth() / 2);
		}
		return res;
	}
	
	public static VLRectF rectWithInsetsRel(VLRectF rect, VLRectF insetsRel) {
		VLRectF result = new VLRectF(rect);
		result.left += rect.getWidth() * insetsRel.left;
		result.top += rect.getHeight() * insetsRel.top;
		result.right -= rect.getWidth() * insetsRel.right;
		result.bottom -= rect.getHeight() * insetsRel.bottom;
		return result;
	}
	
	public static VLRectF rectWithInsetsRel(VLRectF rect, float insetXRel, float insetYRel) {
		float insetX = rect.getWidth() * insetXRel;
		float insetY = rect.getHeight() * insetYRel;
		VLRectF result = new VLRectF(rect);
		result.left += insetX;
		result.top += insetY;
		result.right -= insetX;
		result.bottom -= insetY;
		return result;
	}
	
	public static float correctAngle(float angle) {
		angle = (float) (angle - (long)(angle / (Math.PI*2)) * (Math.PI*2));
		if(angle < 0)
			angle += Math.PI*2;
		return angle;
	}
	
	public static float getDifferenceBetweenAngles(float angle1, float angle2) {
		angle1 = correctAngle(angle1);
		angle2 = correctAngle(angle2);
		float result = angle1 - angle2;
		result = correctAngle(result);
		return result;
	}
	
	public static boolean isPointInCicle(VLPointF pt, VLPointF ptCenter, float radius) {
		float dx = pt.x - ptCenter.x;
		float dy = pt.y - ptCenter.y;
		float dist = (float) Math.sqrt(dx*dx + dy*dy);
		return (dist <= radius);
	}
	
	public static float getDistanceBetweenPoints(VLPointF p1, VLPointF p2) {
		return (float) Math.sqrt((p2.x - p1.x)*(p2.x - p1.x) + (p2.y - p1.y)*(p2.y - p1.y));
	}
}
