package Übung3;

/**
 * The MyCircle class represents a circular doubly linked list where each node is connected forward to the next node and backward to the previous node.
 *
 * @param <T> The type of elements in this circular list.
 */
public class MyCircle<T> {
    /**
     * The Node class represents an individual element in the circular list, containing data and links to the next and previous nodes.
     */
    class Node {
        T data_ = null;
        Node next_ = null;
        Node prev_ = null;

        /**
         * Constructs a new Node with specified data and links to the previous and next nodes.
         *
         * @param obj The data to be stored in the node.
         * @param prv The previous node in the list.
         * @param nxt The next node in the list.
         */
        Node(T obj, Node prv, Node nxt) {
          data_ = obj;
          prev_ = prv;
          next_ = nxt;
        }
    }

    //--- class members
    protected Node head_ = null; // Points to the first element of the circular list or null if the list is empty.

    //--- class methods

    /**
     * Constructs an empty MyCircle instance.
     */
    public MyCircle() { head_ = null; }

    /**
     * Retrieves the first element's data in the circular list.
     *
     * @return The data of the first element or null if the list is empty.
     */
    public T front() { return head_ != null ? head_.data_ : null; }

    /**
     * Returns a string representation of the circular list.
     *
     * @return A string representing the circular list.
     */
    public String toString() {
      if (empty())
        return "[]";
      String rv = "[";
      Node node = head_;
      do {
        rv += node.data_.toString();
        if (node.next_ != head_)
          rv += ",";
        node = node.next_;
      } while(node != head_);
      rv += "]";
      return rv;
    }

    /**
     * Calculates the size of the circular list.
     *
     * @return The number of elements in the list.
     */
    public int size() {
        if (empty()) {
            return 0;
        }
        int count = 0;
        Node node = head_;
        do {
            count++;
            node = node.next_;
        } while (node != head_);
        return count;
    }

    /**
     * Determines whether the circular list is empty or not.
     *
     * @return true if the list is empty, false otherwise.
     */
    public boolean empty() {
        return head_ == null;
    }

    /**
     * Appends a new element to the end of the circular list.
     *
     * @param obj The data of the new element to be added.
     */
    public void push_back(T obj) {
        Node newNode = new Node(obj, null, null);
        if (empty()) {
            newNode.next_ = newNode;
            newNode.prev_ = newNode;
            head_ = newNode;
        } else {
            Node tail = head_.prev_; // Tail is the previous element of head
            tail.next_ = newNode;
            newNode.prev_ = tail;
            newNode.next_ = head_;
            head_.prev_ = newNode;
        }
    }

    /**
     * Removes the first element from the circular list.
     */
    public void pop_front() {
        if (!empty()) {
            if (head_.next_ == head_) {
                // If there is only one element in the circle
                head_ = null;
            } else {
                Node tail = head_.prev_;
                head_ = head_.next_;
                head_.prev_ = tail;
                tail.next_ = head_;
            }
        }
    }

    /**
     * Main method to demonstrate the functionality of the MyCircle class.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        MyCircle<Integer> circle = new MyCircle<>();

        // Test push_back and toString
        circle.push_back(1);
        circle.push_back(2);
        circle.push_back(3);
        System.out.println("After pushes (should be [1,2,3]): " + circle);

        // Test size
        System.out.println("Size (should be 3): " + circle.size());

        // Test pop_front
        circle.pop_front();
        System.out.println("After pop front (should be [2,3]): " + circle);

        // Test empty
        System.out.println("Is empty? (should be false): " + circle.empty());
        
        // Further pop_front calls for testing...
        circle.pop_front();
        circle.pop_front();
        System.out.println("After popping all elements (should be []): " + circle);
        System.out.println("Is empty? (should be true): " + circle.empty());
    }
}