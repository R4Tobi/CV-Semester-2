package aud;

import aud.util.Graphvizable;
import aud.util.GraphvizDecorable;
import aud.util.GraphvizDecorator;

/** Base class for a binary search tree.<p>

    Please keep in mind that this is an <em>educational</em>
    implementation. It is not optimized for a particular
    application!<p>

    <h2>Nodes</h2>

    The internal node (nested class {@link aud.BinarySearchTree.Node})
    representation is based on {@link aud.BinaryTree}!<p>

    On the one hand, this choice enables us to reuse functionality. On
    the other hand, there are some <em>disadvantages</em> due to extra
    data that is stored or extra indirections for method calls.

    {@code BinarySearchTree} stores <em>head</em> as a <b>dummy
    node</b> (member {@code head_}), which has has by definition a
    smaller key than any other node.

    This implementation does <em>not</em> use dummy nodes as leaves!

    Note that {@link aud.BinaryTree#getLeft} and
    {@link aud.BinaryTree#getRight} also update links to parents! While
    this is convenient in many cases, it requires special care in other
    cases! (Explicitly setting {@code null} references, order of
    operations matters "even more".)

    <h2>Nodes store key-value pairs</h2>

    Each {@link aud.BinarySearchTree.Node} stores a <em>key-value</em>
    pair of type {@link aud.BinarySearchTree.Entry} as node data.

    {@link BinarySearchTree} is a
    <a href="http://docs.oracle.com/javase/tutorial/java/generics/types.html">
    generic class</a>, and {@code Key} and {@code Value} are provided as
    <em>type parameters</em>.

    The {@code Key} type must be <em>comparable</em>, i.e., extend
    {@code Comparable<key>}.

    The rationale of storing key-value pairs is that this way the
    search tree can be used to implement a <b>set</b> or a <b>map</b>:
    <p>

    <ul>
    <li>A <b>set</b> stores only comparable objects, i.e., keys. This means that
    value attribute is not used.</li>
    <li>A <b>map</b> associates each key with a value. This means that
    only the key is used for comparison (e.g., by {@link #find} or
    {@link #insert}). A typical application for maps are
    <a href="http://en.wikipedia.org/wiki/Data_dictionary">dictionaries</a>.
    </ul>

    Keys are compared by {@link #compareKeys}, which treats {@code null}
    keys as smallest possible keys. Then <em>head</em>'s key equals
    {@code null} by definition.


    <h3>Finding entries of a binary search tree</h3>

    The method {@link #find} searches a an entry for a given key.

    <h4>Low-level implementation</h4>

    The core function used for finding ({@link #find}) a node by
    key <em>and also </em> for finding an insertion point (for
    {@link #insert}) is {@link #findLowerBound}). It returns a
    {@link aud.BinarySearchTree.LowerBound} instance (which stores
    the node and some additional information).

    <h4>High-level implementation: cursors and iterators</h4>

    The method {@link #findEntry} (and similarly {@link #getMinimum} and
    {@link #getMaximum}) returns a {@link aud.BinarySearchTree.Cursor}
    rather than a node.

    {@link aud.BinarySearchTree.Cursor} provides a reference to nodes
    in the tree, i.e., allows access to its key and value.

    The {@code iterator()} method implements {@code Iterable<Cursor>},
    and this <em>inorder traversal</em> iterator provides {@code Cursor}
    instances. (With some more work, we could alternatively provide
    different iterators over keys, values, and key-value pairs/entries.)

    <h3>Support for balancing</h3>

    Although this is <em>not</em> a balanced tree, {@code BinarySearchTree}
    provides methods for local rebalancing. These are

    <ul>
    <li> {@link #restructure} based on the implementation in <em>Goodrich
    and Tamassia</em> (2nd ed, chapter 7.4, p. 269).<br>
    The following rotations are also based on this implementation!</li>
    <li> {@link #rotateLeft} single rotation, right-right case</li>
    <li> {@link #rotateRight} single rotation, left-left case </li>
    <li> {@link #rotateLeftRight} double rotation, left-right case</li>
    <li> {@link #rotateRightLeft} double rotation, right-left case </li>
    </ul>

    All of these call the auxiliary method {@link #setupBalancedSubtree}
    (as does {@link #restructure}) to perform the rotations. Note that an
    implementation that works exclusively with rotations (not
    restructuring), would probably implement the double rotations as a
    combination of two single rotations.

    Especially {@link #restructure} is used for balancing by sub-classes,
    e.g., {@link aud.AVLTree}.

    The method {@link #restructure(char,Key)} is provided for testing
    the above methods interactively (see
    {@link aud.example.BinarySearchTreeExample}).

    Note that we excessively use the fact that an extra reference
    to the node's parent is stored. In fact, this allows a simple
    bottom-up processing as by {@link #restructure}. A more (memory)
    efficient implementation would avoid these references at the cost
    of a more complex implementation!

    <h2>Notifications on insert/remove</h2>

    {@link #insert} provides a
    <a href="http://en.wikipedia.org/wiki/Callback_%28computer_programming%29">callback </a>
    {@link #onInsert} to notify a subclass of a change of the tree. Similarly,
    {@link #remove} invokes {@link #onRemove}.

    These callback can be used by sub-classes for balancing and
    visualization/output.

    @see aud.example.BinarySearchTreeExample
 */
