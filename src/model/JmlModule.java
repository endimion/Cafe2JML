package model;

import java.util.Vector;

/**
 * store the content after translating a CafeOBJ module to a JML module
 * @author nikos
 *
 */



public class JmlModule {
	
	Vector<TransObserValues> transValues ;
	
	
	public JmlModule(){
		transValues = new Vector<TransObserValues>();
	}//end of constructor
	
	
	
	/**
	 * 
	 * @param val a pair of transition rule and 
	 */
	public void addTransObsVal(TransObserValues val){
		transValues.add(val);
	}//end of addTransVal
	
	
	public Vector<TransObserValues> getTransObsVals(){
		return transValues;
	}//end of getTransObsVals
	
	
	
	
	

}//end of JmlModule
