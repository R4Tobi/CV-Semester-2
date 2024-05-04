package aud.example;

import java.util.Scanner;
import java.io.File;

import aud.util.Sys;
import aud.util.Graphviz;
import aud.util.Graphvizable;
import aud.util.DotViewer;
import aud.BinarySearchTree;
import aud.AVLTree;
import aud.RedBlackTree;
import aud.A234Tree;
import aud.BTree;

/** Utility for rendering various trees.
 */
public class RenderTree {

  private static void usage() {
    final String HELP=
      "usage: java aud.example.RendererTree TREE FORMAT [M]\n"+
      "       Reads and insert words from standard input into tree, which\n"+
      "       is displayed.\n"+
      " - TREE is one of {b,avl,rb,234,B}.\n"+
      " - FORMAT is one of {-,svg,pdf}. If FORMAT is '-' the tree is shown\n"+
      "       in a new window. Otherwise, files 'tree.dot' and 'tree.dot.FORMAT'\n"+
      "       are created instead of direct rendering.\n"+
      " - M   is the parameter of a B-tree of order 2-*m+1.\n"
      ;

    System.err.println(HELP);
    System.exit(-1);
  }

  public static void main(String args[]) {

    if (args.length<2)
      usage();

    String type=null;
    String format=null;

    if (args[1].compareTo("svg")==0 ||
        args[1].compareTo("pdf")==0) {
        format=args[1];
    }
    else if (args[1].compareTo("-")!=0) {
      System.err.println("invalid FORMAT specifier '"+args[1]+"'");
      usage();
    }

    Scanner s=new Scanner(System.in);
    s.useDelimiter("(\\s|;,\\.-\"\\'\\(\\)\\[\\])+");

    Graphvizable showMe=null;
    BinarySearchTree<String,String> tree=null;

    if (args[0].compareTo("b")==0) {
      type="binary search";
      tree=new BinarySearchTree<String,String>();
    }
    if (args[0].compareTo("avl")==0) {
      type="AVL-";
      tree=new AVLTree<String,String>();
    }
    else if (args[0].compareTo("rb")==0) {
      type="red-black ";
      tree=new RedBlackTree<String,String>();
    }

    if (tree!=null) {
      while (s.hasNext())
        tree.insert(s.next(),null);
      showMe=tree;
    }
    else {
      // unfortunately we didn't specify a common interface, so extra case:
      if (args[0].compareTo("234")==0) {
        type="2-3-4-";
        A234Tree<String> t=new A234Tree<String>();
        while (s.hasNext())
          t.insert(s.next());
        showMe=t;
      }
      else if (args[0].compareTo("B")==0) {
        type="B-";
        if (args.length<3) {
          System.err.println("missing parameter M");
          usage();
        }
        BTree<String> t=new BTree<String>(Integer.parseInt(args[2]));
        while (s.hasNext())
          t.insert(s.next());
        showMe=t;
      }
      else {
        System.err.println("invalid TREE specifier '"+args[0]+"'");
        usage();
      }
    }

    System.err.println("Rendering output.");

    if (format!=null) {
      File dotfile=Sys.writeToFile(new File("tree.dot"),showMe.toDot());
      File outfile=(new Graphviz()).renderDotFileToFile(dotfile,format);
      System.err.println("Wrote '"+dotfile+"' and '"+outfile+"'"); // no check!
    }
    else
      DotViewer.displayWindow(showMe,type+"tree").setExitOnClose();
  }
}