public class BinarySearchTree<Key extends Comparable<Key>,Value>
  implements Iterable<BinarySearchTree<Key,Value>.Cursor>,
             Graphvizable, GraphvizDecorable {


  /** Key-value pair as entry (= node data) in a search tree.<p>
      @see BinarySearchTree
  */
  public class Entry {
    final Key   key_;
    Value       value_;

    /** construct node */
    public Entry(Key key,Value value) {
      key_=key;
      value_=value;
    }
    /** get key */
    public Key getKey() { return key_; }
    /** get value */
    public Value getValue() { return value_; }
    /** set value */
    public void setValue(Value value) { value_=value; }

    @Override public String toString() {
      return key_!=null ?
        (key_.toString()+
         (value_!=null? "=>"+value_.toString():"")) : "head";
    }
  }



  /** Node in a {@link BinarySearchTree}.<p>
      The reference to tree is not really required and provided only
      for convenience (visualization).
  */
  protected class Node extends BinaryTree<Entry> {

    BinarySearchTree<Key,Value> tree_ = null;

    /** construct new node without children */
    Node(BinarySearchTree<Key,Value> tree,Key key,Value value) {
      super(new Entry(key,value));
      tree_=tree;
    }

    /** get key (short for {@code getData().getKey()}) */
    public Key getKey() { return getData().getKey(); }
    /** get value (short for {@code getData().getValue()}) */
    public Value getValue() { return getData().getValue(); }
    /** set value (short for {@code getData().setValue(value)}) */
    public void setValue(Value value) { getData().setValue(value); }

    @Override public GraphvizDecorator getDecorator() {
      return tree_.getDecorator();
    }

    /** Get next node (inorder).<p>

        Traverse tree to next node, i.e., the node with the
        <em>smallest</em> key that is <em>greater than</em> this node's
        key.<p>

        Note that this is possible due to (1.) {@link getParent} and
        because (2.) we know that the tree stores an ordered sequence,
        i.e., we know which nodes are already "visited". (This comes
        at the cost of <em>extra comparisons</em>!!)

        @return next node or {@code null} (end of traversal)
     */
    Node next() {
      if (getRight()!=null) { // right -> descend left
        Node node=(Node) getRight();
        while (node.getLeft()!=null) { node=(Node) node.getLeft(); }
        return node;
      }

      Node node=(Node) getParent(); // ascend up ( -> right -> descend left )
      while (node!=head_ && compareKeys(node.getKey(),getKey())<0) {
        Node right=(Node) node.getRight();
        if (right!=null && right!=this &&
            compareKeys(right.getKey(),getKey())>=0) {
          node=right;
          while (node.getLeft()!=null) { node=(Node) node.getLeft(); }
          return node;
        }
        node=(Node) node.getParent();
      }
      return node!=head_ ? node : null;
    }
  }

  protected Node createNode(Key key,Value value) {
    return new Node(this,key,value);
  }

  protected Node head_;

  /** create empty tree */
  public BinarySearchTree() {
    head_ = createNode(null,null);
  }

  /** Is tree empty? */
  public boolean isEmpty() {
    return head_.getRight()==null;
  }

  /**  Compares keys where {@code null} keys refer to smallest key.<p>
       This is the case for the <em>head</em> (dummy) node of {@link
       BinarySearchTree}.
     */
  protected int compareKeys(Key a,Key b) {
      if (a==null) return -1;
      if (b==null) return +1;
      else         return a.compareTo(b);
  }

  /** result of {@link #findLowerBound}
      @see BinarySearchTree
      @see BinarySearchTree#findLowerBound
      @see BinarySearchTree#find
      @see BinarySearchTree#insert
   */
  protected class LowerBound {
    /** the node found or {@code null} for failed search */
    protected Node node;
    /** {@code node}'s parent (always {@code !=null}) */
    protected Node parent;
    /** result of the last call to {@link #compareKeys} */
    protected int  cmp;

    LowerBound(Node node,Node parent,int cmp) {
      this.node=node;
      this.parent=parent;
      this.cmp=cmp;
    }
    /** get {@code node}'s value (handle special case)
        @return value or {@code null} if {@code node==null}
     */
    Value getValue() {
      return node!=null ? node.getValue() : null;
    }
  }

  /** Find node or insertion position.<p>
      There are two cases:
      <ol>

      <li>If a node with {@code key} exists in the tree, then it is
      returned in {@link LowerBound#node}, and {@link
      LowerBound#parent} denotes its parent, and {@link
      LowerBound#cmp} equals 0</li>

      <li>Otherwise, {@link LowerBound#node} equals {@code null}, and
      {@link LowerBound#parent} denotes the last node before
      "descending" to {@code null}, and {@link LowerBound#cmp} is the
      result of comparing {@link LowerBound#parent} to {@code key}. --
      In this case, {@link LowerBound#parent} points to the insertion
      position for a node with {@code key}, and {@link LowerBound#cmp}
      determines if this new node is to be inserted left ({@code
      cmp<0}) or right (@{code cmp>0}).</li>

      </ol>

      In both cases, the result provides a <em>lower bound</em> for a
      range including a node with {@code key}.

      @return result of search
   */
  protected LowerBound findLowerBound(Key key) {
    Node node=(Node) head_.getRight(), parent=head_;
    int  cmp=+1; // always larger than head_

    while (node!=null &&
           (cmp=compareKeys(key,node.getKey()))!=0) {
      parent=node;
      if (cmp<0)
        node=(Node) node.getLeft();
      else
        node=(Node) node.getRight();
    }
    return new LowerBound(node,parent,cmp);
  }

  /** find node with given key
      @return value or {@code null} if not found
   */
  public Value find(Key key) {
    return findLowerBound(key).getValue();
  }

  /** Reference to a key-value pair in a {@link BinarySearchTree}.
      @see BinarySearchTree
   */
  public class Cursor {
    final Node node_;

    Cursor(Node node) { assert(node!=head_); node_=node; }

    /** @return key */
    public Key getKey() { return node_.getKey(); }
    /** @return value */
    public Value getValue() { return node_.getValue(); }
    /** set value */
    public void setValue(Value value) { node_.setValue(value); }
  }

  /** same as {@link #find} but return Cursor or {@code null} */
  public Cursor findEntry(Key key) {
    Node node=findLowerBound(key).node;
    return node!=null ? new Cursor(node) : null;
  }

  /** get minimum entry (greater than {@code null})
      @return Cursor or {@code null} if {@link #isEmpty}
   */
  public Cursor getMinimum() {
    Node node=(Node) head_.getRight(), parent=head_;
    while (node!=null) {
      parent=node;
      node=(Node) node.getLeft();
    }
    return parent!=head_ ? new Cursor(parent) : null;
  }

  /** get maximum value (greater than {@code null})
      @return Cursor or {@code null} if {@link #isEmpty}
   */
  public Cursor getMaximum() {
    Node node=(Node) head_.getRight(), parent=head_;
    while (node!=null) {
      parent=node;
      node=(Node) node.getRight();
    }
    return parent!=head_ ? new Cursor(parent) : null;
  }

  /** visitor for traversing the tree in preorder*/
  public static abstract class Visitor {
    abstract public void visit(String state,
                               String key, String value, String tikz);
  };

  /** visit all nodes in preorder */
  public void visitPreorder(Visitor visitor) {
    visitPreorder(visitor, (Node) head_.getRight());
  }

  void visitPreorder(Visitor visitor, Node node) {
    visitor.visit("node-pre", null, null, null);

    if (node != null) {
      Value val = node.getValue();
      visitor.visit("node", node.getKey().toString(),
                    val != null ? val.toString() : null,
                    node.tikzNodeStyle());

      visitor.visit("left-down", null, null, null);
      visitPreorder(visitor, (Node) node.getLeft());
      visitor.visit("left-up", null, null, null);

      visitor.visit("right-down", null, null, null);
      visitPreorder(visitor, (Node) node.getRight());
      visitor.visit("right-up", null, null, null);
    }

    visitor.visit("node-post", null, null, null);
  }

  /** inorder iterator
      @see BinarySearchTree
   */
  public class Iterator implements java.util.Iterator<Cursor> {
    java.util.Iterator<BinaryTree<Entry> > iter_;

    Iterator
      (java.util.Iterator<BinaryTree<Entry> >iter) {
      iter_=iter;
    }

    @Override public boolean hasNext() {
      return iter_!=null && iter_.hasNext();
    }

    @Override public Cursor next() {
      Node node=(Node) iter_.next();
      return new Cursor(node);
    }
    /** not implemented
        @throws UnsupportedOperationException
    */
    @Override public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /** get iterator for inorder traversal */
  @Override public Iterator iterator() {
    return new Iterator
      (isEmpty() ? null : head_.getRight().inorder().iterator());
  }


  /** Get iterable range [begin,end[.<p>

      {@code begin} and {@code end} denote either a key to or the lower
      bound of the beginning or end of a range.<p>

      Range denotes a subsequence starting from (and including) {@code
      begin} until {@code end} is reached. This means that {@code end}
      is <em>not</em> part of the range.<p>

      If {@code null} keys are provided, the range starts from the
      first entry ({@link #getMinimum}) and/or ends with the last
      entry {@link #getMaximum}), respectively.

      @param begin first entry in range
      @param end defines end of range
      @return range including {@code begin} and excluding {@code end}
   */
  public Range range(Key begin,Key end) {
    if (isEmpty() || (end!=null && compareKeys(begin,end)>=0))
      return new Range(null,null);

    LowerBound lower=findLowerBound(begin);
    Node start=lower.node!=null ? lower.node : lower.parent;
    Node stop =null;
    if (end!=null) {
      lower=findLowerBound(end);
      stop=lower.node!=null ? lower.node : lower.parent;
    }
    return new Range(start,stop);
  }

  /** iterable range (subsequence) */
  public class Range implements Iterable<Cursor> {
    final Node begin_;
    final Node end_;

    Range(Node begin,Node end) {
      begin_=begin;
      end_=end;
    }

    @Override public RangeIterator iterator() {
      return new RangeIterator(this);
    }
  }

  /** Iterator over {@link Range}. <p>
      Implementation is based on {@link Node#next}.
   */
  public class RangeIterator implements java.util.Iterator<Cursor> {
    Range range_;
    Node  node_;

    RangeIterator(Range range) {
      range_=range;
      node_=range_.begin_;
    }

    @Override public boolean hasNext() {
      return node_!=range_.end_;
    }

    @Override public Cursor next() {
      assert(node_!=null);
      Node cur=node_;
      node_=node_.next();
      return new Cursor(cur);
    }
    /** not implemented
        @throws UnsupportedOperationException
    */
    @Override public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /** Insert new key-value pair (new node).<p>

      Let {@code old} be the result of {@link #find} for {@code key}.<br>

      If {@code old==null} then a new node will be inserted in the tree.
      Otherwise, the corresponding node's value will be set to {@code value}.<p>

      Note that this method <em>always</em> assigns a value, regardless
      if a new node was created and inserted or not.<p>

      <b>Internal note</b>: Immediately <em>after</em> insertion of a
      new node (first case) but <em>before</em> returning, {@link
      #onInsert} is called.

      @return {@code old} (can be used to to distinguish the two cases above)
   */
  public Value insert(Key key,Value value) {
    LowerBound r=findLowerBound(key);

    if (r.node!=null) { // key exists in tree
      Value old=r.node.getValue();
      r.node.setValue(value);
      return old;
    }
    assert(r.cmp!=0);
    assert(r.parent!=null);

    Node node=createNode(key,value);
    Node old;

    if (r.cmp<0)
      old=(Node) r.parent.setLeft(node);
    else
      old=(Node) r.parent.setRight(node);

    onInsert(node); // notify via callback

    assert(old==null);

    return null;
  }

  /** Called whenever a new node is inserted.<p>

      Called by {@link #insert} to notify a class derived from {@link
      BinarySearchTree} that a new node has been inserted.
      Using this sort of <a href="http://en.wikipedia.org/wiki/Callback_%28computer_programming%29">callback</a>
      mechanism avoids a reimplementation of {@link #insert}.<p>

      The standard implementation is <em>empty</em>. The idea is to
      use the callback for visualization or have a subclass
      rebalancing the tree after node insertion.

      @param node the newly inserted node
   */
  protected void onInsert(Node node) {}


  /** Remove node with {@code key}.<p>
      Calls {@link #removeNode} which invokes {@code #onRemove}
      <em>after</em> removal.
      @return value of removed node or {@code null} if there was no
      such node
   */
  public Value remove(Key key) {
    LowerBound r=findLowerBound(key);
    Node   node=r.node;

    if (node==null)
      return null; // no such node

    Value value=node.getValue();

    removeNode(node);

    return value;
  }

  /** Remove {@code node} from tree.<p>
      Calls {@link #onRemove}.
   */
  protected void removeNode(Node node) {
    Node    parent=(Node) node.getParent();
    Node    child;             // update to parent

    assert(parent!=null);
    assert(parent.getLeft()==node || parent.getRight()==node);

    if (node.isLeaf())              // trivial case
      child=null;
    else if (node.getLeft()==null)  // 1 child
      child=(Node) node.getRight();
    else if (node.getRight()==null) // 1 child
      child=(Node) node.getLeft();
    else {                          // 2 children
      child=(Node) node.getRight();
      while (child.getLeft()!=null)
        child=(Node) child.getLeft();

      child.setLeft(node.getLeft());
      node.left_=null;     // because setXXX sets parent!

      Node p=(Node) child.getParent();
      if (p!=node) {
        p.setLeft(child.getRight());
        child.right_=null; // because setXXX sets parent!
        child.setRight(node.getRight());
      }
    }

    if (parent.getLeft()==node)
      parent.setLeft(child);
    else
      parent.setRight(child);

    onRemove();
  }

  /** Called whenever a node is removed.<p>
      The standard implementation is <em>empty</em>.
      @see #onInsert
   */
  protected void onRemove() {}

  /** Restructure subtree following <b>Goodrich and Tamassia</b>
      (2nd ed, p. 269).<p>
      See also lecture slides for names of symbols.
      @param grandchild restructure subtree rooted at
      {@code grandchild.getParent().getParent()} (the path towards the root
      is always unique, so specifying the grandchild is simplest).

      @return root node of restructured subtree
      @see #setupBalancedSubtree
   */
  protected Node restructure(Node grandchild) {
    assert(grandchild.getParent()!=head_);
    assert(grandchild.getParent().getParent()!=head_);

    Node child=(Node) grandchild.getParent();
    Node parent=(Node) child.getParent();

    // tri-node restructuring following [Goodrich andTamassia]

    Node x=grandchild, y=child, z=parent;
    boolean xLeft=(x==y.getLeft());
    boolean yLeft=(y==z.getLeft());

    Node a,b,c;       // find inorder traversal of x,y,z
    Node t0,t1,t2,t3;

    if (xLeft && yLeft) {
      a=x; b=y; c=z;
      t0=(Node) a.getLeft();
      t1=(Node) a.getRight();
      t2=(Node) b.getRight();
      t3=(Node) c.getRight();
    }
    else if (!xLeft && yLeft) {
      a=y; b=x; c=z;
      t0=(Node) a.getLeft();
      t1=(Node) b.getLeft();
      t2=(Node) b.getRight();
      t3=(Node) c.getRight();
    }
    else if (xLeft && !yLeft) {
      a=z; b=x; c=y;
      t0=(Node) a.getLeft();
      t1=(Node) b.getLeft();
      t2=(Node) b.getRight();
      t3=(Node) c.getRight();
    }
    else { // right/right
      a=z; b=y; c=x;
      t0=(Node) a.getLeft();
      t1=(Node) b.getLeft();
      t2=(Node) c.getLeft();
      t3=(Node) c.getRight();
    }

    return setupBalancedSubtree(z,a,b,c,t0,t1,t2,t3);
  }

  /** Setup balanced subtree.<p>

      Called by {@link #restructure} to setup the final, locally balanced tree.
      Follows <b>Goodrich and Tamassia</b> (2nd ed, p. 269).<p>
      See also lecture slides for names of symbols.
      As {@link #rotateLeft} and {@link #rotateRight} are implemented
      the same way (each one is a special case in {@link #restructure}), this
      operation becomes a method of its own.
      @see #restructure
      @return root node of restructured subtree
   */
  protected Node setupBalancedSubtree
    (Node z,Node a,Node b,Node c,Node t0,Node t1,Node t2,Node t3) {

    Node root=(Node) z.getParent();
    assert(root!=null);

    if (root.getLeft()==z)
      root.setLeft(b);
    else
      root.setRight(b);

    a.left_=a.right_=null; // avoid update of their parent
    a.setLeft(t0);
    a.setRight(t1);

    c.left_=c.right_=null;
    c.setLeft(t2);
    c.setRight(t3);

    b.left_=b.right_=null;
    b.setLeft(a);
    b.setRight(c);

    return b;
  }

  /** Apply single left rotation (right-right case).
      <pre><code>
         a                     b
       /   \                /    \
      t0    b      ==>     a       c
          /  \           /   \   /   \
         t1   c         t0   t1  t2  t3
             /  \
            t2   t3
      </code></pre>
      @param node {@code a}
      @return new root {@code b}
      @see #setupBalancedSubtree
    */
  protected Node rotateLeft(Node node) {
    Node a=node;
    Node b=(Node) node.getRight(); assert(b!=null);
    Node c=(Node) b.getRight();    assert(c!=null);

    Node t0=(Node) a.getLeft();
    Node t1=(Node) b.getLeft();
    Node t2=(Node) c.getLeft();
    Node t3=(Node) c.getRight();

    Node z=a;

    return setupBalancedSubtree(z, a,b,c, t0,t1,t2,t3);
  }

  /** Apply double rotation (right-left case).
      <pre><code>
         a                     b
       /   \                /     \
      t0    c      ==>     a       c
          /   \          /   \   /   \
         b    t3        t0   t1 t2   t3
        /  \
       t1   t3
      </code></pre>
      @param node {@code a}
      @return new root {@code b}
      @see #setupBalancedSubtree
    */
  protected Node rotateRightLeft(Node node) {
    Node a=node;
    Node c=(Node) node.getRight(); assert(c!=null);
    Node b=(Node) c.getLeft();     assert(b!=null);

    Node t0=(Node) a.getLeft();
    Node t1=(Node) b.getLeft();
    Node t2=(Node) b.getRight();
    Node t3=(Node) c.getRight();

    Node z=a;

    return setupBalancedSubtree(z, a,b,c, t0,t1,t2,t3);
  }

  /** Apply single right rotation (left-left case).
      <pre><code>
                c                    b
              /   \                /    \
             b    t3     ==>     a        c
           /  \                 /   \   /   \
          a     t2              t0   t1 t2   t3
        /   \
       t0   t1
      </code></pre>
      @param node {@code a}
      @return new root {@code b}
      @see #setupBalancedSubtree
    */
  protected Node rotateRight(Node node) {
    Node c=node;
    Node b=(Node) node.getLeft(); assert(b!=null);
    Node a=(Node) b.getLeft();    assert(a!=null);

    Node t0=(Node) a.getLeft();
    Node t1=(Node) a.getRight();
    Node t2=(Node) b.getRight();
    Node t3=(Node) c.getRight();

    Node z=c;

    return setupBalancedSubtree(z, a,b,c, t0,t1,t2,t3);
  }

  /** Apply double rotation (left-right case).
      <pre><code>
                c                    b
              /   \                /    \
             a     t3    ==>     a        c
           /  \                 /   \   /   \
          t0    b              t0   t1 t2   t3
              /   \
             t1   t2
      </code></pre>
      @param node {@code a}
      @return new root {@code b}
      @see #setupBalancedSubtree
    */
  protected Node rotateLeftRight(Node node) {
    Node c=node;
    Node a=(Node) node.getLeft(); assert(a!=null);
    Node b=(Node) a.getRight();   assert(b!=null);

    Node t0=(Node) a.getLeft();
    Node t1=(Node) b.getLeft();
    Node t2=(Node) b.getRight();
    Node t3=(Node) c.getRight();

    Node z=c;

    return setupBalancedSubtree(z, a,b,c, t0,t1,t2,t3);
  }

  /** Interactive restructuring for testing.<p>
      @param action one of {@code '/','\','<', '>', '='} corresponding to
      application of
      {@link #rotateRight}, {@link #rotateLeft}, {@link #rotateLeftRight},
      {@link #rotateRightLeft} (symbols "sketch" tree), and
      {@link #restructure(Node)}, respectively.
      @param key defines for applying {@code action} (will be argument to the
      methods listed above)
      @throws RuntimeException if the local tree structures does not conform to
      the required subtree.
      @see aud.example.BinarySearchTreeExample
   */
  public void restructure(char action,Key key) {
    if (key==null)
      throw new RuntimeException("invalid key 'null'");

    Node node=findLowerBound(key).node;
    if (node==null)
      throw new RuntimeException("no such node '"+key+"'");

    switch (action) {
    case '/':
      if (node.getLeft()==null || node.getLeft().getLeft()==null)
        throw new RuntimeException("missing node->left>left");
      decorator_.highlightNode(rotateRight(node));
      break;
    case '\\':
      if (node.getRight()==null || node.getRight().getRight()==null)
        throw new RuntimeException("missing node->left->right");
      decorator_.highlightNode(rotateLeft(node));
      break;
    case '>':
      if (node.getRight()==null || node.getRight().getLeft()==null)
        throw new RuntimeException("missing node->right->left");
      decorator_.highlightNode(rotateRightLeft(node));
      break;
    case '<':
      if (node.getLeft()==null || node.getLeft().getRight()==null)
        throw new RuntimeException("missing node->left->right");
      decorator_.highlightNode(rotateLeftRight(node));
      break;
    case '=':
      if (node.getParent()==head_ || node.getParent().getParent()==head_)
        throw new RuntimeException("missing node->parent->parent");
      decorator_.highlightNode(restructure(node));
    }
  }

  /** enables decoration "by key" */
  public class Decorator extends aud.util.SimpleDecorator {
    @Override public String getNodeDecoration(GraphvizDecorable object) {
      return object==head_ ? "shape=ellipse" : super.getNodeDecoration(object);
    }
    public void highlight(Key key) {
      highlightNode(findLowerBound(key).node);
    }
    public void mark(Key key) {
      markNode(findLowerBound(key).node);
    }
    public void unmark(Key key) {
      unmarkNode(findLowerBound(key).node);
    }
  }

  Decorator decorator_ = new Decorator();

  @Override public GraphvizDecorator getDecorator() {
    return decorator_;
  }

  @Override public String toDot() {
    return head_.toDot();
  }

  @Override public String toString() {
    String s="";
    if (!isEmpty()) {
      java.util.Iterator<?> ii=head_.getRight().inorder().iterator();
      while (ii.hasNext()) {
        s+=ii.next();
        if (ii.hasNext())
          s+=",";
      }
    }
    return s;
  }

  /** same as {@link BinaryTree#toText} */
  public String toText() {
    return isEmpty() ? "" : head_.getRight().toText();
  }

  /** same as {@link BinaryTree#toTikZ} */
  public String toTikZ() {
    return isEmpty() ? "" : head_.getRight().toTikZ();
  }

  /** consistency checks
      @throws RuntimeException on error (and/or assertion fails)
   */
  public void checkConsistency() {
    if (!isEmpty())
      checkConsistency((Node) head_.getRight());
  }
  protected void checkConsistency(Node node) {
    Node parent=(Node) node.getParent();
    assert(parent!=null);
    assert((Node) parent.getLeft()==node || (Node) parent.getRight()==node);

    if (parent==null)
      throw new RuntimeException("parent!=null");
    if ((Node) parent.getLeft()!=node && (Node) parent.getRight()!=node)
      throw new RuntimeException("invalid parent");

    if (node.getLeft()!=null)
      checkConsistency((Node) node.getLeft());
    if (node.getRight()!=null)
      checkConsistency((Node) node.getRight());
  }

  /** test and example */
  public static void main(String[] args) {
    BinarySearchTree<String,Object> tree=
      new BinarySearchTree<String,Object> ();
    System.out.println(tree);

    //String[] keys={"a","b","c","d","e","f","g"};
    //String[] keys={"d","b","f","a","c","e","g"};
    String[] keys={"a","b","d","c","g","f","e"};


    for (String key : keys)
      tree.insert(key,null);

    System.out.println(tree);
    System.out.println(tree.toText()); tree.checkConsistency();

    for (BinarySearchTree<String,Object>.Cursor c : tree)
      System.out.print(c.getKey());
    System.out.println();

    for (BinarySearchTree<String,Object>.Cursor c : tree.range("b","f"))
      System.out.print(c.getKey());
    System.out.println();

    keys=new String[]{"a","c","e","g"};

    for (String key : keys)
      tree.remove(key);

    System.out.println(tree);
    System.out.println(tree.toText());
  }
}
