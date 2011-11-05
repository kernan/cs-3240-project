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
	public String buffer;
	public int currentpos;//TODO
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
		if(peek) {
			this.peek = false;
			return buffer.charAt(currentpos);
		}
		else {
			System.out.print("[InputBuffer] getting next char: ");
			
			if(currentpos > buffer.length() - 1) {
				System.out.print("overflow... need to go to a new line\n");
				if(buffer.length() == 0) {
					boolean more = gotoNextLine();
					if(more) {
						return getNext();
					}
					else {
						return '\n';
					}
				}
				else {
					return '\n';
				}
			}
			else {
				System.out.print(buffer.charAt(currentpos) + "\n");
				return this.buffer.charAt(currentpos++);
			}
		}
	}
	
	/**
	 * peek at next token in buffer
	 * @return next token in buffer
	 */
	public char peekNext() {
		System.out.print("[InputBuffer] peeking next char: ");
		if(peek) {
			if(this.currentpos > buffer.length() - 1) {
				return '\n';
			}
			else {
				System.out.print(buffer.charAt(currentpos) + "\n");
				return buffer.charAt(currentpos);
			}
		}
		else {
			peek = true;
			System.out.print(" getting the char... ");
			return getNext();
		}
	}
	
	/**
	 * move input buffer to the next line
	 */
	public boolean gotoNextLine() {
		peek = false;
		System.out.println("[InputBuffer] getting next line...");
		
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
