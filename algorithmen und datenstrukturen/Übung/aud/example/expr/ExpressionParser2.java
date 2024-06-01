package aud.example.expr;

import aud.Queue;

/** Same as {@link ExpressionParser} but using modified grammar to provide
    the usual <b>left-associative</b> expressions.<p>

    Here is the "correct" grammar, that implements left-associative expressions,
    i.e., {@code a+b+c=(a+b)+c} and {@code a*b*c=(a*b)*c}.

    <pre><code>
    expression ::= sum

    sum        ::= product |
                   sum '+' product | sum '-' product

    product    ::= factor |
                   product '*' factor | product '/' factor

    factor     ::= number | symbol | '(' expression ')' |
                   '+' factor | '-' factor
    </code></pre>

    <b>Important note:</b> We cannot implement this directly with a recursive
    descent parser as this would result in an infinite recursion! (This
    type of recursion is generally referred to as <em>left recursion</em> and
    requires a transformation of the grammar.)<p>

    I don't go into details but provide a simple solution right ahead:
    model part of the recursion (the <em>left recursion</em>) using an
    iterative implementation. (Can you see how the transformed grammar
    would look like?)

    @see ExpressionParser
 */
public class ExpressionParser2 extends ExpressionParser {



  //
  // Here is the implementation of BNF rules as methods:
  //

  /** parse sum<p>
      <pre><code>
      sum ::= product |
              sum  '+' product | sum '-' product
      </code></pre>
   */
  @Override protected ExpressionTree sum(int level) {
    verbose(level,"<sum>");

    // temporary storage
    Queue<ExpressionTree> expr = new Queue<ExpressionTree>();
    Queue<Integer>        op   = new Queue<Integer>();

    ExpressionTree tree=product(level+1);

    // parse iteratively
    while (lookahead()==Tokenizer.PLUS || lookahead()==Tokenizer.MINUS) {
      op.enqueue(lookahead());
      nextToken();
      expr.enqueue(product(level+1));
    }

    // setup tree
    while (!op.is_empty()) {
      if (op.dequeue()==Tokenizer.PLUS)
        tree=new ExpressionTree(new Plus(),tree,expr.dequeue());
      else
        tree=new ExpressionTree(new Minus(),tree,expr.dequeue());
    }
    return tree;
  }

  /** parse product<p>
      <pre><code>
      product ::= factor |
                  product '*' factor | product '/' factor
      </code></pre>
   */
  protected ExpressionTree product(int level) {
    verbose(level,"<product>");

    // temporary storage
    Queue<ExpressionTree> expr = new Queue<ExpressionTree>();
    Queue<Integer>        op   = new Queue<Integer>();

    ExpressionTree tree=factor(level+1);

    // parse iteratively
    while (lookahead()==Tokenizer.TIMES || lookahead()==Tokenizer.DIVIDE) {
      op.enqueue(lookahead());
      nextToken();
      expr.enqueue(factor(level+1));
    }

    // setup tree
    while (!op.is_empty()) {
      if (op.dequeue()==Tokenizer.TIMES)
        tree=new ExpressionTree(new Times(),tree,expr.dequeue());
      else
        tree=new ExpressionTree(new Divide(),tree,expr.dequeue());
    }
    return tree;
  }


  /** test and example for usage */
  public static void main(String[] args) {

    String input=(args.length>0) ? args[0] : "a*b*c";

    System.out.println("input = '"+input+"'");

    ExpressionParser2 p=new ExpressionParser2();
    p.setVerbose(true);
    ExpressionTree tree=p.parse(input);

    System.out.println("output = '"+tree+"'");

    System.out.println(tree.postorder().toString());

    System.out.println(tree.toTikZ());
  }
}
