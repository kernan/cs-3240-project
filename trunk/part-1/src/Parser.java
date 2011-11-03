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
	
//	<regEx> ->  <rexp> 
	regEx(){
		rexp();
	}
//	<rexp> -> <rexp1> <rexp$>
	rexp(){
		rexp1();
		rexp$();
	}
//	<rexp$> -> UNION <rexp1> <rexp$>  |  E    
	rexp$(){
		if (peekToken() == TokenType.UNION){
			matchToken(TokenType.UNION);
			rexp1();
			rexp$();
		}
		else
			return;
	}
//	<rexp1> -> <rexp2> <rexp1$>
	rexp1(){
		rexp2();
		rexp1$();
	}
//	<rexp1$> -> <rexp2> <rexp1$>  |  E   
	rexp1$(){
		if(peekToken() == TokenType.LPAREN ){//TODO fix all cases
			rexp2();
			rexp1$();
		}
		else
			return;
	}
//	<rexp2> -> (<rexp>) <rexp2Tail>  | RE_CHAR <rexp2Tail> | <rexp3>
	rexp2(){
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
	rexp2Tail(){
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
	rexp3(){
		if(peekToken() == TokenType.DOT || peekToken() == TokenType.LBRACKET ){//TODO add definedClass check
			charClass();
		}
		else
			return;
	}
//	<charClass> ->  .  |  [ <charClass1>  | <definedClass>
	charClass(){
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
	charClass1(){
		if(peekToken() == TokenType.CARET){
			excludeSet();
		}
		else{
			charSetList();
		}
	}
//	<charSetList> ->  <charSet> <charSetList> |  E 
	charSetList(){
		if(peekToken() == TokenType.CLS_CHAR){
			charSet();
			charSetList();
		}
		else
			return;
	}
//	<charSet> -> CLS_CHAR <charSetTail> 
	charSet(){
		matchToken(TokenType.CLS_CHAR);
		charSetTail();
	}
//	<charSetTail> -> – CLS_CHAR | E
	charSetTail(){
		if(peekToken() == TokenType.DASH){
			matchToken(TokenType.DASH);
			matchToken(TokenType.CLS_CHAR);
		}
		else
			return;
	}
//	<excludeSet> -> ^ <charSet>] IN <excludeSetTail>  
	excludeSet(){
		matchToken(TokenType.CARET);
		charSet();
		matchToken(TokenType.RBRACKET);
		matchToken(TokenType.IN);
		excludeSetTail();
	}
//	<excludeSetTail> -> [<charSet>]  | <definedClass>
	excludeSetTail(){
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
