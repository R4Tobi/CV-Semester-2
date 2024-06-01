package aud.test;

import org.junit.runner.*;
import org.junit.runners.*;

@RunWith(Suite.class)
@Suite.SuiteClasses(
    { VectorTest.class,
      SListTest.class,
      DListTest.class,
      StackTest.class,
      QueueTest.class,
      BinaryTreeTest.class,
      BinarySearchTreeTest.class,
      AVLTreeTest.class,
      A234TreeTest.class,
      RedBlackTreeTest.class,
      BTreeTest.class,
      SparseMatrixCSTest.class,
      SparseMatrixTest.class,
    }
)
public class RunTests {
}
