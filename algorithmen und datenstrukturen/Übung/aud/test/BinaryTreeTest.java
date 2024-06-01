package aud.test;

import aud.BinaryTree;
import aud.Vector;
import org.junit.*;
import static org.junit.Assert.*;

public class BinaryTreeTest {

  @Test
  public void testBinaryTree() {
    BinaryTree<Integer> root=new BinaryTree<Integer>(0);
    assertTrue(root.isRoot());
    assertTrue(root.isLeaf());
    assertSame(root.getParent(),null);
    assertSame(root.getLeft(),null);
    assertSame(root.getRight(),null);
    assertSame(root.getRoot(),root);

    BinaryTree<Integer> b=new BinaryTree<Integer>(2);
    BinaryTree<Integer> c=new BinaryTree<Integer>(3);
    BinaryTree<Integer> d=new BinaryTree<Integer>(4);

    BinaryTree<Integer> a=new BinaryTree<Integer>(1,b,c);
    assertTrue(!b.isRoot());
    assertTrue(!c.isRoot());
    assertTrue(!a.isLeaf());
    assertSame(a.getLeft(),b);
    assertSame(a.getRight(),c);
    assertSame(b.getParent(),a);
    assertSame(c.getParent(),a);
    assertSame(a.getRoot(),a);
    assertSame(b.getRoot(),a);
    assertSame(c.getRoot(),a);

    root.setLeft(a);
    root.setRight(d);

    assertSame(a.getParent(),root);
    assertSame(d.getParent(),root);

    assertSame(root.getLeft(),a);
    assertSame(root.getRight(),d);


    assertTrue(!a.isRoot());
    assertTrue(!d.isRoot());

    assertSame(d.getRoot(),root);
    assertSame(c.getRoot(),root);
    assertSame(b.getRoot(),root);
    assertSame(c.getRoot(),root);

    assertTrue(!root.isLeaf());
    assertTrue(!a.isLeaf());
    assertTrue(b.isLeaf());
    assertTrue(c.isLeaf());
    assertTrue(d.isLeaf());

    // preorder traversal by iterator
    Vector<Integer> nodes=new Vector<Integer>();
    for (BinaryTree<Integer> node : root.preorder())
      nodes.push_back(node.getData());
    assertSame(nodes.size(),5);
    assertSame(nodes.at(0).intValue(),0);
    assertSame(nodes.at(1).intValue(),1);
    assertSame(nodes.at(2).intValue(),2);
    assertSame(nodes.at(3).intValue(),3);
    assertSame(nodes.at(4).intValue(),4);

    // inorder traversal by iterator
    nodes=new Vector<Integer>();
    for (BinaryTree<Integer> node : root.inorder())
      nodes.push_back(node.getData());
    assertSame(nodes.size(),5);
    assertSame(nodes.at(0).intValue(),2);
    assertSame(nodes.at(1).intValue(),1);
    assertSame(nodes.at(2).intValue(),3);
    assertSame(nodes.at(3).intValue(),0);
    assertSame(nodes.at(4).intValue(),4);

    // postorder traversal by iterator

    nodes=new Vector<Integer>();
    for (BinaryTree<Integer> node : root.postorder())
      nodes.push_back(node.getData());
    assertSame(nodes.size(),5);
    assertSame(nodes.at(0).intValue(),2);
    assertSame(nodes.at(1).intValue(),3);
    assertSame(nodes.at(2).intValue(),1);
    assertSame(nodes.at(3).intValue(),4);
    assertSame(nodes.at(4).intValue(),0);

    // level-order traversal by iterator

    nodes=new Vector<Integer>();
    for (BinaryTree<Integer> node : root.levelorder())
      nodes.push_back(node.getData());
    assertSame(nodes.size(),5);
    assertSame(nodes.at(0).intValue(),0);
    assertSame(nodes.at(1).intValue(),1);
    assertSame(nodes.at(2).intValue(),4);
    assertSame(nodes.at(3).intValue(),2);
    assertSame(nodes.at(4).intValue(),3);
  }


  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.BinaryTreeTest");
  }
}
