package model;

import java.util.Vector;

public class CafeOperator {
	
	String name ;
	Vector<String> arity ;
	String sort;
	private String type;
	
	char beofreArityChar ; //denotes the last character of the operation name, e.g. op anOperator_ : N -> S
							// beforeArityChar == r
	
	public CafeOperator(){
		arity = new Vector<String>();
		name ="";
		sort = "";
		type = "";
	}//end of constructor
	
	public void setName(String n){this.name = n;}
	public String getName(){return this.name;}
	
	public void addToArity(String arg){
		arity.addElement(arg);
	}
	public Vector<String> getArity(){ return arity;}
	
	
	public void setSort(String s){sort= s;}
	public String getSort(){return sort;}
	
	public String getType(){return this.type;}
	public void setType(String t){this.type = t;}

	public char getSeperationChar(){return this.beofreArityChar;}
	public void setType(char c){this.beofreArityChar = c;}
	
	
}//end of CafeOperator
