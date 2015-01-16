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
	
	
	
	
	/**
	 * 
	 * @return a string representation of the term object
	 */
	@Override
	public String termToString(){
		String print =  getOpName() + "(";
		
		for(Object o : getArgs()){
			if(o instanceof BasicTerm){
				print = print + " " + ((BasicTerm) o).termToString() + ",";
			}else{
				print += print + ((CompTerm) o).termToString() + ",";
			}
		}//end of for loop
		
		print = StringHelper.remLastChar(print);
		print += ")";
		
		return print;
		
	}//end of printTerm
	
	
	
	
	
	
	
	
	
	
	
}//end of OpExpression
