package Uebung10;

import aud.example.graph.*;
import java.util.*;

public class GraphSearchDemo {

  //----------------------------------------------------------------//
  public static MyGraph createGraph() {
    MyGraph graph = new MyGraph();

    // Knoten hinzufügen
    MyNode node1 = graph.addNode("1");
    MyNode node2 = graph.addNode("2");
    MyNode node3 = graph.addNode("3");
    MyNode node4 = graph.addNode("4");
    MyNode node5 = graph.addNode("5");
    MyNode node6 = graph.addNode("6");
    MyNode node7 = graph.addNode("7");
    MyNode node8 = graph.addNode("8");

    // Kanten hinzufügen
    graph.addEdge(node1, node2);
    graph.addEdge(node1, node6);
    graph.addEdge(node1, node7);
    graph.addEdge(node2, node4);
    graph.addEdge(node2, node8);
    graph.addEdge(node3, node8);
    graph.addEdge(node4, node6);
    graph.addEdge(node5, node8);
    graph.addEdge(node8, node3);
    graph.addEdge(node8, node5);
    graph.addEdge(node8, node2);
    graph.addEdge(node8, node1);

    return graph;
  }

  //----------------------------------------------------------------//
  public static void bfs(MyGraph graph, MyNode startNode) {
    Queue<MyNode> queue = new LinkedList<>();
    Set<MyNode> visited = new HashSet<>();

    queue.add(startNode);
    visited.add(startNode);

    while (!queue.isEmpty()) {
      MyNode currentNode = queue.poll();
      System.out.print(currentNode.getLabel() + " ");

      for (MyNode neighbor : graph.getNeighbors(currentNode)) {
        if (!visited.contains(neighbor)) {
          queue.add(neighbor);
          visited.add(neighbor);
        }
      }
    }
  }

  //----------------------------------------------------------------//
  public static void dfs(MyGraph graph, MyNode startNode) {
    Stack<MyNode> stack = new Stack<>();
    Set<MyNode> visited = new HashSet<>();

    stack.push(startNode);
    visited.add(startNode);

    while (!stack.isEmpty()) {
      MyNode currentNode = stack.pop();
      System.out.print(currentNode.getLabel() + " ");

      for (MyNode neighbor : graph.getNeighbors(currentNode)) {
        if (!visited.contains(neighbor)) {
          stack.push(neighbor);
          visited.add(neighbor);
        }
      }
    }
  }

  //----------------------------------------------------------------//
  public static void main(String[] args) {
    MyGraph graph = createGraph();

    MyNode startNode = graph.getNode("8");

    System.out.print("Breitensuche (BFS) ab Knoten 8: ");
    bfs(graph, startNode);
    System.out.println();

    System.out.print("Tiefensuche (DFS) ab Knoten 8: ");
    dfs(graph, startNode);
    System.out.println();
  }
}
