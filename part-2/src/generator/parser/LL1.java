package generator.parser;

import global.Token;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * LL1.java
 * LL1 parser capable of processing input text
 */
public class LL1 {

	private ArrayList<LL1_Rule> ruleList;
	private ArrayList<Terminal> termList;
	private ArrayList<NonTerminal> nonTermList;
	private boolean changeFlag;
	private NonTerminal startSymbol;
	private Grammar_Lexer lex;
	private final Terminal EPSILON;
	
	public static void main(String[] args) throws FileNotFoundException{
		LL1 LL1parser = new LL1("minire-specification.txt");
		System.out.println("Starting Parsing.");
		LL1parser.Parse();
		System.out.println("Completed Parsing.");
		/*System.out.println("Starting First.");
		//LL1parser.first();
		System.out.println("Completed First.");
		System.out.println("Starting Follow.");
		//LL1parser.follow();
		System.out.println("Completed Follow.");*/
	}
	
	public LL1(String file) throws FileNotFoundException{
		this.ruleList = new ArrayList<LL1_Rule>();
		this.termList = new ArrayList<Terminal>();
		this.nonTermList = new ArrayList<NonTerminal>();
		this.changeFlag = true;
		this.EPSILON = new Terminal(new Token<LL1_TokenType>(LL1_TokenType.EPSILON, "EPSILON"));
		lex = new Grammar_Lexer(file);
	}

	public void Parse() throws FileNotFoundException{

		System.out.println("Parsing...");
		//Skip to first Header
		while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
			System.out.println("Searching for header...");
			lex.getNextToken();
		}

		System.out.println("Found a header");
		System.out.println("Header:" + lex.peekNextToken().getValue());
		//Check for Headers

		if(lex.peekNextToken().getValue().equals("Tokens")){
			//Skip to next line to get list of tokens	
			System.out.println("Found Token Header");
			while(lex.peekNextToken().getType() != LL1_TokenType.TERMINAL){
				System.out.println("Consuming: " + lex.peekNextToken().getValue());
				lex.getNextToken();
			}

			//add all terminals to termList
			while(lex.peekNextToken().getType() != LL1_TokenType.EOL){
				System.out.println("Adding terminal: " + lex.peekNextToken().getValue());
				termList.add(new Terminal(lex.getNextToken()));
			}
			System.out.println("Added Terminals");

			//skip to next header
			while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
				lex.getNextToken();
			}

