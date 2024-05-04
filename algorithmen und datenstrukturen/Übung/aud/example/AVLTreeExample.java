package aud.example;

import aud.util.*;
import aud.AVLTree;
import aud.util.SimpleDecorator;
import java.util.Scanner;

/** example: insert entries and maintain balance
    @see AVLTree
 */
public class AVLTreeExample extends SingleStepper {

  class MyAVLTree extends AVLTree<String,String> {
    AVLTreeExample app_ = null;
    public MyAVLTree(AVLTreeExample app) { super(); }

    @Override protected void onInsert(Node node) {
      ((SimpleDecorator) tree_.getDecorator()).highlightNode(node);
      halt("insert '"+node.getKey()+"' before rebalancing");
      super.onInsert(node);
    }
  }

  protected MyAVLTree tree_ = null;
  protected DotViewer viewer_ = DotViewer.displayWindow
    ((String) null,"aud.example.AVLTreeExample");

  /** create application instance */
  public AVLTreeExample() {
    super("aud.example.AVLTreeExample");
    tree_=new MyAVLTree(this);
  }

  public MyAVLTree getTree() { return tree_; }

  @Override protected void onHalt() {
    if (tree_!=null)
      viewer_.display(tree_);
  }

  /** start interactive example */
  public static void main(String[] args) {

    final String HELP=
      "usage: java aud.example.AVLTreeExample [pause]\n"+
      "       Reads and insert words from standard input.\n"+
      "       'quit' quits.\n"+
      "\tpause [milliseconds] set pause between animation steps\n"+
      "\t      A value of 0 requires to explicitly push the 'continue'\n"+
      "\t      button. The default value is 0 (or the value of then\n"+
      "\t      environment variable 'AUD_TIMEOUT')!\n";

    AVLTreeExample app=new AVLTreeExample();

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
