package aud.test;

import aud.Queue;
import aud.QueueDL;
import aud.adt.AbstractQueue;

import java.util.NoSuchElementException;

import org.junit.*;
import static org.junit.Assert.*;

public class QueueTest {

  @Test
  public void testQueue() {
    AbstractQueue<Integer> queue=new Queue<Integer>();
    testQueue(queue);
  }
  @Test
  public void testQueueDL() {
    AbstractQueue<Integer> queue=new QueueDL<Integer>();
    testQueue(queue);
  }

  void testQueue(AbstractQueue<Integer> queue) {
    assertTrue(queue.is_empty());
    queue.enqueue(1);
    assertFalse(queue.is_empty());
    assertTrue(queue.front().intValue()==1);

    assertTrue(queue.dequeue()==1);
    assertTrue(queue.is_empty());

    queue.enqueue(1);
    queue.enqueue(2);
    queue.enqueue(3);
    assertTrue(queue.dequeue()==1);
    assertTrue(queue.dequeue()==2);
    assertTrue(queue.dequeue()==3);
    assertTrue(queue.is_empty());

    for (int i=0;i<10;++i)
      queue.enqueue(i);
    queue.enqueue(-1);
    for (int i=0;i<100;++i)
      queue.enqueue(i);
    queue.enqueue(-1);

    int n;
    for (int i=0;((n=queue.dequeue())!=-1);++i)
      assertTrue(n==i);

    for (int i=0;i<100;++i)
      queue.enqueue(i);
    queue.enqueue(-1);

    for (int i=0;((n=queue.dequeue())!=-1);++i)
      assertTrue(n==i);
    for (int i=0;((n=queue.dequeue())!=-1);++i)
      assertTrue(n==i);

    assertTrue(queue.is_empty());
  }

  @Test(expected=NoSuchElementException.class)
  public void testInvalid_front() {
    Queue<Integer> queue=new Queue<Integer>();
    queue.front();
  }
  @Test(expected=NoSuchElementException.class)
  public void testInvalid_dequeue() {
    Queue<Integer> queue=new Queue<Integer>();
    queue.dequeue();
  }
  @Test(expected=NoSuchElementException.class)
  public void testInvalid_frontDL() {
    QueueDL<Integer> queue=new QueueDL<Integer>();
    queue.front();
  }
  @Test(expected=NoSuchElementException.class)
  public void testInvalid_dequeueDL() {
    QueueDL<Integer> queue=new QueueDL<Integer>();
    queue.dequeue();
  }

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main("aud.test.QueueTest");
  }
}
