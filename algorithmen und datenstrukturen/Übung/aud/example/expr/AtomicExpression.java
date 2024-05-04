package aud.example.expr;

/** Superclass for data associated with a tree node.
    @see ExpressionTree
 */
public abstract class AtomicExpression {
  /** uplink reference to node: {@code node_.getData()==this} <p>

      Set by constructor of {@link ExpressionTree}. <em>This is not
      good style but alllows for reusing {@link aud.BinaryTree} as
      is.</em>
   */
  protected ExpressionTree node_ = null;

  /** node represents operator */
  public boolean isOperator() { return !isTerminal(); }
  /** node represents number or symbol */
  public boolean isTerminal() { return node_.isLeaf(); }

  /** type identifiers returned by {@link #getType} */
  public enum Type {
    OpPlus, OpMinus, OpUnaryMinus, OpTimes, OpDivide, OpPower,
    TNumber, TSymbol
  }
  /** Get type identifier. */
  public abstract Type getType();

  /** Get a copy: a new AtomicExpression of same type/content. */
  @Override public AtomicExpression clone() {
    // Note that this should be an abstract method!
    //
    // The implementation that throws an exception frees us from
    // defining clone(), e.g., in the new Power class (expect less
    // suprises.)
    //
    // Similarly, AtomicExpression should better implement
    // Cloneable; but then we always had to take care of possible
    // exception (CloneNotSupportedException).
    throw new RuntimeException("You didn't overwrite clone()!");
  }

  /** get value
      @throws UnsupportedOperationException if value cannot be determined
   */
  public abstract double getValue();
}
