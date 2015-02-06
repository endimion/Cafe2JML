package model;

import java.util.Vector;

import model.TransObserValues.ObsValPair;

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
	
	
	
	
	/**
	 * takes as input a CafeTerm denoting a transition and returns 
	 * a vector containing the values of the observers for that transition
	 * @param trans
	 * @return
	 */
	public Vector<ObsValPair> getObsValues(CafeTerm trans){
		
		for(TransObserValues v : getTransObsVals()){
			if(v.getTransitionName().equals(trans.getOpName())){
				//System.out.println("AAAAADDDBBBB " + v.getObserversValues().size());
				return v.getObserversValues();
			}
		}
		return null;
	}//end of getObsValues
	
	
	/**
	 * 
	 * @param obs a CafeTerm denoting an observer
	 * @param pairs a vector contain the pairs of observers and values for them
	 * @return a CafeTerm denoting the value of the given observer based on the given set of pairs
	 */
	public CafeTerm getValOfObs(CafeTerm obs, Vector<ObsValPair> pairs){
		for(ObsValPair p : pairs){
			if(obs.getOpName().equals(p.getObs().getOpName())){
				return p.getValue();
			}//end of if the name of the observer is the same as the one found in the pairs
		}//end of looping throught the pairs
		return null;
	}//end of getValOfObs
	
	
	
	
	
	/*
	public Vector<CafeTerm> getObsTerms(CafeTerm trans){
		 Vector<CafeTerm> result = new Vector<CafeTerm>();
		 
		for(TransObserValues v : getTransObsVals()){
			if(v.getTransitionName().equals(trans.getOpName())){
				for(ObsValPair pair : v.getObserversValues()){
					result.add(pair.getValue());
				}
			}
		}
		return result;
	}//end of getObsValues
	*/
	
	
	
	/**
	 * this methods takes as input a Vector<CafeTerms> denoting a sequence of
	 * transitions and an observer and returns the value of that observer
	 * after the execution of the chain of transitions
	 */
	public void getObsValAfterTransCh(CafeTerm obs, Vector<CafeTerm> trans){
		
		CafeTerm currentTrans;
		//System.out.println("sizeeeee " + trans.size());
		//CafeTerm updatedVal = null;
		
		
		// 1) first we get the values of all the observers after the execution of the 
		//  	last transition of the chain!!! and store that in a vector say, oldObsVals
		
		//TODO add a check if the vector of transitions is greater than zero
		Vector<ObsValPair> oldObsVals = getObsValues(trans.get(trans.size()-1)); 
		
		
		for(int i=trans.size()-2; i >=0; i--){
			currentTrans = trans.get(i);
			System.out.println("transition is ::: " + trans.get(i).termToString());
			//System.out.println("the observer is ::: " + obs.termToString());
			
			//TODO
			//2)  for each next transition in the chain we calculate the values of all
			// 		the observers by replacing the each appearance of an observer call
			// 		with its value stored in oldObsVals THIS WILL BE A CAFETERM METHOD
			//  	and store that info in oldObsVals
			if(obs instanceof CompTerm){
				System.out.println("OBSERVER Is ::: " + obs.termToString());
				System.out.println("OLD VALUE IS ::: " + getValOfObs(obs, oldObsVals).termToString());
				
				CafeTerm newObsVal = ((CafeTerm)getValOfObs(obs, oldObsVals)).replaceAllMatching(oldObsVals);

				System.out.println("NEW IS ::: " + newObsVal.termToString());
			}//end if obs is a compTerm
			
			 
			
			
			CafeTerm val = getValOfObs(obs, getObsValues(currentTrans));
			//System.out.println("the value of the given observer : " + obs.termToString() + " is " +val.termToString());
			
		}//end of for loop
		
	}//end of retrieveValAfterTrans
	
	
	
	
	

}//end of JmlModule
