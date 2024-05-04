package aud.example.expr;

/** unary - operator: -A ("sign") */
public class UnaryMinus extends Operator {
  /** create operation */
  public UnaryMinus() {}

  @Override public double getValue() {
    ExpressionTree left =(ExpressionTree) node_.getLeft();
    assert(node_.getRight()==null); // unary operation
    return -left.getData().getValue();
  }
  @Override public Type getType() { return Type.OpUnaryMinus; }
  @Override public UnaryMinus clone() { return new UnaryMinus(); }
  @Override public String toString() { return "-"; }
}
