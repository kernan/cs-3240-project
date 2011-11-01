
import java.util.ArrayList;

/**
 * Represents an NFA for a given language.
 *
 * @author Jonathan Smith
 *
 */

public class NFA {
	
	/**
	 * The alphabet for this NFA, used for reference
	 */
	private ArrayList <CharClass>alphabet;
	
	/**
	 * The alphabet for this NFA
	 */
	private ArrayList states;
	
	/**
	 * Reference to final states
	 */
	private ArrayList <Boolean>finalStates;
	
	/**
	 * The number of states in the NFA
	 */
	private int size;
	
	/**
	 * Constructor, create initial NFA:
	 * assign the alphabet, set size to zero,
	 * set states to ,
	 * 
	 * @param alphabet The alphabet this NFA supports
	 */
	public NFA(ArrayList <CharClass>alphabet) {
		this.alphabet = alphabet;
		this.size = 0;
		
		this.states = new ArrayList();
	}
	
	/**
	 * Adds a state to the NFA
	 * 
	 * @param TODO: list of state transitions
	 */
	public void addState(){
		ArrayList newState = new ArrayList(states.size());
		
		for(int i = 0; i < newState.size(); i++){
			//TODO: initialize all lists to empty, -1?
		}
		//Add state to states
		//adds to finalStates
		size++;
	}
	
	/**
	 * Changes the state transitions on a particular event for given state
	 * 
	 * @param stateNum The number of the state to be edited
	 * @param event The alphabet character to edit
	 * @param transitions The updated transitions for the given event
	 */
	public void editState(int stateNum, CharClass event, ArrayList <CharClass> transitions){
		ArrayList state = (ArrayList)states.get(stateNum);
		ArrayList <CharClass>stateEvent = (ArrayList<CharClass>)state.get(findEventIndex(event));
		stateEvent = transitions;
	}
	
	/**
	 * Gets the index of an event in the alphabet
	 * 
	 * @param event The event to find
	 * @return Index of event
	 */
	public int findEventIndex(CharClass event){
		int index = -1;
		for(int i = 0; i < alphabet.size(); i++){
			if(alphabet.get(i).equals(event)){
				index = i;
				break;
			}
		}
		return index; 
		
	}
	
	/**
	 * Removes a state from the NFA
	 * 
	 * @param stateNum The number of the state to remove
	 */
	public void removeState(int stateNum){
		states.remove(stateNum);
		finalStates.remove(stateNum);
		size--;
	}
	
	/**
	 * Determines if a state is final or not
	 * 
	 * @param stateNum The number of the state
	 * @return True if final, false if non-final
	 */
	public Boolean isFinal(int stateNum){
		return finalStates.get(stateNum);
	}
	
	/**
	 * Sets a state to final or non-final
	 * 
	 * @param stateNum The number of the state to set
	 * @param ifFinal True if final, false if non-final
	 */
	public void setFinal(int stateNum, Boolean ifFinal){
		finalStates.set(stateNum, ifFinal);
	}
	
	/**
	 * accessor for nfa size
	 * @return size of the nfa
	 */
	public int size() {
		return this.size;
	}
	
}

