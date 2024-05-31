package aud.test;

import aud.BTree;
import aud.util.Permutations;

import org.junit.*;
import static org.junit.Assert.*;

public class BTreeTest {

  @Test
  public void testBTree() {
    String[] keys={"a","b","c","d","e","f","g","h","i"};
    int      n=keys.length;

    for (int m=1;m<3;++m) {
      for (int[] p : new Permutations(n)) {

        BTree<String> tree=new BTree<String>(m);

        for (int i : p) {
          String k=tree.find(keys[i]);
          assertEquals(k,null);
          tree.insert(keys[i]);
          k=tree.find(keys[i]);
          assertTrue(k!=null);
          assertTrue(k.compareTo(keys[i])==0);
          tree.checkConsistency();
        }
      }
    }
  }

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.BTreeTest");
  }
}
