package interpreter;

import java.util.ArrayList;

/**
 *
 */
public class InputString {
	
	private String str;
	private ArrayList<StringFileData> metadata;
	
	/**
	 * 
	 * @param str
	 */
	public InputString(String str) {
		this.str = str;
	}
	
	/**
	 * 
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
	 * 
	 * @return
	 */
	public ArrayList<StringFileData> getMetadata() {
		return this.metadata;
	}
	
	/**
	 * 
	 * @param metadata
	 */
	public void addMetadata(ArrayList<StringFileData> metadata) {
		this.metadata.addAll(metadata);
	}
	
	/**
	 * 
	 * @return
	 */
	public InputString clone() {
		InputString copy = new InputString(this.str);
		copy.addMetadata(((ArrayList<StringFileData>)this.metadata.clone()));
		return copy;
	}
}
