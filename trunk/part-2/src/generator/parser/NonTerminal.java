package generator.parser;

import global.Token;

import java.util.ArrayList;
/**
 * 
 *
 */
public class NonTerminal extends LL1_Token {
	
	private ArrayList<Terminal> FollowSet;
	
	public NonTerminal(Token<LL1_TokenType> token) {
		super(token);
		FollowSet = new ArrayList<Terminal>();
	}
	
	
	
	/**
	 * 
	 * @param tok
	 */
	public void addToFollowSet(Terminal tok){
		FollowSet.add(tok);	
	}
	
	
	
	/* Accessors and Mutators	
	 */	



	public ArrayList<Terminal> getFollowSet() {
		return FollowSet;
	}
	public void setFollowSet(ArrayList<Terminal> followSet) {
		FollowSet = followSet;
	}
	
	
	
	
	
	
}
