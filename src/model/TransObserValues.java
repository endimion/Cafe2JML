package model;

import java.util.Vector;

/**
 * Basically this is a custom map
 * 
 * this class contains a Transtition rule which can be retrieved by the getTransition
 * and the values of some observers (the vector of these values is retrieve by getObserverValues)
 * 
 * This classes'objects are meant to be used after translating a transition rule to store the values 
 * of all the observers after the application of this transition rule
 * @author nikos
 *
 */


public class TransObserValues {
	
	Vector<ObsValPair> observerValues ; 
	CafeTerm        transition;
	
	
	
	public TransObserValues(){
		observerValues = new Vector<ObsValPair>();
	}//end of consturctor
	
	public void setTransition(CafeTerm trans){this.transition = trans;}
	public CafeTerm getTransition(){return transition;}
	public String getTransitionName(){return this.transition.getOpName();}
	
	
	public Vector<?> getTransitionArgs(){ return transition.getArgs();}
	
	
	public void addObsValue(CafeTerm obs, CafeTerm value){
		observerValues.addElement(new ObsValPair(obs, value));
	}
	public Vector<ObsValPair> getObserversValues(){return observerValues;}
	
	
	
	

	
	
	
	
	public class ObsValPair{
		CafeTerm obs;
		CafeTerm val;
		
		public ObsValPair(CafeTerm observer, CafeTerm value){
			this.obs = observer;
			this.val = value;
		}
		public CafeTerm getObs(){return obs;}
		public CafeTerm getValue(){return val;}
	}//end of ObsValPair
	
	
}//end of OperatorValues
