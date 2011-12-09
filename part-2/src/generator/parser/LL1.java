package generator.parser;

import global.Token;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * LL1.java
 * LL1 parser capable of processing input text
 */
public class LL1 {
	
	private static ArrayList<LL1_Rule> ruleList = new ArrayList<LL1_Rule>();
	private static ArrayList<Terminal> termList = new ArrayList<Terminal>();
	private static boolean changeFlag = true;
	private static NonTerminal startSymbol;
	
	public static void main(String[] args) throws FileNotFoundException{
		Grammar_Lexer lex = new Grammar_Lexer("minire-specification.txt");
		
		//Skip to first Header
		while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
			lex.getNextToken();
		}
		
		//Check for Headers
		
		if(lex.peekNextToken().getValue() == "Tokens"){
		    //Skip to next line to get list of tokens	
			while(lex.peekNextToken().getType() != LL1_TokenType.TERMINAL){
				lex.getNextToken();
			}
			
			//add all terminals to termList
			while(lex.peekNextToken().getType() != LL1_TokenType.EOL){
				termList.add(new Terminal(lex.getNextToken()));
			}

			//skip to next header
			while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
				lex.getNextToken();
			}
			
			//make sure next header is Start
			if(lex.peekNextToken().getValue() == "Start"){
				//skip to start symbol
				while(lex.peekNextToken().getType() != LL1_TokenType.NON_TERMINAL){
					lex.getNextToken();
				}
				//consume start symbol
				startSymbol = new NonTerminal(lex.getNextToken());
			}
			else{
				//TODO error
				System.out.println("Start symbol must come after tokens");
			}
		}
		else if(lex.peekNextToken().getValue() == "Start"){
				//skip to start symbol
				while(lex.peekNextToken().getType() != LL1_TokenType.NON_TERMINAL){
					lex.getNextToken();
				}
				//consume start symbol
				startSymbol = new NonTerminal(lex.getNextToken());
				
				//skip to next header
				while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
					if(lex.peekNextToken().getType() == LL1_TokenType.EOF){
						//TODO error
					}
					lex.getNextToken();
				}
				
				if(lex.peekNextToken().getValue() == "Tokens"){
					
					//skip to tokens
					while(lex.peekNextToken().getType() != LL1_TokenType.TERMINAL){
						lex.getNextToken();
					}
					
					//consume each token and add to terminal list
					while(lex.peekNextToken().getType() != LL1_TokenType.EOL){
						termList.add(new Terminal(lex.getNextToken()));
					}
				}
				else{
					//TODO error
					System.out.println("Tokens must come after start symbol");
				}	
		}
		else{
			//TODO error catching
			System.out.println("Tokens and start symbol must be listed before rules");
		}
		
		//skip to rules header
		while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
			lex.getNextToken();
		}
		
		if(lex.peekNextToken().getValue() == "Rules"){
			lex.getNextToken();
		}
		else{
			//TODO error
		}
		
		//skip to the rules lists
		while(lex.peekNextToken().getType() != LL1_TokenType.NON_TERMINAL){
			if(lex.peekNextToken().getType() == LL1_TokenType.EOF){
				//TODO error
			}
			lex.getNextToken();
		}
		
		//turn all non-terminals into a rule and add their tokens to the rule
		while(lex.peekNextToken().getType() != LL1_TokenType.EOF){	
			
			LL1_Rule currRule = new LL1_Rule(new NonTerminal(lex.getNextToken()));
			ruleList.add(currRule);
			while(lex.peekNextToken().getType() != LL1_TokenType.EOL && 
			      lex.peekNextToken().getType() != LL1_TokenType.EOF){
				//if next token is a terminal, take the object out of termlist
				//and add it to the current rule
				Terminal testTerm = new Terminal(lex.peekNextToken());
				if(termList.contains(testTerm)){
					int location = termList.indexOf(testTerm);
					currRule.addToTNTList(termList.get(location));
				}
				//check to see if our new nonterminal is already in the rule list
				NonTerminal curNonTerm = new NonTerminal(lex.getNextToken());
				for(int i = 0; i < ruleList.size(); i++){
					NonTerminal nonTerm = ruleList.get(i).getNonTerm();
					if(nonTerm.equals(curNonTerm)){
						curNonTerm = nonTerm;
					}
				}
				//if the new nonterminal is not a duplicate, make it into a new rule
				//and add it to the rule list, if it is a duplicate, curNonTerm now
				//points to the duplicate, so make it into a new rule anyways
				ruleList.add(new LL1_Rule(curNonTerm));
				
				currRule.addToTNTList(new NonTerminal(lex.getNextToken()));
			}
			//consume EOL token
			lex.getNextToken();
			
		}
		
		
		
	}
	/**
	 * FOR all nonterminals A DO First(A) := {};                      
       WHILE there are changes to any First(A) do                     
	       FOR each production choice A --> X1X2...Xn DO              
		     k:= 1 ; Continue := true ;
		     WHILE Continue = true AND k <= n DO
			   add First(Xk)-{epsilon} to First(A);
			   IF epsilon is not in First(Xk) THEN Continue := false ;
			     k := k + 1 ;
		     IF Continue = true THEN add epsilon to First(A) ;

	 */
	private static void First(){
		LL1_Rule curRule = ruleList.get(0);
		NonTerminal curTerm = curRule.getNonTerm();
		  while(changeFlag = true){
		      changeFlag = false;
			  for(int i = 0; i < ruleList.size(); i++){
		          int k = 1;
		          int n = curRule.getTNTList().size();
		          boolean Continue = true;
		          curRule = ruleList.get(i);
		          curTerm = curRule.getNonTerm();
		          
		          while( Continue = true && k <= n){
		              ArrayList<LL1_Token> tokenList = curRule.getTNTList();
		              LL1_Token kToken = tokenList.get(k);
		              ArrayList<Terminal> kFirstList = kToken.getFirstSet();
		              curTerm.getFirstSet().addAll(kFirstList);
		              /*if(!kFirstList.contains(EPSILON){
		                  Continue = false;
		              }*/
		              k++;
		          }
		          /*if(Continue){
		              curTerm.getFirstSet().add(EPSILON);
		          }*/
		      }
		  }
		 
	}
	
/*	}*/
	/**
	 * Follow(start-symbol) := {$} ;
       FOR all nonterminals A != start-symbol DO Follow(A) := {} ;
       WHILE there are changes to any Follow sets DO
	       FOR each production A --> X1X2...Xn DO
		     FOR EACH Xi that is a nonterminal DO
			   add First(Xi+1Xi+2...Xn) - {epsilon} to Follow(Xi)
			   (* Note: if i=n, then Xi+1Xi+2...Xn = epsilon *)
			   IF epsilon is in First(Xi+1Xi+2...Xn) THEN
			     add Follow(A) to Follow(Xi
	 */
	public void follow(){
		Terminal endOfFile = new Terminal(new Token<LL1_TokenType>(LL1_TokenType.EOF, null));
		startSymbol.addToFollowSet(endOfFile);
		
	}
}


/* Algorithm for computing First(A) for all nonterminals A
 * Pseudocode from book
 * 


**********
* Simplified algorithm of above in the absense of epsilon-productions
* We will have to use the first one, but this one makes it easier to understand
* the basic algorithm
* 

FOR all nonterminals A DO First(A) := {};
WHILE there are changes to any First(A) DO
	FOR each production choice A --> X1X2...Xn DO
		add First(X1) to First(A);


*/


/* Algorithm for the computation of Follow sets
 * Pseudocode from book



*/