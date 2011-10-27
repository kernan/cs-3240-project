
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
	<exclude-set-tail>  [<char-set>  | <defined-class>*/
	reg-ex(){
		rexp();
	}
	
	rexp(){
		rexp1();
		rexp$();
	}
	rexp$(){
		
	}
	public static void main(String[] args){

		File inputFile = "example.txt";
		SuperScanner supScan = new SuperScanner(inputFile);
		
	}
}
