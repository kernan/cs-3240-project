package generator.parser;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

import generator.regex.DFA;
import generator.regex.NFA_Identifier;
import generator.regex.RecursiveDescent;
import global.InputBuffer;
import global.Lexer;
import global.Token;

/** Script_Lexer.java
 *	provides capabilities to scan script files using a set of
 *	DFAs that containing token specifications
 */
public class Script_Lexer extends Lexer<Token<String>> {
	
	private static final String REGEX = "REGEX";
	private static final String ASCII_STR = "ASCII-STR";
	private static final String ID = "ID";
	private static final String EOF = "EOF";
	private static final int ID_MAX_SIZE = 10;
	
	private InputBuffer input_stream;
	private ArrayList<String> keywords;
	private ArrayList<String> illegal_chars;
	private boolean done;
	private boolean regex;
	private boolean ascii_str;
	private boolean id;
	
	private static final String ID_MATCH = "([A-Za-z])([A-Za-z0-9_])*";
	private DFA id_match;
	
	/**
	 * setup lexer with given filename and build DFAs from given terminal list
	 * @param filename file to scan
	 * @param terminals list of terminals to build DFAs of
	 * @throws FileNotFoundException thrown by InputBuffer
	 * @throws ParseException thrown by recursive descent dfa builder
	 */
	public Script_Lexer(String filename, ArrayList<Terminal> tokens) throws FileNotFoundException, ParseException {
		this.input_stream = new InputBuffer(filename);
		this.keywords = new ArrayList<String>();
		this.illegal_chars = new ArrayList<String>();
		this.done = false;
		this.regex = false;
		this.ascii_str = false;
		this.id = false;
		//add keywords to the list
		for(int i = 0; i < tokens.size(); i++) {
			if(tokens.get(i).getToken().getValue().equals(REGEX)) {
				this.regex = true;
			}
			else if(tokens.get(i).getToken().getValue().equals(ASCII_STR)) {
				this.ascii_str = true;
			}
			else if(tokens.get(i).getToken().getValue().equals(ID)) {
				this.id = true;
			}
			else if(tokens.get(i).getToken().getValue().length() == 1){
				this.illegal_chars.add(tokens.get(i).getToken().getValue());
			}
			else {
				this.keywords.add(tokens.get(i).getToken().getValue());
			}
		}
		//make id match dfa
		if(this.id) {
			RecursiveDescent rd = new RecursiveDescent(ID_MATCH, null);
			NFA_Identifier id_nfa = rd.descend();
			this.id_match = new DFA(id_nfa.getNFA());
		}
		else {
			this.id_match = null;
		}
	}
	
	/**
	 * close current file and open a new file for scanning
	 * @param filename new file to open
	 * @throws FileNotFoundException thrown  if the file doesn't exist
	 */
	public void newFile(String filename) throws FileNotFoundException {
		this.input_stream = new InputBuffer(filename);
	}
	
	/**
	 * get current position in the script file
	 * @return current position in the script
	 */
	@Override
	public int getPosition() {
		return this.input_stream.getPosition();
	}
	
	/**
	 * get current line in the script
	 * @return current line in the script
	 */
	public int getLine() {
		return this.input_stream.getLine();
	}
	
	/**
	 * generate a new token from the list (using nfa list)
	 * @return new token
	 */
	@Override
	protected Token<String> makeNewToken() throws ParseException {
		char t = this.input_stream.getNext();
		
		//check for end of file
		if(this.done) {
			return new Token<String>(EOF, EOF);
		}
		//check to see if finished with last line
		if(!this.input_stream.hasNext() && t == '\n') {
			this.done = true;
		}
		//if current char is newline
		if(t == '\n') {
			this.input_stream.gotoNextLine();//move to next line
			return this.makeNewToken();
		}
		//if current char is whitespace
		else if(t == '\t' || t == ' ') {
			//this.input_stream.getNext();//consume whitespace
			return this.makeNewToken();
		}
		//match single character tokens
		for(int i = 0; i < this.illegal_chars.size(); i++) {
			if(this.illegal_chars.get(i).charAt(0) == t) {
				return new Token<String>(this.illegal_chars.get(i), new String() + t);
			}
		}
		//match regex
		if(t == '\'' && this.regex) {
			String value = new String();
			t = this.input_stream.getNext();
			while(t != '\'') {
				value += t;
				t = this.input_stream.getNext();
			}
			return new Token<String>(REGEX, value);
		}
		//match ascii-str
		if(t == '"' && this.ascii_str) {
			String value = new String();
			t = this.input_stream.getNext();
			while(t != '"') {
				value += t;
				t = this.input_stream.getNext();
			}
			return new Token<String>(ASCII_STR, value);
		}
		//match keywords/identifiers
		boolean illegal = false;
		String value = new String();
		value += t;
		t = this.input_stream.peekNext();
		for(int i = 0; i < this.illegal_chars.size(); i++) {
			if(this.illegal_chars.get(i).charAt(0) == t) {
				illegal = true;
			}
		}
		while(t != ' ' && t != '\t' && t != '\n' && !illegal) {
			t = this.input_stream.getNext();
			value += t;
			t = this.input_stream.peekNext();
			for(int i = 0; i < this.illegal_chars.size(); i++) {
				if(this.illegal_chars.get(i).charAt(0) == t) {
					illegal = true;
				}
			}
		}
		//check if valid keyword
		for(int i = 0; i < this.keywords.size(); i++) {
			if(this.keywords.get(i).equals(value)) {
				return new Token<String>(this.keywords.get(i), value);
			}
		}
		//check if id
		if(this.id) {
			//match id rules: ([A-Za-z])([A-Za-z0-9_])*
			if(value.length() <= ID_MAX_SIZE) {
				this.id_match.reset();
				for(int i = 0; i < value.length(); i++) {
					this.id_match.gotoNext(value.charAt(i));
				}
				if(this.id_match.atFinal()) {
					return new Token<String>(ID, value);
				}
			}
		}
		
		throw new ParseException("Script ERROR: invalid token \"" + value + "\" at line: " +
				this.getLine() + ", pos: " + this.getPosition(), this.getPosition());
	}
}
