package aud.graph.matrix;

import aud.Vector;
import aud.util.*; // testing

/**
 * Simple sparse matrix data structure. <p>
 *
 * Sparse matrix with efficient access to column and row vectors based on
 * {@code SparseMatrixCS}: the implementation essentially stores
 * <em>the transposed</em> {@code SparseMatrixCS} for row access.
 *
 * No extra matrix is stored if the matrix is declared
 * <em>symmetric</em> at construction. However, to keep the code
 * simple, the upper and lower triangular matrices are stored in this
 * case.
 * @see SparseMatrixCS
 */
public class SparseMatrix<T> extends SparseMatrixCS<T> {

  /** Store transposed. <p>

      {@code rmat_} stores the transposed matrix to enable efficient
      access to rows. For symmetric matrices ({@link
      #isSymmetricMatrix}, {@code rmat_} is a reference to {@code
      this}! (This convention keeps the implementation simple but
      doubles the number of entries to be stored.)
   */
  protected SparseMatrixCS<T> rmat_ = null;

  /** create empty matrix */
  public SparseMatrix() {
    this(false);
  }

  /** create empty matrix (see {@link #isSymmetricMatrix}) */
  public SparseMatrix(boolean symmetric) {
    super();
    rmat_=symmetric ? this : new SparseMatrixCS<T>();
  }
  /** copy constructor */
  public SparseMatrix(SparseMatrix<T> other) {
    super(other);
    rmat_= other.isSymmetricMatrix() ?
      new SparseMatrixCS<T>(other.rmat_) : this;
  }
  /** copy constructor */
  public SparseMatrix(SparseMatrix<T> other,boolean transpose) {
    super((transpose && !other.isSymmetricMatrix()) ? other.rmat_ : other);
    if (other.isSymmetricMatrix())
      rmat_=this;
    else
      rmat_=transpose ?
	new SparseMatrixCS<T>(other) : new SparseMatrixCS<T>(other.rmat_);
  }

  /** Was matrix created explicitly as symmetric matrix? */
  public boolean isSymmetricMatrix() { return rmat_==this; }

  /** computed from maximum column index [O(1)] */
  @Override public int getNumRows() {
    return rmat_.getNumColumns();
  }
  /** get minimum column index [O(1)] */
  @Override public int getMinRowIndex() {
    return rmat_.getMinColumnIndex();
  }

  @Override public T set(int i,int j,T data) {
    T v,vr;
    v=super.set(i,j,data);
    vr=rmat_._set(j,i,data);
    assert(v==vr || i==j);
    return v;
  }

  /** get entries in row i as array
      @throws IndexOutOfBoundsException on error (non-positive index)
   */
  public Vector<T> getRowEntries(int i) {
    return rmat_.getColumnEntries(i);
  }

  /** get column indices in row i as array
      @throws IndexOutOfBoundsException on error (non-positive index)
   */
  public int[] getRowColumnIndices(int i) {
    return rmat_.getColumnRowIndices(i);
  }

  /** get number of nonzero entries in row i
      @throws IndexOutOfBoundsException on error (non-positive index)
      @return lenght of {@link #getRowEntries}
   */
  public int rowDegree(int i) { return getRowEntries(i).size(); }

  @Override public SparseMatrixCS<T> getTransposed() {
    return new SparseMatrixCS<T>(rmat_);
  }

  //
  // TODO multiply (spones)? --> spones_power
  //

  /** Example and test: show {@link aud.util.ColormapCount} color map */
  public static void main(String args[]) {
    SparseMatrix<Integer> m=new SparseMatrix<Integer>();

    m.set(1,1,-1);
    m.set(2,1,0);
    m.set(3,1,1);
    m.set(4,1,2);
    m.set(5,1,3);
    m.set(6,1,4);
    m.set(7,1,5);
    m.set(8,1,6);
    m.set(9,1,7);
    m.set(10,1,8);
    m.set(11,1,9);
    m.set(12,1,10);
    m.set(13,1,11);
    m.set(1,1,11);

    m.set(1,5,11);

    Colormap<Integer> colormap= new ColormapCount();

    //m.renderSpySVG(new java.io.File("colormap.svg"),colormap);

    m.spy("colormap",colormap);

    System.out.println(m.spyTikZ(true,colormap));
  }
}
