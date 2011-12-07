package generator.parser;

/**
 * LL1.java
 * LL1 parser capable of processing input text
 */
public class LL1 {
	//TODO
}

/* Algorithm for computing First(A) for all nonterminals A
 * Pseudocode from book
 * 

FOR all nonterminals A DO First(A) := {};
WHILE there are changes to any First(A) do
	FOR each production choice A --> X1X2...Xn DO
		k:= 1 ; Continue := true ;
		WHILE Continue = true AND k <= n DO
			add First(Xk)-{epsilon} to First(A);
			IF epsilon is not in First(Xk) THEN Continue := false ;
			k := k + 1 ;
		IF Continue = true THEN add epsilon to First(A) ;

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

Follow(start-symbol) := {$} ;
FOR all nonterminals A != start-symbol DO Follow(A) := {} ;
WHILE there are changes to any Follow sets DO
	FOR each production A --> X1X2...Xn DO
		FOR EACH Xi that is a nonterminal DO
			add First(Xi+1Xi+2...Xn) - {epsilon} to Follow(Xi)
			(* Note: if i=n, then Xi+1Xi+2...Xn = epsilon *)
			IF epsilon is in First(Xi+1Xi+2...Xn) THEN
				add Follow(A) to Follow(Xi

*/