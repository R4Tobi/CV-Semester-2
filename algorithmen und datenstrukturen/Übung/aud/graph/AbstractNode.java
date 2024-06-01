package aud.graph;

import aud.util.GraphvizDecorable;
import aud.util.GraphvizDecorator;

/** Interface to nodes of a graph.<p>
    Note that some operations require reimplementation (or throw
    {@code UnsupportedOperationException}.
    @see AbstractGraph
 */
public abstract class AbstractNode
  implements Comparable<AbstractNode>, GraphvizDecorable {

  AbstractGraph<? extends AbstractNode,? extends AbstractEdge> graph_ = null;
  int index_ = -1;

  /** Create new node instance.<p>
      Must initialize any attributes introduced in subclasses
      of {@code AbstractNode}.
      @return node instance
   */
  public abstract AbstractNode create();

  /** get graph */
  public AbstractGraph<? extends AbstractNode,? extends AbstractEdge> graph() {
    return graph_;
  }
  /** get index, i.e., unique integer id within {@link #graph} */
  public int index() { return index_; }
  /** get text description */
  public String getLabel() { return ""+index_; }

  /** set label (if supported) */
  public void setLabel(String label) {
    throw new UnsupportedOperationException("'setLabel' undefined");
  }


  /** helper for drawing the graph (if supported) */
  public void setPosition(double x,double y) {
    throw new UnsupportedOperationException("'setPosition' undefined");
  }
  /** helper for drawing the graph: return {x,y} as array or {@code null} */
  public double[] getPosition() { return null; }

  @Override public GraphvizDecorator getDecorator() {
    return graph_.getDecorator();
  }

  @Override public String toString() { return getLabel(); }

  @Override public int compareTo(AbstractNode other) {
    if (graph_!=other.graph_)
      throw new UnsupportedOperationException();
    return index_-other.index_;
  }
  @Override public boolean equals(Object other) {
    return compareTo((AbstractNode) other)==0;
  }
}
