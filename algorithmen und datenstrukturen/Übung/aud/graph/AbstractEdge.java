package aud.graph;

import aud.util.GraphvizDecorable;
import aud.util.GraphvizDecorator;

/** Interface to edges of a graph.<p>
    Note that some operations require reimplementation (or throw
    {@code UnsupportedOperationException}.
    @see AbstractGraph
 */
public abstract class AbstractEdge
  implements Comparable<AbstractEdge>, GraphvizDecorable {

  AbstractGraph<? extends AbstractNode,? extends AbstractEdge> graph_ = null;
  AbstractNode  src_ = null;
  AbstractNode  dst_ = null;

  /** Create new edge instance.<p>
      Must initialize any attributes introduced in subclasses
      of {@code AbstractEdge}.
      @return edge instance
   */
  public abstract AbstractEdge create();

  /** get graph */
  public AbstractGraph<? extends AbstractNode,? extends AbstractEdge> graph() {
      return graph_;
  }
  /** Get source node.<p>
      For <em>undirected</em> graphs always
      {@code source.index()<=destination.index()}.
      @see #destination
      @see AbstractNode#index
   */
  public AbstractNode source() { return src_; }
  /** get destination node
      @see #source
  */
  public AbstractNode destination() { return dst_; }

  /** determine if edge weight is defined */
  public boolean hasWeight() { return !Double.isNaN(getWeight()); }
  /** set edge weight
      @return <em>not-a-number</em> (NaN) if undefined (default
      implementation)
   */
  public double getWeight() { return Double.NaN; }
  /** set weight
      @throws UnsupportedOperationException if edges are not weighted
      (@{link #hasWeight} returns {@code false})
   */
  public void setWeight(double w) {
    throw new UnsupportedOperationException("'setWeight' undefined");
  }

  /** get text description or {@code null} if there is none */
  public String getLabel() {
    if (hasWeight()) {
      double w=getWeight();
      if (w==Math.floor(w)) // missing proper sprintf, Java's formatting sucks!
        return ""+((int) getWeight());
      else
        return ""+w;
    }
    else
      return null;

    //return hasWeight() ? ""+getWeight() : (String) null;
  }

  @Override public GraphvizDecorator getDecorator() {
    return graph_.getDecorator();
  }

  @Override public String toString() {
    String text=getLabel();
    return src_.toString()+
      (graph_.isDirected() ? " ->" : " --")+
      (text!=null ? "["+text+"] " : " ")+
      dst_.toString();
  }

  @Override public int compareTo(AbstractEdge other) {
    if (graph_!=other.graph_)
      throw new UnsupportedOperationException();
    int c=src_.compareTo(other.src_);
    return c!=0 ? c : dst_.compareTo(other.dst_);
  }
  @Override public boolean equals(Object other) {
    return compareTo((AbstractEdge) other)==0;
  }
}
