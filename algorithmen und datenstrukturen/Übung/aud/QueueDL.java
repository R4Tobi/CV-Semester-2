package aud;

import aud.adt.AbstractQueue;
import java.util.NoSuchElementException;

/**
 *  Impementation of AbstractQueue based on doubly linked list
 *  {@link DList}.
 */
//@<queuedl:class
public class QueueDL<T> extends AbstractQueue<T> {

  DList<T> data_;

  public QueueDL() { data_=new DList<T>(); }
//@>queuedl:class

  //@<queuedl:state
  @Override public boolean is_empty() {
    return data_.empty();
  }
  @Override public T front() {
    if (data_.empty())
      throw new NoSuchElementException();
    return data_.front();
  }
  //@>queuedl:state

  //@<queuedl:manip
  @Override public T dequeue() {
    if (data_.empty())
      throw new NoSuchElementException();
    T obj=data_.front();
    data_.pop_front();
    return obj;
  }

  @Override public void enqueue(T x) {
    data_.push_back(x);
  }
  //@>queuedl:manip

  @Override public String toString() {
    if (is_empty())
      return "<";

    String s="";
    DList<T>.ForwardIterator i=data_.iterator();
    while (i.hasNext()) {
      s+=i.next().toString()+"<";
    }
    return s;
  }
}
