import java.util.ArrayList;
import java.util.Stack;

public class BuildNFA {

	/**
	 * build NFA from given input stream
	 * @param stream regular expression
	 * @return nfa for given regular expression
	 */
	public static NFA buildNFA(ArrayList<Token> stream) {
		Stack<NFA> stack = new Stack<NFA>();
		
		for(int i = 0; i < stream.size(); i++) {
			Token tok = stream.get(i);
			switch(tok.getType()) {
				//scope out
				case LPAREN:
					/* 
					 * create new nfa
					 * push new
					 */
					break;
				//scope back in
				case RPAREN:
					/* 
					 * pop
					 * pop
					 * combine 2 nfas (combine all end states)
					 * push changes
					 */
					break;
				//repetition (0 or more)
				case MULTIPLY:
					/*
					 * pop
					 * new nfa
					 * new start state with Epsilon trans to old start state
					 * loopback from end to start
					 * epsilon transition from start to new state (this is the current now)
					 * push changes
					 */
					break;
				//repetition (1 or more)
				case PLUS:
					/*
					 * pop
					 * loopback from last state to first state
					 * epsilon transition from last state to new state (this is the current now)
					 * push changes
					 */
					break;
				//concatenation
				case IDENTIFIER:
					/*
					 * pop
					 * add transition from last state to new state (this is the current now)
					 * push changes
					 */
					break;
				//alternation
				case OR:
					/*
					 * pop
					 * add split at beginning
					 * (current state = new branch)
					 * push changes
					 */
					break;
			}
		}
		//cleanup final nfa
		/*
		 * combine end states into 1 end state
		 */
		
		return null;
	}
}
