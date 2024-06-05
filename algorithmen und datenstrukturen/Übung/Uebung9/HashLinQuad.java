package Uebung9;

import java.util.Random;

/**
 * This class represents a hash table that uses linear and quadratic probing collision resolution.
 */
public class HashLinQuad {
    // ---------------------------------------------------------------//
    private int[] table_; // array of buckets or slots
    private int size_; // current number of elements
    private int capacity_;

    // Constructor
    /**
     * Constructs a new HashLinQuad object with the specified capacity.
     * 
     * @param capacity the capacity of the hash table
     */
    public HashLinQuad(int capacity) {
        capacity_ = capacity;
        table_ = new int[capacity_];
        size_ = 0;
        for (int i = 0; i < capacity_; i++) {
            table_[i] = -1; // assuming -1 means empty slot
        }
    }

    // ---------------------------------------------------------------//
    /**
     * Adds an element to the hash table using linear probing collision resolution.
     * 
     * @param obj the element to be added
     * @return the number of collisions that occurred during the insertion
     */
    public int addLin(int obj) {
        // linear probing
        int collisions = 0;
        int index = Math.floorMod(obj, capacity_);
        while (table_[index] != -1) {
            collisions++;
            index = Math.floorMod(index + 1, capacity_);
        }
        table_[index] = obj;
        size_++;
        return collisions;
    }

    // ---------------------------------------------------------------//
    
    /**
     * Adds an element to the hash table using quadratic probing collision resolution.
     * 
     * @param obj the element to be added
     * @return the number of collisions that occurred during the insertion
     */
    public int addQuad(int obj) {
        // quadratic probing
        int collisions = 0;
        int index = Math.floorMod(obj, capacity_);
        int i = 1;
        while (table_[index] != -1) {
            collisions++;
            index = Math.floorMod(obj + i * i, capacity_);
            i++;
        }
        table_[index] = obj;
        size_++;
        return collisions;
    }

    // ---------------------------------------------------------------//
    /**
     * Returns a string representation of the hash table.
     * 
     * @return a string representation of the hash table
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < capacity_; i++) {
            sb.append(table_[i]);
            if (i < capacity_ - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // ---------------------------------------------------------------//
    /**
     * The main method creates instances of HashLinQuad, performs linear and quadratic probing tests,
     * and prints the results.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int capacity = 1249;
        HashLinQuad hashTableLin = new HashLinQuad(capacity);

        Random rand = new Random();
        int collisionsLin = 0;
        for (int i = 0; i < 1000; i++) {
            int value = rand.nextInt(2_000_000_000);
            collisionsLin += hashTableLin.addLin(value);
        }

        System.out.println("Hash Table after Linear Probing:");
        //System.out.println(hashTableLin.toString());
        System.out.println("Total Linear Probing Collisions: " + collisionsLin);

        // Reset the table for quadratic probing test
        HashLinQuad hashTableQuad = new HashLinQuad(capacity);
        int collisionsQuad = 0;
        for (int i = 0; i < 1000; i++) {
            int value = rand.nextInt(10000);
            collisionsQuad += hashTableQuad.addQuad(value);
        }

        System.out.println("Hash Table after Quadratic Probing:");
        //System.out.println(hashTableQuad.toString());
        System.out.println("Total Quadratic Probing Collisions: " + collisionsQuad);
    }
}
