
import java.util.ArrayList;
import java.util.Stack;

/**
 * dfa capable of processing input text
 */
public class DFA {
	
	private ArrayList<State> states;
	private NFA nfa;
	private int current;
	
	/**
	 * setup dfa with given alphabet and build it with given nfa
	 * @param alphabet set of symbols this dfa accepts
	 * @param nfa nfa to build this dfa from
	 */
	public DFA(NFA nfa) {
		this.states = new ArrayList<State>();
		this.current = 0;
		this.nfa = nfa;
		this.build_from_nfa(nfa);
	}
	
	/**
	 * build the dfa from a given nfa
	 * @param nfa nfa to build from
	 */
	private void build_from_nfa(NFA nfa) {
		ArrayList<State> table = convert_nfa(nfa);
		this.states = table;
	}
	
	/**
	 *
	 */
	public boolean gotoNext(char letter) {
		
		//if already at invalid state
		if(this.current == -1) {
			return false;
		}
		
		State s = this.states.get(this.current);
		int dfa_index = -1;
		//find transitions for given letter
		for(int i = 0; i < s.getTransitions().size(); i++) {
			if(s.getTransitions().get(i).getLetter() == letter) {
				dfa_index = s.getTransitions().get(i).getNext();
				break;
			}
		}
		this.current = dfa_index;
		if(current == -1) {
			return false;
		}
		return true;
	}
	
