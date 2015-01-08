package model;

import java.util.Vector;

public class CafeOperator {
	
	String name ;
	Vector<String> arity ;
	String sort;
	
	public CafeOperator(){
		arity = new Vector<String>();
		name ="";
		sort = "";
	}//end of constructor
	
	public void setName(String n){this.name = n;}
	public String getName(){return this.name;}
	
	public void addToArity(String arg){
		arity.addElement(arg);
	}
	public Vector<String> getArity(){ return arity;}
	
	
	public void setSort(String s){sort= s;}
	public String getSort(){return sort;}
	
	

}//end of CafeOperator
