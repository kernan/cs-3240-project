package generator.parser;

import java.util.ArrayList;

import global.Token;

/**
 * 
 *
 */
public class LL1_Rule {
	private Token<LL1_TokenType> nonTerm;
	private ArrayList<LL1_Token> TNTList;
	
	/**
	 * 
	 * @param nonTerm
	 */
	public LL1_Rule(Token<LL1_TokenType> nonTerm){
		this.nonTerm = nonTerm;
	}
	/**
	 * 
	 * @param tok
	 */
	public void addToTNTList(LL1_Token tok){
		TNTList.add(tok);	
	}
	
/* Accessors and Mutators
 */

	public Token<LL1_TokenType> getNonTerm() {
		return nonTerm;
	}

	public void setNonTerm(Token<LL1_TokenType> nonTerm) {
		this.nonTerm = nonTerm;
	}

	public ArrayList<Token<LL1_TokenType>> getTNTList() {
		return TNTList;
	}

	public void setTNTList(ArrayList<Token<LL1_TokenType>> tNTList) {
		TNTList = tNTList;
	}
	
	
}
