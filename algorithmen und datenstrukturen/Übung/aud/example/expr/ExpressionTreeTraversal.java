package aud.example.expr;

import aud.util.*;
import aud.Queue;

/** example: visualize expression tree traversal */
public class ExpressionTreeTraversal extends SingleStepper {

  protected ExpressionTree  tree_   = null;
  protected DotViewer       viewer_ = DotViewer.displayWindow
    ((String) null,"aud.example.expr.ExpressionTreeTraversal");
  protected SimpleDecorator decorator;

  /** create traversal application for {@code tree} */
  public ExpressionTreeTraversal(ExpressionTree tree) {
    super("aud.example.expr.ExpressionTreeTraversal");
    tree_=tree;
    decorator=(SimpleDecorator) tree_.getDecorator(); // same per tree
  }

  @Override protected void onHalt() {
    if (tree_!=null)
      viewer_.display(tree_);
  }

  /** output node during traversal
      @see #traverse
   */
  protected void output(ExpressionTree node) {
    decorator.markNode(node);
    decorator.highlightNode(node);
    decorator.setGraphLabel(decorator.getGraphLabel()+node.getData()+" ");
    halt("output "+node.getData()+" ["+node+"]");
  }
  /** arrived {@code node} for first time (for visualization) */
  protected void see(ExpressionTree node) {
    decorator.markEdge(node);
  }

  /** start traversal
      @param type denotes the type of traversal "preorder",
      "inorder","postorder"
      @throws RuntimeException for unknown {@code type}
   */
  public void traverse(String type) {
    decorator.clear();
    decorator.setGraphLabel("Traversal: ");

    if (type.toLowerCase().startsWith("pre")) {
      halt("START preorder traversal");
      preorder(tree_);
    }
    else if (type.toLowerCase().startsWith("in")) {
      halt("START inorder traversal");
      inorder(tree_);
    }
    else if (type.toLowerCase().startsWith("post")) {
      halt("START postorder traversal");
      postorder(tree_);
    }
    else if (type.toLowerCase().startsWith("level")) {
      halt("START level-order traversal");
      levelorder(tree_);
    }
    else
      throw new RuntimeException("unknown traversal '"+type+"'");

    decorator.highlightNode(null);
    halt("FINISHED");
  }

  /** recursive preorder traversal */
  protected void preorder(ExpressionTree node) {
    if (node!=null) {
      see(node);

      output(node);
      preorder((ExpressionTree) node.getLeft());
      preorder((ExpressionTree) node.getRight());
    }
  }
  /** recursive inorder traversal */
  protected void inorder(ExpressionTree node) {
    if (node!=null) {
      see(node);

      inorder((ExpressionTree) node.getLeft());
      output(node);
      inorder((ExpressionTree) node.getRight());
    }
  }
  /** recursive postorder traversal */
  protected void postorder(ExpressionTree node) {
    if (node!=null) {
      see(node);

      postorder((ExpressionTree) node.getLeft());
      postorder((ExpressionTree) node.getRight());
      output(node);
    }
  }
  /** level order traversdal */
  protected void levelorder(ExpressionTree root) {
    Queue<ExpressionTree> queue=new Queue<ExpressionTree>();
    queue.enqueue(null);     // marker: next level
    queue.enqueue(root);
    int level=0;             // keep track of/count levels
    while (!queue.is_empty()) {
      ExpressionTree node=queue.dequeue();

      if (node==null) {
	decorator.highlightNode(null);
	halt("enter next level "+(level++));
	if (!queue.is_empty())
	  queue.enqueue(null); // marker: next level
      }
      else {
	see(node);

	if (node.getLeft()!=null)
	  queue.enqueue((ExpressionTree) node.getLeft());
	if (node.getRight()!=null)
	  queue.enqueue((ExpressionTree) node.getRight());
	output(node);
      }
    }
  }

  public static void main(String[] args) {

    if (args.length<1 || args.length>2) {
      System.err.println
	("usage: java aud.example.expr.ExpressionTreeTraversal expr [mode]\n"+
	 "- expr: an expression, e.g., \"2*(3+4)\" "+
	 "(use quotes \"...\" on command line!)\n"+
	 "- mode: preorder | inorder | postorder | levelorder");
      System.exit(-1);
    }

    ExpressionTree tree=new ExpressionParser().parse(args[0]);
    ExpressionTreeTraversal app=new ExpressionTreeTraversal(tree);

    app.traverse(args.length>1 ? args[1] : "preorder");

    app.halt("QUIT");
    System.exit(0);
  }
}
