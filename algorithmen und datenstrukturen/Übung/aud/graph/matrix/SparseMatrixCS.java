package aud.graph.matrix;

import java.util.Map;
import java.util.TreeMap;
import java.util.SortedMap;
import java.io.*;

import aud.Vector;
import aud.util.Colormap;
import aud.util.SVGViewer;

/**
 * Simple sparse matrix data structure. <p>
 *
 * This data structure is intended for <b>sparse</b> storage: it
 * stores only <em>non-null</em> entries, i.e., the required amount of
 * storage is O({@link #nnz}) (not O(m*n))).
 *
 * <ul>
 *
 * <li>SparseMatrixCS stores and retrieves <em>columns</em>
 * efficiently. The retrieval of rows is <em>not</em> supported.</li>
 *
 * <li>The matrix has potentially infinite dimensions. (Dimension is
 * determined by the maximium row and column indices for all
 * entries. <em>We don't require this property for adjacency
 * matrices.</em></li>
 *
 * <li>The implementation is based on a simplified <a
 *  href="http://netlib.org/linalg/html_templates/node92.html">compressed
 *  column storage (CCS)</a> scheme based on sorted tuples in a {@code
 *  TreeMap}.</li>
 *
 * </ul>
 */
public class SparseMatrixCS<T>  {

  protected TreeMap<Coordinate,T> mat_;

  /** create empty matrix with "arbitrarily growing" dimensions
   */
  public SparseMatrixCS() {
    mat_=new TreeMap<Coordinate,T>();
  }
  /** copy constructor */
  public SparseMatrixCS(SparseMatrixCS<T> other) {
    mat_=new TreeMap<Coordinate,T>(other.mat_);
  }

  /** get number of nonzero entries */
  public int nnz() { return mat_.size(); }

  /** computes from maximium row index [O({@link #nnz})] */
  public int getNumRows() {
    int imax=0;
    for (Coordinate c : mat_.keySet()) {
      if (c.i>imax)
        imax=c.i;
    }
    return imax;
  }
  /** get minimum row index [O({@link #nnz})] */
  public int getMinRowIndex() {
    int imin=Integer.MAX_VALUE;
    for (Coordinate c : mat_.keySet()) {
      if (c.i<imin)
	imin=c.i;
    }
    return (imin==Integer.MAX_VALUE) ? 0 : imin;
  }
  /** computed from maximum column index [O(1)] */
  public int getNumColumns() { return mat_.size()==0 ? 0 : mat_.lastKey().j; }
  /** get minimum row index [O(1)] */
  public int getMinColumnIndex() { return mat_.size()==0 ? 0 : mat_.firstKey().j; }

  /** helper: check indices (i,j)
      @throws IndexOutOfBoundsException (non-positive indices)
      @return mat_
   */
  TreeMap<Coordinate,T> check(int i,int j) {
    if (!(0<=i && 0<=j))
      throw new IndexOutOfBoundsException();
    return mat_;
  }

  /** get entry (i,j) [O(log({@code nnz}))]
      @throws IndexOutOfBoundsException (non-positive indices)
      @return entry or {@code null}
   */
  public T get(int i,int j) {
    return check(i,j).get(new Coordinate(i,j));
  }

  /** Set entry (i,j) to data [O(log({@code nnz}))].
      @param i row index (>=0)
      @param j column index (>=0)
      @param data new value; if {@code data==null}, then the entry
      will be <em>erased</em> from the matrix.
      @throws IndexOutOfBoundsException on error (non-positive indices)a
      @return old value (or {@code null}
   */
  public T set(int i,int j,T data) { return _set(i,j,data); }

  /** set data (require access to original method in subclass) */
  T _set(int i,int j,T data) {
    T e=get(i,j);

    if (e!=data) {
      Coordinate key=new Coordinate(i,j);

      if (data==null)
        mat_.remove(key);
      else
        mat_.put(key,data);
    }
    return e;
  }

  /** get entries in column j as array
      @throws IndexOutOfBoundsException on error (non-positive index)
   */
  @SuppressWarnings("unchecked")
  public Vector<T> getColumnEntries(int j) {
    SortedMap<Coordinate,T> sub=
      check(1,j).subMap(new Coordinate(-1,j),
			new Coordinate(Integer.MAX_VALUE,j));
    Object[] a=sub.values().toArray();
    Vector<T> v=new Vector<T>();
    v.reserve(a.length);
    for (int i=0;i<a.length;++i)
      v.push_back((T) a[i]);
    return v;
  }

