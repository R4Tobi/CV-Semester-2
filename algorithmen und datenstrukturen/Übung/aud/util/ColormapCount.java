package aud.util;

/** color map for (small) positive integer counts */
public class ColormapCount extends Colormap<Integer> {
  @Override public int getRGB(Integer data) {

    if (data<0)
      return 0xff00ff; // magenta

    switch (data) {
    case 0:  return 0xffffff;
    case 1:  return 0x202020;
    case 2:  return 0x0000ff;
    case 3:  return 0x00aaff;
    case 4:  return 0x00ffff;
    case 5:  return 0x00ffaa;
    case 6:  return 0x00ff00;
    case 7:  return 0xaaff00;
    case 8:  return 0xffff00;
    case 9:  return 0xffaa00;
    case 10: return 0xff5500;
    default: return 0xff0000; // red
    }
  }
}
