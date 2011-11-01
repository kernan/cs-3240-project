/**
 * @author Robert Kernan
 * 
 * cs3240 HW-1
 */

import java.util.Scanner;

/** InputBuffer.java
 *	Allows forward and backward stepping through an input stream.
 */

public class InputBuffer {
	
	private Scanner input;
	private String buffer;
	int currentpos;
	
	/** setup the buffer with a given string
	 *	@param buffer
	 */
	public InputBuffer(Scanner input) {
		this.input = input;
		this.buffer = input.nextLine();
		this.currentpos = 0;
	}
	
	/** get the next character in the buffer
	 *	@return the next character in the buffer
	 */
	public char getNext() {
		if(currentpos > buffer.length() - 1) {
			this.buffer = input.nextLine();
			this.currentpos = 0;
			return '\n';
		}
		else {
			return this.buffer.charAt(currentpos++);
		}
	}
	
	/** move the buffer position back 1 character 
	 */
	public void backup() {
		if(this.currentpos > 0) {
			this.currentpos--;
		}
	}
}
