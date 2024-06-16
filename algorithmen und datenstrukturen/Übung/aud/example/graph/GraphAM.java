package aud.graph;

import aud.Vector;
import java.util.Iterator;
import java.util.TreeSet;

/** Graph implementation based on adjacency matrix.<p>

    The implementation uses a sparse {@link AdjacencyMatrix}

    @see AdjacencyMatrix
 */
public class GraphAM<Node extends AbstractNode,Edge extends AbstractEdge>
  extends AbstractGraph<Node,Edge>
  implements Iterable<Node> {

  AdjacencyMatrix<Edge> adj_         = null;
  TreeSet<Node>       nodes_         = null;

  /** Create graph.
      @param nodeGenerator same as for {@link AbstractGraph#AbstractGraph}
      @param edgeGenerator same as for {@link AbstractGraph#AbstractGraph}
      @param directed {@code true} for creating a directed graph,
      {@code false} for an undirected graph
      (<em>symmetric</em> {@link AdjacencyMatrix})
   */
  public GraphAM(Node nodeGenerator,Edge edgeGenerator,
		 boolean directed) {
    super(nodeGenerator,edgeGenerator);

    adj_=new AdjacencyMatrix<Edge>(!directed);
    nodes_=new TreeSet<Node>();
  }

  @Override public boolean isDirected() {
    return !adj_.isSymmetricMatrix();
  }

  @SuppressWarnings("unchecked")
  @Override public Node addNode() {
    Node node=(Node) nodeGenerator_.create();
    node.graph_=this;
    node.index_=nodes_.size();
    nodes_.add(node);
    return node;
  }

  @SuppressWarnings("unchecked")
  @Override public Edge addEdge(Node source,Node destination) {
    Edge edge=getEdge(check(source),check(destination));

    if (edge!=null)
      throw new RuntimeException("edge "+source.index()+
				 (isDirected() ? "->" : "--")+
				 destination.index()+" exists");

    edge=(Edge) edgeGenerator_.create();
    edge.graph_=this;
    if (!isDirected() && source.index_>destination.index_)   {
      edge.dst_=source;
      edge.src_=destination;
    }
    else {
      edge.src_=source;
      edge.dst_=destination;
    }

    adj_.set(source.index_,destination.index_,edge);

    return edge;
  }

  @Override public int getNumNodes() { return nodes_.size(); }
  @Override public Node getSomeNode() { return nodes_.first(); }

  @Override public Iterator<Node> iterator() {
    return (Iterator<Node>) nodes_.iterator();
  }

  @Override public Edge getEdge(Node source,Node destination) {
    return adj_.get(source.index_,destination.index_);
  }

  @Override protected Node check(Node node) {
    super.check(node); // additional assertion
    assert(nodes_.contains(node));
    return node;
  }
  @Override protected Edge check(Edge edge) {
    super.check(edge);  // additional assertion
    assert(adj_.get(edge.src_.index_,edge.dst_.index_)==edge);
    return edge;
  }

  @Override public Vector<Edge> getInEdges(Node node) {
    return adj_.getColumnEntries(check(node).index_);
  }
  @Override public Vector<Edge> getOutEdges(Node node) {
    return adj_.getRowEntries(check(node).index_);
  }

  @Override public void removeNode(Node node) {
    adj_.clearColumnAndRow(check(node).index_);
    boolean removed=nodes_.remove(node);
    assert(removed);
  }

  @Override public void removeEdge(Edge edge) {
    check(edge);
    adj_.set(edge.src_.index_,edge.dst_.index_,null);
  }

  @Override public Iterator<Edge> getEdgeIterator() {
    return adj_.iterator();
  }
}
