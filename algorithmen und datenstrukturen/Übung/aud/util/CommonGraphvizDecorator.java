package aud.util;

/** Interface for simple decorator with preset properties<p>

    Defines styles and allows for

    <ul>

    <li><em>marking sets</em> of nodes/edges
    (e.g., {@link #markNode})</li>

    <li> <em>highlighting one particular</em> node/edge
    (e.g., {@link #highlightNode})</li>

    </ul>
    @see SimpleDecorator
    @see GraphvizDecorable
    @see Graphvizable
*/
public abstract class CommonGraphvizDecorator extends GraphvizDecorator {

  protected String all_nodes_style    = "shape=circle";
  protected String all_edges_style    = null;
  protected String global_style       = null; //"fontname=\"Helvetica\"";
  protected String graph_style        = "fontsize=24";
  protected String graph_label        = null;

  protected String hilit_node_style   = "style=filled,fillcolor=yellow,penwidth=2";
  protected String hilit_edge_style   = "color=red,penwidth=2";

  protected String marked_node_style   = "style=filled,fillcolor=lightgray,penwidth=1.5";
  protected String marked_edge_style   = "color=blue,penwidth=1.5";

  /** Set highlighted node.<p>
      There is exactly one or no such node.
      @param object node or {@code null} (no highlight)
   */
  public abstract void highlightNode(GraphvizDecorable object);
  /** Set highlighted edge.
      @see #highlightNode
  */
  public abstract void highlightEdge(GraphvizDecorable object);

  /* mark node {@code object} */
  public abstract void markNode(GraphvizDecorable object);
  /** unmark node {@code object} */
  public abstract void unmarkNode(GraphvizDecorable object);
  /** unmark all nodes */
  public abstract void unmarkAllNodes();

  /* mark edge {@code object} */
  public abstract void markEdge(GraphvizDecorable object);
  /** unmark edge {@code object} */
  public abstract void unmarkEdge(GraphvizDecorable object);
  /** unmark all edges */
  public abstract void unmarkAllEdges();

  /** get label */
  public String getGraphLabel() { return graph_label; }
  /** set label */
  public void setGraphLabel(String text) { graph_label=text; }

  /** Clear all decorations.<p>
      Calls {@link #unmarkAllNodes} and {@link #unmarkAllEdges} and
      unsets highlighting, unsets label.
   */
  public void clear() {
    unmarkAllNodes();
    unmarkAllEdges();
    highlightNode(null);
    highlightEdge(null);
    setGraphLabel(null);
  }

  @Override public String getGraphDecoration(GraphvizDecorable object) {
    if (graph_label!=null)
      return "label=\""+graph_label+"\""+
	(graph_style!=null ? ","+graph_style :"");
    else
      return graph_style;
  }

  @Override public String getAllNodesDecoration() { return all_nodes_style; }

  @Override public String getAllEdgesDecoration() { return all_edges_style; }

  @Override public String getGlobalStyle() { return global_style; }
}
