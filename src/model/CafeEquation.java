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

	
	
	
	
	
	
}//end of class
