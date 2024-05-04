package aud;

import aud.adt.AbstractQueue;
import java.util.NoSuchElementException;

/**
 *  Implementation of AbstractQueue as a (dynamically resized)
 *  circular buffer based on array.
 */
//@<queue:class
public class Queue<T> extends AbstractQueue<T> {

  T[] data_;
  int head_ = 0;
  int tail_ = 0;
  //@>queue:class

  static final int DEFAULT_SIZE = 16;

  /** create empty queue */
  public Queue() {
    this(DEFAULT_SIZE);
  }
  /** create empty queue and reserve storage */
  @SuppressWarnings("unchecked")
  //@<queue:ctor
  public Queue(int capacity) {
    data_=(T[]) new Object[capacity];
    head_=tail_=0;
  }
  //@>queue:ctor

  @Override
  //@<queue:empty
  public boolean is_empty() {
    return head_==tail_;
  }
  //@>queue:empty

  @Override
  //@<queue:front
  public T front() {
    if (is_empty())
      throw new NoSuchElementException();
    return data_[head_];
  }
  //@>queue:front

  //@<queue:dequeue
  @Override public T dequeue() {
    if (is_empty())
      throw new NoSuchElementException();
    T obj=data_[head_];
    data_[head_]=null; // "free" entry (for GC)
    head_=(head_+1)%data_.length;
    return obj;
  }
  //@>queue:dequeue

  @Override
  //@<queue:enqueue1
  public void enqueue(T x) {
    int newtail=(tail_+1)%data_.length;

    if (newtail!=head_) {
      // no "overrun" we just advance an index.
      data_[tail_]=x;
      tail_=newtail;
    }
    else {
      //@>queue:enqueue1
      //System.err.println("enlarge buffer of size "+data_.length);

      // detected "overrun": the new head "hits" tail --
      // This means the alloacted buffer data is too small!

      // get larger buffer q
      Queue<T> q=new Queue<T>(data_.length*2);

      // copy data into q
      while (!is_empty())
        q.enqueue(dequeue());
      q.enqueue(x);

      // "steal" state of temporary object q
      data_=q.data_;
      head_=q.head_;
      tail_=q.tail_;
      assert(head_==0 && tail_<data_.length);
    }
  }

  /** Get string representation.

      <b>Impementation note:</b><p>

      This method reads out the queue in <em>reverse</em> order, i.e.,
      we are "going backwards" and shift an index by -1 instead of +1
      modulo array length.<p>

      The Java expression a%b computes the remainder of an integer
      division and is <em>differs</em> from the mathematical
      definition of a mod b for negative operands!<p>

      The modulo operation is defined as

      <div>{@code a mod b := b-floor(a/b)*b}</div>

      where {@code floor(x)} rounds to the greatest integer that is
      less than or equal to x (i.e., "towards negative infinity").<p>

      This is what we want, because

      <div>{@code a mod b = (a+k*b) mod b} for any integer {@code k}
      (regardless of sign)</div>

      For example consider an array of size 3 then position -1 should
      refer to the <em>last</em> entry

      <div>{@code -1 mod 3 = -1+3 mod 3 = 2 mod 3 = 2}</div>

      Java (and similarly C/C++) defines the remainder operator
      differently such that the sign is treated "symmetrically" (not
      rounding towards negative infinity but towards 0):<p>

      <div>{@code a%b = (-a%b) for b>=0}</div>

      For the example, we get

      <div>{@code (-1)%3 = 1%3 = 1}</div>

      which is <em>not</em> what we want as index into the buffer!<p>

      We avoid this issue by adding {@code -1+data_.length} instead
      of {@code -1}.<p>

      <b>Summary:</b>
      <ul>
      <li>Computing the remainder of an integer division
      (by an operator) is treated <em>differently in different
      programming languages</em>.</li>
      <li>The same is true for the (rounding mode of the) integer
      division itself. The reason for the "non-mathematical" definition
      is probably the behavior of the machine instructions (like {@code idiv<}
      on Itel CPUs).</li>
      <li>Differences show up for <em>negative operands</em> And
      they can lead to subtle bugs!</li>
      <li><em>To be safe, make sure that operands are nonegative!</em> </li>
      </ul>
   */
  @Override
  public String toString() {
    if (is_empty())
      return "<";

    String s="";

    int t=(tail_+data_.length-1)%data_.length;
    for (;;) {
      s=data_[t]+"<"+s;
      if (t==head_)
        break;
      t=(t+data_.length-1)%data_.length;
    }
    return s;
  }
}
