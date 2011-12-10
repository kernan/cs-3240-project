package interpreter;

import java.io.FileNotFoundException;
import java.text.ParseException;

import generator.parser.LL1_Token;
import global.InputBuffer;
import global.Lexer;

/**
 * 
 */
public class Script_Lexer extends Lexer<LL1_Token> {
	
	private InputBuffer input;
	
	/**
	 * 
	 * @param filename
	 * @throws FileNotFoundException thrown by InputBuffer
	 */
	public Script_Lexer(String filename) throws FileNotFoundException {
		super();
		this.input = new InputBuffer(filename);
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	public int getPosition() {
		return this.input.getPosition();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getLine() {
		return this.input.getLine();
	}
	
	/**
	 * 
	 * @return
	 */
	@Override
	protected LL1_Token makeNewToken() throws ParseException {
		
		return null;
	}
}
