package aud.util;

/** Iterator over all permutations of length {@code n}.<p>

    Permutations are represented as {@code int[]} arrays with
    entries {@code 0,..,n-1}.<p>

    Implementation inspired by
    <a href="http://en.cppreference.com/w/cpp/algorithm/next_permutation">
    std::next_permutation</a> of the C++ standard library: assume ordered
    sequence and "count up" to lexicographically next sequence. (Requires
    only sequence as state.)
 */
public class PermutationIterator implements java.util.Iterator<int[]> {

  private int[] p_  = null; // current permutation

  void swap(int i,int j) {
    int t=p_[i]; p_[i]=p_[j]; p_[j]=t;
  }
  void reverse(int i,int j) {
    --j;
    while (i<j)
      swap(i++,j--);
  }

  /** create new iterator
      @param n length of permutation
   */
  public PermutationIterator(int n) {
    if (n>0) {
      p_=new int[n];
      for (int i=0;i<n;++i)
        p_[i]=i;            // initial
    }
  }

  @Override public boolean hasNext() {
    return p_!=null;
  }

  @Override public int[] next() {
    int[] p = new int[p_.length];

    for (int j=0;j<p_.length;++j)
      p[j]=p_[j];

    int last=p_.length;
    int i=last-1;

    for (;;) {
      int ii=i;
      if (p_[--i]<p_[ii]) {
        int j=last;
        while (p_[i]>=p_[--j]) {}
        swap(i,j);
        reverse(ii,last);

        break;
      }
      else if (i==0) {
        //reverse(0,last);
        p_=null;
        break;
      }
    }

    return p;
  }

  /** @throws UnsupportedOperationException */
  @Override public void remove() {
    throw new UnsupportedOperationException();
  }

  /** demonstration and test */
  public static void main(String[] args) {

    int n=args.length>0 ? Integer.parseInt(args[0]) : 3;

    for (PermutationIterator ii=new PermutationIterator(n);ii.hasNext();) {
      int[] p=ii.next();
      for (int j : p)
        System.out.print(j);
      System.out.println();
    }
  }
}
