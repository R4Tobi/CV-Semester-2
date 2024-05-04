package aud.example.expr;

/** signals syntax error during parsing a term
    @see ExpressionParser
 */
@SuppressWarnings("serial")
public class SyntaxError extends RuntimeException {
  public SyntaxError(String message) {
    super("syntax error: "+message);
  }
}
