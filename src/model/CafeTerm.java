package model;

import java.util.Vector;

public interface CafeTerm {

	
	public String getOpName();
	
	public Vector<?> getArgs();
	
	public void addArg(Object arg);
	
	public String termToString();
	
	public boolean containsOp(String op);
	

	public String printTermSkipArg(int pos, Module mod);

	public Vector<String> getVarsOfTerm();
	
	
	/**
	 * 
	 * @param op the name of an operator
	 * @return the CafeTerm, which is part of this term
	 * and if defined by the given operator
	 */
	public CafeTerm getSubTerm(String op);
	
	//private String printBinaryOpTermSkipArg(int pos, Module mod);
}
