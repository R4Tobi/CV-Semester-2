package aud.example.expr;

/** Node representing constant number.
    @see ExpressionTree
 */
public class Number extends Terminal {

  double value_;

  /** create number */
  public Number(double value) { value_=value; }

  @Override public double getValue() { return value_;  }

  @Override public Type getType() { return Type.TNumber; }

  @Override public Number clone() { return new Number(value_); }

  @Override public String toString() {
    String text=String.format("%.12g",value_);

    // remove trailing zeros (HACK due to java's limited formatting)
    if (text.contains(".") && !text.contains("eE"))
      text=text.replaceFirst("\\.?0*$","");

    return text;
  }
}
