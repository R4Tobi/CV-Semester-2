package aud.example;

import aud.BinaryTree;
import aud.util.*;
import aud.Queue;

/** example: visualize binary tree traversal
    @see aud.BinaryTree
    @see aud.example.IterativePreorderTraversal
    @see aud.BinaryTreeTraversal BinaryTreeTraversal (iterators)
 */
public class BinaryTreeTraversal extends SingleStepper {

  protected MyTree    tree_   = null;
  protected DotViewer viewer_ = DotViewer.displayWindow
  ((String) null,"aud.example.BinaryTreeTraversal");
  protected SimpleDecorator decorator;

  /** create traversal application for {@code tree} */
  public BinaryTreeTraversal(MyTree tree) {
    super("aud.example.BinaryTreeTraversal");
    tree_=tree;
    decorator=(SimpleDecorator) tree_.getDecorator(); // same per tree
  }

  @Override protected void onHalt() {
    if (tree_!=null)
      viewer_.display(tree_);
  }

  /** simple tree with decorator for visualization */
  static class MyTree extends BinaryTree<String> {
    public MyTree(String data) { super(data); }
    public MyTree(String data,MyTree left,MyTree right) {
      super(data,left,right);
    }

    static final GraphvizDecorator decorator = new SimpleDecorator();
    @Override public GraphvizDecorator getDecorator() {
      return decorator;
    }
  }

  /** generate some tree */
  public static MyTree exampleTree() {
    MyTree root=new MyTree
    ("A",
        new MyTree("B",
            new MyTree("C"),
            new MyTree("D")),
            new MyTree("E",
                new MyTree("F"),
                new MyTree("G",
                    new MyTree("H",
                        new MyTree("I",new MyTree("J"),null),
                        new MyTree("K")
                    ),
                    null
                )
            )
    );
    return root;
  }


  /** output node during traversal
      @see #traverse
   */
  protected void output(MyTree node) {
    decorator.markNode(node);
    decorator.highlightNode(node);
    decorator.setGraphLabel(decorator.getGraphLabel()+node);
    halt("output "+node);
  }
  /** arrived {@code node} for first time (for visualization) */
  protected void see(MyTree node) {
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
  protected void preorder(MyTree node) {
    if (node!=null) {
      see(node);

      output(node);
      preorder((MyTree) node.getLeft());
      preorder((MyTree) node.getRight());
    }
  }
  /** recursive inorder traversal */
  protected void inorder(MyTree node) {
    if (node!=null) {
      see(node);

      inorder((MyTree) node.getLeft());
      output(node);
      inorder((MyTree) node.getRight());
    }
  }
  /** recursive postorder traversal */
  protected void postorder(MyTree node) {
    if (node!=null) {
      see(node);

      postorder((MyTree) node.getLeft());
      postorder((MyTree) node.getRight());
      output(node);
    }
  }
  /** level order traversdal */
  protected void levelorder(MyTree root) {
    Queue<MyTree> queue=new Queue<MyTree>();
    queue.enqueue(null);     // marker: next level
    queue.enqueue(root);
    int level=0;             // keep track of/count levels
    while (!queue.is_empty()) {
      MyTree node=queue.dequeue();

      if (node==null) {
        halt("enter next level "+(level++));
        if (!queue.is_empty())
          queue.enqueue(null); // marker: next level
      }
      else {
        see(node);

        if (node.getLeft()!=null)
          queue.enqueue((MyTree) node.getLeft());
        if (node.getRight()!=null)
          queue.enqueue((MyTree) node.getRight());
        output(node);
      }
    }
  }

  public static void main(String[] args) {
    BinaryTreeTraversal app=
      new BinaryTreeTraversal(BinaryTreeTraversal.exampleTree());

    if (args.length==0)
      app.traverse("preorder");
    else
      for (String type : args)
        app.traverse(type);

    app.halt("QUIT");
    System.exit(0);
  }
}
