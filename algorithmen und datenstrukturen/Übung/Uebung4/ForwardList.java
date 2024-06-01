package Uebung4;

import java.util.NoSuchElementException;

import aud.SList;

/**
 * ForwardList represents a list with single linked nodes, that do have a
 * next-pointer.
 */
public class ForwardList<T> implements Iterable<T> {
  public String toString() {
    if (is_empty())
      return "[]";
    String rv = "[";
    Node node = head_;
    do {
      rv += node.data_.toString();
      if (node.next_ != head_)
        rv += ",";
      node = node.next_;
    } while (node != null);
    rv += "]";
    return rv;
  }

  // Inner class representing a single element in the list
  public class Node {
    public T data_; // Data stored in the node
    public Node next_; // Reference to the next node in the list

    /**
     * Constructs a new node with the given data and next node reference.
     *
     * @param data the data to be stored in the node
     * @param next the reference to the next node in the list
     */
    public Node(T data, Node next) {
      data_ = data;
      next_ = next;
    }
  }

  public Node head_ = null; // Reference to the head (first node) of the list

  /**
   * Constructs an empty ForwardList.
   */
  public ForwardList() {
  }

  /**
   * Adds a new element to the front of the list.
   *
   * @param obj the object to be added to the front of the list
   */
  public void push_front(T obj) {
    Node newNode = new Node(obj, head_);
    head_ = newNode;
  }

  /**
   * Checks if the list is empty.
   *
   * @return true if the list is empty, false otherwise
   */
  public boolean is_empty() {
    return head_ == null;
  }

  /**
   * Traverses the list in reverse order and prints the elements.
   */
  public void backTraverse() {
    SList<T> tempList = new SList<>();
    Node node = head_;
    while (node != null) {
      tempList.push_front(node.data_);
      node = node.next_;
    }
  }

  /**
   * Iterator class to iterate over the list in reverse order.
   */
  public class BackIterator implements java.util.Iterator<T> {
    private Node current;

    public BackIterator(Node head) {
      current = head;
      if (head != null) {
        while (current.next_ != null) {
          current = current.next_;
        }
      }
    }

    @Override
    public boolean hasNext() {
      return current != null;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      T data = current.data_;
      current = findPrev(current);
      return data;
    }

    // Helper method to find the previous node
    private Node findPrev(Node node) {
      Node temp = head_;
      while (temp != null && temp.next_ != node) {
        temp = temp.next_;
      }
      return temp;
    }
  }
  /**
   * Returns a new instance of the BackIterator.
   *
   * @return a new BackIterator instance
   */
  public BackIterator iterator() {
    return new BackIterator(head_);
  }

  /**
   * Main method to test the functionality of the ForwardList.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    // Create a new ForwardList of integers
    ForwardList<Integer> list = new ForwardList<>();
    list.push_front(3);
    list.push_front(2);
    list.push_front(1);

    // Iterating backwards over the list using the BackIterator
    for (int el : list) {
      System.out.print(el + " ");
    }
  }
}
