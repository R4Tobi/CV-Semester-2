package aud.example.hash;

import java.io.File;
import java.util.Scanner;

import aud.util.ColormapCount;
import aud.util.SVGViewer;

/** Simple framework for experiments with hash tables.
 *  <p>
 *  The program lets you choose among hash functions and strategies for
 *  collision handling. The you can fill the hash tables either with
 *  integers or with strings. The latter uses {@link HashString} to compute
 *  integer values. You can output the hash table or visualize the history
 *  of inserts to see entries are well-spread in the table.
 *  <p>
 *  Few hash functions and collision handlers for {@link SimpleHashtable}
 *  are implemented as nested classes. Don't hesitate to extend the program
 *  and experiment with your own functions! Have fun! :-)
 *
 *  @see SimpleHashtable
 */
public class HashtableExample {

  /** Compute integer hash value from string.
   *  Treats byte representation of string as a "big integer". Uses
   *  Horner's rule to avoid overflow.
   */
  static public class HashString extends HashFunction<String> {
    long m_ = 2019773507l; // Integer range18014398509481951l;

    public HashString() {}
    public HashString(long m) { this(); m_=m; }

    @Override public long hash(String s) {
      long h=0;
      for (byte b : s.getBytes()) {
        h =(h*0x100+(b+0x80));
        h%=m_;
      }
      //System.err.println(s+" -> "+h);
      return h;
    }
    @Override public String toString() {
      return "HashString[m="+m_+"]";
    }
  }

  /** Identity hash function. This is not really a hash function but it just
   * returns the given integer.
   */
  static public class IdentityHash extends HashFunction<Integer> {
    @Override public long hash(Integer k) { return k; }
    @Override public String toString() {
      return "Identity";
    }
  }

  /** Universal hash function parameterized as described in
   *  <em>Goodrich and Tamassia.</em>
   *  (Read the book chapter!)
   */
  static public class UniversalHash extends HashFunction<Integer> {
    long a_ = 7177;
    long b_ = 3179303;
    long p_ = 18014398509481951l;

    public UniversalHash() {}
    public UniversalHash(long a,long b,long p) {
      this();
      assert(0<a && a<p && 0<=b && b<p);
      a_=a; b_=b; p_=p;
    }

    @Override public long hash(Integer k) {
      return (a_*(k>=0 ? k : -k)+b_) % p_;
    }
    @Override public String toString() {
      return "UniversalHash[a="+a_+", b="+b_+", p="+p_+"]";
    }
  }

  /** Collision handling by linear probing {@code h(x,i)=h(x)+i*b} */
  public class LinearProbing<T> extends CollisionHandler<T> {
    int b_ = 1;
    public LinearProbing() {}
    public LinearProbing(int b) {
      this();
      assert(b>0);
      b_=b;
    }

    @Override public long
      newHash(SimpleHashtable<T> table,T key,long h,int count) {
        return h+count*b_;
      }

    @Override public String toString() {
      return "LinearProbing[b="+b_+"]";
    }
  }

  /** Collision handling by quadratic probing {@code h(x,i)=h(x)+i*b+i*i*c} */
  public class QuadraticProbing<T> extends CollisionHandler<T> {
    int b_ = 1;
    int c_ = 1;
    public QuadraticProbing() {}
    public QuadraticProbing(int b,int c) {
      this();
      assert(b>=0 && c>0);
      b_=b;
      c_=c;
    }

    @Override public long
      newHash(SimpleHashtable<T> table,T key,long h,int count) {
        return h + count*b_ + (count*count)*c_;
      }

    @Override public String toString() {
      return "QuadraticProbing[b="+b_+", c="+c_+"]";
    }
  }

  /** Collision handling by double hashing using {@code h2} */
  public class DoubleHashing<T> extends CollisionHandler<T> {
    HashFunction<T> h2_ = null;

    public DoubleHashing(HashFunction<T> h2) { h2_=h2; }

