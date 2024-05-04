package aud.example.expr;

/** Node representing a symbolic parameter, e.g., a varibale.
    @see ExpressionTree
 */
public class Symbol extends Terminal {

  String name_ = null;

  /** create number */
  public Symbol(String name) { name_=name; }

  /** get symbol's name */
  public String getName() { return name_; }

  @Override public double getValue() {
    throw new UnsupportedOperationException
      ("don't know value of '"+name_+"'");
  }
  @Override public Type getType() { return Type.TSymbol; }
  @Override public Symbol clone() { return new Symbol(name_); }
  @Override public String toString() { return name_; }
}
