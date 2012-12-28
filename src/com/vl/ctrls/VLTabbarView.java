package com.vl.ctrls;

import java.util.ArrayList;

import com.vl.common.VLGeometry;
import com.vl.common.VLPointF;
import com.vl.common.VLRectF;
import com.vl.common.VLSizeF;
import com.vl.drawing.VLColor;
import com.vl.drawing.VLFontInfo;
import com.vl.drawing.VLGraphicsUtils;
import com.vl.drawing.VLPaint;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;

public class VLTabbarView extends VLBaseLayout {
	
	private ArrayList<Item> _items = new ArrayList<VLTabbarView.Item>();
	private int _selectedItemIndex = -1;
	private VLColor _textColor = new VLColor(240/255f, 240/255f, 240/255f);
	private VLColor _textColorH = new VLColor(84/255f, 189/255f, 240/255f);
	private VLColor _backColorH = new VLColor(25/255f, 29/255f, 33/255f, 0.8f);
	private VLFontInfo _font = new VLFontInfo();
	private static float _distanceRatio = 0.04f;
	private OnItemSelectedListener _itemSelectedListener;
	
	public VLTabbarView(Context context) {
		super(context);
		this.setBackgroundColor(Color.DKGRAY);
		_font.heightRatio = 0.03f;
		
		this.setClickable(true);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	private VLRectF getRectOfItem(int index) {
		VLRectF rcBnds = getBounds();
		VLRectF rcItem = new VLRectF(rcBnds);
		rcItem.setWidth(rcBnds.getWidth() / _items.size());
		rcItem.move(rcItem.getWidth() * index, 0);
		return rcItem;
	}

	private int getItemIndexByPoint(VLPointF pt) {
		for(int i = 0; i < _items.size(); i++) {
			VLRectF rcItem = getRectOfItem(i);
			if(rcItem.containsPoint(pt))
				return i;
		}
		return -1;
	}
	
	@SuppressWarnings("unused")
	private Item getItemByPoint(VLPointF pt) {
		int index = getItemIndexByPoint(pt);
		if(index < 0)
			return null;
		return _items.get(index);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.onDrawBackground(canvas);
		for(int nItem = 0; nItem < _items.size(); nItem++)
		{
			Item item = _items.get(nItem);
			VLRectF rcItem = getRectOfItem(nItem);
			this.onDrawItem(canvas, nItem, item, rcItem);
		}
	}
	
	protected void onDrawBackground(Canvas canvas) {
		
	}
	
	protected void onDrawItemBackground(Canvas canvas, int itemIndex, Item item, VLRectF itemRect) {
		boolean selected = (itemIndex == _selectedItemIndex);
		if(selected)
		{
			VLPaint paint = new VLPaint();
			paint.setAntiAlias(true);
			paint.setColor(_backColorH.toIntColor());
			paint.setStyle(Style.FILL_AND_STROKE);
			canvas.drawRect(itemRect.toRectF(), paint);
		}
	}

	protected void onDrawItem(Canvas canvas, int itemIndex, Item item, VLRectF itemRect) {
		VLPaint paint = new VLPaint();
		paint.setAntiAlias(true);
		float distance = Math.round(itemRect.getHeight() * _distanceRatio);
		boolean selected = (itemIndex == _selectedItemIndex);

		VLRectF rcItemInner = itemRect.rectWithInsets(distance, distance);
		
		this.onDrawItemBackground(canvas, itemIndex, item, itemRect);
		
		VLRectF rcImageArea = new VLRectF(rcItemInner);
		rcImageArea.setHeight(rcImageArea.getHeight() * 0.67f);
		
		VLRectF rcTextArea = new VLRectF(rcItemInner);
		rcTextArea.top = rcImageArea.bottom + distance;
		rcTextArea.setHeight(rcItemInner.bottom - rcTextArea.top);
		
		Bitmap image = item.getImage();
		boolean imageIsSel = false;
		if(selected && item.getImageSel() != null) {
			image = item.getImageSel();
			imageIsSel = true;
		}
		if(image != null) {
			VLSizeF szImage = new VLSizeF(image.getWidth(), image.getHeight());
			VLRectF rcImage = VLGeometry.rectOfFitToRectWithSize(rcImageArea, szImage);
			rcImage = VLGeometry.roundRect(rcImage);
			if(selected && !imageIsSel) {
				VLPaint paintH = new VLPaint();
				paintH.setAntiAlias(true);
				float rgb = (_textColorH.Red + _textColorH.Green + _textColorH.Blue) / 3.0f;
				float r = _textColorH.Red / rgb;
				float g = _textColorH.Green / rgb;
				float b = _textColorH.Blue / rgb;
				float[] matrix = { 
						r, 0.0f, 0.0f, 0.0f, 0.0f, //red
				        0.0f, g, 0.0f, 0.0f, 0.0f, //green
				        0.0f, 0.0f, b, 0.0f, 0.0f, //blue
				        0.0f, 0.0f, 0.0f, 1.0f, 0.0f //alpha
				    };
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
				paintH.setColorFilter(filter);
				VLGraphicsUtils.drawBitmap(canvas, image, rcImage, paintH, true);
			} else {
				VLGraphicsUtils.drawBitmap(canvas, image, rcImage, paint, true);
			}
		}
		
		String text = item.getText();
		if(!text.equals("")) {
			VLColor textColor = selected ? _textColorH : _textColor;
			VLSizeF szText = VLGraphicsUtils.getTextSize(text, _font);
			VLRectF rcText = new VLRectF(rcTextArea);
			rcText.setSize(szText);
			rcText.moveToX(rcTextArea.getMiddleX() - szText.width / 2);
			rcText.moveToY(rcTextArea.getMiddleY() - szText.height / 2);
			rcText = VLGeometry.roundRect(rcText);
			paint.setColor(textColor.toIntColor());
			VLGraphicsUtils.drawText(text, _font, canvas, rcText.left, rcText.top, paint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!this.isEnabled())
			return super.onTouchEvent(event);
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
		VLPointF pt = new VLPointF(x, y);
		int itemIndex = getItemIndexByPoint(pt);
		if(action == MotionEvent.ACTION_DOWN) {
			if(itemIndex >= 0 && itemIndex != getSelectedItemIndex()) {
				int lastIndex = getSelectedItemIndex();
				this.setSelectedItemIndex(itemIndex);
				this.performClick();
				if(_itemSelectedListener != null)
					_itemSelectedListener.onItemSelected(null, this, itemIndex, lastIndex);
			}
			return true;
		} else if(action == MotionEvent.ACTION_MOVE) {
			return true;
		} else if(action == MotionEvent.ACTION_UP) {
			return true;
		} else if(action == MotionEvent.ACTION_CANCEL) {
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	public static class Item {
		private String _text = "";
		private Bitmap _image = null;
		private Bitmap _imageSel = null;
		
		public String getText() {
			return _text;
		}
		
		public void setText(String text) {
			_text = text;
		}
		
		public Bitmap getImage() {
			return _image;
		}
		
		public void setImage(Bitmap image) {
			_image = image;
		}
		
		public Bitmap getImageSel() {
			return _imageSel;
		}
		
		public void setImageSel(Bitmap imageSel) {
			_imageSel = imageSel;
		}
	}
	
	public Item addItem(String text, Bitmap image, Bitmap imageSel) {
		Item item = new Item();
		item.setText(text);
		item.setImage(image);
		item.setImageSel(imageSel);
		_items.add(item);
		this.invalidate();
		return item;
	}
	
	public Item addItem(String text, Bitmap image) {
		return addItem(text, image, null);
	}

	public int getSelectedItemIndex() {
		return _selectedItemIndex;
	}
	
	public void setSelectedItemIndex(int selectedItemIndex) {
		if(selectedItemIndex >= _items.size())
			selectedItemIndex = _items.size() - 1;
		if(selectedItemIndex < -1)
			selectedItemIndex = -1;
		if(_selectedItemIndex != selectedItemIndex) {
			_selectedItemIndex = selectedItemIndex;
			this.invalidate();
		}
	}
	
	public void setOnItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
		_itemSelectedListener = itemSelectedListener;
	}

	public VLFontInfo font() {
		return _font;
	}

	public void setFont(VLFontInfo font) {
		_font = font;
		this.invalidate();
	}
}
