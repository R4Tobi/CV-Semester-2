package aud.test;

import aud.AVLTree;
import aud.util.Permutations;

import org.junit.*;
import static org.junit.Assert.*;

public class AVLTreeTest {

  @Test
  public void testAVLTree() {
    String[] keys={"a","b","c","d","e","f","g","h","i"};
    int      n=keys.length;
    int      maxHeight=0;

    { int k=n; while (k>0) { k>>=1; ++maxHeight; } }
    maxHeight=2*maxHeight+2;

    for (int[] p : new Permutations(n)) {

      AVLTree<String,String> tree=new AVLTree<String,String>();

      for (int i : p) {
        String k=tree.find(keys[i]);
        assertEquals(k,null);
        tree.insert(keys[i],keys[i]);
        k=tree.find(keys[i]);
        assertTrue(k!=null);
        assertTrue(k.compareTo(keys[i])==0);
        tree.checkConsistency();
      }
      assertTrue(tree.getHeight()<=maxHeight);
    }
  }

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.AVLTreeTest");
  }
}
