package aud;

import aud.util.Graphvizable;

/** Simple implementation of B-trees based on {@link KTreeNode}.

    Similar comments as for {@link A234Tree} apply.
 */
public class BTree<Key extends Comparable<Key>> implements Graphvizable {

  KTreeNode<Key> root_;
  int            m_;

  /** create an empty tree of order 2*m+1 */
  public BTree(int m) {
    root_=new KTreeNode<Key>();
    m_=m;
    assert(m_>0);
  }

  /** get root node (required for assignment) */
  public  KTreeNode<Key> root() { return root_; }

  /** get order of tree (maximum number of children) */
  public int getOrder() { return 2*m_+1; }

  /** find key in tree
     @return found key, note that this is generally a <em>different</em>
     instance than {@code key}! (We have "only" {@code compareKeys()==0}.)
   */
  public Key find(Key key) {
    return root_.find(key);
  }

  /** insert entry
     @return {@code true} if {@code key} was not an entry of child before
   */
  public boolean insert(Key key) {
    return insert(root_,key);
  }

  /** Split node on overflow and merge with parent.
     <ul>
     <li>I'm sort of cheating here: I let a node eventually become a
     {@code 2*m_+2}-node and split only then. Implementation is somewhat
     simpler.</li>
     <li>This may lead to a recursive split up to the root.</li>
     <li>The method has <em>no effect</em> on non-full nodes.</li>
     <li>{@code split} calls {@link #onSplit} <em>before</em> the split.</li>
     </ul>
   */
  protected void split(KTreeNode<Key> node) {
    if (node.getK()>2*m_+1) { // CHEATING: we created and split a (2*m+2)-node!

      onSplit(node);

      node.split(m_+1);

      if (node.parent_!=null) {
        int i=node.parent_.getIndexOfChild(node);
        assert (i>=0);
        node.parent_.mergeChild(i);
        node.parent_.checkConsistency();

        split(node.parent_); // eventually split parent
      }
    }
  }

  /** callback invoked by {@link #split}, default implementation is empty */
  protected void onSplit(KTreeNode<Key> node) {
  }

  /** recursive find/insert (similar to {@link KTreeNode#find}) */
  protected boolean insert(KTreeNode<Key> node, Key key) {
    // recursive find/insert (similar to KTreeNode#find)
    for (int i=1; i<node.getK(); ++i) {
      int cmp=node.compareKeys(key,node.getKey(i));
      if (cmp==0)
        return false; // key exists in tree
      else if (cmp<0) {
        if (node.getChild(i-1)==null) {

          node.insert(key,i-1);
          split(node);
          return true;

        } else
          return insert(node.getChild(i-1),key); // recursive find
      }
    }

    KTreeNode<Key> right=node.getChild(node.getK()-1);
    if (right!=null)
      return insert(right,key);                 // recursive find

    node.insert(key,node.getK()-1);
    split(node);

    return true;
  }

  @Override
  public String toDot() {
    return root_.toDot();
  }

  /** get TikZ code for LaTeX export (calls {@link KTreeNode#toTikZ})
   */
  public String toTikZ() {
    return root_.toTikZ();
  }

  @Override public String toString() {
    return root_.toString();
  }

  /** few consistency checkschild.entries_.at(i).child
     @throws RuntimeException on error (and/or assertion fails)
   */
  public void checkConsistency() {
    // missing: check particular properties of 2-3-4-tree!
    root_.checkConsistency();
  }

  /** example and test */
  public static void main(String[] args) {
    BTree<Integer> tree=new BTree<Integer>(1);

    int data[]={1,5,2,6,7,4,8,3};

    for (int i : data) {
      tree.insert(i);
      System.out.println(tree.toTikZ()+"\n");
    }

    aud.util.DotViewer.displayWindow(tree,"B-tree");
  }
}
