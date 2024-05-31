package aud.graph;

import aud.Vector;
import aud.util.Graphvizable;
import aud.util.GraphvizDecorable;
import aud.util.GraphvizDecorator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Interface to a graph.
    <p>

    Methods throw an {@code IllegalArgumentException} if a given
    node or edge is invalid, i.e., it equals {@code null}, or it is
    not part of this graph.

    @param <Node> represents a node
    @param <Edge> represents an e
    dge
 */
public abstract class
  AbstractGraph<Node extends AbstractNode,Edge extends AbstractEdge>
  implements Iterable<Node>,
	     Graphvizable, GraphvizDecorable {

  Node                nodeGenerator_ = null;
  Edge                edgeGenerator_ = null;

  /** Constructor.
      <p>
      The constructor takes two objects that serve as "templates"
      for creating new Node ({@link AbstractNode#create}) and Edge
      ({@link AbstractEdge#create}) instances.
      <p>

      <em>Note:</em> The instances are required due to the way how
      Java generics work (see <a
      href="http://docs.oracle.com/javase/tutorial/java/generics/erasure.html">type
      erasure</a>). This is different (indeed much simpler but weaker)
      than, e.g., C++ templates.

      @param nodeGenerator "template" for creating Node instances
      @param edgeGenerator "template" for creating Edge instances
   */
  public AbstractGraph(Node nodeGenerator,Edge edgeGenerator) {
    nodeGenerator_=nodeGenerator;
    edgeGenerator_=edgeGenerator;
  }

  /** Is graph directed? */
  public abstract boolean isDirected();

  /** create and add new node
      @return node instance
   */
  public abstract Node addNode();

  /** Create and add new edge from {@code source} to {@code
      destination}.

      <ul>

      <li>For <em>undirected</em> graphs, {@code source} and
      {@code destination} may be swapped such that <em>always</em>
      {@code source.index()<=destination.index()}.

      <li> {@code RuntimeException} is thrown if the edge already
      exists.</li>

      </ul>
      @param source source node
      @param destination destination node
      @return edge instance
      @throws IllegalArgumentException
      @throws RuntimeException
      @see AbstractNode#index
   */
  public abstract Edge addEdge(Node source,Node destination);

  /** Remove node and all its incident and excident edges.
      @throws IllegalArgumentException
   */

  public abstract void removeNode(Node node);
  /** Remove edge.
      @throws IllegalArgumentException
   */
  public abstract void removeEdge(Edge edge);

  /** get number of nodes */
  public abstract int getNumNodes();

  /** Get some node.<p>
      This may be <em>any</em> node of the graph.
      @return node
      @throws NoSuchElementException (for empty graph)
   */
  public abstract Node getSomeNode();

  /** Get edge from {@code source} to {@code destination}.
      <p>
      Order of arguments is not relevant for <em>undirected</em>
      graphs.
      @throws IllegalArgumentException
      @return edge or {@code null} if there is no such edge
   */
  public abstract Edge getEdge(Node source,Node destination);

  /** Same as {@link #getEdge} but throw if there is no such edge.
      @throws RuntimeException if there is no such edge
      @throws IllegalArgumentException
      @return edge
   */
  public Edge ensureEdge(Node source,Node destination) {
    Edge edge=getEdge(source,destination);
    if (edge==null)
      throw new RuntimeException("no such edge "+source.index()+
				 (isDirected() ? "->" : "--")+
				 destination.index());
    return edge;
  }

  /** Get incident edges of node.<p>
      For undirected graphs same as {@link #getOutEdges}.
      @throws IllegalArgumentException
      @return vector of edges
   */

  public abstract Vector<Edge> getInEdges(Node node);
  /** Get incident edges of node.
      For undirected graphs same as {@link #getInEdges}.
      @throws IllegalArgumentException
      @return vector of edges
  */
  public abstract Vector<Edge> getOutEdges(Node node);

  /** get number of edges incident to node */
  public int getInDegree(Node node) { return getInEdges(node).size(); }

  /** get number of edges emanating from node */
  public int getOutDegree(Node node) { return getOutEdges(node).size(); }
  /** Get total degree.
      <ul>
      <li>sum of {@link #getInEdges} and {@link #getOutEdges} for
      <em>directed</em> graphs {@link #isDirected}</li>
      <li> {@link #getInEdges} (equals {@link #getOutEdges}} for
      <em>undirected</em> graphs
      </ul>
   */
  public int getDegree(Node node) {
    if (isDirected())
      return getInDegree(node)+getOutDegree(node);
    else
      return getInDegree(node);
  }

  /** Get iterator over all edges.<p>
      Edges are traversed in <em>arbitrary order</em>.
   */
  public abstract Iterator<Edge> getEdgeIterator();

  /** helper: check if node is valid
      @return node
      @throws IllegalArgumentException
   */
  protected Node check(Node node) {
    if (node==null)
      throw new IllegalArgumentException("null node");
    if (node.graph_!=this)
      throw new IllegalArgumentException("node is bound to other graph");

    return node;
  }
  /** helper: check if edge is valid
      @return node
      @throws IllegalArgumentException
   */
  protected Edge check(Edge edge) {
    if (edge==null)
      throw new IllegalArgumentException("null edge");
    if (edge.graph_!=this)
      throw new IllegalArgumentException("edge is bound to other graph");

    return edge;
  }

  /** Defines iterator over all edges of an @{link AbstractGraph}.<p>
      @see AbstractGraph#edges
  */
  public class Edges implements Iterable<Edge> {
    AbstractGraph<Node,Edge> graph_;
    Edges(AbstractGraph<Node,Edge> graph) { graph_=graph; }
    @Override public Iterator<Edge> iterator() {
      return (Iterator<Edge>) graph_.getEdgeIterator();
    }
  }

  /** Get Edges instance to obtain iterator.
      Provided for convenience.<p>
      Use as {@code for (edge : graph.edges()) { ... }}
   */
  public Edges edges() { return new Edges(this); }

  @Override public String toString() {
    String rv="nodes:\n";
    for (Node node : this)
      rv+=" "+node.toString()+"\n";
    rv+="edges:\n";
    for (Edge edge : this.edges())
      rv+=" "+edge.toString()+"\n";
    return rv;
  }

  @Override public GraphvizDecorator getDecorator() {
    return null;
  }

  @Override public String toDot() {
    String edgeOp=(isDirected() ? " -> " : " -- ");
    String dot=(isDirected() ? "digraph " : "graph ")+
      "AbstractGraph {\n";

    GraphvizDecorator decorator=getDecorator();
    if (decorator!=null) {
      String d=decorator.getGlobalStyle();
      if (d!=null) dot+=" "+d+";\n";
      d=decorator.getGraphDecoration(this);
      if (d!=null) dot+=" graph ["+d+"];\n";
    }

    for (Node node : this) {
      decorator=node.getDecorator();
      dot+=" \""+node.index()+"\" [label=\""+node.getLabel()+"\",";
      dot+=(decorator!=null) ? decorator.getFullNodeDecoration(node) : "shape=circle";

      double[] p=node.getPosition();
      if (p!=null) {
        dot+=",pos=\""+p[0]+","+p[1]+"\",pin=true,";
      }

      dot+="];\n";
    }

    dot+="\n\n";

    for (Edge edge : this.edges()) {
      decorator=edge.getDecorator();
      dot+=" \""+edge.source().index()+"\""+edgeOp+
           "\""+edge.destination().index()+"\" ";
      String label=edge.getLabel();
      dot+=(label!=null) ? "[label=\""+label+"\"," : "[";
      dot+=edge.hasWeight() ? "weight="+edge.getWeight()+"," : "";
      dot+=edge.hasWeight() ? "len="+edge.getWeight()+"," : "";
      dot+=(decorator!=null) ? decorator.getFullEdgeDecoration(edge) : "";
      dot+="];\n";
    }

    dot+="\n}\n";
    //System.out.println(dot);
    return dot;
  }
}