			//make sure next header is Start
			if(lex.peekNextToken().getValue().equals("Start")) {
				System.out.println("Found Start Header");
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
		else if(lex.peekNextToken().getValue().equals("Start")){
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

			if(lex.peekNextToken().getValue().equals("Tokens")){

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

		if(lex.peekNextToken().getValue().equals("Rules")) {
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
			System.out.println("Current Token is: " + lex.peekNextToken().getValue());
			NonTerminal first = new NonTerminal(lex.getNextToken());
			if(!nonTermList.contains(first)){
				nonTermList.add(first);
			}
			System.out.println("Making new rule with " + first.getToken().getValue());
			LL1_Rule currRule = new LL1_Rule(first);
			ruleList.add(currRule);
			while(lex.peekNextToken().getType() != LL1_TokenType.EOL && 
					lex.peekNextToken().getType() != LL1_TokenType.EOF){
				//if next token is a terminal, take the object out of termlist
				//and add it to the current rule
				if(lex.peekNextToken().getType() == LL1_TokenType.TERMINAL){
					System.out.println("This should be a terminal: " + lex.peekNextToken().getValue());
					Terminal testTerm = new Terminal(lex.getNextToken());
					int location = termList.indexOf(testTerm);
					currRule.addToTNTList(termList.get(location));
				}
				else if(lex.peekNextToken().getType() == LL1_TokenType.NON_TERMINAL){
					NonTerminal curNonTerm = new NonTerminal(lex.getNextToken());
					if(nonTermList.contains(curNonTerm)){
						int location = nonTermList.indexOf(curNonTerm);
						currRule.addToTNTList(nonTermList.get(location));
					}
					else{
						nonTermList.add(curNonTerm);
						currRule.addToTNTList(curNonTerm);
					}
				}
			}
			//consume EOL token
			lex.getNextToken();
			System.out.println("Next while test = " + lex.peekNextToken().getValue());
		}
		System.out.println("Start Symbol: " + startSymbol.getToken().getValue());
		//System.out.println("TermList: ");
		/*for(int i = 0; i < termList.size(); i++){
			System.out.println(termList.get(i).getToken().getValue());
		}*/
		System.out.println("NonTermList: ");
		for(int i = 0; i < nonTermList.size(); i++){
			System.out.println(nonTermList.get(i).getToken().getValue());
		}
		System.out.println("Rules: ");
		for(int i = 0; i < ruleList.size(); i++){
			System.out.println(ruleList.get(i).toString());
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
	private void first(){
		LL1_Rule curRule = ruleList.get(0);
		NonTerminal curTerm = curRule.getNonTerm();
		while(changeFlag){
			changeFlag = false;
			for(int i = 0; i < ruleList.size(); i++){
				int k = 0;
				int n = curRule.getTNTList().size();
				boolean Continue = true;
				curRule = ruleList.get(i);
				curTerm = curRule.getNonTerm();

				while( Continue = true && k < n){
					ArrayList<LL1_Token> tokenList = curRule.getTNTList();
					LL1_Token kToken = tokenList.get(k);
					ArrayList<Terminal> kFirstList = kToken.getFirstSet();
					ArrayList<Terminal> curFirst = curTerm.getFirstSet();
					if(curFirst.addAll(kFirstList)){
						changeFlag = true;
					}
					curTerm.setFirstSet(curFirst);
					
					if(!kFirstList.contains(EPSILON)){
						Continue = false;
					}
					k++;
				}
				if(Continue){
					ArrayList<Terminal> curFirstList = curTerm.getFirstSet();
					curFirstList.add(EPSILON);
					curTerm.setFirstSet(curFirstList);
				}
			}
		}
		changeFlag = true;
	}

	/**
	 * Follow(start-symbol) := {$} ;
       FOR all nonterminals A != start-symbol DO Follow(A) := {} ;
       WHILE there are changes to any Follow sets DO
	       FOR each production A --> X1 X2...Xn DO
		     FOR EACH Xi that is a nonterminal DO
			   add First(Xi+1 Xi+2 ...Xn) - {epsilon} to Follow(Xi)
			   (* Note: if i=n, then Xi+1 Xi+2...Xn = epsilon *)
			   IF epsilon is in First(Xi+1 Xi+2...Xn) THEN
			     add Follow(A) to Follow(Xi)
	 */
	private void follow(){
		Terminal endOfFile = new Terminal(new Token<LL1_TokenType>(LL1_TokenType.EOF, null));
		startSymbol.addToFollowSet(endOfFile);
		while(changeFlag){
			changeFlag = false;
			//for each rule
			for(int ruleNum = 0; ruleNum < ruleList.size(); ruleNum++){
				LL1_Rule curRule = ruleList.get(ruleNum);
				ArrayList<LL1_Token> TNTList = curRule.getTNTList();
				//for each token in the rule 
				for(int x = 0; x < TNTList.size(); x++){
					LL1_Token curTerm = TNTList.get(x);
					//if Xi is a nonterminal, add all First(Xi+1) to First(Xn) to Follow(Xi)
					if(curTerm instanceof NonTerminal){
						for(int i = x + 1; i < TNTList.size(); i++){
							LL1_Token xTerm = TNTList.get(i);
							ArrayList<Terminal> xFirst = xTerm.getFirstSet();

							ArrayList<Terminal> followSet = ((NonTerminal) curTerm).getFollowSet();
							if(followSet.addAll(xFirst)){
								changeFlag = true;
							}
							((NonTerminal) curTerm).setFollowSet(followSet);
							if(xTerm.getFirstSet().contains(EPSILON)){
								NonTerminal ruleTerm = curRule.getNonTerm();
								ArrayList<Terminal> ruleFollow = ruleTerm.getFollowSet();
								if(xTerm instanceof NonTerminal){
									ArrayList<Terminal> xFollow = ((NonTerminal) xTerm).getFollowSet();
									if(xFollow.addAll(ruleFollow)){
										changeFlag = true;
									}
									((NonTerminal) xTerm).setFollowSet(xFollow);
								}
							}
						}
					}
				}
			}
		}
		changeFlag = true;
	}
}

