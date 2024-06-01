package aud.example.expr;

import java.lang.Math;

/** binary power operator: A^B (also A**B) */
public class Power extends Operator {
  /** create operation */
  public Power() {}

  @Override public double getValue() {
    ExpressionTree left =(ExpressionTree) node_.getLeft();
    ExpressionTree right=(ExpressionTree) node_.getRight();
    return
      Math.pow(left.getData().getValue(),right.getData().getValue());
  }
  @Override public Type getType() { return Type.OpPower; }
  @Override public Power clone() { return new Power(); }
  @Override public String toString() { return "**"; }
}
