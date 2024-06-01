package Uebung3;

import java.util.NoSuchElementException;

/** 
 * MySList represents a singly linked list, which holds elements of type integer.
 */
public class MySList implements Iterable<Integer> {
      //-----------------------------------------------------------------//
  //------------- !!Do not change the following lines!! -------------//
  public String toString() {
    if (empty())
      return "[]";
    String rv = "[";
    Node node = head_;
    do {
      rv += node.data_;
      if (node.next_ != null)
        rv += ",";
      node = node.next_;
    } while(node != null);
    rv += "]";
    return rv;
  }

  //-----------------------------------------------------------------//
  //---------------- !!Insert your solution below!! -----------------//

  //----------------------------------------------------------------//
  Node head_ = null;
  
    /**
     * Node represents a single element in the list.
     */
    class Node {
        int data_; // Element value
        Node next_; // Next element in the list

        /**
         * Constructs a new node with given data and next node reference.
         * @param data The integer data to store in this node.
         * @param next The reference to the next node in the list.
         */
        Node(int data, Node next) {
            this.data_ = data;
            this.next_ = next;
        }
    }

    //----------------------------------------------------------------//
    
    /**
     * Constructs an empty singly linked list.
     */
    public MySList() {
        Node head_ = null;
    }

    //----------------------------------------------------------------//

    /**
     * Inserts an element into the singly linked list.
     * @param data The integer element to insert.
     */
    public void insert(int data) {
        if (head_ == null) {
            head_ = new Node(data, null);
        } else {
            Node currentNode = head_;
            while (currentNode.next_ != null) {
                currentNode = currentNode.next_;
            }
            currentNode.next_ = new Node(data, null);
        }
    }

    //----------------------------------------------------------------//

    /**
     * Returns true if the list is empty.
     *
     * @return true if the list contains no elements.
     */
    public boolean empty() {
        return head_ == null;
    }

    //----------------------------------------------------------------//

    /**
     * Iterator is used to traverse odd elements in a list.
     */
    public class Iterator implements java.util.Iterator<Integer> {
        private Node current_;

        /**
         * Constructs an iterator starting with the first odd element of the list.
         */
        public Iterator() {
            this.current_ = head_;
            // Skip to the first odd element
            while (current_ != null && current_.data_ % 2 == 0) {
                current_ = current_.next_;
            }
        }

        /**
         * Returns true if the iteration has more odd elements.
         *
         * @return true if there is another odd element in the iteration.
         */
        @Override
        public boolean hasNext() {
            return current_ != null;
        }

        /**
         * Returns the next odd element in the iteration and advances the iterator.
         *
         * @return the next odd element in the iteration.
         * @throws NoSuchElementException if the iteration has no more elements.
         */
        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more odd elements in the list.");
            }
            int data = current_.data_;
            do {
                current_ = current_.next_;
            } while (current_ != null && current_.data_ % 2 == 0);
            return data;
        }
    }

    //----------------------------------------------------------------//

    /**
     * Returns a new iterator instance that iterates over odd elements in the list.
     *
     * @return A new Iterator instance for this list.
     */
    public Iterator iterator() {
        return new Iterator();
    }

    //----------------------------------------------------------------//

    /**
     * Main method to test MySList functionality.
     *
     * @param args Command line arguments. Not used here.
     */
    public static void main(String[] args) {
        MySList list = new MySList();
        
        // Insert some elements
        list.insert(1);
        list.insert(4);
        list.insert(7);
        list.insert(10);
        list.insert(13);

        // Displaying the list
        System.out.println("The list: " + list);

        // Traversing odd elements using iterator
        Iterator it = list.iterator();
        System.out.print("Odd elements: ");
        while (it.hasNext()) {
            System.out.print(it.next() + " ");
        }
    }
}
