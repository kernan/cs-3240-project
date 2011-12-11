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
	private ParseTable pt;
	
	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws ParseException 
	 */
	public LL1(String file) throws FileNotFoundException, ParseException{
		this.ruleList = new ArrayList<LL1_Rule>();
		this.termList = new ArrayList<Terminal>();
		this.nonTermList = new ArrayList<NonTerminal>();
		this.changeFlag = true;
		lex = new Grammar_Lexer(file);
		this.Parse();
		this.first();
		this.follow();
		pt = new ParseTable(ruleList, termList, nonTermList);
	}

	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws ParseException thrown by lexer
	 */
	public void Parse() throws FileNotFoundException, ParseException{

		//System.out.println("Parsing...");
		//Skip to first Header
		while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
			//System.out.println("Searching for header...");
			lex.getNextToken();
		}

		//System.out.println("Found a header");
		//System.out.println("Header:" + lex.peekNextToken().getValue());
		//Check for Headers

		if(lex.peekNextToken().getValue().equals("Tokens")){
			//Skip to next line to get list of tokens	
			//System.out.println("Found Token Header");
			while(lex.peekNextToken().getType() != LL1_TokenType.TERMINAL){
				//System.out.println("Consuming: " + lex.peekNextToken().getValue());
				lex.getNextToken();
			}

			//add all terminals to termList
			while(lex.peekNextToken().getType() != LL1_TokenType.EOL){
				//System.out.println("Adding terminal: " + lex.peekNextToken().getValue());
				termList.add(new Terminal(lex.getNextToken()));
			}
			//System.out.println("Added Terminals");

			//skip to next header
			while(lex.peekNextToken().getType() != LL1_TokenType.HEADER){
				//System.out.println("Chucking token: " + lex.peekNextToken().getValue());
				lex.getNextToken();
			}

			//make sure next header is Start
			if(lex.peekNextToken().getValue().equals("Start")) {
				//System.out.println("Found Start Header");
				//skip to start symbol
				while(lex.peekNextToken().getType() != LL1_TokenType.NON_TERMINAL){
					lex.getNextToken();
				}
				//consume start symbol
				startSymbol = new NonTerminal(lex.getNextToken());
				nonTermList.add(startSymbol);
			}
			else{
				//TODO error
				//System.out.println("Start symbol must come after tokens");
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
				//System.out.println("Tokens must come after start symbol");
			}	
		}
		else{
			//TODO error catching
			//System.out.println("Tokens and start symbol must be listed before rules");
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
			//System.out.println("Current Token is: " + lex.peekNextToken().getValue());
			NonTerminal first = new NonTerminal(lex.getNextToken());
			LL1_Rule currRule;
			if(!nonTermList.contains(first)){
				nonTermList.add(first);
				currRule = new LL1_Rule(first);
				ruleList.add(currRule);
			}
			else{
				int firstLocation = nonTermList.indexOf(first);
				first = nonTermList.get(firstLocation);
				currRule = new LL1_Rule(first);
				ruleList.add(currRule);
				//System.out.println("Making new rule with: " + first.getToken().getValue());
			}
			

			while(lex.peekNextToken().getType() != LL1_TokenType.EOL && 
					lex.peekNextToken().getType() != LL1_TokenType.EOF){
				//if next token is a terminal, take the object out of termlist
				//and add it to the current rule
				if(lex.peekNextToken().getType() == LL1_TokenType.TERMINAL){
					//System.out.println("This should be a terminal: " + lex.peekNextToken().getValue());
					Terminal testTerm = new Terminal(lex.getNextToken());
					int termLocation = termList.indexOf(testTerm);
					currRule.addToTNTList(termList.get(termLocation));
				}
				else if(lex.peekNextToken().getType() == LL1_TokenType.NON_TERMINAL){
					NonTerminal curNonTerm = new NonTerminal(lex.getNextToken());
					if(nonTermList.contains(curNonTerm)){
						int nonTermLocation = nonTermList.indexOf(curNonTerm);
						currRule.addToTNTList(nonTermList.get(nonTermLocation));
					}
					else{
						nonTermList.add(curNonTerm);
						currRule.addToTNTList(curNonTerm);
					}
				}
			}
			//consume EOL token
			//System.out.println("THIS SHOULD BE AN EOL OR EOF: " + lex.peekNextToken().getValue());
			lex.getNextToken();
			//System.out.println("Next while test = " + lex.peekNextToken().getValue());
		}
		//System.out.println("\nStart Symbol: " + startSymbol.getToken().getValue());
		//System.out.println("TermList: ");
		/*for(int i = 0; i < termList.size(); i++){
			System.out.println(termList.get(i).getToken().getValue());
		}*/
		/*System.out.println("\nNonTermList: ");
		for(int i = 0; i < nonTermList.size(); i++){
			System.out.println(nonTermList.get(i).getToken().getValue());
		}
		System.out.println("\nRules: ");
		for(int i = 0; i < ruleList.size(); i++){
			System.out.println(ruleList.get(i).toString());
		}*/
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
				//System.out.println("--------Rule Header: " + curTerm.getToken().getValue() + "--------");
				int n = curRule.getTNTList().size();

				while( Continue && k < n){
					ArrayList<LL1_Token> tokenList = curRule.getTNTList();
					LL1_Token kToken = tokenList.get(k);
					//System.out.println("Checking: " + kToken.getToken().getValue());
					ArrayList<Terminal> kFirstList = kToken.getFirstSet();
					ArrayList<Terminal> curFirst = curTerm.getFirstSet();

					/*System.out.println("Already contains: ");
					for(int y = 0; y < curFirst.size(); y++){
						System.out.println(curFirst.get(y).toString());
					}*/
					//set initial size
					int checkSize = curFirst.size();
					//loops thru k's first list and adds anything that isn't epsilon or already existing
					ArrayList<Terminal> derivation = curRule.getFirstSet();
					for(int j = 0; j < kFirstList.size(); j++){
						//System.out.println("Looping thru first list: " + kFirstList.get(j).toString());
						if(!kFirstList.get(j).getToken().getValue().equals("EPSILON") && !curFirst.contains(kFirstList.get(j))){
							//System.out.println("ADDING: " + kFirstList.get(j).toString());
							curFirst.add(kFirstList.get(j));
							derivation.add(kFirstList.get(j));
						}
					}
					curRule.setFirstSet(derivation);

					/*System.out.println("Updated first list: ");
					for(int y = 0; y < curFirst.size(); y++){
						System.out.println(curFirst.get(y).toString());
					}*/

					if(curFirst.size() != checkSize){
						changeFlag = true;
						//System.out.println("Change detected!");
					}



					//System.out.println("checkSize: " + checkSize + "   curFirst size: " + curFirst.size());


					curTerm.setFirstSet(curFirst);

					boolean epFlag = false;
					for(int y = 0; y < kFirstList.size(); y++){
						if(kFirstList.get(y).getToken().getValue().equals("EPSILON")){
							epFlag = true;
						}
					}
					if(!epFlag){
						Continue = false;
					}

					k++;
				}
				if(Continue){
					int epLocation = -1;
					//get epsilon object
					for(int q = 0; q < termList.size(); q++){
						if(termList.get(q).getToken().getValue().equals("EPSILON")){
							epLocation = q;
						}
					}
					ArrayList<Terminal> curFirstList = curTerm.getFirstSet();
					if(!curFirstList.contains(termList.get(epLocation))){
						//System.out.println("Adding EPSILON!");
						curFirstList.add(termList.get(epLocation));
						curTerm.setFirstSet(curFirstList);
						
						ArrayList<Terminal> derivation = curRule.getFirstSet();
						derivation.add(termList.get(epLocation));
						curRule.setFirstSet(derivation);
						
						changeFlag = true;
					}
				}

			}
		}
		changeFlag = true;
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
		//getting epsilon object out of the term list
		Terminal epsilon = new Terminal(new Token<LL1_TokenType>(LL1_TokenType.EOF, "EOF"));
		for(int q = 0; q < termList.size(); q++){
			if(termList.get(q).getToken().getValue().equals("EPSILON")){
				epsilon = termList.get(q);
			}
		}
		//make EOF and add it to start symbol
		Terminal endOfFile = new Terminal(new Token<LL1_TokenType>(LL1_TokenType.EOF, "EOF"));
		termList.add(endOfFile);
		startSymbol.addToFollowSet(endOfFile);

		while(changeFlag){
			changeFlag = false;
			//for each rule
			for(int ruleNum = 0; ruleNum < ruleList.size(); ruleNum++){
				LL1_Rule curRule = ruleList.get(ruleNum);
				//System.out.println("-----In rule: " + curRule.getNonTerm().getToken().getValue() + " ------");
				ArrayList<LL1_Token> TNTList = curRule.getTNTList();

				//for each token in the rule 
				for(int x = 0; x < TNTList.size(); x++){
					LL1_Token curTerm = TNTList.get(x);
					//if Xi is a nonterminal, add all First(Xi+1) to First(Xn) to Follow(Xi)
					if(curTerm instanceof NonTerminal){
						//System.out.println("This is the nonTerminal: " + curTerm.getToken().getValue());
						LL1_Token xTerm = epsilon;
						if(x + 1 < TNTList.size()){
							xTerm = TNTList.get(x + 1);
						}
						//System.out.println("This is what follows it: " + xTerm.getToken().getValue());
						ArrayList<Terminal> xFirst = xTerm.getFirstSet();
						ArrayList<Terminal> followSet = ((NonTerminal) curTerm).getFollowSet();
						
						/*System.out.println("Current follow set: ");
						for(int p = 0; p < followSet.size(); p++){
							System.out.println(followSet.get(p));
						}*/

						//add First(Xi+1 Xi+2 ...Xn) - {epsilon} to Follow(Xi)
						int checkSize = followSet.size();
						for(int q = 0; q < xFirst.size(); q++){
							if(!xFirst.get(q).getToken().getValue().equals("EPSILON") && !followSet.contains(xFirst.get(q))){
								followSet.add(xFirst.get(q));
								//System.out.println("Adding: " + xFirst.get(q).getToken().getValue());
							}
						}
						if(checkSize != followSet.size()){
							changeFlag = true;
						}

						/*System.out.println("New follow set: ");
						for(int p = 0; p < followSet.size(); p++){
							System.out.println(followSet.get(p));
						}*/

						((NonTerminal) curTerm).setFollowSet(followSet);
						//IF epsilon is in First(Xi+1 Xi+2...Xn) THEN
						if(xTerm.getFirstSet().contains(epsilon)){
							//System.out.println("Adding follow A to follow Xi");
							NonTerminal ruleTerm = curRule.getNonTerm();
							//System.out.println("A: " + ruleTerm.getToken().getValue());
							//System.out.println("Xi: " + curTerm.getToken().getValue());
							ArrayList<Terminal> ruleFollow = ruleTerm.getFollowSet();
							if(curTerm instanceof NonTerminal){
								ArrayList<Terminal> xFollow = ((NonTerminal) curTerm).getFollowSet();

								//add Follow(A) to Follow(Xi)
								int checkSize2 = xFollow.size();
								for(int q = 0; q < ruleFollow.size(); q++){
									if(!xFollow.contains(ruleFollow.get(q))){
										xFollow.add(ruleFollow.get(q));
									}
								}
								if(checkSize2 != xFollow.size()){
									changeFlag = true;
								}

								((NonTerminal) curTerm).setFollowSet(xFollow);
							}
						}

					}
				}
			}
		}
		changeFlag = true;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<LL1_Rule> getRuleList() {
		return ruleList;
	}

	/**
	 * 
	 * @param ruleList
	 */
	public void setRuleList(ArrayList<LL1_Rule> ruleList) {
		this.ruleList = ruleList;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<Terminal> getTermList() {
		return termList;
	}
	
	/**
	 * 
	 * @param termList
	 */
	public void setTermList(ArrayList<Terminal> termList) {
		this.termList = termList;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<NonTerminal> getNonTermList() {
		return nonTermList;
	}
	
	/**
	 * 
	 * @param nonTermList
	 */
	public void setNonTermList(ArrayList<NonTerminal> nonTermList) {
		this.nonTermList = nonTermList;
	}

	/**
	 * 
	 * @return
	 */
	public NonTerminal getStartSymbol() {
		return startSymbol;
	}

	/**
	 * 
	 * @param startSymbol
	 */
	public void setStartSymbol(NonTerminal startSymbol) {
		this.startSymbol = startSymbol;
	}

	/**
	 * 
	 * @return
	 */
	public ParseTable getPt() {
		return pt;
	}

	/**
	 * 
	 * @param pt
	 */
	public void setPt(ParseTable pt) {
		this.pt = pt;
	}
}

