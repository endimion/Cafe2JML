package model;

import java.util.Vector;

public interface CafeTerm {

	
	public String getOpName();
	
	public Vector<?> getArgs();
	
	public void addArg(Object arg);
	
	public String termToString();
}
