package aud.example.expr;

/** binary + operator: A+B */
public class Plus extends Operator {
  /** create operation */
  public Plus() {}

  @Override public double getValue() {
    ExpressionTree left =(ExpressionTree) node_.getLeft();
    ExpressionTree right=(ExpressionTree) node_.getRight();
    return
      left.getData().getValue() + right.getData().getValue();
  }
  @Override public Type getType() { return Type.OpPlus; }
  @Override public Plus clone() { return new Plus(); }
  @Override public String toString() { return "+"; }
}