  /** get row indices in column j as array
      @throws IndexOutOfBoundsException on error (non-positive index)
   */
  public int[] getColumnRowIndices(int j) {
    SortedMap<Coordinate,T> sub=
      check(1,j).subMap(new Coordinate(-1,j),
			new Coordinate(Integer.MAX_VALUE,j));
    int[] idx=new int[sub.size()];
    int i=0;
    for (Coordinate c : sub.keySet()) {
      idx[i++]=c.i;
    }
    assert(i==idx.length);
    return idx;
  }

  /** get number of nonzero entries in column j
      @throws IndexOutOfBoundsException on error (non-positive index)
      @return length of {@link #getColumnEntries}
   */
  public int columnDegree(int j) { return getColumnEntries(j).size(); }


  /** get transposed matrix */
  public SparseMatrixCS<T> getTransposed() {
    SparseMatrixCS<T> m=new SparseMatrixCS<T>();
    for (Map.Entry<Coordinate,T> entry : mat_.entrySet()) {
      Coordinate c=entry.getKey();
      m.mat_.put(new Coordinate(c.j,c.i),entry.getValue());
    }
    return m;
  }

  /** Get nonzero pattern.
      Returns a matrix with integer entries that equal 1 for
      every nonzero entry. (Same as Matlab's
      <a href="http://www.mathworks.de/help/techdoc/ref/spones.html">spones</a>).
  */
  public SparseMatrixCS<Integer> spones() {
    SparseMatrixCS<Integer> m=new SparseMatrixCS<Integer>();
    for (Map.Entry<Coordinate,T> entry : mat_.entrySet())
      m.mat_.put(entry.getKey(),1);
    return m;
  }

  //
  // helpers
  //

  /** Get array of row indices.
      @see #getColumnIndices
      @see #getValues
   */
  public int[] getRowIndices() {
    int[] ri=new int[nnz()];
    int i=0;
    for (Map.Entry<Coordinate,T> entry : mat_.entrySet())
      ri[i++]=entry.getKey().i;
    return ri;
  }
  /** Get array of column indices in same order as for
      {@link #getRowIndices}.
  */
  public int[] getColumnIndices() {
    int[] ci=new int[nnz()];
    int i=0;
    for (Map.Entry<Coordinate,T> entry : mat_.entrySet())
      ci[i++]=entry.getKey().j;
    return ci;
  }
  /** Get array of values in same order as for {@link #getRowIndices}.
   */
  @SuppressWarnings("unchecked")
  public T[] getValues() {
    T[] values=(T[]) new Object[nnz()];
    int i=0;
    for (Map.Entry<Coordinate,T> entry : mat_.entrySet())
      values[i++]=entry.getValue();
    return values;
  }

  @Override public String toString() {
    String rv="[";
    for (Map.Entry<Coordinate,T> entry : mat_.entrySet()) {
      rv+=entry+" ";
    }
    return rv.trim()+"]";
  }

  //
  // visualize matrix in various ways
  //

  /** get LaTeX code for displaying (TikZ) matrix */
  public String toLaTeX(String name) {
    String rv="\\matrix ("+name+") [matrix of math nodes]\n{\n";
    int m=getNumRows(),n=getNumColumns();
    Class<?> intclass=Integer.valueOf(1).getClass();
    for (int i=getMinRowIndex();i<=m;++i) {
      for (int j=getMinColumnIndex();j<=n;++j) {
	T v=get(i,j);
	if (v!=null) {
	  rv+=" ";
	  rv+=(v.getClass()==intclass) ? v.toString() : "1";
	}
	if (j!=n)
	  rv+=" &";
      }
      rv+=" \\\\\n";
    }
    rv+="};\n";
    return rv;
  }

