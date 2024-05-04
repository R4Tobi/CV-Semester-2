package aud.util;

/** simple interface for color map */
public class Colormap<T> {
  /** Map data to rgb color.
   *  Returns RGB color as integer {@code 0xRRGGBB} with hex digits
   *  {@code R,B,G}). The default implementation returns 0x000000 (black).
   */
  public int getRGB(T data) {
    return 0x000000;
  }

  /** get string representation from {@link #getRGB} */
  public String getString(T data) {
    return String.format("#%06x",getRGB(data));
  }
}
