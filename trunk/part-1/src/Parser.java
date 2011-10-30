
public class Parser {

	
	Char currentToken;
	
	//returns the current token without consuming it
	public String peekToken(){
		return currentToken;
	}
	
	//takes in a token and matches it the regex language to make sure it is valid
	matchToken(Token tok){
		
	}
	
	/*<reg-ex>  <rexp> 
	<rexp> <rexp1> <rexp$>
	<rexp$>  UNION <rexp1> <rexp$>  |  E         
	<rexp1>  (<rexp>) <rexp1-tail>  | RE_CHAR <rexp1-tail1> | <rexp2>
	<rexp1-tail>  * <rexp> | + <rexp> |  E 
	<rexp1-tail1>  <rexp1-tail2> | <rexp>     
	<rexp1-tail2>  * <rexp> | + <rexp>
	<rexp2>  <char-class> <rexp>  |  E  
	<char-class>  .  |  [ <char-class1>  | <defined-class>
	<char-class1>   <char-set> | <exclude-set>
	<char-set>  CLS_CHAR ]  |  <range>
	<range>  CLS_CHAR – CLS_CHAR ]
	<exclude-set>  ^ <char-set>] IN <exclude-set-tail>  
	<exclude-set-tail>  [<char-set>  | <defined-class>   */
	//<reg-ex>  <rexp>
	reg-ex(){ 
		rexp();
	}
	//<rexp> <rexp1> <rexp$>
	rexp(){
		rexp1();
		rexp$();
	}
	//<rexp$>  UNION <rexp1> <rexp$>  |  E 
	rexp$(){
		//UNION
		rexp1();
		rexp$();
		else
			return;
	}
	//<rexp1>  (<rexp>) <rexp1-tail>  | RE_CHAR <rexp1-tail1> | <rexp2>
	rexp1(){
		if ( peekToken() == LPAREN){
			matchToken(LPAREN); 
		    rexp(); 
	        matchToken(RPAREN);
	        rexp1Tail();
	    }
		else if(peekToken() == RE_CHAR){
			rexp1Tail1();
		}
		else{
			rexp2();
		}
	}
	//<rexp1-tail>  * <rexp> | + <rexp> |  E
	rexp1Tail(){
		if(peekToken() == MULT){
			matchToken(MULT);
			rexp();
		}
		else if(peekToken() == PLUS){
			rexp();
		}
		else
			return;
	}
	//<rexp1-tail1>  <rexp1-tail2> | <rexp>
	rexp1Tail1(){
		if
	}
	//<rexp1-tail2>  * <rexp> | + <rexp>
	//<rexp2>  <char-class> <rexp>  |  E  
	//<char-class>  .  |  [ <char-class1>  | <defined-class>
	//<char-class1>   <char-set> | <exclude-set>
	//<char-set>  CLS_CHAR ]  |  <range>
	//<range>  CLS_CHAR – CLS_CHAR ]
	//<exclude-set>  ^ <char-set>] IN <exclude-set-tail>  
	//<exclude-set-tail>  [<char-set>  | <defined-class> 
	excludeSetTail(){
		
	}
	public static void main(String[] args){

		File inputFile = "example.txt";
		Lexer lex = new Lexer(inputFile);
		
	}
}
