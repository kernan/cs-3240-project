
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;

/**
 *
 */
public class Parser {
	
	public static void main(String[] args) throws FileNotFoundException, ParseException {
		
		System.out.println("GrammarParser start...\n================================================================================\n");
		
		//tokenizer
		Lexer input = new Lexer(new Scanner(new File(args[0])));
		//list of all <rexp> (as nfas)
		ArrayList<NFA_Identifier> nfa_list = new ArrayList<NFA_Identifier>();
		
		do {
			Token id = input.getNextToken();
			System.out.println("     [Parser] got ID: " + id.getValue());
			if(id.getType() != TokenType.DEFINED) {
				throw new ParseException("Invalid syntax, new lines must begin with a id definition", -1);
			}
			RecursiveDescent rd = new RecursiveDescent(input, nfa_list);
			NFA_Identifier new_nfa = rd.descend();
			new_nfa.setName(id.getValue());
			nfa_list.add(new_nfa);
		} while(input.gotoNextLine());
		
		System.out.println("     [Parser] merging...");
		
		NFA big_nfa = nfa_list.get(2).getNFA();
		
		System.out.println("     [Parser] defined class?");
		/*NFA big_nfa = new NFA();
		for(int i = 0; i < nfa_list.size(); i++) {
			System.out.println("     [Parser]\t" + i + ": " + nfa_list.get(i).getCharClass());
			if(!nfa_list.get(i).getCharClass()) {
				System.out.println("     [Parser] merging #" + i + " with final");
				big_nfa.merge(nfa_list.get(i).getNFA());
			}
		}*/
		
		System.out.println("     [Parser] BIG NFA STATES");
		ArrayList<NFA.State> states = big_nfa.getStates();
		for(int i = 0; i < states.size(); i++) {
			System.out.print("\t" + i + ": " + states.get(i) + ":\t");
			ArrayList<NFA.State.Transition> trans = states.get(i).getTransitions();
			for(int j = 0; j < trans.size(); j++) {
				System.out.print(trans.get(j).getLetter() + "->" + trans.get(j).getNext() + "\t|\t");
			}
			System.out.print("\n");
		}
		
		System.out.println("     [Parser] Converting to DFA...");
		ArrayList<ArrayList<NFA.State>> dfa = convertNFA(big_nfa);
		
		for(int i = 0; i < dfa.size(); i++) {
			System.out.println("\t" + i + ": ");
			for(int j = 0; j < dfa.get(i).size(); j++) {
				for(int k = 0; k < dfa.get(i).get(j).getTransitions().size(); k++) {
					NFA.State.Transition trans = dfa.get(i).get(j).getTransitions().get(k);
					System.out.print(trans.getLetter() + "->" + trans.getNext() + "\t|\t");
				}
			}
			System.out.println();
		}
	}
	
	/**
	 * 
	 * @param nfa
	 */
	public static ArrayList<ArrayList<NFA.State>> convertNFA(NFA nfa) {
		ArrayList<NFA.State> S = new ArrayList<NFA.State>();
		S.add(nfa.getStart());
		ArrayList<ArrayList<NFA.State>> DTrans = new ArrayList<ArrayList<NFA.State>>();
		//DTrans = e_closure of S
		DTrans.add(epsilonClosure(S));
		//while unmarked state T in DTrans
		boolean keep_going = true;
		int index = 0;
		while(keep_going) {
			//get next Set
			ArrayList<NFA.State> T = DTrans.get(index);//implicitly marked
			//for each input symbol 'a'
			for(int i = 0; i < T.size(); i++) {
				ArrayList<NFA.State.Transition> trans = T.get(i).getTransitions();
				for(int j = 0; j < ascii.length; j++) {
					for(int k = 0; k < trans.size(); k++) {
						NFA.State.Transition a = trans.get(k);
						if(a.getLetter() == ascii[j]) {
							ArrayList<NFA.State> list = new ArrayList<NFA.State>();
							list.add(a.getNext());
							ArrayList<NFA.State> u = epsilonClosure(list);
							//add to DTrans if doesn't already exist
							boolean exists = false;
							for(int i_1 = 0; i_1 < u.size(); i_1++) {
								for(int i_2 = 0; i_2 < DTrans.size(); i_2++) {
									for(int i_3 = 0; i_3 < DTrans.get(i_2).size(); i_3++) {
										if(u.get(i_1) == DTrans.get(i_2).get(i_3)) {
											exists = true;
										}
									}
								}
							}
							if(!exists) {
								DTrans.add(u);
							}
						}
					}
				}
			}
			
			//terminating condition
			index++;
			if(index >= DTrans.size()) {
				keep_going = false;
			}
		}
		return DTrans;
	}
	
	/**
	 * 
	 * @param T
	 * @return
	 */
	public static ArrayList<NFA.State> epsilonClosure(ArrayList<NFA.State> T) {
		Stack<NFA.State> stack = new Stack<NFA.State>();
		//push all states in T into stack
		for(int i = 0; i < T.size(); i++) {
			stack.push(T.get(i));
		}
		//initialize e-closure(T) to T
		ArrayList<NFA.State> e_closure = T;
		//while stack is not empty
		while(!stack.empty()) {
			//get next state in stack
			NFA.State t = stack.pop();
			//for each state u with epsilon-edge t to u
			ArrayList<NFA.State.Transition> t_transitions = t.getTransitions();
			for(int i = 0; i < t_transitions.size(); i++) {
				if(t_transitions.get(i).getLetter() == NFA.EPSILON) {
					//get u
					NFA.State u = t_transitions.get(i).getNext();
					//if u not in e_closure(T)
					if(!T.contains(u)) {
						//add u to e_closure(T)
						T.add(u);
						//push u onto stack
						stack.push(u);
					}
				}
			}
		}
		return e_closure;
	}
	
	/*
	 * ASCII printable characters (dfa alphabet)
	 */
	 public static final char[] ascii = {
             ' ', '!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?', 
             '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 
             'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '[', '\\', ']', '^', '_', 
             '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 
             'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '{', '|', '}', '~'
     };

}
