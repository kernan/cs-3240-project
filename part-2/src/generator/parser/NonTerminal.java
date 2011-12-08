package generator.parser;

import global.Token;

import java.util.ArrayList;
/**
 * 
 *
 */
public class NonTerminal extends LL1_Token {
	
	private ArrayList<Token<Terminal>> FollowSet;
	
	public NonTerminal(Token<LL1_TokenType> token) {
		super(token);
		FollowSet = new ArrayList<Token<Terminal>>();
	}
	
	
	
	/**
	 * 
	 * @param tok
	 */
	public void addToFollowSet(Token<Terminal> tok){
		FollowSet.add(tok);	
	}
	
	
	
	/* Accessors and Mutators	
	 */	



	public ArrayList<Token<Terminal>> getFollowSet() {
		return FollowSet;
	}
	public void setFollowSet(ArrayList<Token<Terminal>> followSet) {
		FollowSet = followSet;
	}
	
	
	
	
	
	
}
