package aud.example.hash;

import aud.Vector;
import aud.SList;
import aud.graph.matrix.SparseMatrix;
import aud.util.ColormapCount;
import aud.util.SVGViewer;

/** Base class for simple hash tables (mainly for demonstration).
 *  <p>
 *  <b>Note:</b>This class is intended to <em>visualize</em> hash tables,
 *  it is not an efficient or particularly useful implementation of a hash
 *  table!<p>
 *
 * @param <T> table entry
 */
public class SimpleHashtable<T> {

  HashFunction<T> hash_ = null;

  /** this is the hash table, ready for separate chaining */
  Vector<SList<T> >  table_ = new Vector<SList<T> >();

  /** number of entries */
  int n_ = 0;


  /** What to do on collision ({@code null} => separate chaining) */
  CollisionHandler<T> onCollision_;

  /** number of calls to collision handling */
  int nCollisions_ = 0;

  /** record "history" in matrix for visualization */
  SparseMatrix<Integer> history_ = null;

  /** current time step in "history" */
  int time_ = 1;

  /** print information on  */
  boolean verbose = false;

  /** create new hash table
   * @param size size of table (could be any "bad" size, "good" sizes are prime)
   * @param hash hash function
   * @param onCollision strategy for collision handling, a value {@code null}
   * forces separate chaining
   */
  public SimpleHashtable(
        int size,HashFunction<T> hash,
        CollisionHandler<T> onCollision) {
    hash_=hash;
    onCollision_=onCollision;

    table_.reserve(size);
    for (int i=0;i<size;++i)
      table_.push_back(new SList<T>());
  }

  /** get number of entries */
  public int getNumEntries() { return n_; }

  /** get size of hash table */
  public int getTableSize() { return table_.size(); }

  /** get load factor */
  public float getLoadFactor() {
    return (float) n_/(float) table_.size();
  }

  /** get number of collision handled (including secondary collisions) */
  int nCollsionsHandled() { return nCollisions_; }

  /** insert {@code key}
   *  @return {@code true} if this caused a collision
   */
  boolean insert(T key) {
    long h=hash_.hash(key);
    int idx=(int) (h % table_.size());
    SList<T> bucket=table_.at(idx);
    if (bucket.empty()) {
      ++n_;
      bucket.push_front(key);
      recordInsert(idx);
      if (verbose)
        System.err.println("SimpleHashTable: insert "+key+" @ "+idx);
      return false;
    }
    else if (onCollision_==null) { // separate chaining
      ++n_;
      bucket.push_front(key);
      recordInsert(idx);
      ++nCollisions_;
      if (verbose)
        System.err.println(
              "SimpleHashTable: insert "+key+" @ "+idx+
              " (bucket size="+bucket.size()+")");
      return true;
    }
    else {
      for (int j=1;j<table_.size();++j) {
        if (verbose)
          System.err.println(
                "SimpleHashTable: insert "+key+" with "+onCollision_);

        h=onCollision_.newHash(this,key,h,j);

        if (verbose)
          System.err.print("SimpleHashTable: "+idx+" occupied => ");

        idx=(int) (h % table_.size());

        if (verbose)
          System.err.println(idx);

        ++nCollisions_;

        if ((bucket=table_.at(idx)).empty()) {
          ++n_;
          bucket.push_front(key);
          recordInsert(idx);
          return true;
        }
      }
      // give up after N tries !
      throw new RuntimeException("collision handling failed!");
    }
  }

  @Override public String toString() {
    String s=
      "SimpleHashTable [m="+getTableSize()+
      ", n="+getNumEntries()+", n/m="+getLoadFactor()+", counted "+nCollisions_+
      " collisions]\n";
    for (int i=0;i<table_.size();++i) {
      SList<T> bucket=table_.at(i);
      s+=i+": ";
      if (!bucket.empty())
        s+=bucket.toString();
      s+="\n";
    }
    return s;
  }

  /** record insert at index {@code i} */
  void recordInsert(int i) {
    if (history_!=null) {
      Integer k=history_.get(i+1,time_);
      history_.set(i+1,time_,k==null ? 1 : k+1);
    }
  }

  /** start recording of history */
  public void beginRecording() {
    history_=new SparseMatrix<Integer>();
    time_=1;
    history_.set(getTableSize(),1,0);

    for (int i=0;i<table_.size();++i) {
      SList<T> bucket=table_.at(i);
      if (!bucket.empty())
        history_.set(i,time_,bucket.size());
    }
  }

  /** advance one time step in history, e.g., after every insert */
  public void nextTimeStep() {
    // copy previous row
    Vector<Integer> cnt=history_.getColumnEntries(time_);
    int[] ri=history_.getColumnRowIndices(time_);
    ++time_;

    int i=0;
    for (Integer c : cnt)
      history_.set(ri[i++],time_,c);
  }

  /** show history */
  public SVGViewer showHistory() {
    return history_.spy(
        "size m="+getTableSize()+", n="+getNumEntries()+
        ", n/m="+getLoadFactor()+" ("+time_+" time steps)",
        new ColormapCount());
  }

  public static void main(String args[]) {
    SimpleHashtable<Integer> h=
        new SimpleHashtable<Integer>(37,
              new HashFunction<Integer>() {
          public long hash(Integer i) { return i*13+7; }
              },null
              );

    h.beginRecording();
    for (int i=0;i<100;++i) {
      h.nextTimeStep();
      h.insert(i);
    }
    h.showHistory();
  }
}
