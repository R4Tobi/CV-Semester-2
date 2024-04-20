package Übung2;

/**
 * A class representing a node in a singly linked list
 * 
 * @param <T> The type of data stored in the node
 */
public class LinkedNode<T> {
    private T data_; // The data stored in the node
    private LinkedNode<T> next_; // Reference to the next node

    /**
     * Constructs a new LinkedNode with the given data and null next reference
     * 
     * @param data The data to be stored in the node
     */
    public LinkedNode(T data) {
        this.data_ = data;
        this.next_ = null;
    }

    /**
     * Constructs a new LinkedNode with the given data and next node reference
     * 
     * @param data The data to be stored in the node
     * @param next The reference to the next node
     */
    public LinkedNode(T data, LinkedNode<T> next) {
        this.data_ = data;
        this.next_ = next;
    }

    /**
     * Retrieves the data stored in the node
     * 
     * @return The data stored in the node
     */
    public T getData() {
        return data_;
    }

    /**
     * Sets the data stored in the node
     * 
     * @param data The data to be stored in the node
     */
    public void setData(T data) {
        this.data_ = data;
    }

    /**
     * Retrieves the reference to the next node
     * 
     * @return The reference to the next node
     */
    public LinkedNode<T> getNext() {
        return next_;
    }

    /**
     * Sets the reference to the next node
     * 
     * @param next The reference to the next node
     */
    public void setNext(LinkedNode<T> next) {
        this.next_ = next;
    }

    /**
     * Returns a string representation of the node and its subsequent nodes
     * 
     * @return A string representation of the node and its subsequent nodes
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(data_);
        LinkedNode<T> current = next_;
        while (current != null) {
            sb.append(" -> ");
            sb.append(current.data_);
            current = current.next_;
        }
        return sb.toString();
    }

    /**
     * Main method to test the LinkedNode class
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create nodes for Monday, Thursday, and Friday
        LinkedNode<String> monday = new LinkedNode<>("Monday");
        LinkedNode<String> thursday = new LinkedNode<>("Thursday");
        LinkedNode<String> friday = new LinkedNode<>("Friday");

        // Print initial list
        System.out.println("Initial list:");
        System.out.println(monday);
        System.out.println(thursday);
        System.out.println(friday);

        // Add "Mensa gehen" and "Vorlesung besuchen"
        monday.setNext(new LinkedNode<>("Mensa gehen", thursday));
        thursday.setNext(new LinkedNode<>("Vorlesung besuchen", friday));

        // Print list after adding tasks
        System.out.println("\nList after adding tasks:");
        System.out.println(monday);
        System.out.println(thursday);
        System.out.println(friday);

        // Add more nodes for Tuesday and Wednesday
        LinkedNode<String> tuesday = new LinkedNode<>("Tuesday", thursday);
        monday.setNext(tuesday);
        LinkedNode<String> wednesday = new LinkedNode<>("Wednesday", friday);
        tuesday.setNext(wednesday);

        // Print list after adding Tuesday and Wednesday
        System.out.println("\nList after adding Tuesday and Wednesday:");
        System.out.println(monday);
    }
}