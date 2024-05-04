package aud.example;

import aud.util.*;
import aud.RedBlackTree;
import aud.util.SimpleDecorator;
import java.util.Scanner;

/** example: insert entries and maintain balance
    @see RedBlackTree
 */
public class RedBlackTreeExample extends SingleStepper {

  class MyRedBlackTree extends RedBlackTree<String,String> {
    RedBlackTreeExample app_ = null;
    public MyRedBlackTree(RedBlackTreeExample app) { super(); }

    @Override protected void onInsert(Node node) {
      ((SimpleDecorator) tree_.getDecorator()).highlightNode(node);
      halt("insert '"+node.getKey()+"' before rebalancing");
      ((SimpleDecorator) tree_.getDecorator()).highlightNode(null);
      super.onInsert(node);
    }
    @Override protected void onRestructuring() {
      halt("- after restructuring");
      super.onRestructuring();
    }
    @Override protected void onRecoloring() {
      halt("- after one recoloring step");
      super.onRestructuring();
    }
  }

  protected MyRedBlackTree tree_ = null;
  protected DotViewer viewer_ = DotViewer.displayWindow
    ((String) null,"aud.example.RedBlackTreeExample");

  /** create application instance */
  public RedBlackTreeExample() {
    super("aud.example.RedBlackTreeExample");
    tree_=new MyRedBlackTree(this);
  }

  public MyRedBlackTree getTree() { return tree_; }

  @Override protected void onHalt() {
    if (tree_!=null)
      viewer_.display(tree_);
  }

  /** start interactive example */
  public static void main(String[] args) {

    final String HELP=
      "usage: java aud.example.RedBlackTreeExample [pause]\n"+
      "       Reads and insert words from standard input.\n"+
      "       'quit' quits.\n"+
      "\tpause [milliseconds] set pause between animation steps\n"+
      "\t      A value of 0 requires to explicitly push the 'continue'\n"+
      "\t      button. The default value is 0 (or the value of then\n"+
      "\t      environment variable 'AUD_TIMEOUT')!\n";

    RedBlackTreeExample app=new RedBlackTreeExample();

    if (args.length>0) {
      try {
        app.setTimeout(Integer.parseInt(args[0]));
      } catch (NumberFormatException e) {
        System.err.println(HELP);
        System.exit(-1);
      }
    }

    app.halt("EMPTY TREE");

    Scanner s=new Scanner(System.in);
    s.useDelimiter("\\s+");

    while (s.hasNext()) {
      String key=s.next();
      if (key.compareTo("quit")==0)
        break;
      else {
        app.getTree().insert(key,null);
        app.halt("inserted '"+key+"' and rebalanced");
      }
    }
    app.halt("QUIT");
    System.exit(0);
  }
}
