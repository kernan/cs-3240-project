package generator.parser;

import global.Token;

import java.io.FileNotFoundException;
import java.text.ParseException;
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
	
	/**
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 * @throws ParseException thrown by lexer
	 */
	public static void main(String[] args) throws FileNotFoundException, ParseException{
		LL1 LL1parser = new LL1("book_grammar.txt");
		System.out.println("Starting Parsing.");
		LL1parser.Parse();
		System.out.println("Completed Parsing.");
		System.out.println("Starting First.");
		LL1parser.first();
		System.out.println("Completed First.");
		System.out.println("First Lists: ");
		for(int i = 0; i < LL1parser.nonTermList.size(); i++){
			NonTerminal nonTerm = LL1parser.nonTermList.get(i);
			ArrayList<Terminal> fList = nonTerm.getFirstSet();
			System.out.println("First List for: " + nonTerm.getToken().getValue());
			for(int j = 0; j < fList.size(); j++){
				System.out.println(fList.get(j).getToken().getValue());
			}
		}
		/*System.out.println("Starting Follow.");
		LL1parser.follow();
		System.out.println("Completed Follow.");*/
	}
	
	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 */
	public LL1(String file) throws FileNotFoundException{
		this.ruleList = new ArrayList<LL1_Rule>();
		this.termList = new ArrayList<Terminal>();
		this.nonTermList = new ArrayList<NonTerminal>();
		this.changeFlag = true;
		this.EPSILON = new Terminal(new Token<LL1_TokenType>(LL1_TokenType.EPSILON, "EPSILON"));
		lex = new Grammar_Lexer(file);
	}

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ParseException thrown by lexer
	 */
	public void Parse() throws FileNotFoundException, ParseException{

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
				System.out.println("Chucking token: " + lex.peekNextToken().getValue());
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
			System.out.println("THIS SHOULD BE AN EOL OR EOF: " + lex.peekNextToken().getValue());
			lex.getNextToken();
			System.out.println("Next while test = " + lex.peekNextToken().getValue());
		}
		System.out.println("\nStart Symbol: " + startSymbol.getToken().getValue());
		//System.out.println("TermList: ");
		/*for(int i = 0; i < termList.size(); i++){
			System.out.println(termList.get(i).getToken().getValue());
		}*/
		System.out.println("\nNonTermList: ");
		for(int i = 0; i < nonTermList.size(); i++){
			System.out.println(nonTermList.get(i).getToken().getValue());
		}
		System.out.println("\nRules: ");
		for(int i = 0; i < ruleList.size(); i++){
			System.out.println(ruleList.get(i).toString());
		}
	}
	
	/*
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
	/**
	 * 
	 */
	private void first(){
		LL1_Rule curRule;
		NonTerminal curTerm;
		while(changeFlag){
			changeFlag = false;
			for(int i = 0; i < ruleList.size(); i++){
				int k = 0;
				boolean Continue = true;
				curRule = ruleList.get(i);
				curTerm = curRule.getNonTerm();
				System.out.println("Rule Header: " + curTerm.getToken().getValue());
				int n = curRule.getTNTList().size();

				while( Continue && k < n){
					ArrayList<LL1_Token> tokenList = curRule.getTNTList();
					LL1_Token kToken = tokenList.get(k);
					System.out.println("Checking: " + kToken.getToken().getValue());
					ArrayList<Terminal> kFirstList = kToken.getFirstSet();
					ArrayList<Terminal> curFirst = curTerm.getFirstSet();
					
					
					System.out.println(curTerm.toString());
					
					System.out.println("\ncurFirst 1: ");
					for(int y = 0; y < curFirst.size(); y++){
						System.out.println(curFirst.get(y).toString() + "\n");
					}
					
					int checkSize = curFirst.size();
					for(int j = 0; j < kFirstList.size(); j++){
						//System.out.println("Looping thru first list: " + kFirstList.get(j).toString());
						if(!kFirstList.get(j).equals(EPSILON) && !curFirst.contains(kFirstList.get(k))){
						//  System.out.println("ADDING: " + kFirstList.get(j).toString());
							curFirst.add(kFirstList.get(j));
						}
					}
					
					System.out.println("\ncurFirst 2: ");
					for(int y = 0; y < curFirst.size(); y++){
						System.out.println(curFirst.get(y).toString() + "\n");
					}
					
					//System.out.println("checkSize: " + checkSize + "   curFirst size: " + curFirst.size());
					if(curFirst.size() != checkSize){
						changeFlag = true;
					}
					
				/*	if(curFirst.addAll(kFirstList)){
						changeFlag = true;
					}*/
					curTerm.setFirstSet(curFirst);
					
			/*		System.out.println("\ncurTerm: ");
					for(int y = 0; y < curFirst.size(); y++){
						System.out.println(curFirst.get(y).toString() + "\n");
					}*/
					
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
		
		for(int i = 0; i < nonTermList.size(); i++){
			System.out.println("\nNonTerminal:   " + nonTermList.get(i).toString());
			String firsts = "First set:   ";
			for(int j = 0; j < nonTermList.get(i).getFirstSet().size(); j++){
				firsts += nonTermList.get(i).getFirstSet().get(j).toString() + " ";
			}
			System.out.println(firsts);
			
		/*	String follows = "Follow set:   ";
			for(int j = 0; j < nonTermList.get(i).getFollowSet().size(); j++){
				follows += nonTermList.get(i).getFollowSet().get(j).toString() + " ";
			}
			System.out.println(follows);*/
			
		}
		
	}

	/*
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
	/**
	 * 
	 */
	private void follow(){
		Terminal endOfFile = new Terminal(new Token<LL1_TokenType>(LL1_TokenType.EOF, "EOF"));
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

	public ArrayList<LL1_Rule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(ArrayList<LL1_Rule> ruleList) {
		this.ruleList = ruleList;
	}

	public ArrayList<Terminal> getTermList() {
		return termList;
	}

	public void setTermList(ArrayList<Terminal> termList) {
		this.termList = termList;
	}

	public ArrayList<NonTerminal> getNonTermList() {
		return nonTermList;
	}

	public void setNonTermList(ArrayList<NonTerminal> nonTermList) {
		this.nonTermList = nonTermList;
	}

	public NonTerminal getStartSymbol() {
		return startSymbol;
	}

	public void setStartSymbol(NonTerminal startSymbol) {
		this.startSymbol = startSymbol;
	}
	
	
	
	
}

