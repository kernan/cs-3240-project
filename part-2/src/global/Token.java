package global;

/**
 * Token.java
 * Represents a token of specified type with specified value.
 * @param <E> defines a set of valid types
 */

public class Token<E> {

	private E type;
	private String value;
	
	/**
	 * Constructor: create token with given values
	 * @param type type of token
	 * @param value string the token represents
	 */
	public Token(E type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Accessor for the token type
	 * @return the token's type
	 */
	public E getType() {
		return this.type;
	}
	
	/**
	 * Accessor for string token represents
	 * @return the token's value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Mutator for token type
	 * @param type token enum type
	 */
	public void setType(E type) {
		this.type = type;
	}
	
	/**
	 * Mutator for string token represents
	 * @param value character literal stored by this token
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * get string for token
	 * @return string representation of a token
	 */
	public String toString() {
		return "value: " + this.value + ", type: " + this.type.toString();
	}
}
