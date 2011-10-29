/**
 * A buffer that allows forward a backward stepping through
 * a Scanner input stream.
 * 
 * @author Robert Kernan
 *
 */

import java.util.Scanner;

public class InputBuffer {

	private Scanner input;
	private String buffer;
	private int currentpos;
	
	/**
	 * setup the buffer with given scanner
	 * @param input scanner to use
	 */
	public InputBuffer(Scanner input) {
		this.input = input;
	}
	
	/**
	 * get the next character in the buffer
	 * @return next character in the buffer
	 */
	public char getNext() {
		if(currentpos > buffer.length() -1) {
			this.buffer = input.nextLine();
			this.currentpos = 0;
			return '\n';
		}
		else {
			return this.buffer.charAt(currentpos++);
		}
	}
	
	/**
	 * move the buffer back a character
	 */
	public void backup() {
		if(this.currentpos > 0) {
			this.currentpos--;
		}
	}
}
