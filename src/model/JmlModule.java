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
	public Vector<ObsValPair> getObsValbyTrans(CafeTerm trans, Module mod){
		
		for(TransObserValues v : getTransObsVals()){
			if(v.getTransitionName().equals(trans.getOpName())){
				//then if the arguments of the transition given are not
				// the same as the arguments of the transition we have saved
				// then we should replace each non equal with the given ones
				// and then return the new vector
				//System.out.println("Transition match! "+ v.getTransitionName() );
				
				Vector<ObsValPair> returnPairs = v.getObserversValues();
				
				CafeTerm savedTrans = v.getTransition(); //the CafeTerm transition for which 
		
				Vector<Object> origVal = new Vector<Object>();
				Vector<Object> replVal = new Vector<Object>();
				
				for(int i=0; i < trans.getArgs().size(); i++){
					Object o = trans.getArgs().get(i);
					if(o instanceof CafeTerm){
						if( !((CafeTerm)o).isEqual((CafeTerm) savedTrans.getArgs().get(i))){
							// them the non matching arguments 
							//(this must be done for all none matching terms)
							//must write a method that takes a vectorOfObsValPair
							// and replaces each occurrence of a given argument with another
							//System.out.println("i will replace " + ((CafeTerm)savedTrans.getArgs().get(i)).termToString(mod) + " with " + ((CafeTerm)o).termToString(mod));
							
							origVal.add(savedTrans.getArgs().get(i));
							replVal.add(o);
							
							//returnPairs = replaceInValues(returnPairs, savedTrans.getArgs().get(i),o,mod);
							
						}//end if we found non matching arguments in the transition

					}else{
						if( !((String)o).equals((String) savedTrans.getArgs().get(i))){
							// them the non matching arguments 
							//(this must be done for all none matching terms)
							
							origVal.add(savedTrans.getArgs().get(i));
							replVal.add(o);
							
							
							//returnPairs = replaceInValues(returnPairs, savedTrans.getArgs().get(i),o,mod);
				
						}//end if we found non matching arguments in the transition 
					}//end if the argument is not a CafeTerm, i.e. is a String
				
				
				
				}//end of looping through the arguments of the transition rule
				
				returnPairs = replaceInValues(returnPairs, origVal,replVal,mod);
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
			//System.out.println(" is Equal ? "+ obs.getOpName() + " with " + p.getObs().getOpName());
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
	public Vector<ObsValPair>  getObsValAfterTransCh(Vector<CafeTerm> chain, Module mod){

		CafeTerm currentTrans;
		Vector<ObsValPair> origObsValP ;  // the pairs obtained after the individual trans rule
		Vector<ObsValPair> replacePairs;  // the pairs we should use to replace the appearence of the obs with

		Vector<ObsValPair> newPairs;
		ObsValPair newP;

		replacePairs = getObsValbyTrans(chain.get(chain.size()-1),mod);
		
		Vector<CafeTerm> allObs =  getObFromPairVect(replacePairs); //stores all observer expressions that have appeared
		
		for(int i=chain.size()-2; i >=0; i--){
			currentTrans = chain.get(i);
			
			origObsValP = getObsValbyTrans(currentTrans,mod);
			
			for(int k=0; k < origObsValP.size();k++){
				boolean match = false;
				for(CafeTerm obs : allObs){
					if(obs.isEqual(origObsValP.get(k).getObs())){
						match = true;
					}
				}//end of for loop through allObs
				if(!match){allObs.add(origObsValP.get(k).getObs()); }
			}//end of looping through origObsValP
			
			System.out.println("TRANSITION IS "+  i +" ::: " + currentTrans.termToString(mod));
			//for(ObsValPair p : origObsValP){
			//	System.out.println("##### " + p.getObs().termToString(mod) + " " + p.getValue().termToString(mod));
			//}

			newPairs = new Vector<ObsValPair>();
			
			for(CafeTerm obsrv : allObs){
			
				CafeTerm newObsVal =  ((CafeTerm)getValOfObs(obsrv, replacePairs));
				if(newObsVal != null) newObsVal = newObsVal.replaceAllMatching(origObsValP);
				else{
					newObsVal =getValOfObs(obsrv, origObsValP);
				}//end if newObsVal was null
				
				CafeTerm prevVal = getValOfObs(obsrv, replacePairs);
				CafeTerm original =  getValOfObs(obsrv, origObsValP);
			
				if(prevVal instanceof BasicTerm && original != null){
					if(! original.termToString(mod).equals(obsrv.termToString(mod))){
						newP = new ObsValPair(obsrv, original);
					}else{newP = new ObsValPair(obsrv, prevVal);}
				}else{
					newP = new ObsValPair(obsrv, newObsVal);
				}
						
				
				
				//newP = new ObsValPair(obsrv, newObsVal);
				newPairs.add(newP);
				
				System.out.println("OBSERVER Is ::: " + obsrv.termToString(mod));
				if(getValOfObs(obsrv, replacePairs) != null)System.out.println("PREVIOUS VALUE IS ::: " + getValOfObs(obsrv, replacePairs).termToString(mod));
				//if(getValOfObs(obsrv, origObsValP) != null) System.out.println("ORIGINAL VALUE IS ::: " + getValOfObs(obsrv, origObsValP).termToString(mod));
				if(newP.getValue() != null)System.out.println("NEW IS ::: " + newP.getValue().termToString(mod));
			}//end of looping through the observers of the module
			
			System.out.println();
			replacePairs = newPairs;
		}//end of for loop through the chains
		
		
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
	public Vector<ObsValPair> replaceInValues(Vector<ObsValPair> pairs, 
			Vector<Object> orig, Vector<Object> repl, Module mod){
		
		Vector<ObsValPair> newPairs = new Vector<ObsValPair>();
		 //first we add the original pairs
		
		 for(int i =0; i< pairs.size();i++){
			
			CafeTerm obs = pairs.get(i).getObs().replaceTerms(orig, repl);
			CafeTerm val = pairs.get(i).getValue().replaceTerms(orig, repl);
			
			newPairs.add(new ObsValPair(obs, val));
		}//end of looping through the pairs
		 
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
