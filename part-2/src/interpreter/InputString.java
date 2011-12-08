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
		this.metadata = new ArrayList<StringFileData>();
	}
	
	/**
	 * setup an input string with given 
	 * @param str
	 * @param filename
	 * @param positions
	 */
	public InputString(String str, String filename, ArrayList<Integer> positions) {
		this(str);
		this.metadata.add(new StringFileData(filename, positions));
	}
	
	/**
	 * accessor for this input string's string data
	 * @return this input string's data
	 */
	public String getString() {
		return this.str;
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
	 * @param metadata list of file data to add
	 */
	public void addMetadata(ArrayList<StringFileData> metadata) {
		for(int i = 0; i < metadata.size(); i++) {
			this.addMetadata(metadata.get(i));
		}
	}
	
	/**
	 * add new file metadata to this string's metadata
	 * @param metadata file data to add
	 */
	public void addMetadata(StringFileData metadata) {
		//avoid duplicate metadata
		boolean contains = false;
		for(int i = 0; i < this.metadata.size(); i++) {
			if(this.metadata.get(i).equals(metadata)) {
				contains = true;
				break;
			}
		}
		if(!contains) {
			this.metadata.add(metadata);
		}
	}
	
	/**
	 * check if this is equal to another
	 * @return true: they are equal, false: they are not
	 */
	public boolean equals(Object other) {
		return this.str.equals(((InputString)other).getString());
	}
	
	/**
	 * generate string representation of this input string
	 * @return string representation of this input string
	 */
	public String toString() {
		String result = new String();
		result += "\"" + this.str + "\" ";
		for(int i = 0; i < this.metadata.size(); i++) {
			result += this.metadata.get(i).toString();
		}
		return result;
	}
	
	/**
	 * make a copy of this string
	 * @return copy of this string
	 */
	@SuppressWarnings("unchecked")
	public InputString clone() {
		InputString copy = new InputString(this.str);
		copy.addMetadata(((ArrayList<StringFileData>)this.metadata.clone()));
		return copy;
	}
}
