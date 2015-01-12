package model;

import java.util.Vector;

public class BasicTerm implements CafeTerm{
	
	String opName;
	//Vector<BasicBlock> args = new Vector<BasicBlock>();
	//Maybe it is better instead of a BasicBlock to have a vector of strings
	Vector<Object> args  = new Vector<Object>();
	
	public BasicTerm(){}
	
	public void setOpName(String name){this.opName = name;}
	public String getOpName(){return this.opName;}
	
	public Vector<Object> getArgs(){return args;}
	public void addArg(String arg){args.addElement(arg);}
	
	
	
	
}//end of BasicOpExpr