  public File renderSpySVG(File svgfile,Colormap<T> colormap) {
    if (colormap==null)
      colormap=new Colormap<T>();

    try {
      FileOutputStream f=new FileOutputStream(svgfile);

      // write header
      float sz=5.0f; // cell size
      float r=2.0f;  // radius
      float w=(getNumColumns()+1)*sz, h=(getNumRows()+1)*sz;

      String buf=
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
        "<svg xmlns=\"http://www.w3.org/2000/svg\"\n"+
        " width=\""+w+"mm\"\n"+
        " height=\""+h+"mm\"\n"+
        " version=\"1.1\"\n>\n\n";
      f.write(buf.getBytes());

      // rulers
      f.write(("<line x1=\""+0+"mm\" y1=\""+(sz-r)+
               "mm\" x2=\""+w+"mm\" y2=\""+(sz-r)+"mm\" "+
               "style=\"stroke:#000000;stroke-width:1pt\"/>\n").getBytes());
      for (int i=1;i<=getNumColumns();++i) {
        float x=sz*(0.5f+i);
        f.write(("<line x1=\""+x+"mm\" y1=\""+(r)+
                 "mm\" x2=\""+x+"mm\" y2=\""+(r+r)+"mm\" "+
                 "style=\"stroke:#000000;stroke-width:1pt\"/>\n").getBytes());
      }
      f.write(("<line x1=\""+(sz-r)+"mm\" y1=\""+0+
               "mm\" x2=\""+(sz-r)+"mm\" y2=\""+h+"mm\" "+
               "style=\"stroke:#000000;stroke-width:1pt\"/>\n").getBytes());
      for (int i=1;i<=getNumRows();++i) {
        float y=sz*(0.5f+i);
        f.write(("<line x1=\""+(r)+"mm\" y1=\""+y+
                 "mm\" x2=\""+(r+r)+"mm\" y2=\""+(y)+"mm\" "+
                 "style=\"stroke:#000000;stroke-width:1pt\"/>\n").getBytes());
      }
      f.write("\n\n".getBytes());

      // data
      for (Map.Entry<Coordinate,T> entry : mat_.entrySet()) {
        float x=sz*(0.5f+entry.getKey().j), y=sz*(0.5f+entry.getKey().i);
        String fill=String.format("%06x",colormap.getRGB(entry.getValue()));

        buf="<circle style=\"fill:#"+fill+";stroke:#000000;stroke-width:0px\" "+
            "r=\""+r+"mm\" cx=\""+x+"mm\" cy=\""+y+"mm\"/>\n";
        f.write(buf.getBytes());
      }

      f.write("</svg>\n".getBytes());
      f.close();
    } catch (IOException e) {
      System.err.println("ERROR: "+e.getMessage());
      return null; // note: probably triggers NullPointerException
    }
    return svgfile;
  }

  public String spyTikZ(boolean rulers,Colormap<T> colormap) {
    if (colormap==null)
      colormap=new Colormap<T>();

    String s="\\begin{tikzpicture}[dot/.style={draw=none,fill=c},scale=1]\n";

    if (rulers) {
      s+="  \\foreach \\y in {0,...,"+(getNumRows()-1)+"} {\n"+
         "     \\draw (-2.5pt,-\\y) -- (2.5pt,-\\y);\n"+
         "     \\node[left] at (-1ex,-\\y-0.5) {{\\scriptsize \\y}};\n"+
         "  }\n"+
         "  \\draw (0,0) -- (0,-"+getNumRows()+");\n\n"
         ;
      s+="  \\foreach \\x in {1,...,"+(getNumColumns())+"} {\n"+
      "     \\draw (\\x,-2.5pt) -- (\\x,2.5pt);\n"+
      "     \\node[above] at (\\x-0.5,0) {{\\scriptsize \\x}};\n"+
      "  }\n"+
      "  \\draw (0,0) -- ("+(getNumColumns())+",0);\n\n"
      ;
    }

    float sz=1;
    for (Map.Entry<Coordinate,T> entry : mat_.entrySet()) {
      int rgb=colormap.getRGB(entry.getValue());
      float r=(float) (rgb>>16)/255.0f;
      float g=(float) ((rgb>> 8)&0xff)/255.0f;
      float b=(float) (rgb&0xff)/255.0f;
      s+="  \\definecolor{c}{rgb}{"+r+","+g+","+b+"}\n";
      float x=sz*(-0.5f+entry.getKey().j), y=-sz*(-0.5f+entry.getKey().i);
      s+="  \\draw[dot] ("+x+","+y+") circle (0.4" +
      		");\n";
    }

    s+="\\end{tikzpicture}\n";
    return s;
  }

  /** render <a href="http://www.mathworks.de/help/techdoc/ref/spy.html">spy
   * </a> plot in new window */
  public SVGViewer spy(String caption,Colormap<T> colormap) {
    File tmp=null;
    try {
      tmp=File.createTempFile("aud-","svg");
    }
    catch (IOException e) {
      System.err.println("ERROR: "+e.getMessage());
      System.exit(-1);
    }
    tmp.deleteOnExit();
    renderSpySVG(tmp,colormap!=null ? colormap: new Colormap<T>());

    SVGViewer v=SVGViewer.displayWindow(tmp,caption!=null ? caption : "spy");

    return v;
  }
}
