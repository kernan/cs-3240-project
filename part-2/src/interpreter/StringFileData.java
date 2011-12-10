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
	 * accessor for the number of occurances of this input string
	 * @return number of occurances
	 */
	public int getNumOccurances() {
		return this.positions.size();
	}
	
	/**
	 * adds a position to this metadata
	 * @param new_position new position to add
	 */
	public void addPosition(int new_position) {
		this.positions.add(new_position);
	}
	
	/**
	 * accessor for positions array
	 * @return
	 */
	public ArrayList<Integer> getPositions() {
		return this.positions;
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
		result += "<" + filename + ": ";
		if(positions.size() == 0) {
			return result;
		}
		else {
			result += positions.get(0).toString();
			for(int i = 1; i < positions.size(); i++) {
				result += " " + positions.get(i).toString();
			}
			return result + ">";
		}
	}
}
