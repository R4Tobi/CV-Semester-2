package Uebung9;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * MyHash class represents a simple hash table implementation.
 */
public class MyHash {
    public BucketArray e_;  // Bucket array to store hash table entries.
    int size_;              // Capacity of the hash table.

    /**
     * Constructor to initialize the hash table with a given size.
     *
     * @param size the size of the hash table.
     */
    public MyHash(int size) {
        this.size_ = size;
        e_ = new BucketArray(size);
    }

    /**
     * Simple hash function to compute the index for a given string.
     *
     * @param s the string to hash.
     * @return the computed hash value.
     */
    private int hash(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = (31 * hash + s.charAt(i)) % size_;
        }
        return hash;
    }

    /**
     * Inserts a string into the hash table using linear probing for collision resolution.
     *
     * @param s the string to insert.
     */
    public void insert(String s) {
        int index = hash(s);
        while (!e_.insert(index, s)) {
            index = (index + 1) % size_;
        }
    }

    /**
     * Main method to demonstrate the usage of MyHash class.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        int size = 1249;  // Example size for the hash table.
        MyHash hash = new MyHash(size);
        try {
            DataInput s = new DataInputStream(new FileInputStream("C:\\Users\\tobia\\Documents\\GitHub\\CV-Semester-2\\algorithmen und datenstrukturen\\Übung\\Uebung9\\ww1.txt"));
            // Reading the file line by line and inserting into the hash table.
            String line;
            while ((line = s.readLine()) != null) {
                hash.insert(line);
            }
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(-1);
        }
        System.out.println(hash.e_.toString());
        System.out.println("Collisions: " + hash.e_.getCollisions() + "\n");
    }

    /**
     * BucketArray class represents the array of buckets used in the hash table.
     */
    class BucketArray {
        protected String[] arr;  // Array to store hash table entries.
        protected int col;       // Counter for collisions.

        /**
         * Constructor to initialize the bucket array with a given size.
         *
         * @param size the size of the bucket array.
         */
        public BucketArray(int size) {
            col = 0;
            arr = new String[size];
        }

        /**
         * Inserts a string into the bucket array at a given position.
         *
         * @param pos the position to insert the string.
         * @param s the string to insert.
         * @return true if the insertion is successful, false if a collision occurs.
         */
        public boolean insert(int pos, String s) {
            if (arr[pos] == null) {
                arr[pos] = s;
                return true;
            }
            col++;
            return false;
        }

        /**
         * Gets the number of collisions that have occurred during insertions.
         *
         * @return the number of collisions.
         */
        public int getCollisions() {
            return col;
        }

        /**
         * Returns a string representation of the bucket array.
         *
         * @return the string representation of the bucket array.
         */
        public String toString() {
            StringBuilder res = new StringBuilder();
            for (String s : arr) {
                res.append(s).append("\n");
            }
            return res.toString();
        }
    }
}