	/**
	 * check if current state is a final state
	 */
	public boolean atFinal() {
		ArrayList<Integer> s = this.states.get(this.current).getStatesSet();
		for(int i = 0; i < s.size(); i++) {
			if(nfa.get(s.get(i)).getEnd()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 *
	 */
	public void reset() {
		this.current = 0;
	}
	
	/**
	 * create a transition table from the given nfa
	 * @param nfa machine to build a transition table for
	 * @return transition table representing the given nfa
	 */
	private ArrayList<State> convert_nfa(NFA nfa) {
		//make initial states set
		ArrayList<Integer> S = new ArrayList<Integer>();
		//add start to initial states set
		S.add(nfa.getStart());
		//make new DFA start state
		State start = new State();
		//start = S + all equivalent states
		start.setStatesSet(epsilonClosure(S, nfa.getStates()));
		//make DFA transition table
		ArrayList<State> DTrans = new ArrayList<State>();
		//add start to DFA
		DTrans.add(start);
		//loop terminators
		boolean keep_going = true;
		int index = 0;
		while(keep_going) {
			//get next state in DFA transition list
			State T = DTrans.get(index);
			//get list of states in T
			ArrayList<Integer> states_set = T.getStatesSet();
			//for nfa states in current dfa state
			for(int i = 0; i < states_set.size(); i++) {
				//get transitions of current state in states_set
				ArrayList<NFA.State.Transition> transition_list = nfa.get(states_set.get(i)).getTransitions();
				//for transitions in current nfa state's transition list
				for(int j = 0; j < transition_list.size(); j++) {
					//get current transition
					NFA.State.Transition curr_transition = transition_list.get(j);
					//if NOT an epsilon transition & the letter doesn't already exist in the current DFA state
					if(curr_transition.getLetter() != NFA.EPSILON && 
							!DTrans.get(index).hasTransition(curr_transition.getLetter())) {
						//create next DFA state
						ArrayList<Integer> next_dfa_state = new ArrayList<Integer>();
						//state the current transition goes to to the next dfa state
						next_dfa_state.add(curr_transition.getNext());
						//START: transition find
						//for nfa states in current dfa state (**starting at current nfa state?)
						for(int k = 0; k < states_set.size(); k++) {
							//get transition list for currrent states set (in transition find)
							ArrayList<NFA.State.Transition> transition_list_temp = nfa.get(states_set.get(k)).getTransitions();
							//for transitions in current nfa state (in transition find)
							for(int l = 0; l < transition_list_temp.size(); l++) {
								//get current transition in current nfa state (in transition find)
								NFA.State.Transition transition_temp = transition_list_temp.get(l);
								//if the letters match & current temp next state is NOT in the new dfa state
								if(curr_transition.getLetter() == transition_temp.getLetter() &&
										!next_dfa_state.contains(transition_temp.getNext())) {
									//add the temp transition's next state to the new dfa state
									next_dfa_state.add(transition_temp.getNext());
								}
							}
						}
						//find all equivalent nfa states in next dfa state
						next_dfa_state = epsilonClosure(next_dfa_state, nfa.getStates());
						
						//dfa state is a dud
						boolean dud_state = false;
						int transition_index = -1;
						//check if any nfa state in next dfa state already exists in the dfa
						for(int k = 0; k < DTrans.size() && !dud_state; k++) {
							//get current DTrans state set
							ArrayList<Integer> nfa_state_set = DTrans.get(k).getStatesSet();
							//assume equivalancy
							boolean equivalent = true;
							//for states in the new dfa state
							for(int l = 0; l < next_dfa_state.size() && equivalent; l++) {
								//if doesn't exist in current dfa state
								if(!nfa_state_set.contains(next_dfa_state.get(l))) {
									//not equivalent
									equivalent = false;
								}
							}
							//if equivalency found
							if(equivalent) {
								//IS dud state
								dud_state = true;
								//found transition index
								transition_index = k;
							}
						}
							
						/*	//get next state
							Integer next_state = next_dfa_state.get(k);
							//for dfa state in the dfa
							for(int l = 0; l < DTrans.size() && !dud_state; l++) {
								//get current dfa state
								ArrayList<Integer> nfa_state_set = DTrans.get(l).getStatesSet();
								//if state already exists in the dfa
								if(nfa_state_set.contains(next_state)) {
									//this new dfa state is a dud
									dud_state = true;
									//add all states to the current dfa state (that don't already exist there)
									for(int m = 0; m < next_dfa_state.size(); m++) {
										if(!nfa_state_set.contains(next_dfa_state.get(m))) {
											nfa_state_set.add(next_dfa_state.get(m));
										}
									}
									
								}
							}
						}*/
						
						//if next state is NOT a dud
						if(!dud_state) {
							//create next DFA state
							State U = new State();
							//add states set to the new state
							U.setStatesSet(next_dfa_state);
							//add new state to the dfa
							DTrans.add(U);
							//add transition index
							T.addTransition(curr_transition.getLetter(), DTrans.size()-1);
						}
						else {
							T.addTransition(curr_transition.getLetter(), transition_index);
						}
					}
				}
			}
			
			index++;
			if(index >= DTrans.size()) {
				keep_going = false;
			}
		}
		
		return DTrans;
	}
	
	/**
	 * build set of all states that share an epsilon closure
	 * @param T set of states that already share an epsilon closure
	 * @return the set of states that share an epsilon closure
	 */
	private ArrayList<Integer> epsilonClosure(ArrayList<Integer> T, ArrayList<NFA.State> nfa_table) {
		
		Stack<Integer> stack = new Stack<Integer>();
		for(int i = 0; i < T.size(); i++) {
			stack.push(T.get(i));
		}
		while(!stack.empty()) {
			int t = stack.pop();
			ArrayList<NFA.State.Transition> t_transitions = nfa_table.get(t).getTransitions();
			for(int i = 0; i < t_transitions.size(); i++) {
				if(t_transitions.get(i).getLetter() == NFA.EPSILON) {
					int u = t_transitions.get(i).getNext();
					if(!T.contains(u)) {
						T.add(u);
						stack.push(u);
					}
				}
			}
		}
		return T;
	}
	
	/**
	 * minimize this dfa
	 */
	public void minimize() {
		//TODO minimize dfa
	}
	
	/**
	 * generate string representation of the dfa
	 */
	public String toString() {
		String result = "\n";
		for(int i = 0; i < this.states.size(); i++) {
			State s = this.states.get(i);
			ArrayList<Integer> s_set = s.getStatesSet();
			result += "States: ";
			for(int j = 0; j < s_set.size(); j++) {
				result += s_set.get(j) + ", ";
			}
			ArrayList<State.Transition> s_trans = s.getTransitions();
			result += "\nTrans: ";
			for(int j = 0; j < s_trans.size(); j++) {
				result += s_trans.get(j).getLetter() + "->" + s_trans.get(j).getNext() + "; ";
			}
			result += "\n";
		}
		return result;
	}
	
	/**
	 * dfa state with a single transition for every element in the dfa alphabet
	 */
	public class State {
		
		private ArrayList<Integer> states_set;
		private ArrayList<Transition> transitions;
		
		/**
		 * initialize state with empty transition list and states set
		 */
		public State() {
			this.states_set = new ArrayList<Integer>();
			this.transitions = new ArrayList<State.Transition>();
		}
		
		/**
		 * accessor for this dfa state's nfa state set
		 * @return the nfa state set corresponding to this dfa state
		 */
		public ArrayList<Integer> getStatesSet() {
			return this.states_set;
		}
		
		/**
		 * accessor for this dfa's transition list
		 * @return this dfa's transition list
		 */
		public ArrayList<State.Transition> getTransitions() {
			return this.transitions;
		}
		
		/**
		 * mutator for this dfa's states set
		 * @param states_set new states set for this dfa state
		 */
		public void setStatesSet(ArrayList<Integer> states_set) {
			this.states_set = states_set;
		}
		
		/**
		 * add transition to this state
		 * @param letter the letter to transition on
		 * @param next node to transition to
		 */
		public void addTransition(char letter, int next) {
			this.transitions.add(new Transition(letter, next));
		}
		
		/**
		 * set transition list from an outside source
		 * @param transitions list of transitions to set as the transition list
		 */
		public void setTransitions(ArrayList<State.Transition> transitions) {
			this.transitions = transitions;
		}
		
		/**
		 * check if a transition exists for the given letter
		 * @param letter value to check for a transition on
		 * @return true: a transition exists, false: does not
		 */
		public boolean hasTransition(char letter) {
			for(int i = 0; i < this.transitions.size(); i++) {
				if(transitions.get(i).getLetter() == letter) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * DFA transition
		 */
		public class Transition {
			
			private char letter;
			private int next_index;
			
			/**
			 * setup transition with given letter and index to next state
			 * @param letter value to transition on
			 * @param next_index index of state to transition to
			 */
			public Transition(char letter, int next_index) {
				this.letter = letter;
				this.next_index = next_index;
			}
			
			/**
			 * accessor for value this transition applies to
			 * @return value to transition on
			 */
			public char getLetter() {
				return this.letter;
			}
			
			/**
			 * accessor for state this transition goes to
			 * @return index to transition to
			 */
			public int getNext() {
				return this.next_index;
			}
			
			/**
			 * mutator for letter this transition applies to
			 * @param letter value to transition on
			 */
			public void setLetter(char letter) {
				this.letter = letter;
			}
			
			/**
			 * mutator for state this transition goes to
			 * @param next_index index of state to transition to
			 */
			public void setNext(int next_index) {
				this.next_index = next_index;
			}
		}
	}
}
