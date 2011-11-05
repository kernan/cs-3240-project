import java.util.ArrayList;
import java.util.Stack;
import java.io.IOException;

public class NFA_Utils {

	
	public char[] ascii = {
			' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', 
			'@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 
			'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', 
			'`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'
		};
	
	public String[] RE_CHAR = {
			"\\ ", "!", "\"", "#", "$", "%", "&", "\'", "\\(", "\\)", "\\*", "\\+", ",", "-", "\\.", "/",
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "\\?", 
			"@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", 
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "\\[", "\\", "\\]", "^", "_", 
			"`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", 
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "\\|", "}", "~"	
			
			
	};
	
	public String[] CLS_CHAR = {
			" ", "!", "\"", "#", "$", "%", "&", "\'", "(", ")", "*", "+", ",", "\\-", ".", "/",
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ":", ";", "<", "=", ">", "?", 
			"@", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", 
			"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "\\[", "\\", "\\]", "\\^", "_", 
			"`", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", 
			"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "{", "|", "}", "~"	
			
			
	};
	
	
	
	
	/**
	 * build NFA from given input stream
	 * @param stream regular expression
	 * @param alphabet set of characters to use as alphabet
	 * @return nfa for given regular expression
	 * @throws IOException if a token is found that does not exist in the alphabet, if parentheses unbalanced
	 */
	public static NFA buildNFA(ArrayList<Token> stream, ArrayList<Character> alphabet) throws IOException {
		Stack<NFA> stack = new Stack<NFA>();
		
		//temp vars
		NFA t1;
		NFA t2;
		NFA.State s1;
		
		//keep track of scoping
		boolean scope_back = false;
		
		//create initial nfa
		stack.push(new NFA(alphabet));
		
		for(int i = 0; i < stream.size(); i++) {
			Token tok = stream.get(i);
			switch(tok.getType()) {
				//scope out
				case LPAREN:
					stack.push(new NFA(alphabet));
					break;
				//scope back in
				case RPAREN:
					if(stack.size() == 1) {
						throw new IOException("Invalid input stream: parentheses unbalanced");
					}
					scope_back = true;
					break;
				//repetition (0 or more)
				case MULTIPLY:
					if(scope_back) {
						scope_back = false;
						t2 = stack.pop();
						
						//current ---(EPSILON)---> start
						//start ---(EPSILON)---> new
						//current = new
						t2.addTransition(t2.getCurr(), t2.getStart(), NFA.EPSILON);
						s1 = t2.addTransition(t2.getStart(), t2.addState(), NFA.EPSILON);
						t2.setCurr(s1);
						
						t1 = stack.pop();
						t1.concatNFA(t2);
						stack.push(t1);
					}
					else {
						t2 = stack.pop();
						
						//current ---(EPSILON)---> current.previous
						//current.previous ---(EPSILON)---> new
						//current = new
						s1 = t2.addTransition(t2.getCurr(), t2.getCurr().getPrev(), NFA.EPSILON);
						t2.setCurr(s1);
						s1 = t2.addTransition(t2.getCurr(), t2.addState(), NFA.EPSILON);
						s1.setPrev(t2.getCurr());
						t2.setCurr(s1);
						
						stack.push(t2);
					}
					break;
				//repetition (1 or more)
				case PLUS:
					if(scope_back) {
						scope_back = false;
						t2 = stack.pop();
						
						//current ---(EPSILON)---> start
						//current = current
						t2.addTransition(t2.getCurr(), t2.getStart(), NFA.EPSILON);
						
						t1 = stack.pop();
						t1.concatNFA(t2);
						stack.push(t1);
					}
					else {
						t2 = stack.pop();
						
						//current ---(EPSILON)---> current.previous
						//current = current
						t2.addTransition(t2.getCurr(), t2.getCurr().getPrev(), NFA.EPSILON);
						
						stack.push(t2);
					}
					break;
				//concatenation
				case CHAR_CLASS:
					//TODO
					break;
				case IDENTIFIER:
					t2 = stack.pop();
					
					//TODO handle character classes
					
					//make sure token is valid (in the alphabet)
					if(alphabet.contains(tok.getValue().charAt(0))) {
						throw new IOException("Invalid input stream: character not in alphabet: " + tok.getValue());
					}
					
					//current ---(Token.value)---> new
					//current = new
					s1 = t2.addTransition(t2.getCurr(), t2.addState(), tok.getValue().charAt(0));
					s1.setPrev(t2.getCurr());
					t2.setCurr(s1);
					
					if(scope_back) {
						scope_back = false;
						t1 = stack.pop();
						t1.concatNFA(t2);
						stack.push(t1);
					}
					else {
						stack.push(t2);
					}
					break;
				//alternation
				case OR:
					t2 = stack.pop();
					
					//current ---(EPSILON)---> end
					//current = start
					//current ---(EPSILON)---> new
					//current = new
					t2.addTransition(t2.getCurr(), t2.getEnd(), NFA.EPSILON);
					s1 = t2.addTransition(t2.getStart(), t2.addState(), NFA.EPSILON);
					s1.setPrev(t2.getStart());
					t2.addTransition(t2.getStart(), s1, NFA.EPSILON);
					t2.setCurr(s1);
					
					if(scope_back) {
						scope_back = false;
						t1 = stack.pop();
						t1.concatNFA(t2);
						stack.push(t1);
					}
					else {
						stack.push(t2);
					}
					break;
			}
		}
		//cleanup final nfa
		t1 = stack.pop();
		//make sure stack is empty
		if(stack.size() != 0) {
			throw new IOException("Invalid input stream: parentheses unbalanced");
		}
		//add transition to end
		t1.addTransition(t1.getCurr(), t1.getEnd(), NFA.EPSILON);
		
		return t1;
	}
}
