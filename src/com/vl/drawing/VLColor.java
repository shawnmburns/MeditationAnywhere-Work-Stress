package com.vl.drawing;

import android.graphics.Color;

public class VLColor {
	
	public float Red = 0;
	public float Green = 0;
	public float Blue = 0;
	public float Alpha = 1.0f;
	
	public VLColor() {
	}
	
	public VLColor(VLColor other) {
		Red = other.Red;
		Green = other.Green;
		Blue = other.Blue;
		Alpha = other.Alpha;
	}
	
	public VLColor(int hexRgbValue) {
		this.setHexRgbValue(hexRgbValue);
	}
	
	public VLColor(float red, float green, float blue, float alpha) {
		Red = red;
		Green = green;
		Blue = blue;
		Alpha = alpha;
	}
	
	public VLColor(float red, float green, float blue) {
		this(red, green, blue, 1.0f);
	}
	
	public static VLColor colorWithHex(int hexRValue, int hexGValue, int hexBValue) {
		return new VLColor(hexRValue/255f, hexGValue/255f, hexBValue/255f);
	}
	
	public void setHexRgbValue(int hexRgbValue) {
		Red = ((hexRgbValue >> 16) & 0xFF) / 255.0f;
		Green = ((hexRgbValue >> 8) & 0xFF) / 255.0f;
		Blue = ((hexRgbValue >> 0) & 0xFF) / 255.0f;
	}
	
	public void setHexArgbValue(int hexArgbValue) {
		Alpha = ((hexArgbValue >> 24) & 0xFF) / 255.0f;
		Red = ((hexArgbValue >> 16) & 0xFF) / 255.0f;
		Green = ((hexArgbValue >> 8) & 0xFF) / 255.0f;
		Blue = ((hexArgbValue >> 0) & 0xFF) / 255.0f;
	}
	
	public int getHexArgbValue() {
		int result = (((int)(Alpha * 255.0)) << 24) | (((int)(Red * 255.0)) << 16)
				| (((int)(Green * 255.0)) << 8) | (((int)(Blue * 255.0)) << 0);
		return result;
	}
	
	public class ColorsSetBase {
		
	}
	
	public static class ColorsSetCommon {
		public VLColor White = new VLColor(0xFFFFFF);
		public VLColor Silver = new VLColor(0xC0C0C0);
		public VLColor Gray = new VLColor(0x808080);
		public VLColor Black = new VLColor(0x000000);
		public VLColor Red = new VLColor(0xFF0000);
		public VLColor Maroon = new VLColor(0x800000);
		public VLColor Yellow = new VLColor(0xFFFF00);
		public VLColor Olive = new VLColor(0x808000);
		public VLColor Lime = new VLColor(0x00FF00);
		public VLColor Green = new VLColor(0x008000);
		public VLColor Aqua = new VLColor(0x00FFFF);
		public VLColor Teal = new VLColor(0x008080);
		public VLColor Blue = new VLColor(0x0000FF);
		public VLColor Navy = new VLColor(0x000080);
		public VLColor Fuchsia = new VLColor(0xFF00FF);
		public VLColor Purple = new VLColor(0x800080);
		
		public VLColor Transparent = new VLColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	public static final ColorsSetCommon ColorsCommon = new ColorsSetCommon();
	
	public int toIntColor() {
		int result = Color.argb((int)(Alpha*255), (int)(Red*255), (int)(Green*255), (int)(Blue*255));
		return result;
	}
	
