package model;

import java.util.Vector;

public class BasicOpExpr {
	
	String opName;
	Vector<BasicBlock> args = new Vector<BasicBlock>();
	
	
	public BasicOpExpr(){}
	
	public void setOpName(String name){this.opName = name;}
	public String getOpName(){return this.opName;}
	
	public Vector<BasicBlock> getArgs(){return args;}
	public void addArg(BasicBlock arg){args.addElement(arg);}
	
	
	
	
}//end of BasicOpExpr
