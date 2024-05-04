package aud.example.expr;

/** binary - operator: A-B */
public class Minus extends Operator {
  /** create operation */
  public Minus() {}

  @Override public double getValue() {
    ExpressionTree left =(ExpressionTree) node_.getLeft();
    ExpressionTree right=(ExpressionTree) node_.getRight();
    return
      left.getData().getValue() - right.getData().getValue();
  }
  @Override public Type getType() { return Type.OpMinus; }
  @Override public Minus clone() { return new Minus(); }
  @Override public String toString() { return "-"; }
}
