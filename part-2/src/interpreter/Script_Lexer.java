package interpreter;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;

import generator.parser.LL1_Token;
import generator.parser.LL1_TokenType;
import generator.parser.Terminal;
import generator.regex.DFA;
import generator.regex.NFA_Identifier;
import generator.regex.RecursiveDescent;
import global.InputBuffer;
import global.Lexer;
import global.Options;
import global.Token;

/** Script_Lexer.java
 *	provides capabilities to scan script files using a set of
 *	DFAs that containing token specifications
 */
public class Script_Lexer extends Lexer<Token> {
	
	//preset token values
	public static final String REGEX = "\\\' ([^'] IN [\\ -~])+ \\\'";
	public static final String ASCII_STR = "\\\" ([^\"] IN [\\ -~])+ \\\"";
	public static final String ID = "([A-Za-z])([A-Za-z0-9_])*";
	public static final String EPSILON = "EPSILON";
	
	private InputBuffer input;
	private ArrayList<Token<DFA>> tokens;
	private boolean done;
	
	/**
	 * setup lexer with given filename and build DFAs from given terminal list
	 * @param filename file to scan
	 * @param terminals list of terminals to build DFAs of
	 * @throws FileNotFoundException thrown by InputBuffer
	 * @throws ParseException thrown by recursive descent dfa builder
	 */
	public Script_Lexer(String filename, ArrayList<Terminal> tokens) throws FileNotFoundException, ParseException {
		super();
		this.done = false;
		this.newFile(filename);
		//make DFAs
		this.tokens = new ArrayList<Token<DFA>>();
		boolean id = false;
		for(int i = 0; i < tokens.size(); i++) {
			Token<LL1_TokenType> current = tokens.get(i).getToken();
			//need to add ascii-str preset
			if(current.getValue().equals("ASCII-STR")) {
				RecursiveDescent t = new RecursiveDescent(ASCII_STR, null);
				NFA_Identifier t_id = t.descend();
				this.tokens.add(new Token<DFA>(new DFA(t_id.getNFA()), "ASCII-STR"));
			}
			//need to add regex preset
			else if(current.getValue().equals("REGEX")) {
				RecursiveDescent t = new RecursiveDescent(REGEX, null);
				NFA_Identifier t_id = t.descend();
				this.tokens.add(new Token<DFA>(new DFA(t_id.getNFA()), "REGEX"));
			}
			//need to ass id preset
			else if(current.getValue().equals("ID")) {
				id = true;
			}
			//make the dfa and add it to the dfa list
			else if(!current.getValue().equals("EPSILON")){
				RecursiveDescent t = new RecursiveDescent(current.getValue(), null);
				NFA_Identifier t_id = t.descend();
				this.tokens.add(new Token<DFA>(new DFA(t_id.getNFA()), current.getValue()));
			}
		}
		if(id) {
			//create id DFA (needs to be at bottom)
			RecursiveDescent t = new RecursiveDescent(ID, null);
			NFA_Identifier t_id = t.descend();
			this.tokens.add(new Token<DFA>(new DFA(t_id.getNFA()), "ID"));
		}
	}
	
	/**
	 * close current file and open a new file for scanning
	 * @param filename new file to open
	 * @throws FileNotFoundException thrown  if the file doesn't exist
	 */
	public void newFile(String filename) throws FileNotFoundException {
		this.input = new InputBuffer(filename);
	}
	
	/**
	 * get current position in the script file
	 * @return current position in the script
	 */
	@Override
	public int getPosition() {
		return this.input.getPosition();
	}
	
	/**
	 * get current line in the script
	 * @return current line in the script
	 */
	public int getLine() {
		return this.input.getLine();
	}
	
	/**
	 * generate a new token from the list (using nfa list)
	 * @return new token
	 */
	@Override
	protected Token<String> makeNewToken() throws ParseException {
		/*
		 * find longest matching regex expression
		 * 
		 * look for end of file
		 * skip newlines
		 * start_pos = input.getPosition
		 * for dfa : tokens
		 *   dfa.reset
		 *   while(!dfa.inDead())
		 *     dfa.gotoNext(input.getNext())
		 *     if dfa
		 */
		
		int start_position = this.input.getPosition();
		
		return null;
	}
}
