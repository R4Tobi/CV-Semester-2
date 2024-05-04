package aud;

/** Simple implementation of a red-black tree.<p>

    This implementation is based on {@link BinarySearchTree} and in
    particular {@link BinarySearchTree#restructure} for rebalancing.<p>

    Similarly to {@link AVLTree}, the implementation is based on
    {@link BinarySearchTree#restructure} (which is called from the
    {@link #onInsert} callback. See <em>Goodrich and Tamassia</em>
    (2nd ed, chapter 13.3) for details!<p>

    This implementation is considerably simpler than the "standard"
    implementations based on rotations and split (see, e.g., Saake or
    Sedgewick) and the analogy to <em>top-down 2-3-4 trees</em>.<p>

    However, note that here the red-black property is enforced
    entirely <em>bottom-up</em> as either "restructuring" or
    "recoloring" in {@link #remedyDoubleRed}. (One particular
    advantage is that we don't have to reimplement {@link
    BinarySearchTree#insert}; the {@link #onInsert} callback is
    sufficient.)<p>

    The {@link #onRestructuring} and {@link #onRecoloring} callbacks
    provide additional hooks for visualization.<p>

    This implementation <b>does not</b> implement removal of nodes!
    {@link #remove} always throws {@code
    UnsupportedOperationException}!
    <p>

    <b>Note:</b> The construction of red-black trees is not unique. A remarkable
    alternative are <em>left-leaning red-black trees</em>. The original
    <a href="http://www.cs.princeton.edu/~rs/talks/LLRB/LLRB.pdf">article</a>
    features a nice explanation and a short and elegant implementation.

    @see aud.example.RedBlackTreeExample
 */
public class RedBlackTree<Key extends Comparable<Key>,Value>
  extends BinarySearchTree<Key,Value> {

  /** node in a {@link RedBlackTree} */
  protected class RBNode extends BinarySearchTree<Key,Value>.Node {
    RBNode(RedBlackTree<Key,Value> tree,Key key,Value value) {
      super(tree,key,value);
      tree_=tree;
      red_=true; // insert red node
    }

    boolean red_;

    /** Is node red? */
    public boolean isRed() { return red_; }

    @Override protected String textLabel() {
      if (red_)
        return "["+getData().toString()+"]";
        // return "\033[91m"+getData().toString()+"\033[0m";
      else
        return getData().toString();
    }

    @Override protected String tikzNodeStyle() {
      return isRed() ? "[red]" : "[black]";
    }
  }

  /** override: create new RBNode */
  @Override protected Node createNode(Key key,Value value) {
    return new RBNode(this,key,value);
  }

  /** create empty tree */
  public RedBlackTree() {
    super();
    decorator_ = new RedBlackDecorator();
  }

  /** helper: {@code node==null} is black */
  private boolean isRed(RBNode node) {
    return node!=null ? node.isRed() : false;
  }

  /** helper: root is below {@code head_} */
  private boolean isRoot(RBNode node) {
    assert(node!=null && node!=head_);
    return node.getParent()==head_;
  }

  @Override protected void onInsert(Node _node) {
    RBNode node=(RBNode) _node;
    node.red_=true;

    if (isRoot(node))
      node.red_=false;
    else
      remedyDoubleRed(node);
  }

  protected void remedyDoubleRed(RBNode node) {
    RBNode parent=(RBNode) node.getParent();

    assert(parent!=head_); // !isRoot(node)

    if (isRoot(parent) ||  // root is black!
        !parent.isRed())
      return;

    RBNode grandparent=(RBNode) parent.getParent();

    RBNode sibling=(RBNode) grandparent.getLeft();
    if (sibling==parent)
      sibling=(RBNode) grandparent.getRight();

    if (!isRed(sibling)) {           // restructuring
      RBNode b=(RBNode) restructure(node);
      b.red_=false;
      ((RBNode) b.getLeft()).red_=true;
      ((RBNode) b.getRight()).red_=true;

      onRestructuring();
    }
    else {                           // recoloring
      parent.red_=false;
      assert(sibling!=null) : "red node must exist";
      sibling.red_=false;

      assert(grandparent!=null && grandparent!=head_);
      if (!isRoot(grandparent)) {
        grandparent.red_=true;
        remedyDoubleRed(grandparent);

        onRecoloring();
      }
    }
  }

  /** callback after restructuring in {@link #remedyDoubleRed} */
  protected void onRestructuring() {
  }
  /** callback after one step of recoloring in {@link #remedyDoubleRed} */
  protected void onRecoloring() {
  }

  /** additionally check red-node property */
  @Override protected void checkConsistency(Node node) {
    super.checkConsistency(node);

    RBNode anode=(RBNode) node;

    if (anode.isRed()) {
      RBNode left=(RBNode) anode.getLeft();
      RBNode right=(RBNode) anode.getRight();

      assert(left ==null || !left .isRed());
      assert(right==null || !right.isRed());

      if ((left !=null && left .isRed()) ||
          (right!=null && right.isRed()))
        throw new RuntimeException("double red node at '"+anode+"'");
    }
  }

  /** <b>not implemented!</b>
      @throws UnsupportedOperationException <b>always!</b>
   */
  @Override public Value remove(Key key) {
    throw new UnsupportedOperationException("RedBlackTree#remove");
  }

  public class RedBlackDecorator extends Decorator {
    @Override
    @SuppressWarnings("unchecked")
    public String getNodeDecoration(aud.util.GraphvizDecorable object) {
      String style=super.getNodeDecoration(object);
      if (style!=null || object==head_)
        return style;
      return ((RBNode) object).isRed() ?
         "style=filled,fillcolor=lightcoral,penwidth=1" :
         "style=filled,fillcolor=lightblue,penwidth=3";
    }
  }

  /** exmaple and test */
  public static void main(String[] args) {

    //RedBlackTree<String,Object> tree=new RedBlackTree<String,Object> ();
    RedBlackTree<Integer,Object> tree=new RedBlackTree<Integer,Object> ();

    System.out.println(tree);

    //String[] keys={"a","b","c","d","e","f","g"};
    int[] keys={4,7,12,15,3,5,14,18,16,17,};

    //for (String key : keys) {
    for (int key : keys) {
      System.out.println("insert '"+key+"'");
      tree.insert(key,null);
      System.out.println(tree);
      System.out.println(tree.toText());
      tree.checkConsistency();
    }

    System.out.println(tree.toTikZ());
  }
}
