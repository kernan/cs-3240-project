package scanner_generator;

import java.util.ArrayList;

/**
 * NFA.java
 * Represents a non-useable nfa state table.
 */
public class NFA {
	
	public static final char EPSILON = '\u0000';
	
	private ArrayList<State> states;
	private int current, current_old;
	
	/**
	 * setup NFA with start state and empty current state
	 */
	public NFA() {
		this.states = new ArrayList<State>();
		this.current = 0;
		this.current_old = -1;
		this.addState();
		this.addState();
		this.addTransition(current, current+1, EPSILON);
		this.current++;
	}
	
	/**
	 * accessor for the start index of this nfa
	 * @return start index of this nfa, will always be 0
	 */
	public int getStart() {
		return 0;
	}
	
	/**
	 * accessor for the current index of this nfa
	 * @return current index of this nfa
	 */
	public int getCurrent() {
		return this.current;
	}
	
	/**
	 * accessor for the old current index of this nfa (one step back)
	 * @return old current index of this nfa
	 */
	public int getCurrentOld() {
		return this.current_old;
	}
	
	/**
	 * accessor for any state in the nfa
	 * @param index place the state appears
	 * @return the state that appears at the given index
	 */
	public State get(int index) {
		return this.states.get(index);
	}
	
	/**
	 * accessor for this nfa's states table
	 * @return the state table for this nfa
	 */
	public ArrayList<State> getStates() {
		return this.states;
	}
	
	/**
	 * mutator for end value of any given state
	 * @param index position of state to change
	 * @param end value to change it to
	 */
	public void setEnd(int index, boolean end) {
		this.states.get(index).setEnd(end);
	}
	
	/**
	 * accessor for the size of this nfa
	 * @return size of the nfa
	 */
	public int size() {
		return this.states.size();
	}
	
	/**
	 * add a new state to the nfa
	 * @return true: the state added successfully, false: it did not
	 */
	public boolean addState() {
		return this.states.add(new State());
	}
	
	/**
	 * add new transition from given state to given state on given letter
	 * @param start state the transition should go from
	 * @param next state the transition should go to
	 * @param letter value to transition on
	 * @return true: the transition added successfully, false: it did not
	 */
	public boolean addTransition(int start, int next, char letter) {
		return this.states.get(start).addTransition(letter, next);
	}
	
	/**
	 * concatenate a given nfa onto the end of this nfa
	 * @param other nfa to concatenate with
	 */
	public void concatenate(NFA other) {
		other.finalize();
		int c = this.size();
		//copy all states from other
		for(int i = 0; i < other.size(); i++) {
			this.addState();
			//copy all transitions
			ArrayList<State.Transition> trans = other.get(i).getTransitions();
			for(int j = 0; j < trans.size(); j++) {
				State.Transition temp = trans.get(j);
				this.addTransition(this.size()-1, temp.getNext()+c, temp.getLetter());
			}
		}
		//add transition from end of this to beginning of current
		this.addTransition(this.getCurrent(), c, EPSILON);
		//move current index
		this.current_old = other.getCurrentOld();
		this.current = other.getCurrent()+c;
		
		//remove all end states
		/*for(int i = 0; i < other.size(); i++) {
			State temp = other.get(i);
			if(temp.getEnd()) {
				temp.setEnd(false);
				temp.addTransition(EPSILON, this.current);
			}
		}*/
	}
	
	/**
	 * attach given nfa in parallel to this nfa
	 * @param other nfa to merge with
	 */
	public void merge(NFA other) {
		
		other.finalize();
		int c = this.size();
		
		//move current index
		this.current_old = this.current;
		this.current = other.getCurrent()+c;
		
		//copy all states from other
		for(int i = 0; i < other.size(); i++) {
			this.addState();
			//copy all transitions
			ArrayList<State.Transition> trans = other.get(i).getTransitions();
			for(int j = 0; j < trans.size(); j++) {
				State.Transition temp = trans.get(j);
				this.addTransition(this.size()-1, temp.getNext()+c, temp.getLetter());
			}
			if(other.get(i).getEnd()) {
				this.get(this.size()-1).setEnd(true);
			}
		}
		//remove all end states
		/*for(int i = 0; i < this.size(); i++) {
			State temp = this.get(i);
			if(temp.getEnd()) {
				temp.setEnd(false);
			}
		}*/
		//add transition from start of this to start of other
		this.addTransition(this.getStart(), other.getStart()+c, EPSILON);
	}
	
	/**
	 * merge all end states and current state into one
	 */
	public void finalize() {
		//setup a global end state
		this.addState();
		this.setEnd(this.size()-1, true);
		for(int i = 0; i < this.size()-1; i++) {
			State temp = this.get(i);
			if(temp.getEnd()) {
				//set end to false
				temp.setEnd(false);
				//add transition to new end
				temp.addTransition(EPSILON, this.size()-1);
			}
		}
		this.addTransition(this.getCurrent(), this.size()-1, EPSILON);
		this.current_old = this.current;
		this.current = this.size()-1;
	}
	
