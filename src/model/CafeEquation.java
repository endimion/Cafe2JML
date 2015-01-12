package model;


public class CafeEquation {

	String opName ;
	//Vector<CafeTerm> arg = new Vector<CafeTerm>();
	
	private CafeTerm leftT ;
	private CafeTerm rightT;
	
	
	
	public CafeEquation(){}
	
	public void setOpName(String opName){this.opName = opName;}
	public String getOpName(){return this.opName;}
	
	
	public void setLeftTerm(CafeTerm t){ leftT = t;}
	public void setRightTerm(CafeTerm t){ rightT = t;}
	
	public CafeTerm getLeftTerm(){return this.leftT;}
	public CafeTerm getRightTerm(){return this.rightT;}
	
		
}//end of class
