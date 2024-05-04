package aud.util;

/** map values in {@code [minValue,maxValue]} to color */
public class ColormapJet extends Colormap<Double> {

  public double minValue=0.0;
  public double maxValue=1.0;

  static int JET[] = { // borrowed from octave
      0x00008f, 0x00009f, 0x0000af, 0x0000bf, 0x0000cf,
      0x0000df, 0x0000ef, 0x0000ff, 0x0010ff, 0x0020ff, 0x0030ff, 0x0040ff,
      0x0050ff, 0x0060ff, 0x0070ff, 0x0080ff, 0x008fff, 0x009fff, 0x00afff,
      0x00bfff, 0x00cfff, 0x00dfff, 0x00efff, 0x00ffff, 0x10ffef, 0x20ffdf,
      0x30ffcf, 0x40ffbf, 0x50ffaf, 0x60ff9f, 0x70ff8f, 0x80ff80, 0x8fff70,
      0x9fff60, 0xafff50, 0xbfff40, 0xcfff30, 0xdfff20, 0xefff10, 0xffff00,
      0xffef00, 0xffdf00, 0xffcf00, 0xffbf00, 0xffaf00, 0xff9f00, 0xff8f00,
      0xff8000, 0xff7000, 0xff6000, 0xff5000, 0xff4000, 0xff3000, 0xff2000,
      0xff1000, 0xff0000, 0xef0000, 0xdf0000, 0xcf0000, 0xbf0000, 0xaf0000,
      0x9f0000, 0x8f0000, 0x800000, };

  static int INVALID = 0xffffff;

  public ColormapJet() {}

  public ColormapJet(double minValue,double maxValue) {
    this.minValue=minValue;
    this.maxValue=maxValue;
  }

  @Override public int getRGB(Double data) {

    assert(maxValue>minValue);

    if (Double.isInfinite(data))
      return INVALID;

    if (data<minValue)
      return JET[0];
    if (data>maxValue)
      return JET[JET.length-1];

    double t=(data-minValue)/(maxValue-minValue);

    assert(0.0<=t && t<=1.0);

    return JET[(int) (t*(JET.length-1))];
  }

  public static void main(String[] args) {
    ColormapJet jet=new ColormapJet();
    jet.minValue=-2.0;
    jet.maxValue= 2.0;

    double t=-2.125;
    while (t<=2.125) {
      System.out.println(t+" => "+jet.getString(t));
      t+=0.125;
    }
  }
}
