package interpreter;

import java.util.Collection;

/**
 * Identifier.java
 * generic identifier with no type
 */
public class Identifier {

	protected String name;
	private Object value;
	
	/**
	 * setup generic identifier with given name
	 * @param name name of this identifier
	 */
	public Identifier(String name) {
		this.name = name;
		this.value = null;
	}
	
	/**
	 * accessor for the name of this identifier
	 * @return name of this identifier
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * accessor for identifier value
	 * @return null pointer
	 */
	public Object getValue() {
		return null;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	/**
	 * give a string representation of an empty identifier
	 * @return string representation of this empty identifier
	 */
	public String toString() {
		if(this.value instanceof Collection) {
			String result = this.name + " = {\n";
			for(Object value: ((Collection<?>)this.value)) {
				result += "\t" + value.toString() + "\n";
			}
			result += "}";
			return result;
		}
		else {
			return this.name + " = " + this.value;
		}
	}
}
