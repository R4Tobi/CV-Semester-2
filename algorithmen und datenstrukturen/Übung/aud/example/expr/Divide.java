package aud.example.expr;

/** binary / operator: A/B */
public class Divide extends Operator {
  /** create operation */
  public Divide() {}

  @Override public double getValue() {
    ExpressionTree left =(ExpressionTree) node_.getLeft();
    ExpressionTree right=(ExpressionTree) node_.getRight();
    return
      left.getData().getValue() / right.getData().getValue();
  }
  @Override public Type getType() { return Type.OpDivide; }
  @Override public Divide clone() { return new Divide(); }

  @Override public String toString() { return "/"; }
}
