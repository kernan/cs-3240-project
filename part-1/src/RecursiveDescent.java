import java.io.IOException;
import java.util.ArrayList;

public class RecursiveDescent {

	private ArrayList<Token> stream;
	private NFA nfa;
	private Lexer lex;
	
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
	 * @throws IOException 
	 */
	public RecursiveDescent() throws IOException {
		this.stream = new ArrayList<Token>();
		this.nfa = null;
		this.lex = new Lexer("example.txt");
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
		if (lex.peekToken() == TokenType.UNION){
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
		if(lex.peekToken() == TokenType.LPAREN || lex.peekToken() == TokenType.RE_CHAR
				|| lex.peekToken() == TokenType.DOT || lex.peekToken() == TokenType.LBRACKET
				|| lex.peekToken() == TokenType.DOLLAR){
			rexp2();
			rexp1$();
		}
		else
			return;
	}
//	<rexp2> -> (<rexp>) <rexp2Tail>  | RE_CHAR <rexp2Tail> | <rexp3>
	private void rexp2(){
		if(lex.peekToken() == TokenType.LPAREN){
			matchToken(TokenType.LPAREN);
			rexp();
			matchToken(TokenType.RPAREN);
			rexp2Tail();
		}
		else if(lex.peekToken() == TokenType.RE_CHAR){
			matchToken(TokenType.RE_CHAR);
			rexp2Tail();
		}
		else{
			rexp3();
		}
	}
//	<rexp2Tail> -> * | + |  E
	private void rexp2Tail(){
		if(lex.peekToken() == TokenType.MULTIPLY){
			matchToken(TokenType.MULTIPLY);
		}
		else if(lex.peekToken() == TokenType.PLUS){
			matchToken(TokenType.PLUS);
		}
		else
			return;
	}
//	<rexp3> -> <charClass>  |  E   
	private void rexp3(){
		if(lex.peekToken() == TokenType.DOT || lex.peekToken() == TokenType.LBRACKET
				|| lex.peekToken() == TokenType.DOLLAR){
			charClass();
		}
		else
			return;
	}
//	<charClass> ->  .  |  [ <charClass1>  | <definedClass>
	private void charClass(){
		if(lex.peekToken() == TokenType.DOT){
			matchToken(TokenType.DOT);
		}
		else if(lex.peekToken() == TokenType.LBRACKET){
			matchToken(TokenType.LBRACKET);
			charClass1();
		}
		else 
			definedClass();
	}
//	<charClass1> ->  <charSetList> | <excludeSet>
	private void charClass1(){
		if(lex.peekToken() == TokenType.CARET){
			excludeSet();
		}
		else{
			charSetList();
		}
	}
//	<charSetList> ->  <charSet> <charSetList> |  E 
	private void charSetList(){
		if(lex.peekToken() == TokenType.CLS_CHAR){
			charSet();
			charSetList();
		}
		else
			return;
	}
//	<charSet> -> CLS_CHAR <charSetTail> 
	private void charSet(){
		
		char start = matchToken(TokenType.CLS_CHAR);
		
		charSetTail(new ArrayList<Character>().add(start));
	}
//	<charSetTail> -> � CLS_CHAR | E
	private void charSetTail(ArrayList<Character> list){
		if(lex.peekToken() == TokenType.DASH){
			matchToken(TokenType.DASH);
			char end = matchToken(TokenType.CLS_CHAR);
			for(int i = ((int)list.get(0)); )
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
		if(lex.peekToken() == TokenType.LBRACKET){
			matchToken(TokenType.LBRACKET);
			charSet();
			matchToken(TokenType.RBRACKET);
		}
		else{
			definedClass();
		}
	}
	
	private void definedClass(){
		
	}

}

