package aud.test;

import aud.Stack;
import java.util.NoSuchElementException;

import org.junit.*;
import static org.junit.Assert.*;

public class StackTest {

  @Test
  public void testStack() {
    Stack<Integer> stack=new Stack<Integer>();

    assertTrue(stack.is_empty());
    stack.push(1);
    assertFalse(stack.is_empty());
    assertEquals(stack.top().intValue(),1);

    stack.pop();
    assertTrue(stack.is_empty());

    stack.push(1);
    stack.push(2);
    stack.push(3);
    assertTrue(stack.pop()==3);
    assertTrue(stack.pop()==2);
    assertTrue(stack.pop()==1);
    assertTrue(stack.is_empty());
  }

  @Test(expected=NoSuchElementException.class)
  public void testInvalid_top() {
    Stack<Integer> stack=new Stack<Integer>();
    stack.top();
  }
  @Test(expected=NoSuchElementException.class)
  public void testInvalid_pop() {
    Stack<Integer> stack=new Stack<Integer>();
    stack.pop();
  }

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.StackTest");
  }
}
