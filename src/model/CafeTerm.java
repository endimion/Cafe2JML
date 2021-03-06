package model;

import java.util.Vector;


public interface CafeTerm {

	
	public String getOpName();
	
	public Vector<?> getArgs();
	
	public void addArg(Object arg);
	
	public String termToString(Module mod, JmlGenerator gen);
	
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
	/**
	 * 
	 * @param t another CafeTerm
	 * @return true if the two CafeTerms have the same op name and the same arguments
	 */
	public boolean isEqual(Object t);
	
	/**
	 * 
	 * @param orig the vector of objects that will be replaced
	 * @param repl the vector of object we are replacing them with
	 * @return the CafeTerm obtained by replacing each appearence of the orig.get(i) object in the arguments
	 * of the term with the repl.get(i)
	 */
	public CafeTerm replaceTerms(Vector<Object> orig , Vector<Object> repl);
	
	
	public CafeTerm removeArg(String argName);
	
}
