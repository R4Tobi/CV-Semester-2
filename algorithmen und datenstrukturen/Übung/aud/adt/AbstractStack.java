package aud.adt;

import java.util.NoSuchElementException;

/**
 * Interface for an ADT stack.
 */
public abstract class AbstractStack<T> {

  /** create empty stack */
  protected AbstractStack() {}

  /** Is stack empty? */
  public abstract boolean is_empty();

  /** Get stack top.
      Requires <code>!is_empty()</code>.
      @throws NoSuchElementException
      @return top
  */
  public abstract T top();

  /** Pop element from stack.
      Requires <code>!is_empty()</code>.
      @throws NoSuchElementException
      @return removed element
  */
  public abstract T pop();

  /** Push x onto stack.
      @param x new element
  */
  public abstract void push(T x);

  /** Get string representation {@code "|a|b|c"}.
      (Here, {@code "c"} is stack top. {@code "|"} denotes empty
      stack.)<p>
  */
  @Override
  public String toString() {
    if (!is_empty()) {
      T x=pop();
      String bottom=toString();
      push(x);
      return (bottom+(bottom.length()>1 ? "|":""))+x;
    }
    else
      return "|";
  }
}