	/**
	 * generate string representation of the nfa
	 * @return string representation of the nfa
	 */
	public String toString() {
		String result = new String();
		for(int i = 0; i < this.size(); i++) {
			result += i + ": ";
			ArrayList<State.Transition> trans = this.get(i).getTransitions();
			for(int j = 0; j < trans.size(); j++) {
				if(trans.get(j).getLetter() == '\u0000') {
					result += "EPS, ";
				}
				else {
					result += "\'" + trans.get(j).getLetter() + "\', ";
				}
				result += trans.get(j).getNext() + "; ";
			}
			if(this.get(i).getEnd()) {
				result += "FINAL";
			}
			result += "\n";
		}
		return result;
	}
	
	/*==========================================================================
	 * regex operations
	 =========================================================================*/
	
	/**
	 * add concatenation from current to next on given letter
	 * @param letter value to concatenate
	 */
	public void addConcatenation(char letter) {
		this.addState();
		//current ---(letter)---> new
		this.addTransition(this.current, this.size()-1, letter);
		//current = new
		this.current_old = this.current;
		this.current = this.size()-1;
	}
	
	/**
	 * add concatenation from current to next on a list of letters
	 * @param letters list of value to concatenate
	 */
	public void addConcatenation(ArrayList<Character> letters) {
		this.addState();
		//current ---(letters)---> new
		for(int i = 0; i < letters.size(); i++) {
			this.addTransition(this.current, this.size()-1, letters.get(i));
		}
		//current = new
		this.current_old = this.current;
		this.current = this.size()-1;
	}
	
	/**
	 * add union from beginning to new state
	 */
	public void addAlternation() {
		this.addState();
		//start ---(EPS)--> new
		this.addTransition(this.getStart(), this.size()-1, EPSILON);
		//set current as final
		this.setEnd(this.current, true);
		//current = new
		this.current_old = this.getStart();
		this.current = this.size()-1;
	}
	
	/**
	 * add repitition (0 or more) from current
	 */
	public void addRepetitionKleene() {
		this.addState();
		//current ---(EPS)---> current_old
		this.addTransition(this.current, this.current_old, EPSILON);
		//current_old ---(EPS)---> new
		this.addTransition(this.current_old, this.size()-1, EPSILON);
		//current = new
		this.current_old = this.current;
		this.current = this.size()-1;
	}
	
	/**
	 * add repitition (0 or more) on whole nfa
	 */
	public void addRepetitionKleeneGlobal() {
		this.addState();
		//current ---(EPS)---> start
		this.addTransition(this.current, this.getStart(), EPSILON);
		//start ---(EPS)---> new
		this.addTransition(this.getStart(), this.size()-1, EPSILON);
		//current = new
		this.current_old = this.getStart();
		this.current = this.size()-1;
	}
	
	/**
	 * add repitition (1 or more) from current
	 */
	public void addRepetitionPlus() {
		this.addState();
		//current ---(EPS)---> current_old
		this.addTransition(this.current, this.current_old, EPSILON);
		//current ---(EPS)---> new
		this.addTransition(this.current, this.size()-1, EPSILON);
		//current = new
		this.current_old = this.current;
		this.current = this.size()-1;
	}
	
	/**
	 * add repitition (1 or more) on whole nfa
	 */
	public void addRepetitionPlusGlobal() {
		this.addState();
		//current ---(EPS)---> start
		this.addTransition(this.current, this.getStart(), EPSILON);
		//current ---(EPS)---> new
		this.addTransition(this.current, this.size()-1, EPSILON);
		//current = new
		this.current_old = this.current;
		this.current = this.size()-1;
	}
	
	/*==========================================================================
	 * END regex operations
	 =========================================================================*/
	
	/**
	 * NFA state
	 */
	public class State {
		
		private ArrayList<Transition> transitions;
		private boolean end;
		
		/**
		 * setup state with empty list of transitions
		 */
		public State() {
			this.transitions = new ArrayList<Transition>();
			end = false;
		}
		
		/**
		 * accessor for end flag for this state
		 * @return true: this state is a final state; false: it is not 
		 */
		public boolean getEnd() {
			return this.end;
		}
		
		/**
		 * mutator for end flag for this state
		 * @param end true: this state is a final state, false: it is not
		 */
		public void setEnd(boolean end) {
			this.end = end;
		}
		
		/**
		 * add new transition to this state
		 * @param letter value to transition on
		 * @param next_index index of state to transition to
		 * @return true: the transition added successfully, false: it did not
		 */
		public boolean addTransition(char letter, int next_index) {
			return this.transitions.add(new Transition(letter, next_index));
		}
		
		/**
		 * return all transitions in this state
		 * @return array containing all transitions in this state
		 */
		public ArrayList<Transition> getTransitions() {
			return this.transitions;
		}
		
		/**
		 * return all transitions for this state for the given letter
		 * @param letter value transitions transition on
		 * @return array containing all transitions in this state with given letter
		 */
		public ArrayList<Transition> getTransitions(char letter) {
			ArrayList<Transition> result = new ArrayList<Transition>();
			for(int i = 0; i < this.transitions.size(); i++) {
				if(this.transitions.get(i).getLetter() == letter) {
					result.add(this.transitions.get(i));
				}
			}
			return result;
		}
		
		/**
		 * NFA transition
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
