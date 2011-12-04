package interpreter;

/**
 * NumIdentifier.java
 * identifier representing a number
 */
public class NumIdentifier extends Identifier {
	
	private int value;
	
	/**
	 * setup an identifier with given name and given value
	 * @param name name of this identifier
	 * @param value data this identifier holds
	 */
	public NumIdentifier(String name, int value) {
		super(name);
		this.value = value;
	}
	
	/**
	 * setup given identifier with given value
	 * @param id identifier to assign a value to
	 * @param value data this identifier represents
	 */
	public NumIdentifier(Identifier id, int value) {
		this(id.getName(), value);
	}
	
	/**
	 * accessor for this identifier's data
	 * @return the data this identifier represents
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * returns a string representation of the data this identifier represents
	 * @return string representation of this identifier
	 */
	public String toString() {
		return ((Integer)this.value).toString();
	}
}
