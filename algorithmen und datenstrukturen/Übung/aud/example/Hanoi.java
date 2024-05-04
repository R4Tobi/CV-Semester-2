package aud.example;

import aud.Stack;

/** Recursive and iterative solution of the  Towers-of-Hanoi puzzle.
    <p>

    Note that the solution strategy is essentially an <em>inorder
    traversal</em> of a binary tree. In addition, we have to update
    "positions" of the source, destination, and free tower.<p>

    This class provides four different implementation of the same,
    recursive solution strategy.

    <ul>

    <li>{@link #recusive_solve} is a direct implementation of the
    recursive algorithm.</li>

    <li>{@link #recursive_solve_1} is the same with end-recursion
    removed. This is an intermediate step towards the non-recursive
    implementation.</li>

    <li>The non-recursive solution {@link #iterative_solve} is a
    transformation of {@link #recursive_solve} where end-recursive is
    removed, and a stack is used to "simulate" the second (recursive)
    function call.<br>
    We use a {@link aud.Stack} to save and restore the current state
    ({@link Configuration}).</li>

    <li>Finally, we realize that the solution can be expressed as a
    complete binary tree of height {@code n} (i.e., with {@code 2^n-1}
    nodes). The descending to the left and right "child" corresponds to
    the first and second recursive call to {@link #move_towers} in the
    recursive implementation.<br> I borrowed the code from {@link
    BinaryTreeTraversal.Inorder} with few adaptations. (Note that we
    don't really store a tree, see {@link InorderIterator.Node}.)<br>
    The {@link #solution} method provides the iterator representing the
    solution.</li>

    </ul>

    The last implementation based on inorder traversal is equivalent
    to but probably much nicer than the direct transformation from a
    recursive to a non-recursive algorithm.<p>

    We used a transformation of the recursive algorithm. Note that for
    this particular problem, there are <em>various other</em>
    non-recursive solutions!

    @see aud.Stack
    @see aud.BinaryTreeTraversal.Inorder
 */
class Hanoi {

  /** Solve Towers of Hanoi for n disks. */
  public static void recursive_solve(int n) {
    move_towers(n,0,2,1);
  }

  /** Move a single disk. */
  private static void move_disk(int from,int to) {
    System.out.println("move disk from "+from+" to "+to);
  }
  /** Solve problem recursively. */
  //@<hanoi:movetowers
  private static void move_towers(int n,int from,int to,int free) {
    if (n==1)
      move_disk(from,to);
    else {
      move_towers(n-1,from,free,to);
      move_disk(from,to);
      move_towers(n-1,free,to,from);
    }
  }
  //@>hanoi:movetowers

  /** recursive solve with end-recursion removed */
  public static void recursive_solve_1(int n) {
    move_towers_1(n,0,2,1);
  }

  /** Solve problem recursively with end-recursion removed. */
  private static void move_towers_1(int n,int from,int to,int free) {
    while (n>1) {
      move_towers_1(n-1,from,free,to);
      move_disk(from,to);

      --n;
      int t=from; from=free; free=t;
    }

    move_disk(from,to); // n=1
  }

  /** Solve iteratively. */
  public static void iterative_solve(int n) {
    move_towers_iterative(n,0,2,1);
  }

  /** A particular state of the solution.<p>
      This corresponds to the arguments of the recursive function {@link
      #move_towers}.
      @see Hanoi
    */
  private static class Configuration {
    Configuration(int n,int from,int to,int free) {
      this.n=n;
      this.from=from;
      this.to=to;
      this.free=free;
    }
    int   n;
    int   from, to, free;
  }

  /** Solve problem iteratively. */
  private static void move_towers_iterative(int n,int from,int to,int free) {

    Stack<Configuration> stack=new Stack<Configuration>();

    boolean done=false;

    DESCEND: do {
      while (n>1) {
        stack.push(new Configuration(n,from,to,free)); // save
        --n;
        int t=to; to=free; free=t;
      }

      do {
        while (n>1) {

          move_disk(from,to);

          --n;
          int t=from; from=free; free=t;

          continue DESCEND; // essentially a GOTO (ugly!)
                            // alternatively copy 1st while loop (we have n==1!)
        }
        move_disk(from,to); // n=1

        if (stack.is_empty()) {
          done=true;
        }
        else {
          Configuration c=stack.pop();
          n=c.n; from=c.from; to=c.to; free=c.free;    // restore
        }
      } while (!done);
    } while (!done);
  }



  /** inorder iterator for solving Towers of Hanoi */
  public static class InorderIterator implements java.util.Iterator<String> {

    /** Solution state interpreted as a binary tree.<p>
        Defines methods {@link #getLeft} and {@link #getRight}, which generate
        the child nodes (which represent recursive calls in
        {@link #move_towers}.
     */
    protected class Node extends Configuration {
      public Node(int n,int from,int to,int free) {
        super(n,from,to,free);
      }
      @Override public String toString() {
        return "move disk from "+from+" to "+to;
      }

      /** left: from -> free (replaces first recusive call to {@link #move_towers}) */
      public Node getLeft() {
        return n>1 ? new Node(n-1,from,free,to) : null;
      }
      /** left: free -> to (replaces second recusive call to {@link #move_towers}) */
      public Node getRight() {
        return n>1 ? new Node(n-1,free,to,from) : null;
      }
    }

    Stack<Node> stack_ = new Stack<Node>();

    InorderIterator(int n) {
      stack_.push(new Node(n,0,2,1));
      descendLeft();
    }

    private void descendLeft() {
      Node node=stack_.top();
      for (node=node.getLeft();node!=null;node=node.getLeft())
        stack_.push(node);
    }

    @Override public String next() {
      Node node=stack_.pop();
      if (node.getRight()!=null) {
        stack_.push(node.getRight());
        descendLeft();
      }

      return node.toString();
    }

    @Override public boolean hasNext() {
      return !stack_.is_empty();
    }
    /** @throws UnsupportedOperationException (not implemented) */
    @Override public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /** helper: generates {@link InorderIterator} */
  public static class Inorder implements Iterable<String>{
    int n_;
    Inorder(int n) { n_=n; }
    @Override public java.util.Iterator<String> iterator() {
      return new InorderIterator(n_);
    }
  }

  /** return instance of generator
      @see BinaryTree#inorder
  */
  public static Inorder solution(int n) {
    return new Inorder(n);
  }


  /** enumerate solutions */
  public static void main(String args[]) {
    int n=Integer.parseInt(args[0]);
    System.out.println("recursive solution:\n");
    recursive_solve(n);

    System.out.println("\nrecursive solution w/o end-recursion:\n");
    recursive_solve_1(n);

    System.out.println("\niterative solution:\n");
    iterative_solve(n);

    System.out.println("\niterative solution using inorder iterator:\n");
    for (String move : Hanoi.solution(n)) {
      System.out.println(move);
    }
  }
}
