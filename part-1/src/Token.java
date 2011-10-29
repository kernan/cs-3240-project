/**
 * Represents a token of specified type with specified value.
 * 
 * ex: Token(IDENTIFIER, "foo") <- IDENTIFIER with name "foo"
 * 
 * @author Robert Kernan
 *
 */

//TODO determine how types should be determined
public class Token {

	private TokenType type;
	private String value;
	
	/**
	 * Constructor: create token with given values
	 * @param type type of token
	 * @param value string the token represents
	 */
	public Token(TokenType type, String value) {
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Accessor for the token type
	 * @return the token's type
	 */
	public TokenType getType() {
		return this.type;
	}
	
	/**
	 * Accessor for string token represents
	 * @return the token's value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * Mutator for token type
	 * @param type
	 */
	public void setType(TokenType type) {
		this.type = type;
	}
	
	/**
	 * Mutator for string token represents
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
