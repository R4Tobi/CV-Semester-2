package aud.example;

import java.util.NoSuchElementException;
import aud.Queue;

/** A queue that outputs messages on {@link #enqueue} and {@link #dequeue}.
    On {@link #enqueue} ({@link #dequeue}) print the new (removed) entry to
    {@code System.err}. The printed message is indented by the size
    of the queue.<p>

    You can replace {@link Queue} in any class by {@link VerboseQueue}
    to trace modifications of the queue.
 */
public class VerboseQueue<T> extends Queue<T> {

  int size_ = 0;

  public VerboseQueue() {}

  @Override public T dequeue() throws NoSuchElementException {
    T x=super.dequeue();
    --size_;
    print("dequeue "+x);
    return x;
  }

  @Override public void enqueue(T x) {
    super.enqueue(x);
    print("enqueue "+x);
    ++size_;
  }

  private void print(String message) {
    for (int i=0;i<size_;++i)
      System.err.print(" ");
    System.err.println(message+" => "+toString());
  }

};
