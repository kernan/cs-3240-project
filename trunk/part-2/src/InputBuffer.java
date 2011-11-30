 
import java.util.Scanner;

/** InputBuffer.java
 *	Allows forward and backward stepping through an input stream.
 */

public class InputBuffer {
	
	private Scanner input;
	private String buffer;
	private int currentpos;
	private boolean peek;
	
	/** 
	 * setup the buffer with a given string
	 * @param buffer string of current line
	 */
	public InputBuffer(Scanner input) {
		this.input = input;
		this.buffer = input.nextLine();
		this.currentpos = 0;
		this.peek = false;
	}
	
	/**
	 * get the next character in the buffer
	 * @return the next character in the buffer
	 */
	public char getNext() {
		if((this.currentpos >= buffer.length()) || (buffer.length() == 0)) {
			return '\n';
		}
		
		if(peek) {
			this.peek = false;
			return buffer.charAt(currentpos);
		}
		else {
			return this.buffer.charAt(currentpos++);
		}
	}
	
	/**
	 * peek at next token in buffer
	 * @return next token in buffer
	 */
	public char peekNext() {
		if(this.currentpos >= buffer.length()) {
			return '\n';
		}
		if(peek) {
			return buffer.charAt(currentpos);
		}
		else {
			peek = true;
			return getNext();
		}
	}
	
	/**
	 * move input buffer to the next line
	 */
	public boolean gotoNextLine() {
		peek = false;
		if(this.input.hasNextLine()) {
			this.buffer = input.nextLine();
			this.currentpos = 0;
			this.peek = false;
			return true;
		}
		else {
			return false;
		}
	}
}