	public static VLColor createGray(float lightness) {
		return new VLColor(lightness, lightness, lightness);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof VLColor))
			return false;
		VLColor other = (VLColor)o;
		return this.Red == other.Red && this.Green == other.Green
				&& this.Blue == other.Blue && this.Alpha == other.Alpha;
	}
	
	static long HSL2RGB(float h, float sl, float l) {
		float outR = 0, outG = 0, outB = 0;
		if(sl == 0.0) {
			outR = l;
			outG = l;
			outB = l;
		} else {
			float v;
			float r,g,b;
			
			r = l;   // default to gray
			g = l;
			b = l;
			v = (l <= 0.5f) ? (l * (1.0f + sl)) : (l + sl - l * sl);
			if (v > 0) {
				float m;
				float sv;
				int sextant;
				float fract, vsf, mid1, mid2;
				
				m = l + l - v;
				sv = (v - m ) / v;
				h *= 6.0;
				sextant = (int)h;
				fract = h - sextant;
				vsf = v * sv * fract;
				mid1 = m + vsf;
				mid2 = v - vsf;
				//////////////////
				if(sextant >= 6)
					sextant -= 6;
				//////////////////
				switch (sextant) {
					case 0:
						r = v;
						g = mid1;
						b = m;
						break;
					case 1:
						r = mid2;
						g = v;
						b = m;
						break;
					case 2:
						r = m;
						g = v;
						b = mid1;
						break;
					case 3:
						r = m;
						g = mid2;
						b = v;
						break;
					case 4:
						r = mid1;
						g = m;
						b = v;
						break;
					case 5:
						r = v;
						g = m;
						b = mid2;
						break;
				}
			}
			outR = r;
			outG = g;
			outB = b;
		}
		int outR_l = (int) (outR * 65535);
		int outG_l = (int) (outG * 65535);
		int outB_l = (int) (outB * 65535);
		long result = (outR_l << 32) | (outG_l << 16) | outB_l;
		return result;
	}
	
	static long RGB2HSL(float r, float g, float b) {
		float outH = 0, outS = 0, outL = 0;
		float v;
		float m;
		float vm;
		float r2, g2, b2;
		
		float h,s,l;
		
		h = 0; // default to black
		s = 0;
		l = 0;
		outH = h;
		outS = s;
		outL = l;
		v = Math.max(r,g);
		v = Math.max(v,b);
		m = Math.min(r,g);
		m = Math.min(m,b);
		l = (m + v) / 2.0f;
		if (l <= 0.0) {
			return 0;
		}
		vm = v - m;
		s = vm;
		if (s > 0.0) {
			s /= (l <= 0.5) ? (v + m ) : (2.0 - v - m) ;
		} else {
			return 0;
		}
		r2 = (v - r) / vm;
		g2 = (v - g) / vm;
		b2 = (v - b) / vm;
		if (r == v) {
			h = (g == m ? 5.0f + b2 : 1.0f - g2);
		} else if (g == v) {
			h = (b == m ? 1.0f + r2 : 3.0f - b2);
		} else {
			h = (r == m ? 3.0f + g2 : 5.0f - r2);
		}
		h /= 6.0;
		
		outH = h;
		outS = s;
		outL = l;
		int outH_l = (int) (outH * 65535);
		int outS_l = (int) (outL * 65535);
		int outL_l = (int) (outS * 65535);
		long result = (outH_l << 32) | (outS_l << 16) | outL_l;
		return result;
	}
	
	long getHueSatLight() {
		long res = RGB2HSL(Red, Green, Blue);
		return res;
	}
	
	void setHueSatLight(float hue, float saturation, float lightness) {
		long val = HSL2RGB(hue, saturation, lightness);
		Red = ((val & 0xFFFF00000000l) >> 32) / 65535.0f;
		Green = ((val & 0xFFFF0000l) >> 16) / 65535.0f;
		Blue = ((val & 0xFFFFl) >> 0) / 65535.0f;
	}
	
	public float getLightness() {
		if(Red == Green && Green == Blue)
			return Red;
		if((Red==0 && Green==0) || (Green==0 && Blue==0) || (Blue==0 && Red==0)) {
			if(Red!=0)
				return Red;
			if(Green!=0)
				return Green;
			if(Blue!=0)
				return Blue;
		}
		long val = getHueSatLight();
		int valI = (int) ((int)val & 0xFFFF);
		float lightness = valI / 65535.0f;
		return lightness;
	}
	
	public void setLightness(float value) {
		value = (float) Math.max(Math.min(value, 1.0f), 0);
		if(Red == Green && Green == Blue) {
			Red = Green = Blue = value;
			return;
		}
		if((Red==0 && Green==0) || (Green==0 && Blue==0) || (Blue==0 && Red==0)) {
			if(Red!=0)
				Red = value;
			if(Green!=0)
				Green = value;
			if(Blue!=0)
				Blue = value;
			return;
		}
		long val = getHueSatLight();
		float hue = ((val & 0xFFFF00000000l) >> 32) / 65535.0f;
		float saturation = ((val & 0xFFFF0000l) >> 16) / 65535.0f;
		//float lightness = ((val & 0xFFFFl) >> 0) / 65535.0f;
		setHueSatLight(hue, saturation, value);
	}
	
	public VLColor lightUp(float value) {
		value = (float) Math.max(Math.min(value, 1.0f), 0);
		float curVal = this.getLightness();
		float newVal = curVal + (1.0f - curVal) * value;
		this.setLightness(newVal);
		return this;
	}
}