    @Override public long
      newHash(SimpleHashtable<T> table,T key,long h,int count) {
        long h2=h2_.hash(key);
        return h+count*(h2!=0 ? h2 : 18014398509481951l);
    }

    @Override public String toString() {
      return "DoubleHahsing[h2="+h2_+"]";
    }
  }

  //
  // test framework
  //

  /** Key could be integer or string.
   *  <b>Note</b>: Simplifies implementation but generally this is <em>not</em>
   *  a good a idea!
   */
  class Key {
    final HashString hashstring = new HashString();

    public long intValue;
    String s = null;

    /** get gey from integer */
    public Key(Integer i) { intValue=i; }
    /** get key from string (convert to integer using {@link HashString}) */
    public Key(String s)  { intValue=hashstring.hash(s); this.s=s; }

    public boolean equals(Key other) {
      return s==null ?
            (intValue==other.intValue) :
            (s.compareTo(other.s)==0);
    }

    @Override public String toString() {
      return s!=null ? s : (""+intValue);
    }
  }

  /** {@code HashFunction{ for {@link Key} instances */
  class HashKey extends HashFunction<Key> {
    HashFunction<Integer> ihash;
    HashKey(HashFunction<Integer> hash) { this.ihash=hash; }
    @Override public long hash(Key k) { return ihash.hash((int) k.intValue); }

    @Override public String toString() {
      return ihash.toString();
    }
  }

  /** the hash table */
  SimpleHashtable<Key>  hashtable=null;

  /** the hash function */
  HashFunction<Key>     hashfunction=new HashKey(new IdentityHash());

  /** the collision handling strategy */
  CollisionHandler<Key> collisionhandler=null;

  /** Do we insert integers? (Strings otherwise.) */
  boolean               readIntegers=true;

  /** timestep for recording history: snapshot every {@code timestep} inserts */
  int timestep = 1;

  /** current time */
  int time=0;

  /** verbosity level*/
  int verbose = 0;

  /** print help on usage */
  static void usage() {
    System.err.println(
        "java HashtableExample [-m M] [-s|--string] [--timestep T]\n"+
        "  [-v | --verbose LEVEL]\n"+
        "  [-u] [--universal A B P]\n"+
        "  [-L] [--linear B]\n"+
        "  [-Q] [--quadratic B C]\n"+
        "  [-S] [--separate]\n"+
        "  [-D] [--double]\n[-h] [--help]\n\n"+
        " The short form switches ('-' except '-m') don't take parameters,\n"+
        " the long forms '--' require all listed parameters. (See methods\n"+
        " for a description of parameters.)\n\n"+
        " -s | --string forces input of strings otherwise integers are\n"+
        "               expected!\n"+
        " --timestep set timestep for recording history\n"+
        " -v | --verbose print additonal information\n"+
        " The default hash function is modulo table size M (default M=11).\n"+
        " -u | --universal select the universal has function\n\n"+
        " -L,-Q,-S select linear probing, quadratic probing, or separate\n"+
        "          chaining (default)\n"+
        " -D  selects double hashing, any subsequent -u | --universal modifies\n"+
        "     the secondary hash function, which should be specified\n"+
        "     differently from the primal hash function! (No checks!)\n\n"+
        " -h | --help displays this message\n\n"+
        "The program reads from standard input and inserts integers/words to\n"+
        "into the hash table.\n"+
        "The following \"words\" have a special meaning:\n"+
        " '.' quits,\n" +
        " '?' prints the contents of the hash table to standard output\n"+
        " '!' visualizes the history of the hash table in a new window\n\n"
        );
    System.exit(-1);
  }

