package aud.graph;

/** plain simple edge */
public class SimpleEdge extends AbstractEdge {

  String label_ = null;
  double weight_ = Double.NaN;

  @Override public SimpleEdge create() {
    return new SimpleEdge();
  }
  @Override public String getLabel() {
    return label_==null ? super.getLabel() : label_;
  }

  /** set label (default label if {@code label==null}) */
  public void setLabel(String label) { label_=label; }

  @Override public double getWeight() { return weight_; }
  @Override public void setWeight(double w) { weight_=w; }
}
