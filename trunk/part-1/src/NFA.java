
import java.util.ArrayList;

/**
 * Represents an NFA for a given language.
 *
 * @author Jonathan Smith
 *
 */


/**
 *
 */
public class NFA {
	
	
	private State start, end, current;
	int size;
	
	public static final char EPSILON = '\u0000';
	
	/**
	 * setup nfa with start, end, and current state
	 * add unconditional transition from start to current
	 * @param alphabet
	 */
	public NFA() {
		this.size = 0;
		this.start = this.addState();
		this.end = this.addState();
		this.current = this.addTransition(this.start, new State(), EPSILON);
		this.current.setPrev(this.start);
	}
	
	/**
	 * add transition from given state to given state for given value
	 * @param t1 state to transition from
	 * @param t2 state to transition to
	 * @param letter value to transition on
	 * @return state that was transitioned to
	 */
	public State addTransition(State t1, State t2, char letter) {
		t1.addTransition(letter, t2);
		return t2;
	}
	
	/**
	 * add new state to the nfa
	 * @return state that was added
	 */
	public State addState() {
		size++;
		return new State();
	}
	
	/**
	 * accessor for NFA start state
	 * @return start state
	 */
	public State getStart() {
		return this.start;
	}
	
	/**
	 * attach a given nfa to the end of this nfa
	 * @param other nfa to merge with
	 */
	public void concat(NFA other) {
		this.addTransition(this.current, this.end, EPSILON);
		this.addTransition(this.end, other.getStart(), EPSILON);
		this.size += other.size();
		this.current = other.getCurr();
		this.end = other.getEnd();
	}
	
	/**
	 * accessor for NFA end state
	 * @return end state
	 */
	public State getEnd() {
		return this.end;
	}
	
	/**
	 * accessor for current state
	 * @return current state
	 */
	public State getCurr() {
		return this.current;
	}
	
	/**
	 * accessor for size of the nfa
	 * @return size of nfa
	 */
	public int size() {
		return this.size;
	}
	
	/**
	 * finalize the nfa (add transition from current state to end)
	 */
	public void finalize() {
		this.addTransition(this.current, this.end, this.EPSILON);
	}
	
	/**
	 * mutator for current state
	 * @param curr new current state
	 */
	public void setCurr(State current) {
		this.current = current;
	}
	
	/**
	 * state containing list of transitions 
	 */
	public class State {
		
		private ArrayList<Transition> transitions;
		private State previous;
		
		/**
		 * setup state with empty transition list and previous state
		 */
		public State() {
			this.transitions = new ArrayList<Transition>();
			this.previous = null;
		}
		
		/**
		 * mutator for previous state
		 * @param prev state to set as previous
		 */
		public void setPrev(State previous) {
			this.previous = previous;
		}
		
		/**
		 * accessor for previous state
		 * @return previous state
		 */
		public State getPrev() {
			return this.previous;
		}
		
		/**
		 * add transition from this state to given state 
		 * @param letter transition value
		 * @param next state to transition to
		 */
		public void addTransition(char letter, State next) {
			transitions.add(new Transition(letter, next));
		}
		
		/**
		 * map containing transition character and state
		 */
		private class Transition {
			private char letter;
			private State next;
			
			/**
			 * setup transition for particular symbol to specified state
			 * @param letter symbol to transition on
			 * @param next state to transition to
			 */
			public Transition(char letter, State next) {
				this.letter = letter;
				this.next = next;
			}
		}
	}
}

