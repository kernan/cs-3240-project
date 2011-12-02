package scanner_generator;

/** NFA_Identifier.java
 *	Maps an nfa with a defined class' name and character class flag.
 */

public class NFA_Identifier {
	
	private String name;
	private NFA nfa;
	private boolean char_class;
	
	/**
	 * initialize an nfa identifier with given name and nfa
	 * @param name the name of the defined class this nfa represents
	 * @param nfa the state machine that corresponds to the defined class
	 */
	public NFA_Identifier(String name, NFA nfa, boolean char_class) {
		this.name = name;
		this.nfa = nfa;
		this.char_class = char_class;
	}
	
	/**
	 * accessor for this defined class' name
	 * @return the name of this defined class representation
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * mutator for this defined class' name
	 * @param name the new name for this defined class representation
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * accessor for this defined class' state machine
	 * @return the state machine for this defined class representation
	 */
	public NFA getNFA() {
		return this.nfa;
	}
	
	/**
	 * accessor for this defined class' char class status
	 * @return true: this defined class is a character class, false: it is not
	 */
	public boolean getCharClass() {
		return this.char_class;
	}
	
	/**
	 * checks if this defined class has the same name as another defined class
	 * @param other defined class to compare to
	 * @return true: they are equal, false: they are not
	 */
	@Override
	public boolean equals(Object other) {
		return this.name.equals(((NFA_Identifier)other).getName());
	}
}
