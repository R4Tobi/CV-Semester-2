package aud.graph;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import aud.graph.matrix.Coordinate;
import aud.graph.matrix.SparseMatrix;

/**
 * Sparse adjacency matrix. <p>
 *
 * @param <Edge> edge data
 */
public class AdjacencyMatrix<Edge>
  extends SparseMatrix<Edge> implements Iterable<Edge> {


  /** create empty matrix */
  public AdjacencyMatrix(boolean symmetric) {
    super(symmetric);
  }

  /** Set all entries in row idx and column idx to {@code null}.<p>
      Note that this method does not shift indices (coordinates) but
      only "overwrites" existing entries.
   */
  public void clearColumnAndRow(int idx) {
    int[] ri=getColumnRowIndices(idx);
    for (int k=0;k<ri.length;++k)
      set(ri[k],idx,null);

    if (!isSymmetricMatrix()) {
      int[] ci=getRowColumnIndices(idx);
       for (int k=0;k<ci.length;++k)
         set(idx,ci[k],null);
    }
  }

  /** Get iterator over all edges.<p>
      For a symmetric matrix, every (undirected) edges is
      enumerated only <em>once</em>.
      @see SparseMatrix#isSymmetricMatrix
   */
  @Override public Iterator<Edge> iterator() {
    // inefficient!
    if (isSymmetricMatrix()) {
      // copy and remove_if
      TreeMap<Coordinate,Edge> m=new TreeMap<Coordinate,Edge>(mat_);
      for (Iterator<Map.Entry<Coordinate,Edge>> ii=m.entrySet().iterator();
           ii.hasNext();) {
        Map.Entry<Coordinate,Edge> entry=ii.next();
        Coordinate c=entry.getKey();
        if (c.i>c.j)
          ii.remove();
      }
      return m.values().iterator();
    }
    else
      return mat_.values().iterator();
  }
}
