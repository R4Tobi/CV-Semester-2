package Uebung10;

import java.util.*;

//---------------------------------------------------------------//
/**
 * This class represents an adjacency matrix and provides methods for analyzing the graph represented by the matrix.
 */
public class AdjMatrix {
    //-------------------------------------------------------------//
    /**
     * Calculates the in-degree of a node in the graph.
     * 
     * @param k The index of the node.
     * @param m The adjacency matrix representing the graph.
     * @return The in-degree of the node.
     */
    public static int inDegree(int k, int[][] m) {
        int count = 0;
        // Iterate through each row of the matrix
        for (int[] ints : m) {
            // If there is an edge pointing to column k, increment the counter
            if (ints[k] == 1) {
                count++;
            }
        }
        return count;
    }

    //-------------------------------------------------------------//
    /**
     * Calculates the out-degree of a node in the graph.
     * 
     * @param k The index of the node.
     * @param m The adjacency matrix representing the graph.
     * @return The out-degree of the node.
     */
    public static int outDegree(int k, int[][] m) {
        int count = 0;
        // Iterate through each column of the row k
        for (int j = 0; j < m[k].length; j++) {
            // If there is an edge going from row k, increment the counter
            if (m[k][j] == 1) {
                count++;
            }
        }
        return count;
    }

    //-------------------------------------------------------------//
    /**
     * Determines the adjacent nodes of a given node in the graph.
     * 
     * @param k The index of the node.
     * @param m The adjacency matrix representing the graph.
     * @return A list of adjacent nodes.
     */
    public static List<Integer> adjacent(int k, int[][] m) {
        List<Integer> adjNodes = new ArrayList<>();
        // Iterate through each column of the row k
        for (int j = 0; j < m[k].length; j++) {
            // If there is an edge from row k to column j, add j to the list
            if (m[k][j] == 1) {
                adjNodes.add(j);
            }
        }
        return adjNodes;
    }

    //-------------------------------------------------------------//
    /**
     * Checks if the graph contains a triangle.
     * 
     * @param m The adjacency matrix representing the graph.
     * @return True if the graph contains a triangle, false otherwise.
     */
    public static boolean hasTriangle(int[][] m) {
        int n = m.length;
        // Iterate through all possible combinations of i, j, and k
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    // Make sure i, j, and k are different
                    if (i != j && j != k && i != k) {
                        // Check if a triangle edge exists (i -> j -> k -> i)
                        if (m[i][j] == 1 && m[j][k] == 1 && m[k][i] == 1) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //-------------------------------------------------------------//
    /**
     * Main method to test the other methods.
     * 
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Example adjacency matrix
        int[][] adjacencyMatrix = {
            {0, 1, 1, 0, 0},
            {1, 0, 0, 1, 1},
            {1, 0, 0, 1, 0},
            {0, 1, 1, 0, 1},
            {0, 1, 0, 1, 0}
        };

        int node = 2;
        // Print the in-degree of node 2
        System.out.println("In-degree of node " + node + ": " + inDegree(node, adjacencyMatrix));
        // Print the out-degree of node 2
        System.out.println("Out-degree of node " + node + ": " + outDegree(node, adjacencyMatrix));
        // Print the adjacent nodes of node 2
        System.out.println("Adjacent nodes of node " + node + ": " + adjacent(node, adjacencyMatrix));
        // Check if the graph contains a triangle
        System.out.println("Does the graph contain a triangle? " + hasTriangle(adjacencyMatrix));
    }
}
