package aud.test;

import aud.Vector;
import aud.graph.matrix.SparseMatrix;

import org.junit.*;
import static org.junit.Assert.*;

public class SparseMatrixTest {

  @Test
  public void testMatrix() {
    SparseMatrix<Integer> m=new SparseMatrix<Integer>();
    assertFalse(m.isSymmetricMatrix());

    assertSame(m.nnz(),0);
    assertSame(m.getNumRows(),0);
    assertSame(m.getNumColumns(),0);
    assertSame(m.getMinRowIndex(),0);
    assertSame(m.getMinColumnIndex(),0);

    m.set(2,2,2);
    m.set(9,9,9);

    assertSame(m.getNumRows(),9);
    assertSame(m.getNumColumns(),9);
    assertSame(m.getMinRowIndex(),2);
    assertSame(m.getMinColumnIndex(),2);

    assertSame(m.get(2,2),2);
    assertSame(m.get(9,9),9);
    assertSame(m.nnz(),2);

    m.set(2,2,3);
    assertSame(m.get(2,2),3);
    assertSame(m.nnz(),2);

    m.set(2,2,null);
    assertSame(m.get(2,2),null);
    assertSame(m.nnz(),1);

    m.set(2,2,2);

    m.set(4,2,-1);
    assertSame(m.get(4,2),-1);
    assertSame(m.get(2,4),null);

    int[] ri=m.getColumnRowIndices(2);
    Vector<Integer> v=m.getColumnEntries(2);

    assertSame(ri.length,2);
    assertSame(m.columnDegree(2),2);
    assertSame(ri[0],2);
    assertSame(ri[1],4);
    assertEquals(v.at(0),Integer.valueOf(2));
    assertEquals(v.at(1),Integer.valueOf(-1));

    int[] ci=m.getRowColumnIndices(2);
    v=m.getRowEntries(2);
    assertSame(ci.length,1);
    assertSame(m.rowDegree(2),1);
    assertSame(v.size(),1);

    assertSame(ci[0],2);
    assertEquals(v.at(0),Integer.valueOf(2));
  }

  @Test
  public void testSymmatrixMatrix() {
    SparseMatrix<Integer> m=new SparseMatrix<Integer>(true);
    assertTrue(m.isSymmetricMatrix());

    assertSame(m.nnz(),0);
    assertSame(m.getNumRows(),0);
    assertSame(m.getNumColumns(),0);
    assertSame(m.getMinRowIndex(),0);
    assertSame(m.getMinColumnIndex(),0);

    m.set(2,2,2);
    m.set(9,9,9);

    assertSame(m.getNumRows(),9);
    assertSame(m.getNumColumns(),9);
    assertSame(m.getMinRowIndex(),2);
    assertSame(m.getMinColumnIndex(),2);

    assertSame(m.get(2,2),2);
    assertSame(m.get(9,9),9);
    assertSame(m.nnz(),2);

    m.set(2,2,3);
    assertSame(m.get(2,2),3);
    assertSame(m.nnz(),2);

    m.set(2,2,null);
    assertSame(m.get(2,2),null);
    assertSame(m.nnz(),1);

    m.set(2,2,2);

    m.set(4,2,-1);
    assertSame(m.get(4,2),-1);
    assertSame(m.get(2,4),-1);

    int[] ri=m.getColumnRowIndices(2);
    Vector<Integer> v=m.getColumnEntries(2);

    assertSame(ri.length,2);
    assertSame(m.columnDegree(2),2);
    assertSame(ri[0],2);
    assertSame(ri[1],4);
    assertEquals(v.at(0),Integer.valueOf(2));
    assertEquals(v.at(1),Integer.valueOf(-1));

    int[] ci=m.getRowColumnIndices(2);
    v=m.getRowEntries(2);
    assertSame(ci[0],2);
    assertSame(ci[1],4);
    assertEquals(v.at(0),Integer.valueOf(2));
    assertEquals(v.at(1),Integer.valueOf(-1));
  }

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.SparseMatrixTest");
  }
}
