import java.io.IOException;
import java.util.ArrayList;

public class Parser {

	private ArrayList<Token> stream;
	private NFA nfa;
	
	//TODO list of char classes

	/**
	 * setup parser with an empty input stream
	 */
	public Parser() {
		this.stream = new ArrayList<Token>();
		this.nfa = null;
	}

	/**
	 * consumes next token and adds it to nfa
	 * 
	 * @param type expected token type
	 * @throws IOException when token is not valid
	 */
	public void matchToken(TokenType type) throws IOException {
		
		//TODO handle character classes
		//TODO build the NFA
		
		Token token = null;
		
		switch(token.getType()) {
			case IDENTIFIER:
			case LPAREN:
			case RPAREN:
			case MULTIPLY:
			case PLUS:
			case OR:
				stream.add(token);
				break;
			default:
				throw new IOException("Token not recognized: " + token.toString());
		}
	}
}
