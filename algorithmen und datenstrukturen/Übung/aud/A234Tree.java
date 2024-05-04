package aud;

import aud.util.Graphvizable;

/** Simple implementation of 2-3-4-trees based on {@link KTreeNode}.

   Note that in contrast to {@link BinarySearchTree} ...
   <ul>
   <li>we store only keys rather than key-value pairs.</li>
   <li>there is no dummy node {@code head}, there is no reference to parents.</li>
   </ul>

   The class implements insertion with <em>top-down</em> and
   <em>bottom-up</em> splits. The strategy for splitting is selected
   at construction.

   <ul>
   <li>In <em>top-down</em> mode, {@link insert} calls the recursive
   {@link #insert_top_down}, which splits all 4-nodes (using
   {@link split_top_down}) on the way down to the insertion position.
   (This may split the root!) So the final {@link KTreeNode#insert} is
   guaranteed to not cause a split.</li>
   <li>In <em>bottom-up</em> mode, {@link insert} calls the recursive
   {@link #insert_bottom_up}, which inserts first and splits if the
   new node is a 5-node (<em>recursive</em> {@link split_bottom_up}).<br>
   Note that this is sort of <b>cheating</b> for ease of
   implementation because in a proper 2-3-4-tree, there cannot be
   5-nodes! (We use {@link KTreeNode}.) This has the <em>side
   effect</em>, that the selection of the node that is "pulled up" to
   the parent by {@link KTreeNode#mergeChild}) possibly has to be
   "corrected" if the insertion position was less than 2.</li></ul>

   <p>

   <em>Note that there is currently no operation for removal. (This would cause
    {@link KTreeNode#mergeChild}.)</em>
 */
public class A234Tree<Key extends Comparable<Key>> implements Graphvizable {

  KTreeNode<Key> root_;
  boolean        top_down_ = true;

  /** create an empty 2-3-4-tree */
  public A234Tree() {
    root_=new KTreeNode<Key>();
  }
  /** create an empty 2-3-4-tree and select insertion strategy */
  public A234Tree(boolean top_down) {
    this();
    top_down_=top_down;
  }


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
    if (top_down_)
      return insert_top_down(root_,key);
    else
      return insert_bottom_up(root_,key);
  }

  /** Split 5-node and merges with parent.
     <ul>
     <li>I'm sort of cheating here: I let a node eventually become a 5-node
     and split only then. Implementation is simpler, but it's not (storage)
     efficient!</li>
     <li>The {@code pos} argument stores the insertion position
     into the 5-node before splitting. It is used to correctly determine the
     index of the element that is "pulled up".</li>
     <li>This may lead to a recursive split up to the root.</li>
     <li>The method has <em>no effect</em> on 2- and 3-nodes.</li>
     <li>{@code split} calls {@link #onSplit} <em>before</em> the split.</li>
     </ul>
   */
  protected void split_bottom_up(KTreeNode<Key> node, int pos) {
    if (node.getK()>4) { // CHEATING: we created and split a 5-node!

      onSplit(node);

      node.split(2 + (pos<2 ? 1 : 0));

      if (node.parent_!=null) {
        int i=node.parent_.getIndexOfChild(node);
        assert (i>=0);
        node.parent_.mergeChild(i);
        node.parent_.checkConsistency();

        split_bottom_up(node.parent_,i); // eventually split parent
      }
    }
  }

  /** Split 4-node and merges with parent.
     <ul>
     <li>This method is called on {@link #insert} while traversing top-down.</li>
     <li>{@code split} calls {@link #onSplit} <em>before</em> the split.</li>
     </ul>
     @return {@code node} (no split) or its parent (on slit), such that
     insertion "continues" at returned node
   */
  protected KTreeNode<Key> split_top_down(KTreeNode<Key> node) {
    if (node.getK()>3) {

      onSplit(node);

      node.split(2);

      if (node.parent_!=null) {
        int i=node.parent_.getIndexOfChild(node);
        assert (i>=0);
        node.parent_.mergeChild(i);
        node.parent_.checkConsistency();

        assert(node.parent_.getK()<=4) :
        "parent was not a 4-node (top-down split)";

        return node.parent_; // split: continue inserting at parent
      }
    }
    return node;             // no split: just continue
  }

  /** callback invoked by {@link split_bottom_up} or {@link split_top_down},
      default implementation is empty
  */
  protected void onSplit(KTreeNode<Key> node) {
  }

  protected boolean insert_bottom_up(KTreeNode<Key> node, Key key) {
    // recursive find/insert (similar to KTreeNode#find)
    for (int i=1; i<node.getK(); ++i) {
      int cmp=node.compareKeys(key,node.getKey(i));
      if (cmp==0)
        return false; // key exists in tree
      else if (cmp<0) {
        if (node.getChild(i-1)==null) {

          // Note: We pass the insertion index i-1 (and likewise k below)
          //       such that split_bottom_up() can determine correct the index
          //       of the element that is to be "pulled up":
          //       In a classic implementation this happens (recursively) *before*
          //       inserting. Here, we allow a temporary 5-node and merge after
          //       insertion at the price that not always the second element
          //       will be "pulled up".

          node.insert(key,i-1);
          split_bottom_up(node,i-1);
          return true;

        } else
          return insert_bottom_up(node.getChild(i-1),key); // recursive find
      }
    }

    int k=node.getK()-1;
    KTreeNode<Key> right=node.getChild(k);
    if (right!=null)
      return insert_bottom_up(right,key);                 // recursive find

    node.insert(key,k);
    split_bottom_up(node,k);

    return true;
  }

  protected boolean insert_top_down(KTreeNode<Key> node, Key key) {
    node=split_top_down(node);

    // recursive find/insert (similar to KTreeNode#find)
    for (int i=1; i<node.getK(); ++i) {
      int cmp=node.compareKeys(key,node.getKey(i));
      if (cmp==0)
        return false; // key exists in tree
      else if (cmp<0) {
        if (node.getChild(i-1)==null) {
          // ASSERT
          node.insert(key,i-1);
          return true;

        } else {
          return insert_top_down(node.getChild(i-1),key); // recursive find
        }
      }
    }

    KTreeNode<Key> right=node.getChild(node.getK()-1);
    if (right!=null)
      return insert_top_down(right,key);                 // recursive find

    node.insert(key,node.getK()-1);

    return true;
  }

  @Override
  public String toDot() {
    return root_.toDot();
  }

  /** get TikZ code for LaTeX export (calls {@link KTreeNode#toTikZ}) */
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
    A234Tree<String> tree=new A234Tree<String>();

    tree.insert("b");
    tree.checkConsistency();
    tree.insert("c");
    tree.checkConsistency();
    tree.insert("a");
    tree.checkConsistency();
    tree.insert("d");
    tree.checkConsistency();
    tree.insert("e");
    tree.checkConsistency();
    tree.insert("f");
    tree.checkConsistency();
    tree.insert("g");
    tree.checkConsistency();

    aud.util.DotViewer.displayWindow(tree,"234 tree");
    System.out.println(tree.toTikZ());

    // aud.util.DotViewer.displayWindow(tree,"234 tree");
  }
}
