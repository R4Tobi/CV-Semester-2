package aud.example;

import aud.util.*;

import aud.BTree;
import aud.KTreeNode;
import java.util.Scanner;

/** example: insert entries
    @see aud.BTree
 */
public class BTreeExample extends SingleStepper {

  class MyTree extends BTree<String> {
    BTreeExample app_ = null;
    public MyTree(int m,BTreeExample app) { super(m); }

    @Override protected void onSplit(KTreeNode<String> node) {
      // TODO decorate node
      halt("insert requires split...");
      super.onSplit(node);
    }
  }

  protected MyTree tree_ = null;
  protected DotViewer viewer_ = DotViewer.displayWindow
    ((String) null,"aud.example.BTreeExample");

  /** create application instance */
  public BTreeExample(int m) {
    super("aud.example.BTreeExample");
    tree_=new MyTree(m,this);
  }

  public MyTree getTree() { return tree_; }

  @Override protected void onHalt() {
    if (tree_!=null)
      viewer_.display(tree_);
  }

  /** start interactive example */
  public static void main(String[] args) {

    final String HELP=
      "usage: java aud.example.BTreeExample [M] [pause]\n"+
      "       Reads and insert words from standard input.\n"+
      "       'quit' quits.\n"+
      "\tpause [milliseconds] set pause between animation steps\n"+
      "\t      A value of 0 requires to explicitly push the 'continue'\n"+
      "\t      button. The default value is 0 (or the value of then\n"+
      "\t      environment variable 'AUD_TIMEOUT')!\n";

    int m=args.length>0 ? Integer.parseInt(args[0]) : 1;

    BTreeExample app=new BTreeExample(m);

    if (args.length>1) {
      try {
        app.setTimeout(Integer.parseInt(args[1]));
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
        app.getTree().insert(key);
        app.halt("inserted '"+key);
      }
    }
    app.halt("QUIT");
    System.exit(0);
  }
}
