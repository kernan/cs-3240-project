import java.awt.List;

public class CharClass {

	String name;
	String chars;
	
	
	public CharClass(String name, String chars){
		this.name = name;
		this.chars = chars;
		
	}
	
	public boolean equals(Object o){
		return this.name.equals(((CharClass)o).getName());
	}
	
	public String getName(){
		return name;
	}
	
}
