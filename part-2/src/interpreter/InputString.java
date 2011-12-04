package interpreter;

import java.util.ArrayList;

/**
 * InputString.java
 * represents a string with data representing it's appearance
 * in files
 */
public class InputString {
	
	private String str;
	private ArrayList<StringFileData> metadata;
	
	/**
	 * setup an input string with given value
	 * @param str value of this string
	 */
	public InputString(String str) {
		this.str = str;
	}
	
	/**
	 * setup an input string with given 
	 * @param str
	 * @param filename
	 * @param positions
	 */
	public InputString(String str, String filename, ArrayList<Integer> positions) {
		this(str);
		this.metadata = new ArrayList<StringFileData>();
		this.metadata.add(new StringFileData(filename, positions));
	}
	
	/**
	 * accessor for this string's metadata
	 * @return this string's metadata
	 */
	public ArrayList<StringFileData> getMetadata() {
		return this.metadata;
	}
	
	/**
	 * add new file metadata to this string's metadata
	 * @param metadata file data to add
	 */
	public void addMetadata(ArrayList<StringFileData> metadata) {
		this.metadata.addAll(metadata);
	}
	
	/**
	 * make a copy of this string
	 * @return copy of this string
	 */
	public InputString clone() {
		InputString copy = new InputString(this.str);
		copy.addMetadata(((ArrayList<StringFileData>)this.metadata.clone()));
		return copy;
	}
}
