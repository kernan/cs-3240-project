package generator.parser;

import global.Token;

import java.util.ArrayList;

/**
 * 
 */
public class NonTerminal extends LL1_Token {
	
	private ArrayList<Terminal> FollowSet;
	
	/**
	 * 
	 * @param token
	 */
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
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Terminal> getFollowSet() {
		return FollowSet;
	}
	
	/**
	 * 
	 * @param followSet
	 */
	public void setFollowSet(ArrayList<Terminal> followSet) {
		FollowSet = followSet;
	}
}
