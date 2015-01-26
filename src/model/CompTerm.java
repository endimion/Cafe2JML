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
	
	
	
	/**
	 * 
	 * @param op the name of an operator
	 * @return true if the given name appears in this term
	 */
	public boolean containsOp(String op){
		if(op.equals(opName)){
			return true;
		}else{
			for(Object arg:getArgs()){
				if(arg instanceof String){
					if(arg.equals(op))return true;
				}else{
					if(arg instanceof CafeTerm){
						if(((CafeTerm) arg).containsOp(op)) return true;
					}//end if the arg is an instance of CafeTerm
				}//end if arg is not a string
			}//end of looping through the term arguments
		}//end if it is not the operator name
		return false;
	}//end of containsOp
	
	
	
		
	/**
	 * @return a string representation of the term skipping the given 
	 * position of the arguments
	 */
	public String printTermSkipArg(int pos){
		String res = getOpName() + "(";
		
		if(getArgs().size() >0){
			for(int i =0; i< getArgs().size();i++){
				if(getArgs().get(i) instanceof BasicTerm){
					if(i != pos)res = res + " " + ((BasicTerm) getArgs().get(i)).termToString() + ",";
				}else{
					if(i != pos) res += res + ((CompTerm) getArgs().get(i)).termToString() + ",";
				}
			}//end of for loop
			
			if(!res.trim().endsWith("(")) res = StringHelper.remLastChar(res);
			
			res += ")";
		}
		
		
		return res;
	}//end of printTermNoFirstArg
	
	
}//end of OpExpression
