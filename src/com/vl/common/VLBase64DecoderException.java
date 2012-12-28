package com.vl.common;

/**
 * Exception thrown when encountering an invalid Base64 input character.
 *
 * @author nelson
 */
public class VLBase64DecoderException extends Exception {
  public VLBase64DecoderException() {
    super();
  }

  public VLBase64DecoderException(String s) {
    super(s);
  }

  private static final long serialVersionUID = 1L;
}
