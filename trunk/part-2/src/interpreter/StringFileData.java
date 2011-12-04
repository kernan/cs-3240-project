package interpreter;

import java.util.ArrayList;

/**
 * StringFileData.java
 * represents metadata that tells what file and positions in that file
 * some data appears at
 */
public class StringFileData {

	private String filename;
	private ArrayList<Integer> positions;
	
	/**
	 * setup metadata with given filename
	 * @param filename file associated with this metadata
	 */
	public StringFileData(String filename, ArrayList<Integer> positions) {
		this.filename = filename;
		this.positions = positions;
	}
	
	/**
	 * accessor for this metadata's filename
	 * @return the filename this is associated with
	 */
	public String getFilename() {
		return this.filename;
	}
	
	/**
	 * adds a position to this metadata
	 * @param new_position new position to add
	 */
	public void addPosition(int new_position) {
		this.positions.add(new_position);
	}
	
	/**
	 * checks equality of two objects
	 * @return true: the two objects are equal, false: they are not
	 */
	public boolean equals(Object other) {
		return this.filename.equals(((StringFileData)other).getFilename());
	}
	
	/**
	 * gives a string representation of a StringFileData metadata
	 * @return string representation of this metadata
	 */
	public String toString() {
		String result = new String();
		result += "< " + filename + "; ";
		for(int i = 0; i < positions.size(); i++) {
			result += positions.get(i).toString() + " ";
		}
		return result + ">";
	}
}
