package aud.graph;

/** plain simple node*/
public class SimpleNode extends AbstractNode {

  String label_       = null;
  double[]  position_ = null;

  @Override public SimpleNode create() {
    return new SimpleNode();
  }
  @Override public String getLabel() {
      return label_==null ? super.getLabel() : label_;
  }
  /** set label (default label if {@code label==null}) */
  public void setLabel(String label) { label_=label; }

  @Override public void setPosition(double x,double y) {
    position_=new double[] {x,y};
  }
  /** helper for drawing the graph: return {x,y} as array or {@code null} */
  public double[] getPosition() { return position_; }
}
