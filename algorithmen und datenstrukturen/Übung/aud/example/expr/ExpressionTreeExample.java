package aud.example.expr;
/** ExpressionTree: minimal example */
public class ExpressionTreeExample {

  public static void main(String[] args) {
    ExpressionTree tree=new ExpressionTree
      (new Times(),
       new ExpressionTree
       (new Plus(),
        new ExpressionTree(new Number(2)),
        new ExpressionTree(new Number(3))),
       new ExpressionTree(new Number(5))
      ); // (2+3)*5
    System.out.println(tree);
    System.out.println(tree.getValue());
  }
}
