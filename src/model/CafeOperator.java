package model;

import java.util.Vector;

public class CafeOperator implements BasicBlock {
	
	String name ;
	Vector<String> arity ;
	String sort;
	private String type;
	
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

	
	
	
}//end of CafeOperator
