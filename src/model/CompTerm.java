package model;

import java.util.Vector;

/**
 * This class defines objects which correspond to operator expressions of CafeOBJ
 * For now we will assume that all arguments in such an expression are parenthesized properly
 * although the language does not strictly require this
 * @author nikos
 *
 */
public class CompTerm implements CafeTerm{
	
	String opName;
	Vector<Object> args  = new Vector<Object>();
	
	
	
	public CompTerm(){}
	
	
	
	public void setOpName(String name){this.opName = name;}
	public String getOpName(){return this.opName;}
	
	public Vector<Object> getArgs(){return args;}
	public void addArg(Object arg){args.addElement(arg);}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of OpExpression
