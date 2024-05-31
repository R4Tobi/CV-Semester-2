package aud.graph.matrix;

/**
    Row/column coordinates (i,j).

    Coordinates in a 2d matrix (e.g., SparseMatrixCS).
    <p>

    Implements column order (lexicographic, columns before rows).
*/
public class Coordinate implements Comparable<Coordinate> {

  /** row index */
  public int i;
  /** column index */
  public int j;

  /** create coordinate (i,j) */
  public Coordinate(int i,int j) {
    this.i=i;
    this.j=j;
  }

  @Override public int compareTo(Coordinate other) {
    int rv=j-other.j;
    return rv!=0 ? rv : (i-other.i);
  }
  @Override public boolean equals(Object other) {
    Coordinate coord=(Coordinate) other;
    return i==coord.i && j==coord.j;
  }

  @Override public String toString() {
    return "("+i+","+j+")";
  }
}
