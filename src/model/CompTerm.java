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
		
		if(!TermParser.isBinary(getOpName())){
			if(getArgs().size() >0){
				for(int i =0; i< getArgs().size();i++){
					if(i != pos){
							if(!TermParser.isBinary( ((CafeTerm)getArgs().get(i)).getOpName())){
								res = res + " " + ((CafeTerm) getArgs().get(i)).termToString() + ",";
							}else{ 
								res = res + " " + ((CafeTerm) getArgs().get(i)).printBinaryOpTermSkipArg(0) + ",";
								}
						
						}//end if pos is not equal to i
				}//end of for loop
			}//end of if args size is greater than zero
				
				if(!res.trim().endsWith("(")) res = StringHelper.remLastChar(res);
				
				
		}else{
			res = "(" +  ((CafeTerm) getArgs().get(0)).printTermSkipArg(-1) 
						+ getOpName() 
					  + ( ((CafeTerm) getArgs().get(1))).printTermSkipArg(-1) ;
		}
		res += ")";
		return res;
		
		//TODO make it so that the system sort is not printed in the left and right terms
		//i.e. found out where the correct position for each such sort is
		
	}//end of printTermNoFirstArg
	
	
	/**
	 * 
	 * @param pos
	 * @return a string representation of the term skipping the 
	 * argument in the given position
	 */
	public String printBinaryOpTermSkipArg(int pos){
		String res ="";
		
		String leftTerm = (TermParser.isBinary(((CafeTerm)getArgs().get(0)).getOpName()))?
				((CafeTerm)getArgs().get(0)).printBinaryOpTermSkipArg(0):
					((CafeTerm)getArgs().get(0)).printTermSkipArg(-1);
		
		String rightTerm = (TermParser.isBinary(((CafeTerm)getArgs().get(1)).getOpName()))?
					((CafeTerm)getArgs().get(1)).printBinaryOpTermSkipArg(1):
						((CafeTerm)getArgs().get(1)).printTermSkipArg(-1);		
				
				
		res = " (" + leftTerm + " " + getOpName() + " " + rightTerm+ " )";
		
		
		if(getArgs().size() == 2){
			return res;
		}else{
			return "This is not a binary operator!!! something went wrong!!!!";
		}
	}//end of printTermSkippingArg
	
	
	
}//end of OpExpression
