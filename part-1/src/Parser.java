import java.io.IOException;
import java.util.ArrayList;

public class Parser {

	private ArrayList<Token> stream;
	private NFA nfa;
	
	//ASCII printable characters
	public char[] ascii = {
		' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', 
		'@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 
		'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', 
		'`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 
		'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'
	};
	
	//TODO list of char classes

	/**
	 * setup parser with an empty input stream
	 */
	public Parser() {
		this.stream = new ArrayList<Token>();
		this.nfa = null;
	}
	
//	<regEx> ->  <rexp> 
	private void regEx(){
		rexp();
	}
//	<rexp> -> <rexp1> <rexp$>
	private void rexp(){
		rexp1();
		rexp$();
	}
//	<rexp$> -> UNION <rexp1> <rexp$>  |  E    
	private void rexp$(){
		if (peekToken() == TokenType.UNION){
			matchToken(TokenType.UNION);
			rexp1();
			rexp$();
		}
		else
			return;
	}
//	<rexp1> -> <rexp2> <rexp1$>
	private void rexp1(){
		rexp2();
		rexp1$();
	}
//	<rexp1$> -> <rexp2> <rexp1$>  |  E   
	private void rexp1$(){
		if(peekToken() == TokenType.LPAREN || peekToken() == TokenType.RE_CHAR
				|| peekToken() == TokenType.DOT || peekToken() == TokenType.LBRACKET
				|| peekToken() == TokenType.DOLLAR){
			rexp2();
			rexp1$();
		}
		else
			return;
	}
//	<rexp2> -> (<rexp>) <rexp2Tail>  | RE_CHAR <rexp2Tail> | <rexp3>
	private void rexp2(){
		if(peekToken() == TokenType.LPAREN){
			matchToken(TokenType.LPAREN);
			rexp();
			matchToken(TokenType.RPAREN);
			rexp2Tail();
		}
		else if(peekToken() == TokenType.RE_CHAR){
			matchToken(TokenType.RE_CHAR);
			rexp2Tail();
		}
		else{
			rexp3();
		}
	}
//	<rexp2Tail> -> * | + |  E
	private void rexp2Tail(){
		if(peekToken() == TokenType.MULTIPLY){
			matchToken(TokenType.MULTIPLY);
		}
		else if(peekToken() == TokenType.PLUS){
			matchToken(TokenType.PLUS);
		}
		else
			return;
	}
//	<rexp3> -> <charClass>  |  E   
	private void rexp3(){
		if(peekToken() == TokenType.DOT || peekToken() == TokenType.LBRACKET
				|| peekToken() == TokenType.DOLLAR){
			charClass();
		}
		else
			return;
	}
//	<charClass> ->  .  |  [ <charClass1>  | <definedClass>
	private void charClass(){
		if(peekToken() == TokenType.DOT){
			matchToken(TokenType.DOT);
		}
		else if(peekToken() == TokenType.LBRACKET){
			matchToken(TokenType.LBRACKET);
			charClass1();
		}
		else 
			definedClass();
	}
//	<charClass1> ->  <charSetList> | <excludeSet>
	private void charClass1(){
		if(peekToken() == TokenType.CARET){
			excludeSet();
		}
		else{
			charSetList();
		}
	}
//	<charSetList> ->  <charSet> <charSetList> |  E 
	private void charSetList(){
		if(peekToken() == TokenType.CLS_CHAR){
			charSet();
			charSetList();
		}
		else
			return;
	}
//	<charSet> -> CLS_CHAR <charSetTail> 
	private void charSet(){
		matchToken(TokenType.CLS_CHAR);
		charSetTail();
	}
//	<charSetTail> -> � CLS_CHAR | E
	private void charSetTail(){
		if(peekToken() == TokenType.DASH){
			matchToken(TokenType.DASH);
			matchToken(TokenType.CLS_CHAR);
		}
		else
			return;
	}
//	<excludeSet> -> ^ <charSet>] IN <excludeSetTail>  
	private void excludeSet(){
		matchToken(TokenType.CARET);
		charSet();
		matchToken(TokenType.RBRACKET);
		matchToken(TokenType.IN);
		excludeSetTail();
	}
//	<excludeSetTail> -> [<charSet>]  | <definedClass>
	private void excludeSetTail(){
		if(peekToken() == TokenType.LBRACKET){
			matchToken(TokenType.LBRACKET);
			charSet();
			matchToken(TokenType.RBRACKET);
		}
		else{
			definedClass();
		}
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
		
		Token token = Lexer.getNextToken();
		
		switch(token.getType()) {
			case AND:
			case OR:
			case NOT:
			case PLUS:
				if(token.getValue().equals("+")){
					stream.add(token);
					break;
				}
			case MINUS:
				if(token.getValue().equals("-")){
					stream.add(token);
					break;
				}
			case MULTIPLY:
				if(token.getValue().equals("*")){
					stream.add(token);
					break;
				}
			case DIVIDE:
				if(token.getValue().equals("/")){
					stream.add(token);
					break;
				}
			case MOD:
				if(token.getValue().equals("%")){
					stream.add(token);
					break;
				}
			case CHAR_CLASS:
			case LPAREN:
				if(token.getValue().equals("(")){
					stream.add(token);
					break;
				}
			case RPAREN:
				if(token.getValue().equals(")")){
					stream.add(token);
					break;
				}
			case LBRACKET:
				if(token.getValue().equals("[")){
					stream.add(token);
					break;
				}
			case RBRACKET:
				if(token.getValue().equals("]")){
					stream.add(token);
					break;
				}
			case LCURLY:
				if(token.getValue().equals("{")){
					stream.add(token);
					break;
				}
			case RCURLY:
				if(token.getValue().equals("}")){
					stream.add(token);
					break;
				}
			case IDENTIFIER:
			case EOF:
			case UNION:
				if(token.getValue().equals("|")){
					stream.add(token);
					break;
				}
			case RE_CHAR:
			case DOT:
				if(token.getValue().equals(".")){
					stream.add(token);
					break;
				}
			case CARET:
				if(token.getValue().equals("^")){
					stream.add(token);
					break;
				}
			case CLS_CHAR:
			case DASH:
				if(token.getValue().equals("-")){
					stream.add(token);
					break;
				}
			case IN:

			default:
				throw new IOException("Token not recognized: " + token.toString());
		}
	}
}
