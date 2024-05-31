package aud.test;

import aud.A234Tree;
import aud.util.Permutations;

import org.junit.*;
import static org.junit.Assert.*;

public class A234TreeTest {

  @Test
  public void testA234Tree() {
    String[] keys={"a","b","c","d","e","f","g","h","i"};
    int      n=keys.length;

    for (int[] p : new Permutations(n)) {

      A234Tree<String> tree=new A234Tree<String>(false);

      for (int i : p) {
        String k=tree.find(keys[i]);
        assertEquals(k,null);
        tree.insert(keys[i]);
        k=tree.find(keys[i]);
        assertTrue(k!=null);
        assertTrue(k.compareTo(keys[i])==0);
        tree.checkConsistency();
      }

      tree=new A234Tree<String>(true);

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

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.A234TreeTest");
  }
}
