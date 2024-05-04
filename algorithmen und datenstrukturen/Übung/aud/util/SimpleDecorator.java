package aud.util;

import java.util.HashSet;

/** Example for a simple decorator.<p>

    The entire state is held in the decorator instance. There is no
    access of/no need for node and edge attributes.
 */
public class SimpleDecorator extends CommonGraphvizDecorator {

  protected GraphvizDecorable hilit_node  = null;
  protected GraphvizDecorable hilit_edge  = null;

  protected HashSet<GraphvizDecorable> marked_nodes = new HashSet<GraphvizDecorable>();
  protected HashSet<GraphvizDecorable> marked_edges = new HashSet<GraphvizDecorable>();

  @Override public void highlightNode(GraphvizDecorable object) {
    hilit_node=object;
  }
  @Override public void highlightEdge(GraphvizDecorable object) {
    hilit_edge=object;
  }

  @Override public void markNode(GraphvizDecorable object) {
    marked_nodes.add(object);
  }
  @Override public void unmarkNode(GraphvizDecorable object) {
    marked_nodes.remove(object);
  }
  @Override public void unmarkAllNodes() {
    marked_nodes.clear();
  }

  @Override public void markEdge(GraphvizDecorable object) {
    marked_edges.add(object);
  }
  @Override public void unmarkEdge(GraphvizDecorable object) {
    marked_edges.remove(object);
  }
  @Override public void unmarkAllEdges() {
    marked_edges.clear();
  }

  @Override public String getNodeDecoration(GraphvizDecorable object) {
    if (object==hilit_node)
      return hilit_node_style;
    else if (marked_nodes.contains(object))
      return marked_node_style;
    else
      return null;
  }

  @Override public String getEdgeDecoration(GraphvizDecorable object) {
    if (object==hilit_edge)
      return hilit_edge_style;
    else if (marked_edges.contains(object))
      return marked_edge_style;
    else
      return null;
  }
}
