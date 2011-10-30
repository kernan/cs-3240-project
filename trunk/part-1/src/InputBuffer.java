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
	private char current;
	
	/**
	 * setup the buffer with given scanner
	 * @param input scanner to use
	 */
	public InputBuffer(Scanner input) {
		this.input = input;
	}
}
