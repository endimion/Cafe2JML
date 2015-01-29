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
	 * @pos an integer denoting the position of the system sort
	 * @mod the module from which the system sort is retrieved
	 * @return a string representation of the term skipping the given 
	 * position of the arguments
	 */
	public String printTermSkipArg(int pos, Module mod){
		String res ="";
		int newPos = -1;
		
		
		if(!TermParser.isBinary(getOpName())){
			if(getArgs().size() >0){
				res = getOpName() + "(";
				for(int i =0; i< getArgs().size();i++){
					String endingChar = (i == getArgs().size()-1)?"":", ";

					if(getArgs().get(i) instanceof BasicTerm){
						if(i != pos){
								
							newPos = TermParser.getPositionOfSystemSort((CafeTerm) getArgs().get(i), mod);
							res = res +  ((CafeTerm) getArgs().get(i)).printTermSkipArg(newPos, mod) + endingChar 
									+" "	;
							}//end if pos is not equal to i
					}else{
						newPos = TermParser.getPositionOfSystemSort((CafeTerm) getArgs().get(i), mod);
						//System.out.println(((CafeTerm) getArgs().get(i)).getOpName() + " AAAASSS " + newPos);
						res = res +  ((CafeTerm) getArgs().get(i)).printTermSkipArg(newPos, mod) + endingChar
								;
					}
				}//end of for loop
				
				res += ")";
			}else{  //end of if args size is greater than zero
				res = getOpName();
			}//end if the term is binary and has no arguments                  
			//if(!res.trim().endsWith("(")) res = StringHelper.remLastChar(res);
				
				
		}else{ //if the term has as a many operator a binary term
			int leftPos = TermParser.getPositionOfSystemSort((CafeTerm) getArgs().get(0), mod);
			int rightPos = TermParser.getPositionOfSystemSort((CafeTerm) getArgs().get(1), mod);
			
			if(!getOpName().equals("equals")){
				res = "(" +  ((CafeTerm) getArgs().get(0)).printTermSkipArg(leftPos,mod) +" "
							+ getOpName() +" "
						  + ( ((CafeTerm) getArgs().get(1))).printTermSkipArg(rightPos,mod) + ")";
			}else{
				String sort = mod.getOpSortByName( ((CafeTerm) getArgs().get(1)).getOpName());
				if(sort.equals("int")||sort.equals("boolean")){
					res = "(" +  ((CafeTerm) getArgs().get(0)).printTermSkipArg(leftPos,mod).trim()+") == (" 
							  + (((CafeTerm) getArgs().get(1))).printTermSkipArg(rightPos,mod) + ")"  ;
				}else{
					
					res =   ((CafeTerm) getArgs().get(0)).printTermSkipArg(leftPos,mod).trim()+"." 
							+ getOpName()+"(" 
						  + ( ((CafeTerm) getArgs().get(1))).printTermSkipArg(rightPos,mod) + ")";
				}
			
			}//end of else if the name of the operator is "equals"
		}
		if(res.trim().endsWith("(")){res += ")";}
		return res;
	}//end of printTermNoFirstArg
	
	
	
	
}//end of OpExpression
