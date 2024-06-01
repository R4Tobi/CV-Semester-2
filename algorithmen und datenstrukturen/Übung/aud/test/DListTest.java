package aud.test;

import aud.DList;
import org.junit.*;
import static org.junit.Assert.*;

public class DListTest {
  @Test
  public void testCtor() {
    DList<String> list=new DList<String>();
    assertTrue(list.empty());
  }

  @Test
  public void testSize() {
    DList<String> list=new DList<String>();
    assertEquals(list.size(),0);
    for (int i=0;i<10;++i) {
      list.push_front("x");
      assertEquals(list.size(),i+1);
    }
  }

  @Test
  public void testEntries() {
    DList<Integer> list=new DList<Integer>();
    for (int i=0;i<10;++i) {
      list.push_front(i);
      assertEquals(list.front().intValue(),i);
    }
    int n=list.size();
    for (int i=0;i<10;++i) {
      assertTrue(list.at(i)==n-1-i);
    }
  }

  @Test
  public void testList() {
    DList<Integer> list=new DList<Integer>();
    list.push_front(4); // [4]
    list.push_back(6);  // [4,6]
    list.push_back(8);  // [4,6,8]
    list.push_front(2); // [2,4,6,8]
    list.insert(0,1);   // [1,2,4,6,8]
    list.insert(2,3);   // [1,2,3,4,6,8]
    list.insert(4,5);   // [1,2,3,4,5,6,8]
    list.insert(6,7);   // [1,2,3,4,5,6,7,8]
    list.insert(8,9);   // [1,2,3,4,5,6,7,8,9]

    for (int i=0;i<9;++i)
      assertTrue(list.at(i)==i+1);

    list.pop_front();   // [2,3,4,5,6,7,8,9]
    assertTrue(list.size()==8);
    assertTrue(list.front()==2);
    assertTrue(list.at(0)==2);

    list.pop_back();    // [2,3,4,5,6,7,8]
    assertTrue(list.size()==7);
    assertTrue(list.back()==8);
    assertTrue(list.at(list.size()-1)==8);

    assertTrue(list.at(2)==4);
    list.erase(2);     // [2,3,5,6,7,8]
    assertTrue(list.size()==6);
    assertTrue(list.at(2)==5);

    while (!list.empty()) {
      int sz=list.size();
      assertTrue(sz>0);
      list.erase(sz-1);
    }
    assertTrue(list.empty());

    list.push_back(1);
    assertTrue(list.front().intValue()==1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_at() {
    DList<Integer> list=new DList<Integer>();
    list.push_front(1);
    list.at(1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_front() {
    DList<Integer> list=new DList<Integer>();
    list.front();
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_back() {
    DList<Integer> list=new DList<Integer>();
    list.back();
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_erase() {
    DList<Integer> list=new DList<Integer>();
    list.push_front(1);
    list.erase(1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_pop_front() {
    DList<Integer> list=new DList<Integer>();
    list.pop_front();
  }
  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_pop_back() {
    DList<Integer> list=new DList<Integer>();
    list.pop_back();
  }

  @Test
  public void testIterators() {
    DList<Integer> list=new DList<Integer>();
    for (int i=0;i<5;++i)
      list.push_back(i);

    int n;

    DList<Integer>.ForwardIterator ii=list.iterator();
    for (int i=0;i<5;++i) {
      assertTrue(ii.hasNext());
      n=ii.next().intValue();
      assertTrue(n==i);
    }
    assertFalse(ii.hasNext());

    n = 0;
    for (Integer item : list) {
      assertTrue(n==item.intValue());
      ++n;
    }

    n = 4;
    for (Integer item : list.backwards()) {
      assertTrue(n==item.intValue());
      --n;
    }

    ii=list.iterator();
    list.insert_before(ii,-1);
    list.insert_after(ii,-2);
    assertTrue(ii.hasNext());
    ii.previous();
    assertTrue(ii.hasNext());
    n=ii.next();
    assertTrue(ii.hasNext());
    assertTrue(n==-1);
    n=ii.next();
    assertTrue(ii.hasNext());
    assertTrue(n==0);
    n=ii.next();
    assertTrue(ii.hasNext());
    assertTrue(n==-2);
    n=ii.next();
    assertTrue(ii.hasNext());
    assertTrue(n==1);

    DList<Integer>.BackwardIterator jj=list.reverse_iterator();
    for (int i=4;i>=1;--i) {
      assertTrue(jj.hasNext());
      n=jj.next().intValue();
      assertTrue(n==i);
    }
  }

  public static void main(String args[]) {
    // prefer command line:
    // > java org.junit.runner.JUnitCore  aud.tests.DListTest

    org.junit.runner.JUnitCore.main("aud.test.DListTest");
  }
}
