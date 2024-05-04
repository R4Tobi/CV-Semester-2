package aud.util;

/** Generate permutations.
    @see PermutationIterator
 */
public class Permutations implements Iterable<int[]> {
  int n_;

  public Permutations(int n) { n_=n; }

  @Override public PermutationIterator iterator() {
    return new PermutationIterator(n_);
  }

  /** demonstration and test */
  public static void main(String[] args) {

    int n=args.length>0 ? Integer.parseInt(args[0]) : 3;

    for (int[] p : new Permutations(n)) {
      for (int j : p)
        System.out.print(j);
      System.out.println();
    }
  }
}
