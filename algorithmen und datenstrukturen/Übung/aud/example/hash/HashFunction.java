package aud.example.hash;

/** interface for a hash function
 */
public abstract class HashFunction<T> {
    /** get hash value of {@code data} */
    public abstract long hash(T data);
}
