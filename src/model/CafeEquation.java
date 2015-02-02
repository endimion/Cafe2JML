package model;

import java.util.Vector;


public class CafeEquation {

	String opName ;
	//Vector<CafeTerm> arg = new Vector<CafeTerm>();
	
	private CafeTerm leftT ;
	private CafeTerm rightT;
	private CafeTerm condition;
	
	
	
	public CafeEquation(){}
	
	public void setOpName(String opName){this.opName = opName;}
	public String getOpName(){return this.opName;}
	
	
	public void setLeftTerm(CafeTerm t){ leftT = t;}
	public void setRightTerm(CafeTerm t){ rightT = t;}
	
	public CafeTerm getLeftTerm(){return this.leftT;}
	public CafeTerm getRightTerm(){return this.rightT;}
	
	public void setCondition(CafeTerm t){this.condition = t;}
	public CafeTerm getCondition(){return this.condition ;}
	
	
	
	/**
	 * 
	 * @param opName the name of an operator
	 * @return true if the given operator name exists in the left-hand-side of the equation
	 */
	public boolean containsLeftOp(String opName){
	
		CafeTerm t = getLeftTerm();
		return (containsTerm(t,opName)!= null)?true:false;
		
	}//end of containsLeftOp
	
	
	
	/**
	 * 
	 * @param opName the name of an operator
	 * @return true if the given operator name exists in the right hand side of the equation
	 */
	public boolean containsRightOp(String opName){
	
		CafeTerm t = getRightTerm();
		return(containsTerm(t,opName)!= null)?true:false;
		
	}//end of containsLeftOp
	
	
	
	
	/**
	 * 
	 * @param t a CafeTerm
	 * @param opName the name of an operator
	 * @return returns null if no operator with this name appears in the term
	 * or the CafeTerm of t which has that name 
	 */
	@SuppressWarnings("unchecked")
	private CafeTerm containsTerm(CafeTerm t, String opName){
		if(t.getOpName().equals(opName)){
			return t;
		}else{
			Vector<Object> args = (Vector<Object>) t.getArgs();
			for(Object arg : args){
				if(arg instanceof String){
					if(arg.equals(opName)){
						BasicTerm b = new BasicTerm();
						b.setOpName(opName);
						return b;
					}
				}else{
					if(arg instanceof CafeTerm){
						if(((CafeTerm) arg).containsOp(opName)) return (CafeTerm) arg;
					}//end if arg is a CafeTerm
					
				}//if the argument is not a String
				
			}//end of looping through the arguments
		}//end if the operator name is not the same as the input

		return null;
	}//end of containsTerm
	
	
	
	
	/**
	 * 
	 * @param opName the name of an operator
	 * @return a vector of strings containing the arguments 
	 * that appear for the given operator in this equation
	 */
	@SuppressWarnings("unchecked")
	public Vector<String> getLeftArguments(String opName){
		
		Vector<String> res = new Vector<String>();
		
		if(containsTerm(getLeftTerm(), opName)!=null){
			res = (Vector<String>) containsTerm(getLeftTerm(), opName).getArgs();
		}
		
		return res;
	}//end of getOpArguments

	
	
	
	/**
	 * 
	 * @param opName the name of a CafeOperator
	 * @return the CafeTerm for which defined by this operator
	 * in the lhs of the equation
	 */
	public CafeTerm getTermInEqLeft(String opName){
		
		CafeTerm lhs = getLeftTerm();
		
		if(lhs.getOpName().equals(opName)){
			return lhs;
		}else{
			for(Object o : lhs.getArgs()){
				if(o instanceof String){
					BasicTerm t = new BasicTerm();
					t.setOpName((String)o);
				}//end of if the argument is a string
				else{
					if(o instanceof CompTerm){
						if( ((CompTerm) o).getOpName().equals(opName)) {
							return (CompTerm) o;
						}else{
							if(((CafeTerm)o).getSubTerm(opName) !=  null){
								return ((CafeTerm)o).getSubTerm(opName);
							}
						} 
						
					}else{
						if(o instanceof BasicTerm){
							if( ((BasicTerm) o).getOpName().equals(opName)) 
								return ((BasicTerm) o);
						}//end if o is a BasicTerm
					}
				}//end of if the argument is not a string
			}//end of looping through the arguments of the left hand side
		}//end if the op is not the main operator of the left part of the eq
		
		return null;
	}//end of getTermInEqLeft
	
	
	
	
	
}//end of class
