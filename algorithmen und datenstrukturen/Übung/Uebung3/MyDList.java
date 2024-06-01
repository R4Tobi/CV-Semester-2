package Uebung3;

import aud.DList;
import java.util.Iterator;

/**
 * A custom implementation of a doubly linked list that extends the
 * functionality
 * of the aud.DList class.
 *
 * @param <T> The type of elements held in the list.
 */
public class MyDList<T> extends DList<T> {

    /**
     * Constructs an empty MyDList.
     */
    public MyDList() {
        super();
    }

    /**
     * Appends all elements from another MyDList to the end of this list.
     *
     * @param other The MyDList from which elements are to be appended to this list.
     *              Must not be null.
     */
    public void append(MyDList<T> other) {
        // Complexity: O(n), where n is the number of elements in other.
        Iterator<T> it = other.iterator();
        while (it.hasNext()) {
            this.push_back(it.next());
        }
    }

    /**
     * Inserts all elements from another MyDList into this list at the specified
     * index.
     *
     * @param index The index at which to insert the first element from the other
     *              list.
     * @param other The MyDList containing the elements to be inserted into this
     *              list.
     *              Must not be null.
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (index < 0 || index > size()).
     */
    public void insert(int index, MyDList<T> other) {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + this.size());
        }

        // Complexity: O(m+n), where m is the number of elements in this list up to
        // position index,
        // and n is the number of elements in other.
        Iterator<T> itThis = this.iterator();
        int counter = 0;

        // Move the iterator to the insertion point.
        while (counter < index && itThis.hasNext()) {
            itThis.next();
            counter++;
        }

        Iterator<T> itOther = other.iterator();
        while (itOther.hasNext()) {
            T element = itOther.next();
            if (counter == index) {
                // Insert before the current position via a convenience method or similar
                // approach.
                this.insert(index++, element);
            } else {
                this.push_back(element);
            }
        }
    }

    // Add Javadoc for main method as well if needed

    /**
     * Demonstrates the usage of MyDList with some basic operations.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // TODO: test your code with appropriate examples

        MyDList<Integer> list1 = new MyDList<>();
        MyDList<Integer> list2 = new MyDList<>();

        // Populate lists with some example data
        list1.push_back(1);
        list1.push_back(2);
        list2.push_back(3);
        list2.push_back(4);

        // Testing appending list2 to list1
        list1.append(list2); // list1 should now contain [1, 2, 3, 4], list2 remains unchanged
        System.out.println("After append: " + list1);

        // Resetting list1 for next test
        list1.clear();

        list1.push_back(1);
        list1.push_back(2);

        // Testing inserting list2 into list1 at position 1
        list1.insert(1, list2); // list1 should now contain [1, 3, 4, 2], list2 remains unchanged
        System.out.println("After insert: " + list1);
    }
}
