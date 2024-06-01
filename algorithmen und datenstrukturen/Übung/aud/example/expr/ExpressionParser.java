
package aud.example.expr;

import aud.util.Sys;

/** Simple expression parser.<p>

    Build {@link ExpressionTree} from (infix) string representation of
    a term.<p>

    Implements a top-down <a
    href="http://en.wikipedia.org/wiki/Recursive_descent_parser">recusive
    descent parser</a> based on the following rules in <a
    href="http://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form">BNF</a>
    form (w/o {@code <>} to ease HTML formatting of this page)

    <pre><code>
    expression ::= sum

    sum        ::= product |
                   product '+' sum | product '-' sum

    product    ::= factor |
                   factor '*' product | factor '/' product

    factor     ::= number | symbol | '(' expression ')' |
                   '+' factor | '-' factor
    </code></pre>

    {@code <number>} and {@code <symbol>} are identified during the
    lexcical analysis. They correspond to {@link AtomicExpression}
    instances, which are used to build the tree. The same applies to
    operators {@code '+'},...<p>

    Notes

    <ul>

    <li>The left-hand-sides of the BNF rules are "mapped" to
    (recursive) method calls, e.g. {@link #expression}.</li>

    <li>The {@code level} argument is provided only for formatting
    {@link #verbose} output.</li>

    <li>The parser uses a {@link Tokenizer} for lexcical scanning.</li>

    <li>The current token is used for "looking ahead" ({@link
    #lookahead}): it is advanced (by {@link #nextToken} only if this
    token is "consumed" by part of a BNF rule.</li>

    <li>The given grammar defines expressions <b>right-associative</b>, which
    is not quite correct: {@code a+b+c} should equal {@code (a+b)+c} (left
    associative) rather than {@code a+(b+c)}!<br>
    A simple change of the grammar however leads to an infinite recursion (due
    to <a href="http://en.wikipedia.org/wiki/Left_recursion"><em>left
    recursion</em></a> in the new grammar.<br>
    This can be fixed easily by a transformation of the grammar. Also, the
    practical implementation is easy (use an iteration). However, both is
    boyond the scope of the lecture at this point. For a correct
    implementation see {@link ExpressionParser2}
    </li>

    </ul>

    @see ExpressionTree
    @see Tokenizer
    @see AtomicExpression
 */
public class ExpressionParser {

  Tokenizer scanner_ = null;
  boolean   verbose_ = false;

  /** parse input
      @param input term
      @throws SyntaxError on syntax error
      @return extression tree that represents input term
   */
  public ExpressionTree parse(String input) {
    scanner_ = new Tokenizer(input);
    scanner_.next(); // use as "lookahead"
    ExpressionTree tree=expression(0);
    expect(Tokenizer.END_OF_INPUT,"<end of input>");
    return tree;
  }

  /** set verbose mode (report state to {@code System.err}) */
  public void setVerbose(boolean b) {
    verbose_=b;
  }

  /** helper: generate a simple error message */
  protected String errorNear() {
    return "near '"+scanner_.matchedText()+
      "'\nbefore '"+scanner_.remainder()+"'";
  }

  /** helper: "lookahead" is the usual phrasing */
  protected int lookahead() {
    return scanner_.matchedTokenId();
  }

  /** helper: consume current token and advance to next token */
  protected void nextToken() {
    if (scanner_.next()==Tokenizer.NO_MATCH)
      throw new SyntaxError("unknown token: lexcial analysis failed at '"+
			    scanner_.remainder()+"'");
  }

  /** helper: get match corresponding to {@link #lookahead}
      @return {@link Tokenizer#matchedText}
   */
  protected String matchedText() {
    return scanner_.matchedText();
  }

  /** helper: check token (without calling {@link #nextToken}!)
      @throws SyntaxError if token does not match
   */
  protected void expect(int tokenid,String text) {
    if (lookahead()!=tokenid)
      throw new SyntaxError("expected '"+text+"' got '"+
			    scanner_.matchedText()+"'\nbefore '"+
			    scanner_.remainder());
  }

  /** helper: print out information */
  protected void verbose(int level,String message) {
    if (verbose_) {
      System.err.println(Sys.indent(level)+message+
			 " ['"+scanner_.matchedText()+"','"+
			 scanner_.remainder()+"']");
    }
  }

  //
  // Here is the implementation of BNF rules as methods:
  //

  /** parse expression
      <pre><code>expression ::= sum</code></pre> */
  protected ExpressionTree expression(int level) {
    verbose(level,"<expression>");
    return sum(level+1);
  }

  /** parse sum<p>
      <pre><code>
      sum ::= product |
              product '+' sum | product '-' sum
      </code></pre>
   */
  protected ExpressionTree sum(int level) {
    verbose(level,"<sum>");

    ExpressionTree left=product(level+1);

    if (lookahead()==Tokenizer.PLUS) {
      nextToken();
      ExpressionTree right=sum(level+1);
      return new ExpressionTree(new Plus(),left,right);
    }
    else if (lookahead()==Tokenizer.MINUS) {
      nextToken();
      ExpressionTree right=sum(level+1);
      return new ExpressionTree(new Minus(),left,right);
    }

    return left;
  }

  /** parse product<p>
      <pre><code>
      product ::= factor |
                  factor '*' product | factor '/' product
      </code></pre>
   */
  protected ExpressionTree product(int level) {
    verbose(level,"<product>");

    ExpressionTree left=factor(level+1);

    if (lookahead()==Tokenizer.TIMES) {
      nextToken();
      ExpressionTree right=product(level+1);
      return new ExpressionTree(new Times(),left,right);
    }
    else if (lookahead()==Tokenizer.DIVIDE) {
      nextToken();
      ExpressionTree right=product(level+1);
      return new ExpressionTree(new Divide(),left,right);
    }

    return left;
  }

  /** parse factor<p>
     <pre><code>
     factor ::= number | symbol | '(' expression ')' |
                '+' factor | '-' factor
     </code></pre>

     The last part of the rule correspong to <em>unary</em> plus and
     minus operators {@code +x} and {@code -x}. The implementation
     <em>ignores</em> unary plus, i.e., {@code +x -> x}.
   */
  protected ExpressionTree factor(int level) {
    verbose(level,"<factor>");

    ExpressionTree tree=null;

    if (lookahead()==Tokenizer.NUMBER) {
      tree=new ExpressionTree
                (new Number(Double.parseDouble(matchedText())));
    }
    else if (lookahead()==Tokenizer.IDENTIFIER) {
      tree=new ExpressionTree(new Symbol(matchedText()));
    }
    else if (lookahead()==Tokenizer.LEFT_PAREN) {
      nextToken();
      tree=expression(level+1);
      expect(Tokenizer.RIGHT_PAREN,"closing parenthesis )");
    }
    else if (lookahead()==Tokenizer.PLUS) { // unary plus
      nextToken();
      return factor(level+1); // ignore: +x == x
     }
    else if (lookahead()==Tokenizer.MINUS) { // unary minus
      nextToken();
      return new ExpressionTree(new UnaryMinus(),factor(level+1),null);
    }
    else
      throw new SyntaxError(errorNear());

    nextToken();
    assert(tree!=null);

    return tree;
  }




  /** test and example for usage */
  public static void main(String[] args) {

    String input=(args.length>0) ? args[0] : "1+2*(3+4)";

    System.out.println("input = '"+input+"'");

    ExpressionParser p=new ExpressionParser();
    p.setVerbose(true);
    ExpressionTree tree=p.parse(input);

    System.out.println("output = '"+tree+"'");

    System.out.println(tree.postorder().toString());

    System.out.println(tree.toTikZ());
  }
}
