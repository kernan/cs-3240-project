package interpreter;

import java.util.ArrayList;

/**
 * ListIdentifier.java
 * identifier representing a list of input strings
 */
public class ListIdentifier extends Identifier {

	private ArrayList<InputString> value;
	
	/**
	 * setup identifier with given name and given data
	 * @param name name of this identifier
	 * @param value data this identifier represents
	 */
	public ListIdentifier(String name, ArrayList<InputString> value) {
		super(name);
		this.value = value;
	}
	
	/**
	 * setup given identifier with given value
	 * @param id identifier to assign a value to
	 * @param value data this identifier represents
	 */
	public ListIdentifier(Identifier id, ArrayList<InputString> value) {
		this(id.getName(), value);
	}
	
	/**
	 * accessor for this identifier's data
	 * @return data this identifier represents
	 */
	public ArrayList<InputString> getValue() {
		return this.value;
	}
	
	/**
	 * adds a value to a list, if it already exists, it adds metadata
	 * @param t value to add to the list
	 */
	public void add(InputString t) {
		for(int i = 0; i < this.value.size(); i++) {
			//if a duplicate is found, replace it and return
			this.value.get(i).addMetadata(t.getMetadata());
		}
		//add normally if not found
		this.value.add(t);
	}
	
	/**
	 * generates a string representation of this identifier's value
	 * @return string representation of this identifier
	 */
	public String toString() {
		String result = new String();
		for(int i = 0; i < this.value.size(); i++) {
			result += this.value.get(i).toString() + "\n";
		}
		return result;
	}
}
