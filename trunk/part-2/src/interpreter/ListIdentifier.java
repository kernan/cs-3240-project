package interpreter;

import java.util.ArrayList;

/**
 * 
 */
public class ListIdentifier extends Identifier {

	private ArrayList<InputString> value;
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public ListIdentifier(String name, ArrayList<InputString> value) {
		super(name);
		this.value = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<InputString> getValue() {
		return this.value;
	}
}
