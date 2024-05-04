package aud.example;

import java.util.NoSuchElementException;
import aud.Stack;

/** A stack that outputs messages on {@link #push} and {@link #pop}.
    On {@link #push} ({@link #pop}) print the pushed (popped) entry to
    {@code System.err}. The printed message is indented by the height
    of the stack.<p>

    You can replace {@link Stack} in any class by {@link VerboseStack}
    to trace modifications of the stack. This is useful, e.g., for
    comparing iterative and recursive implementations of algorithms.
 */
public class VerboseStack<T> extends Stack<T> {

  int height_ = 0;

  public VerboseStack() {}

  @Override public T pop() throws NoSuchElementException {
    T x=super.pop();
    --height_;
    print("pop "+x);
    return x;
  }

  @Override public void push(T x) {
    super.push(x);
    print("push "+x);
    ++height_;
  }

  private void print(String message) {
    for (int i=0;i<height_;++i)
      System.err.print(" ");
    System.err.println(message+" => "+toString());
  }

  @Override
  public String toString() {
    if (!is_empty()) {
      T x=super.pop();
      String bottom=toString();
      super.push(x);
      return (bottom+(bottom.length()>1 ? "|":""))+x;
    }
    else
      return "|";
  }

};
