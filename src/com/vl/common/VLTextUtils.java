package com.vl.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VLTextUtils {
	
	private static Pattern _emailPattern;
	private static final String _EMAIL_PATTERN = 
                 "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	  /**
	   * Validate hex with regular expression
	   * @param hex hex for validation
	   * @return true valid hex, false invalid hex
	   */
	public static boolean validateEmail(final String email) {
		if(_emailPattern == null)
			_emailPattern = Pattern.compile(_EMAIL_PATTERN);
		Matcher matcher = _emailPattern.matcher(email);
		return matcher.matches();
	}
}
