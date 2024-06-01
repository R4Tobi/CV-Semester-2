package aud.test;

import aud.Vector;
import org.junit.*;
import static org.junit.Assert.*;

public class VectorTest {
  @Test
  public void testCtor() {
    Vector<String> vec=new Vector<String>();
    assertTrue(vec.empty());
  }

  @Test
  public void testSize() {
    Vector<String> vec=new Vector<String>();
    assertEquals(vec.size(),0);
    for (int i=0;i<10;++i) {
      vec.push_back("x");
      assertEquals(vec.size(),i+1);
      assertTrue(vec.capacity()>=vec.size());
    }
  }

  @Test
  public void testVec() {
    Vector<Integer> vec=new Vector<Integer>();
    vec.push_back(2);
    vec.push_back(4);
    vec.push_back(6); // [2,4,6]

    vec.insert(0,1); // [1,2,4,6]
    vec.insert(2,3); // [1,2,3,4,6]
    vec.insert(4,5); // [1,2,4,5,6]
    vec.insert(6,7); // [1,2,4,5,6,7]

    assertTrue(vec.size()==7);
    assertTrue(vec.capacity()>=vec.size());

    for (int i=0;i<vec.size();++i)
      assertTrue(vec.at(i).intValue()==i+1);

    assertTrue(vec.front()==1);
    assertTrue(vec.back()==7);

    vec.pop_back(); // [1,2,3,4,5,6]
    assertTrue(vec.size()==6);
    assertTrue(vec.back()==6);

    vec.erase(0); // [2,3,4,5,6]
    assertTrue(vec.size()==5);
    assertTrue(vec.front()==2);

    vec.erase(4); // [2,3,4,5]
    assertTrue(vec.size()==4);
    assertTrue(vec.back()==5);

    vec.erase(1); // [2,4,5]

    assertTrue(vec.size()==3);
    vec.erase(1); // [2,5]
    assertTrue(vec.size()==2);

    assertTrue(vec.front()==2);
    assertTrue(vec.back()==5);

    vec.pop_back();
    vec.erase(0);
    assertTrue(vec.empty());

    vec.push_back(1);
    vec.pop_back();
    assertTrue(vec.empty());
  }

  @Test
  public void testVecIterator() {
    Vector<Integer> vec=new Vector<Integer>();

    final int n=10;
    for (int i=0;i<n;++i) vec.push_back(i);

    int k=0;
    for (Integer i : vec) {
      assertTrue(k==i.intValue());
      ++k;
    }
  }


  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_at() {
    Vector<Integer> vec=new Vector<Integer>();
    vec.push_back(1);
    vec.at(1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_front() {
    Vector<Integer> vec=new Vector<Integer>();
    vec.front();
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_back() {
    Vector<Integer> vec=new Vector<Integer>();
    vec.back();
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_erase() {
    Vector<Integer> vec=new Vector<Integer>();
    vec.push_back(1);
    vec.erase(1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_pop_back() {
    Vector<Integer> vec=new Vector<Integer>();
    vec.pop_back();
  }

  public static void main(String args[]) {
    // prefer command line:
    // > java org.junit.runner.JUnitCore aud.test.VectorTest

    org.junit.runner.JUnitCore.main("aud.test.VectorTest");
  }
}
