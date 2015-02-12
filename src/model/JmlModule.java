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
	
	
	/**
	 * 
	 * @return a Vector<CafeTerm> containing all the observers of the module
	 */
	public Vector<CafeTerm> getModuleObservers(){
		Vector<CafeTerm> obser = new Vector<CafeTerm>();
		Vector<ObsValPair> pairs = getTransObsVals().get(0).getObserversValues();
		
		for(ObsValPair ov : pairs){
			obser.add(ov.getObs());
		}
		return obser;
	}//end of getModuleObservers
	
	
	
	
	
	
	/**
	 * takes as input a CafeTerm denoting a transition and returns 
	 * a vector containing the values of the observers for that transition
	 * @param trans
	 * @return
	 */
	public Vector<ObsValPair> getObsValues(CafeTerm trans){
		for(TransObserValues v : getTransObsVals()){
			if(v.getTransitionName().equals(trans.getOpName())){
				//System.out.println("THE TRANSITION WAS:"+ trans.getOpName());
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
	
	
	
	
	
	
	
	/**
	 * this methods takes as input a Vector<CafeTerms> denoting a sequence of
	 * transitions and returns a Vector<ObsValPair> denoting the values of all the
	 * observers of the module after the execution of all the transitions
	 * @param chain a Vector<CafeTerms> denoting an execution chain of transitions
	 */
	public Vector<ObsValPair>  getObsValAfterTransCh(Vector<CafeTerm> chain){

		CafeTerm currentTrans;
		Vector<ObsValPair> origObsValP ; 
		Vector<ObsValPair> replacePairs;
		Vector<ObsValPair> newPairs;
		ObsValPair newP;

		replacePairs = getObsValues(chain.get(chain.size()-1));
		
		
		for(int i=chain.size()-2; i >=0; i--){
			currentTrans = chain.get(i);
			origObsValP = getObsValues(currentTrans);
			
			System.out.println("TRANSITION IS "+  i +" ::: " + currentTrans.termToString());
			newPairs = new Vector<ObsValPair>();
			
			for(CafeTerm obsrv : getModuleObservers()){
				CafeTerm newObsVal = 
						((CafeTerm)getValOfObs(obsrv, replacePairs)).replaceAllMatching(origObsValP);
				newP = new ObsValPair(obsrv, newObsVal);
				newPairs.add(newP);
				
				System.out.println("OBSERVER Is ::: " + obsrv.termToString());
				System.out.println("PREVIOUS VALUE IS ::: " + getValOfObs(obsrv, replacePairs).termToString());
				System.out.println("ORIGINAL VALUE IS ::: " + getValOfObs(obsrv, origObsValP).termToString());
				System.out.println("NEW IS ::: " + newObsVal.termToString());
			}//end of looping through the observers of the module
			
			replacePairs = newPairs;
		}//end of for loop through the chains
		
		return replacePairs;
	}//end of retrieveValAfterTrans
	
	
	
	
	

}//end of JmlModule
