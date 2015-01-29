package model;

import java.util.Vector;

public interface CafeTerm {

	
	public String getOpName();
	
	public Vector<?> getArgs();
	
	public void addArg(Object arg);
	
	public String termToString();
	
	public boolean containsOp(String op);
	

	public String printTermSkipArg(int pos, Module mod);
	
	//private String printBinaryOpTermSkipArg(int pos, Module mod);
}
