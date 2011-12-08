package generator.parser;

import global.Token;

import java.util.ArrayList;
/**
 * 
 *
 */
public class NonTerminal extends LL1_Token {
	
	private ArrayList<Token<LL1_TokenType>> FollowSet;
	
	public NonTerminal(Token<LL1_TokenType> token) {
		super(token);
		FollowSet = new ArrayList<Token<LL1_TokenType>>();
	}
	
	
	
	/**
	 * 
	 * @param tok
	 */
	public void addToFollowSet(Token<LL1_TokenType> tok){
		FollowSet.add(tok);	
	}
	
	
	
	/* Accessors and Mutators	
	 */	



	public ArrayList<Token<LL1_TokenType>> getFollowSet() {
		return FollowSet;
	}
	public void setFollowSet(ArrayList<Token<LL1_TokenType>> followSet) {
		FollowSet = followSet;
	}
	
	
	
	
	
	
}
