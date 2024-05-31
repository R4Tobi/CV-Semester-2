package aud.test;

import aud.BinarySearchTree;
import aud.util.Permutations;

import org.junit.*;
import static org.junit.Assert.*;

public class BinarySearchTreeTest {

  @Test
  public void testBinarySearchTree() {
    String[] keys={"a","b","c","d","e","f"};
    int      n=keys.length;

    /* Includes most general case for removal:

       remove b of

             b
            / \
           a   e
              /
             c
              \
               d

        More entries are possible. However note that we are
        going to check *all* permutatiations for insertions,
        and for each one *all* permutatiations for removal!
        This yields O(n!*n!) iterations each of cost O(n*log n)!
     */

    BinarySearchTree<String,String> tree=new BinarySearchTree<String,String>();

    for (int[] p : new Permutations(n)) {
      for (int[] q : new Permutations(n)) {
        for (int i : p) {
          String k=tree.find(keys[i]);
          assertEquals(k,null);
          tree.insert(keys[i],keys[i]);
          k=tree.find(keys[i]);
          assertTrue(k!=null);
          assertTrue(k.compareTo(keys[i])==0);
          tree.checkConsistency();
        }

        assertTrue(tree.getMinimum().getValue().compareTo(keys[0])==0);
        assertTrue(tree.getMaximum().getValue().compareTo(keys[n-1])==0);

        {
          int i=0;
          for (BinarySearchTree<String,String>.Cursor c : tree) {
            assertTrue(c.getKey().compareTo(keys[i])==0);
            assertTrue(c.getValue().compareTo(keys[i])==0);
            ++i;
          }
          i=0;
          for (BinarySearchTree<String,String>.Cursor c : tree.range(null,null)) {
            assertTrue(c.getKey().compareTo(keys[i])==0);
            assertTrue(c.getValue().compareTo(keys[i])==0);
            ++i;
          }
          i=1;
          for (BinarySearchTree<String,String>.Cursor c :
                 tree.range(keys[1],keys[4])) {
            assertTrue(c.getKey().compareTo(keys[i])==0);
            assertTrue(c.getValue().compareTo(keys[i])==0);
            ++i;
          }
          assertSame(i,4);
        }

        for (int i : q) {
          String k=tree.find(keys[i]);
          assertTrue(k!=null);
          assertTrue(k.compareTo(keys[i])==0);
          tree.remove(keys[i]);
          k=tree.find(keys[i]);
          assertEquals(k,null);
          tree.checkConsistency();
        }
      }
      assertTrue(tree.isEmpty());
    }
  }

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.BinarySearchTreeTest");
  }
}
