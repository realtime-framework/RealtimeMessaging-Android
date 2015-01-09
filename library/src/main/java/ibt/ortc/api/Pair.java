/**
 * @fileoverview This file contains the implementation of a key value generic class
 */
package ibt.ortc.api;

/**
 * Key Value generic class
 *
 * @param <A> Key
 * @param <B> Value
 */
public class Pair<A, B> {
	/**
	 * Key element
	 */
    public A first;
	
	/**
	 * Value element
	 */
    public B second;

	/**
	 * Creates a instance of a key value object
	 * 
	 * @param first Key element
	 * @param second Value element
	 */
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the key element
	 * @return Key element 
	 */
	public A getFirst() {
		return first;
	}

	/**
	 * Gets the value element
	 * @return Value element 
	 */
	public B getSecond() {
		return second;
	}

	/**
	 * Sets the key element
	 */
	public void setFirst(A v) {
		first = v;
	}

	/**
	 * Sets the value element
	 */
	public void setSecond(B v) {
		second = v;
	}

	/**
	 * Gets a string representation of the object
	 * 
	 * @return String representation of the object
	 */
    @Override
	public String toString() {
        // CAUSE: Prefer String.format to +
        return String.format("Pair[%s,%s]", first, second);
	}

	private static boolean equals(Object x, Object y) {
		return (x == null && y == null) || (x != null && x.equals(y));
	}

	/**
	 * Compares the current object with another
	 * 
	 * @return boolean True if other object is equal to the current otherwise false 
	 */
	@SuppressWarnings("rawtypes")
    @Override
	public boolean equals(Object other) {
		return other instanceof Pair && equals(first, ((Pair) other).first) && equals(second, ((Pair) other).second);
	}

	/**
	 * Generates a hashcode based on the elements hashcodes
	 * 
	 * @return int the object hashcode 
	 */
    @Override
	public int hashCode() {
        // CAUSE: If-Else Statements Should Use Braces
        if (first == null) {
			return (second == null) ? 0 : second.hashCode() + 1;
        } else if (second == null) {
			return first.hashCode() + 2;
        } else {
			return first.hashCode() * 17 + second.hashCode();
	}
    }

	public static <A, B> Pair<A, B> of(A a, B b) {
		return new Pair<A, B>(a, b);
	}
}
