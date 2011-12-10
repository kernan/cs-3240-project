package interpreter;

import generator.parser.LL1_Token;
import global.Lexer;

/**
 * 
 */
public class Script_Lexer extends Lexer<LL1_Token> {
	
	public Script_Lexer() {
		super();
	}
	
	@Override
	public int getPosition() {
		// TODO 
		return 0;
	}
	
	@Override
	protected LL1_Token makeNewToken() {
		// TODO Auto-generated method stub
		/*
		 * while peekNextChar() == ' '
		 *   getNextChar()
		 *   
		 * continue = false
		 * token = new String()
		 * type = null
		 * do {
		 *   t = peekNextChar()
		 *   for dfa : dfa_list
		 *     dfa.gotoNext(t)
		 *   continue = false;
		 *   for dfa : dfa_list
		 *     if !dfa.inDead()
		 *       continue = false
		 *   if continue
		 *     value += getNextChar()
		 *   else
		 *     for dfa : dfa_list
		 *       if dfa.inFinal()
		 *         type = dfa.getType()
		 *         break
		 * while(continue)
		 * 
		 * if type == null
		 *   Parse ERROR, unrecognized token
		 * 
		 * else
		 *   return new Token(value, type)
		 */
		return null;
	}
	//TODO
	
	
}
