package model;

import java.util.Vector;

public class BasicTerm implements CafeTerm{
	
	String opName;
	//Vector<BasicBlock> args = new Vector<BasicBlock>();
	//Maybe it is better instead of a BasicBlock to have a vector of strings
	Vector<String> args  = new Vector<String>();
	
	public BasicTerm(){}
	
	public void setOpName(String name){this.opName = name;}
	
	@Override
	public String getOpName(){return this.opName;}
	
	@Override
	public Vector<String> getArgs(){return args;}
	
	@Override
	public void addArg(Object arg) { args.addElement((String)arg);}
	
	
	
	/**
	 * 
	 * @return a string representation of the term
	 */
	public String termToString(){
		String print = getOpName();
		String extra="";
		
		for(String s: getArgs()){
			extra += " "+s +",";
		}//end of looping through the term argumnets
		
		if(extra.length() > 0){
			extra = StringHelper.remLastChar(extra); //we remove the last comma
			print  += "(" + extra +")";
		}

		return print;
	}//end of termToString
	
	
}//end of BasicOpExpr
