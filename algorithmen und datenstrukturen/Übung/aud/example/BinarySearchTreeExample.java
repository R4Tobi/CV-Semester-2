package aud.example;

import aud.util.*;
import aud.BinarySearchTree;
import java.util.Scanner;

/** example: insert, remove, and restructure entries
    @see BinarySearchTree
 */
public class BinarySearchTreeExample extends SingleStepper {

  protected BinarySearchTree<String,String>  tree_ = null;
  protected DotViewer viewer_ = DotViewer.displayWindow
    ((String) null,"aud.example.BinarySearchTreeExample");
  protected BinarySearchTree<String,String>.Decorator decorator_;

  /** create application instance */
  @SuppressWarnings("unchecked") // see below
  public BinarySearchTreeExample(BinarySearchTree<String,String> tree) {
    super("aud.example.BinarySearchTreeExample");
    tree_=tree;
    decorator_=(BinarySearchTree.Decorator)
      //(BinarySearchTree<String,String>.Decorator) // compile fails on some platforms!?
      tree_.getDecorator(); // same per tree
  }

  @Override protected void onHalt() {
    if (tree_!=null)
      viewer_.display(tree_);
  }

  /** Start interactive example.<p>
      Type '?' for help.
   */
  public static void main(String[] args) {

    final String HELP=
      "usage: java aud.example.BinarySearchTreeExample\n"+
      "       Reads and insert words from standard input.\n"+
      "\tWords may be prefixed with a single character, e.g., '-x':\n"+
      "\t- '-' remove from the tree.\n"+
      "\t- '/' apply right rotation to left-left case with key as root\n"+
      "\t- '\\' apply left rotation to right-right case with key as root\n"+
      "\t- '<' apply double rotation to right-left case with key as root\n"+
      "\t- '>' apply double rotation to left-right case with key as root\n"+
      "\t- '=' apply tri-node restructuring with key as grandchild\n"+
      "\t- '!' highlights a node (or none if not found)\n"+
      "\t- '~' pauses execution\n"+
      "\t- '?' prints this message\n"+
      "\t- 'quit' quits.\n"+
      "\tDuring the whole process, the tree will be visualized.";

    if (args.length>0) {
      System.err.println(HELP);
      System.exit(-1);
    }

    BinarySearchTree<String,String> tree=
      new BinarySearchTree<String,String>();
    BinarySearchTreeExample app=new BinarySearchTreeExample(tree);

    app.halt("EMPTY TREE",1);

    Scanner s=new Scanner(System.in);
    s.useDelimiter("\\s+");

    while (s.hasNext()) {
      String key=s.next();
      if (key.compareTo("quit")==0)
        break;
      else if (key.startsWith("?")) {
        System.err.println(HELP);
      }
      else if (key.startsWith("!")) {
        key=key.substring(1);
        app.decorator_.highlight(key);
        app.halt("highlight '"+key+"'",10);
      }
      else if (key.startsWith("-")) {
        key=key.substring(1);
        tree.remove(key);
        app.halt("remove '"+key+"'",200);
      }
      else if (key.startsWith("~")) {
        app.halt("PAUSE '"+key.substring(1)+"'",0);
      }
      else if (key.startsWith("<") || key.startsWith(">") ||
               key.startsWith("/") || key.startsWith("\\") ||
               key.startsWith("=")) {
        try {
          tree.restructure(key.charAt(0),key.substring(1));
          app.halt("restructure '"+key.substring(0,1)+
              "' near '"+key.substring(1)+"'",200);
          //System.out.println(tree.toText());
        } catch (RuntimeException e) {
          System.err.println(e);
          app.halt(e.getMessage(),1);
        }
        tree.checkConsistency();
      }
      else {
        tree.insert(key,null);
        app.halt("insert '"+key+"'",200);
      }
    }
    app.halt("QUIT",200);
    System.exit(0);
  }
}
