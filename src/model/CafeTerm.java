package model;

import java.util.Vector;

import model.TransObserValues.ObsValPair;

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
	
	
	
	
	//we need to write a method that will replace the appearance of a (CafeTerm) argument with another
	// CafeTerm this is required for translating composite objects
	
	/**
	 * 
	 * @param newArg a new Argument that should be replaced in the existing term
	 * @param pos the position of the argument that should be replaced
	 * @return a new CafeTerm which in the position pos contains the new term
	 */
	public CafeTerm replaceArg(Object newArg, int pos);
	
	
	public CafeTerm replaceAllMatching(Vector<ObsValPair> newValues);
	
	//private String printBinaryOpTermSkipArg(int pos, Module mod);
}
