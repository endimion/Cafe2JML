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
	
	
	
	
}//end of BasicOpExpr
