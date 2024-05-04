package aud.example.expr;

/** binary * operator: A*B */
public class Times extends Operator {
  /** create operation */
  public Times() {}

  @Override public double getValue() {
    ExpressionTree left =(ExpressionTree) node_.getLeft();
    ExpressionTree right=(ExpressionTree) node_.getRight();
    return
      left.getData().getValue() * right.getData().getValue();
  }
  @Override public Type getType() { return Type.OpTimes; }
  @Override public Times clone() { return new Times(); }
  @Override public String toString() { return "*"; }
}
