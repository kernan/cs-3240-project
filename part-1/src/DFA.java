import java.util.ArrayList;

/**
 * 
 */
public class DFA {
	
	private ArrayList<State> states;
	
	/**
	 * 
	 */
	public DFA() {
		this.states = new ArrayList<State>();
	}
	
	public ArrayList<State> getStates(){
		return this.states
	}
	
	/**
	 * 
	 */
	public class State {
		
		private ArrayList<Set> set;
		private ArrayList<Transition> transitions;
		
		/**
		 * 
		 */
		public State() {
			this.set = new ArrayList<Set>();
			this.transitions = new ArrayList<Transition>(); 
		}
		
		/**
		 * 
		 */
		public class Transition {
			
			private char letter;
			private State next;
			
			/**
			 * 
			 * @param letter
			 * @param next
			 */
			public Transition(char letter, State next) {
				this.letter = letter;
				this.next = next;
			}
		}
		
		/**
		 *
		 */
		public class Set {
			
			private ArrayList<NFA.State> states;
			
			/**
			 * 
			 */
			public Set() {
				this.states = new ArrayList<NFA.State>();
			}
		}
	}
}