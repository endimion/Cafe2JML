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
				//TODO
				//then if the arguments of the transition given are not
				// the same as the arguments of the transition we have saved
				// then we should replace each non equal with the given ones
				// and then return the new vector
				
				Vector<ObsValPair> returnPairs = v.getObserversValues();
				CafeTerm savedTrans = v.getTransition();
				
				for(int i=0; i < trans.getArgs().size(); i++){
					Object o = trans.getArgs().get(i);
					if(o instanceof CafeTerm){
						if( !((CafeTerm)o).isEqual((CafeTerm) savedTrans.getArgs().get(i))){
							//System.out.println("Not equal cafeterm in arguments of " + v.getTransitionName());
						
							//TODO get the value of the observers for this transition and replace in
							// them the non matching arguments 
							//(this must be done for all none matching terms)
							//must write a method that takes a vectorOfObsValPair
							// and replaces each occurrence of a given argument with another
							returnPairs = replaceInValues(returnPairs, savedTrans.getArgs().get(i),o);
							
						}//end if we found non matching arguments in the transition

					}else{
						if( !((String)o).equals((String) savedTrans.getArgs().get(i))){
							//TODO get the value of the observers for this transition and replace in
							// them the non matching arguments 
							//(this must be done for all none matching terms)
							returnPairs = replaceInValues(returnPairs, savedTrans.getArgs().get(i),o);
							
						}//end if we found non matching arguments in the transition 
					}//end if the argument is not a CafeTerm, i.e. is a String
				
				
				
				}//end of looping through the arguments of the transition rule
				
				return returnPairs;//v.getObserversValues();
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
			if(obs.isEqual(p.getObs())){
				return p.getValue();
			}
			
		}//end of looping through the pairs
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
			//getObFromPairVect(replacePairs)
			
			for(CafeTerm obsrv :getObFromPairVect(replacePairs)){
				
				CafeTerm newObsVal = ((CafeTerm)getValOfObs(obsrv, replacePairs));
				if(newObsVal != null) newObsVal = newObsVal.replaceAllMatching(origObsValP);
				
				CafeTerm prevVal = getValOfObs(obsrv, replacePairs);
				CafeTerm original =  getValOfObs(obsrv, origObsValP);
				
				if(prevVal instanceof BasicTerm){
					//if(!original.getOpName().equals(obsrv.getOpName())){
						newP = new ObsValPair(obsrv, original);
					//}else{newP = new ObsValPair(obsrv, prevVal);}
				}else{
					newP = new ObsValPair(obsrv, newObsVal);
				}
						
				
				
				//newP = new ObsValPair(obsrv, newObsVal);
				newPairs.add(newP);
				
				System.out.println("OBSERVER Is ::: " + obsrv.termToString());
				System.out.println("PREVIOUS VALUE IS ::: " + getValOfObs(obsrv, replacePairs).termToString());
				System.out.println("ORIGINAL VALUE IS ::: " + getValOfObs(obsrv, origObsValP).termToString());
				System.out.println("NEW IS ::: " + newP.getValue().termToString());
			}//end of looping through the observers of the module
			
			System.out.println();
			replacePairs = newPairs;
		}//end of for loop through the chains
		
		int i =0;
		for(ObsValPair p: replacePairs){
			System.out.println("TRANS "+chain.get(i).termToString());
			System.out.println("observer " + p.getObs().termToString() + " val " + p.getValue().termToString());
			i++;
		}
		System.out.println(" ");
		
		
		return replacePairs;
	}//end of retrieveValAfterTrans
	
	
	
	/**
	 * Takes as input a vector of ObsValPair and adds the new pairs obtained by replacing 
	 * inside the VALUES and Expression of the observer each occurrence of the term orig with the term repl
	 * @param pairs a Vector<ObsValPair> which denotes the values of the observers in some transition
	 * @param orig the original term in the values that need to be replaced
	 * @param repl the new term which will replace the original
	 * @return
	 */
	public Vector<ObsValPair> replaceInValues(Vector<ObsValPair> pairs, Object orig, Object repl){
		
		
		Vector<ObsValPair> newPairs = new Vector<ObsValPair>();
		 //first we add the original pairs
		 for(ObsValPair p : pairs){
			 newPairs.add(p);
		 }//end of looping through the original pairs
		 
		 for(int i =0; i< pairs.size();i++){
			
			CafeTerm obs = pairs.get(i).getObs().replaceTerm(orig, repl);
			CafeTerm val = pairs.get(i).getValue().replaceTerm(orig, repl);
			
			System.out.println("");
			
			System.out.println("The original observer was " + pairs.get(i).getObs().termToString());
			System.out.println("with val " + pairs.get(i).getValue().termToString());
			System.out.println("The replaced observer is " +  pairs.get(i).getObs().replaceTerm(orig, repl).termToString());
			System.out.println("with val " +val.termToString());
			System.out.println("");
			
			
			if(obs.isEqual(pairs.get(i).getObs())){
				newPairs.removeElementAt(i);
				newPairs.add(i, new ObsValPair(obs,val) );
				
			}else{
				newPairs.add(new ObsValPair(obs,val));
			}
		}
		return newPairs;
	}//end of replaceInValues
	
	
	/**
	 * 
	 * @param pairs a Vector<ObsValPair>
	 * @return a vector of CafeTerms containing all the observers which appear in pairs
	 */
	public Vector<CafeTerm> getObFromPairVect(Vector<ObsValPair> pairs){
		Vector<CafeTerm> obser = new Vector<CafeTerm>();
		for(ObsValPair p: pairs){
			obser.add(p.getObs());
		}
		return obser;
	}//end of getObserversByTransition
	

}//end of JmlModule
