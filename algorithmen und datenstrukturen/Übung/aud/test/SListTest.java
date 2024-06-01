package aud.test;

import aud.SList;
import org.junit.*;
import static org.junit.Assert.*;

public class SListTest {
  @Test
  public void testCtor() {
    SList<String> list=new SList<String>();
    assertTrue(list.empty());
  }

  @Test
  public void testSize() {
    SList<String> list=new SList<String>();
    assertEquals(list.size(),0);
    for (int i=0;i<10;++i) {
      list.push_front("x");
      assertEquals(list.size(),i+1);
    }
  }

  @Test
  public void testEntries() {
    SList<Integer> list=new SList<Integer>();
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
    SList<Integer> list=new SList<Integer>();
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
    SList<Integer> list=new SList<Integer>();
    list.push_front(1);
    list.at(1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_front() {
    SList<Integer> list=new SList<Integer>();
    list.front();
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_back() {
    SList<Integer> list=new SList<Integer>();
    list.back();
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_erase() {
    SList<Integer> list=new SList<Integer>();
    list.push_front(1);
    list.erase(1);
  }

  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_pop_front() {
    SList<Integer> list=new SList<Integer>();
    list.pop_front();
  }
  @Test(expected=IndexOutOfBoundsException.class)
  public void testInvalid_pop_back() {
    SList<Integer> list=new SList<Integer>();
    list.pop_back();
  }

  @Test
  public void testIterators() {
    SList<Integer> list=new SList<Integer>();
    for (int i=0;i<5;++i)
      list.push_back(i);

    int n;

    SList<Integer>.Iterator ii=list.iterator();
    for (int i=0;i<5;++i) {
      assertTrue(ii.hasNext());
      n=ii.next().intValue();
      assertTrue(n==i);
    }
    assertFalse(ii.hasNext());

    n=0;
    for (Integer i : list) {
      assertTrue(n==i.intValue());
      ++n;
    }

    ii=list.iterator();
    list.insert_after(ii,-1);
    assertTrue(ii.hasNext());
    n=ii.next();
    assertTrue(n==0);
    assertTrue(ii.hasNext());
    n=ii.next();
    assertTrue(n==-1);
    assertTrue(ii.hasNext());
    n=ii.next();
    assertTrue(n==1);
  }

  public static void main(String args[]) {
    // prefer command line:
    // > java org.junit.runner.JUnitCore aud.test.SListTest

    org.junit.runner.JUnitCore.main("aud.test.SListTest");
  }
}
