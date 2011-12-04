package interpreter;

/**
 * Identifier.java
 * generic identifier with no type
 */
public class Identifier {

	private String name;
	
	/**
	 * setup generic identifier with given name
	 * @param name name of this identifier
	 */
	public Identifier(String name) {
		this.name = name;
	}
	
	/**
	 * accessor for the name of this identifier
	 * @return name of this identifier
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * give a string representation of an empty identifier
	 * @return string representation of this empty identifier
	 */
	public String toString() {
		return "NULL";
	}
}
