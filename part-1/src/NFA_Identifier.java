
public class NFA_Identifier {
	
	private String name;
	private NFA nfa;
	private boolean char_class;
	
	/**
	 * 
	 * @param name
	 * @param nfa
	 */
	public NFA_Identifier(String name, NFA nfa, boolean char_class) {
		this.name = name;
		this.nfa = nfa;
		this.char_class = char_class;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public NFA getNFA() {
		return this.nfa;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getCharClass() {
		return this.char_class;
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	@Override
	public boolean equals(Object other) {
		return this.name.equals(((NFA_Identifier)other).getName());
	}
}
