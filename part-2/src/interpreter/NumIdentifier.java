package interpreter;

/**
 * 
 */
public class NumIdentifier extends Identifier {
	
	private int value;
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public NumIdentifier(String name, int value) {
		super(name);
		this.value = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getValue() {
		return this.value;
	}
}
