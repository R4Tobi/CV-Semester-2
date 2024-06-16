# Blatt 10

## Aufgabe 1

### Was ist ein Graph

Ein Graph ist eine mathematische Struktur, die verwendet wird, um paarweise Beziehungen zwischen Objekten darzustellen.
Er besteht aus Knoten (oder Ecken) und Kanten, die die Verbindungen zwischen den Knoten darstellen.
Graphen werden in vielen Bereichen verwendet, darunter Informatik, Mathematik, Physik und Sozialwissenschaften, um Netzwerke, Verbindungen und Strukturen zu modellieren.

### Arten von Graphen

1. **Ungerichtete Graphen**
2. **Gerichtete Graphen**
3. **Gewichtete Graphen**
4. **Ungewichtete Graphen**
5. **Zusammenhängende Graphen**
6. **nicht Zusammenhängende Graphen**
7. **Zyklenfreihe Graphen**

## Aufgabe 2

B = (V, E) mit V = {1,2,3,4,5} und E = \{{1, 2} ,{1, 3}, {1, 4}, {1, 5}, {2, 4}, {2, 5}, {3, 4}, {4, 5}}

## Aufgabe 3

\[
\begin{array}{c}
\{1, 2\} \\
\{1, 3\} \\
\{1, 4\} \\
\{1, 5\} \\
\{2, 4\} \\
\{2, 5\} \\
\{3, 4\} \\
\{4, 5\} \\
\end{array}
\]

### Knotenliste:

\[
\{1, 2, 3, 4, 5\}
\]

### Adjazenzmatrix:

\[
\begin{array}{c|ccccc}
  & 1 & 2 & 3 & 4 & 5 \\
\hline
1 & 0 & 1 & 1 & 1 & 1 \\
2 & 1 & 0 & 0 & 1 & 1 \\
3 & 1 & 0 & 0 & 1 & 0 \\
4 & 1 & 1 & 1 & 0 & 1 \\
5 & 1 & 1 & 0 & 1 & 0 \\
\end{array}
\]

### Adjazenzliste:

\[
\begin{array}{c|c}
1 & \{2, 3, 4, 5\} \\
2 & \{1, 4, 5\} \\
3 & \{1, 4\} \\
4 & \{1, 2, 3, 5\} \\
5 & \{1, 2, 4\} \\
\end{array}
\]

Die entsprechenden Datenstrukturen in Java:

\- Kantenliste:

```java
import java.util.ArrayList;
import java.util.List;

class Edge {
    int source, destination;

    public Edge(int source, int destination) {
        this.source = source;
        this.destination = destination;
    }
}

public class Graph {
    List<Edge> edges;

    public Graph() {
        edges = new ArrayList<>();
    }

    public void addEdge(int source, int destination) {
        edges.add(new Edge(source, destination));
    }
}
```

\- Knotenliste:

```java
import java.util.HashSet;
import java.util.Set;

public class Graph {
    Set<Integer> nodes;

    public Graph() {
        nodes = new HashSet<>();
    }

    public void addNode(int node) {
        nodes.add(node);
    }
}
```

\- Adjanzenzmatrix:

```java
public class Graph {
    int[][] adjacencyMatrix;
    int numNodes;

    public Graph(int numNodes) {
        this.numNodes = numNodes;
        adjacencyMatrix = new int[numNodes][numNodes];
    }

    public void addEdge(int source, int destination) {
        adjacencyMatrix[source][destination] = 1;
        adjacencyMatrix[destination][source] = 1; // für ungerichtete Graphen
    }
}
```

\- Adjazenzliste:

```java
import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<List<Integer>> adjacencyList;

    public Graph(int numNodes) {
        adjacencyList = new ArrayList<>(numNodes);
        for (int i = 0; i < numNodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source); // für ungerichtete Graphen
    }
}
```

## Aufgabe 4

### Kantenliste in Adjazenzmatrix (java)

```java
public class Graph {
    int[][] adjacencyMatrix;
    int numNodes;

    public Graph(int numNodes, List<Edge> edges) {
        this.numNodes = numNodes;
        adjacencyMatrix = new int[numNodes][numNodes];

        for (Edge edge : edges) {
            addEdge(edge.source, edge.destination);
        }
    }

    public void addEdge(int source, int destination) {
        adjacencyMatrix[source][destination] = 1;
        adjacencyMatrix[destination][source] = 1; // für ungerichtete Graphen
    }

    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1));
        edges.add(new Edge(0, 2));
        edges.add(new Edge(0, 3));
        edges.add(new Edge(0, 4));
        edges.add(new Edge(1, 3));
        edges.add(new Edge(1, 4));
        edges.add(new Edge(2, 3));
        edges.add(new Edge(3, 4));

        Graph graph = new Graph(5, edges);
    }
}
```

### Kantenliste in Adjazenzliste (java)

```java
import java.util.ArrayList;
import java.util.List;

class Edge {
    int source, destination;

    public Edge(int source, int destination) {
        this.source = source;
        this.destination = destination;
    }
}

public class Graph {
    List<List<Integer>> adjacencyList;

    public Graph(int numNodes, List<Edge> edges) {
        adjacencyList = new ArrayList<>(numNodes);
        for (int i = 0; i < numNodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (Edge edge : edges) {
            addEdge(edge.source, edge.destination);
        }
    }

    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source); // für ungerichtete Graphen
    }

    public static void main(String[] args) {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0, 1));
        edges.add(new Edge(0, 2));
        edges.add(new Edge(0, 3));
        edges.add(new Edge(0, 4));
        edges.add(new Edge(1, 3));
        edges.add(new Edge(1, 4));
        edges.add(new Edge(2, 3));
        edges.add(new Edge(3, 4));

        Graph graph = new Graph(5, edges);
    }
}
```

### O-Notation der Algorithmen

1. **Kantenliste in Adjazenzmatrix**: O(n), wobei n die Anzahl der Kanten darstellt. Linearer Aufwan, weil jede Kante einmal durchlaufen werden muss.
2. **Kantenliste in Adjazenzliste**: ebenfalls O(n), weil Kante einmal durchlaufen wird, um sie der Adjazenzliste hinzuzufügen.
