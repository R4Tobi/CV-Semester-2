package Uebung8;

import java.util.*;

//-----------------------------------------------------------------//
/**
 * This class represents a MaxHeap data structure. It is a specialized tree-based data structure
 * that satisfies the heap property. In a Max Heap, for any given node i, the value of i is greater
 * than or equal to the values of its children.
 *
 * @param <T> the type of elements in this heap, which must be a type that implements Comparable
 */
public class MaxHeap<T extends Comparable<T>> {
  //-----------------------------------------------------------------//
  //------------- !!Do not change the following lines!! -------------//
  /**
   * Returns a string representation of the heap.
   * This method is not to be changed because of backend dependencies.
   *
   * @return a string representation of the heap
   */
  public String toString() {
    return heap_.toString();  // don't change because of backend
  }

  //---------------------------------------------------------------//
  //--------------- !!Insert your solution below!! ----------------//
  private List<T> heap_;  // or Vector

  //---------------------------------------------------------------//
  /**
   * Constructs an empty MaxHeap.
   */
  public MaxHeap() {
    // TODO: implementation
    heap_ = new ArrayList<>();
  }

  //---------------------------------------------------------------//
  /**
   * Constructs a MaxHeap from an array of elements.
   * The heap property is maintained by calling downHeap on each element, starting from the middle of the array and working backwards.
   *
   * @param arr the array from which to create the heap
   */
  public MaxHeap(T[] arr) {
    // TODO: construct a heap from array a (use downHeap)
    heap_ = new ArrayList<>(Arrays.asList(arr));
    for (int i = heap_.size() / 2 - 1; i >= 0; i--) {
        downHeap(i);
    }
  }

  //---------------------------------------------------------------//
  /**
   * Returns the underlying list representing the heap.
   *
   * @return the underlying list representing the heap
   */
  public List<T> getHeap(){
    // TODO: implementation
    return heap_;
  }

  //---------------------------------------------------------------//
  /**
   * Returns the number of elements in the heap.
   *
   * @return the number of elements in the heap
   */
  public int getSize() {
    // TODO: implementation
    return heap_.size();
  }

  //---------------------------------------------------------------//
  /**
   * Returns whether the heap is empty.
   *
   * @return true if the heap is empty, false otherwise
   */
  public boolean isEmpty() {
    // TODO: implementation
    return heap_.isEmpty();
  }

  //---------------------------------------------------------------//
  /**
   * Restores the heap property in the heap by percolating the element at index n downwards.
   * It compares the element at index n with its children; if a child is greater, they are swapped.
   * The process is repeated until the element is at a position where it is greater than both children.
   *
   * @param n the index of the element to percolate down
   */
  final public void downHeap(int n) {
    // TODO: implementation
    int parent = n;
    int leftChild = 2 * n + 1;
    int rightChild = 2 * n + 2;

    if (leftChild < heap_.size() && heap_.get(leftChild).compareTo(heap_.get(parent)) > 0) {
        parent = leftChild;
    }

    if (rightChild < heap_.size() && heap_.get(rightChild).compareTo(heap_.get(parent)) > 0) {
        parent = rightChild;
    }

    if (parent != n) {
        swap(n, parent);
        downHeap(parent);
    }
  }

  /**
   * Swaps the elements at the given indices in the heap.
   *
   * @param n the index of one element
   * @param parent the index of the other element
   */
  private void swap(int n, int parent) {
    T temp = heap_.get(n);
    heap_.set(n, heap_.get(parent));
    heap_.set(parent, temp);
  }

  //---------------------------------------------------------------//
  /**
   * Inserts an element into the heap.
   * The element is initially placed at the end of the heap, and then it is percolated up to its correct position.
   *
   * @param obj the element to insert
   */
  public void insert(T obj) {
    // TODO: implementation, use upHeap
    heap_.add(obj);
    upHeap(heap_.size() - 1);
  }

  //---------------------------------------------------------------//
  /**
   * Restores the heap property in the heap by percolating the element at index n upwards.
   * It compares the element at index n with its parent; if the element is greater, they are swapped.
   * The process is repeated until the element is at a position where it is less than or equal to its parent.
   *
   * @param n the index of the element to percolate up
   */
private void upHeap(int n) {
    int parent = (n - 1) / 2;

    if (parent >= 0 && heap_.get(n).compareTo(heap_.get(parent)) > 0) {
        swap(n, parent);
        upHeap(parent);
    }
}

  //---------------------------------------------------------------//
  /**
   * The main method that tests the MaxHeap class.
   * It creates a MaxHeap, inserts some elements, and then prints the heap, its size, and whether it is empty.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    // TODO: test your code with appropriate examples
    MaxHeap<Integer> heap = new MaxHeap<>();
    heap.insert(5);
    heap.insert(10);
    heap.insert(3);
    System.out.println(heap.getHeap());
    System.out.println(heap.getSize());
    System.out.println(heap.isEmpty());
  }
}
