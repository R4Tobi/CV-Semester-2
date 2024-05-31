package aud.example.hash;


/** Collision handling strategy in {@link SimpleHashtable}.
 */
public abstract class CollisionHandler<T> {
  /** Handle collision by computing a new hash value.
   * @param table hash table
   * @param key new entry that is to be inserted but caused the collision
   * @param h previously used hash value
   * @param count iteration count for collision handling starting with 1
   *  (=first collision)
   * @return new hash value
   */
  public abstract long newHash(SimpleHashtable<T> table,T key,long h,int count);
}