  /** constructor takes arguments of {@code main} */
  public HashtableExample(String args[]) {
    int m=11;

    HashFunction<Integer> h2=new IdentityHash();
    boolean haveDoubleHashing=false;

    for (int i=0;i<args.length;++i) {
      if (args[i].compareTo("-m")==0)
        m=Integer.parseInt(args[++i]);
      else if (args[i].compareTo("--timestep")==0)
        timestep=Integer.parseInt(args[++i]);
      else if (args[i].compareTo("-v")==0)
        ++verbose;
      else if (args[i].compareTo("--verbose")==0)
        verbose=Integer.parseInt(args[++i]);
      else if (args[i].compareTo("--string")==0 || args[i].compareTo("-s")==0)
        readIntegers=false;
      else if (args[i].compareTo("--help")==0 || args[i].compareTo("-h")==0)
        usage();
      else if (args[i].compareTo("-u")==0)
        hashfunction=new HashKey(new UniversalHash());
      else if (args[i].compareTo("--universal")==0) {
        HashFunction<Integer> h=
          new UniversalHash(
              Integer.parseInt(args[++i]), // a
              Integer.parseInt(args[++i]), // b
              Integer.parseInt(args[++i])  // p
              );
        if (haveDoubleHashing)
          h2=h;
        else
          hashfunction=new HashKey(h);
      }
      else if (args[i].compareTo("--separate")==0 || args[i].compareTo("-S")==0)
        collisionhandler=null;
      else if (args[i].compareTo("-L")==0)
        collisionhandler=new LinearProbing<Key>();
      else if (args[i].compareTo("--linear")==0)
        collisionhandler=new LinearProbing<Key>(Integer.parseInt(args[++i]));
      else if (args[i].compareTo("-Q")==0)
        collisionhandler=new QuadraticProbing<Key>();
      else if (args[i].compareTo("--quadratic")==0)
        collisionhandler=new QuadraticProbing<Key>
        (Integer.parseInt(args[++i]),Integer.parseInt(args[++i]));
      else if (args[i].compareTo("--double")==0 || args[i].compareTo("-D")==0) {
        haveDoubleHashing=true;
        collisionhandler=null;
      }
      else
        usage();
    }

    if (haveDoubleHashing)
      collisionhandler=new DoubleHashing<Key>(new HashKey(h2));


    System.err.println("> hash table with m="+m);
    System.err.println("> hash function="+hashfunction);
    System.err.println("> collision handling="+
        (collisionhandler==null ?
            "separate chaining" : collisionhandler.toString()
        ));
    if (readIntegers)
      System.err.println("> reading integers...\n");
    else
      System.err.println("> reading strings (HashString)...\n");

    hashtable=new SimpleHashtable<Key>(m,hashfunction,collisionhandler);
    hashtable.verbose=(verbose>0);
  }

  /** start inserting data */
  public void start() {
    SVGViewer viewer=null;
    time=0;
    hashtable.beginRecording();

    Scanner s=new Scanner(System.in);
    s.useDelimiter("\\s+");

    while (s.hasNext()) {
      String key=s.next();
      if (key.startsWith("."))
        break;
      else if (key.startsWith("?")) {
        if (key.startsWith("?svg:")) {
          hashtable.history_.renderSpySVG(
              new File(key.substring(5)+".svg"),new ColormapCount());
        }
        else if (key.startsWith("?tikz"))
          System.out.println(hashtable
                .history_.spyTikZ(true,new ColormapCount()));
        else
          System.out.println(hashtable);
      }
      else if (key.startsWith("!")) {
        if (viewer!=null)
          viewer.close();
        viewer=hashtable.showHistory();
      }
      else {
        Key k=readIntegers ? new Key(Integer.parseInt(key)) : new Key(key);
        if ((time++)%timestep==0)
          hashtable.nextTimeStep();
        hashtable.insert(k);
      }
    }

    if (viewer!=null)
      viewer.close();
    viewer=hashtable.showHistory();
    // TODO reuse viewer instance (write to same tempfile)
  }
  // TODO: catch exceptions (cycles while probing)

  /** start the program */
  public static void main(String args[]) {
    new HashtableExample(args).start();
  }
}
