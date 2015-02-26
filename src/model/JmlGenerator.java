package model;

import java.util.Vector;



public class JmlGenerator {

	Vector<Module> modules;
	Vector<JmlModule> jModules;
	
	public JmlGenerator(Vector<Module> mod){
		this.modules = mod;
		this.jModules = new Vector<JmlModule>();
	}//end of constructor
	
	
	
	
	
	/**
	 * @param  mod a Module Object
	 * @return  a string consisting of the translation of the initial states
	 * for the given module
	 */
	public String translateInitStates(Module mod){
		String res = "";
		Vector<CafeOperator> initStates = mod.getInitialStates();
		
//		String forallDecl="";
		
		
		
		for(CafeOperator init : initStates){
			//1)get the equations matching that have on the left hand side the intial state operator
			//2) get the variables for each such equation
			//2') cheat: just add all the variables which might not appear on the term!!!!
			//3) add to the output an expression: (\forall D1 x1... Dn xn; mainOperatorOfEquation(x1,..xn)
			// == with the translation of the right part of the equation)
			
			Vector<CafeEquation> matchingEqs = mod.getMatchingLeftEqs(init.getName());
		
		//String  forString ="";
			
			for(int i = 0 ; i < matchingEqs.size() ; i++){
				CafeEquation eq = matchingEqs.get(i);
				CafeTerm left = eq.getLeftTerm();
				CafeTerm right = eq.getRightTerm();
				
				res +=  forallDecl(mod, "/*@ initially",eq,null);
				res += "  @ "+ left.printTermSkipArg(0,mod) + " == " + right.termToString(mod,this);
				res +=(i != matchingEqs.size()-1)? " &&" + '\n': ");"+'\n';
				
			}//end of looping through the equations except form the last one
			res += " @*/"+ '\n' + '\n' + '\n' +"public "+init.getName() + "(){}"  + '\n' + '\n';
		}//end of looping through the inital states of the module

		return res;
	}//end of translateInitStates
	
	
	/**
	 * 
	 * @param mod the module under translation
	 * @return a  string representing the Java declaration for the observers
	 * of the module
	 */
	public String translateObservers(Module mod){
		Vector<CafeOperator> obs = mod.getObservers();
		String start ="public /*@ pure @*/ " ;
		String res ="";
		CafeOperator bop;
		String observerArguments; 
		
		
		for(int j=0; j < obs.size();j++){
			bop = obs.get(j);
			observerArguments = "(" + opArgsToString(bop, mod, 
										null);
			res += start + bop.getSort()+ " "+ bop.getName() + observerArguments +"){}" +'\n';
			
		}//end of looping through the observers
		
		
		return res;
	}//end of translateObservers
	
	
	
	/**
	 * Translates a Simple equation, i.e. without projection operators
	 * and returns part of a JML contract
	 * 
	 * @param cond a CafeTErm denoting the condition of the equation
	 * @param mod the Module object in which the equation translated belongs to
	 * @param left a CafeTerm denoting the leftHandside of the equation
	 * @param right a CafeTerm denoting the rightHandside of the equation
	 * @param leftPos an int denoting the position 
	 *        (in the arguments Vector) of the CafeTerm who denotes the system in the left term
	 * @param rightPos an int denoting the position 
	 *        (in the arguments Vector) of the CafeTerm who denotes the system in the right term
	 * @param valOfObser a TransObserValues containing the values of all observers for this transition
	 * @param i the index of the transition in the loop
	 * @param jmod an JmlModule object that will store info about the translation
	 * @param transEq a vector of CafeEquations
	 * @param forallStart  a String that denotes the for all declaration
	 * @return a String containing the part of the JML contract from the translation of the equation
	 */
	public String translateSimpleEquation(Module mod, CafeTerm left, 
			CafeTerm right, int leftPos , int rightPos, 
			TransObserValues valOfObser, int i, JmlModule jmod, Vector<CafeEquation> transEq,
			String forallStart, CafeTerm cond){
		
		String res ="";
		boolean isObject;
		
		
		res +="@ " ;
		
		res += left.printTermSkipArg(leftPos,mod) ;
		isObject =isObjectByOpName(left.getOpName(), mod);
		if(isObject){
			res += ".equals("; 
					
		}else{
			res += " == ";
		}
		
		if(StringHelper.numOf(right.printTermSkipArg(rightPos,mod), '(') > 0){
			//res += "\\old("+right.printTermSkipArg(rightPos,mod) +")" ;
			res += "\\old("+right.termToString(mod, this) +")" ;
		}else{
			//res += right.printTermSkipArg(rightPos,mod)  ;
			res += right.termToString(mod, this) ;	
		}
		
		//if(isObject){res += ")";}
		//res += rightHS;
		
		/* !!!!!!!!!!!!!!!!!!!!!!!!!!! */
		//now since the rightHS of the equation has been translated it can be stored
		CafeTerm leftObs =  left;
		CafeTerm trans = (CafeTerm)left.getArgs().get(leftPos); //we retrieve the sub term that denotes the transition rule
		int sysPos = TermParser.getPositionOfSystemSort(trans, mod); //find the system sort in the transition term
		
		trans.getArgs().remove(sysPos); //end remove it
		
		if(left instanceof BasicTerm){
			BasicTerm temp = new BasicTerm(false);
			temp.setOpName(left.getOpName());
			int k = 0;
			for(Object o: left.getArgs()){
				if(k!= leftPos)temp.addArg(o);
				k++;
			}
			leftObs = temp;
		}else{
			CompTerm temp = new CompTerm();
			temp.setOpName(left.getOpName());
			int k = 0;
			for(Object o: left.getArgs()){
				if(k!= leftPos)temp.addArg(o);
				k++;
			}
			leftObs = temp;
		}
		
		
		CafeTerm rightHS = right;
		sysPos = TermParser.getPositionOfSystemSort(right, mod);//find the system sort in the rhs
		if(sysPos >= 0){
			rightHS.getArgs().remove(rightPos); //end if it exists remove it
		}
		
		valOfObser.setTransition(trans); //save the CafeTerm denoting the transition
		valOfObser.addObsValue(leftObs, rightHS); //save the CafeTerm denoting the observer and the term denoting its value
		//and we can save this information
		
		jmod.addTransObsVal(valOfObser); //add the <transition, (observer, value)> pair to the jmlmod
		
		if(i == transEq.size() -1){
			res += (forallStart.contains("forall"))? "));" +'\n':")"+'\n';
		}else{
			if(cond == null)res += (forallStart.contains("forall"))? ")) &&" +'\n':" &&"+'\n';
			else res += (forallStart.contains("forall"))? ")) &&" +'\n':") &&"+'\n';
		}
		
		return res;
	}//end of translateSimpleEquation
	
	
	
	
	
	/**
	 * Returns a CafeTerm which does not contain any arguments of the given projection sort
	 * 
	 * @param projSort a Sort denoting a projection operator sort
	 * @param trans a CafeTerm denoting a transition rule
	 * @return a new CafeTerm obtained by trans by not including any proSorted arguments
	 */
	private CafeTerm removeProjectSort(String projSort, CafeTerm trans){
		if(trans instanceof BasicTerm){
			BasicTerm temp = new BasicTerm(false);
			temp.setOpName(trans.getOpName());
			for(String arg: ((BasicTerm)trans).getArgs()){
				BasicTerm b = new BasicTerm(false);
				b.setOpName(arg);
				if(!getTermSort(b).equals(projSort)){
					temp.addArg(arg);
				}
			}
			trans = temp;
			
		}else{
			CompTerm temp = new CompTerm();
			temp.setOpName(trans.getOpName());
			for(Object arg : trans.getArgs()){
				
				
				if(arg instanceof String){
					BasicTerm b = new BasicTerm(false);
					b.setOpName((String)arg);
					if(!getTermSort(b).equals(projSort)){
						temp.addArg(arg);
					}
				}else{
					if(!getTermSort(((CafeTerm)arg)).equals(projSort)){
						temp.addArg(arg);
					}
				}//end if arg is not a String
				trans = temp;
			}//end of looping through the args
		}//end if trans is a CompTerm

		return trans;
	}//end of removeProjectSort
	
	
	/**
	 * 
	 * @param obsValP a VEctor<ObsValPair>, i.e. containing pairs of observer and values
	 * @return a Vector<String> which contains all the variables which appear in obsValP 
	 */
	private Vector<String> getVarsOfObsList(Vector<ObsValPair>  obsValP){
		
		Vector<String> res = new Vector<String>();
		
		for(ObsValPair p : obsValP){
			CafeTerm obs = p.getObs();
			for(Object arg : obs.getArgs()){
				if(arg instanceof String){
					if(!isOperator((String)arg)){
						res.add((String)arg);
					}
				}else{
					if(!isOperator( ((CafeTerm)arg).getOpName())){
						res.add(((CafeTerm)arg).getOpName());
					}
				}
			}//end of looping through the arguments of the observer

		}//end of looping through the pairs

		//for(String s : res){System.out.println(s);}
		
		return res;
	}//end of getVarsOfObsList
	
	
	/**
	 * 
	 * @param projSort the sort of the module of the projection
	 * @param mod the module we found the projection equation inside
	 * @param project the CafeTerm denoting the projection operator
	 * @return a String representation of the translation of a projection equation which does not change
	 * the state of the projected object
	 */
	private String projNoStateChangeTranslation(String projSort, Module mod, CafeTerm project){
		String res="";
		Module projMod = getModBySort(projSort);
		
		int j = 0;
		for(CafeOperator op : projMod.getObservers()){
			
			String varsDecl="";
			
			if(op.getArity().size() > 0){
				if(op.getArity().size() == 1 && op.getArity().get(0).equals(projSort)){
					res += "@";
				}else{res += "@( \\forall ";}
				
				String aVar ="";
				String sort="";
				Vector<String> vars = new Vector<String>();
				
				for(CafeVariable v : projMod.getVars()){
					vars.add(v.getName());
				}//end of looping through the variables of the module
				
				for(int i = 0; i < op.getArity().size();i++){
					sort = TermParser.cafe2JavaSort(op.getArity().get(i));
					if(!(sort.equals(mod.getClassSort()) || sort.equals(projSort) ) ){
						aVar = getVarBySort(op.getArity().get(i), projMod, "", vars);
						res +=  sort +  " " 
								+ aVar + ", ";
						varsDecl += aVar +", ";
					}//end if sort is equal to the module sort or the sort equals to the projected module sort
				}//end of looping through the arity of the operator
				if(res.endsWith(", ")){res = StringHelper.remLastChar(res.trim()) + ";";}
				res +='\n';
			}//end if op.getArity().size() > 0
			
			if(varsDecl.endsWith(", ")){varsDecl = StringHelper.remLastChar(varsDecl.trim());}
			res += "@ "+ project.termToString(mod, this) +"."+ op.getName() + "("+ varsDecl +") "
					+ "==" + "\\old(" +    project.termToString(mod, this) +"."+ op.getName() + "("+ varsDecl +")) " ; 
			
			
			if(!(op.getArity().size() <= 1 && op.getArity().get(0).equals(projSort))){
				res += ")";
			}
			if(j < projMod.getObservers().size()-1) res += " && " + '\n';
			j++;
		}//end of looping through the observers
		
		return res;
	}//end of projNoStateChange
	
	
	
	
	//TODO add the guards of the transitions of the projected object
	/**
	 * 
	 * @return a JML contract for translating a projection operator equation
	 */
	public String translateProjectEquation(CafeTerm rightHS,  CafeTerm leftHS,int rightPos, 
			Module mod,	String projSort, CafeTerm project, JmlModule jmod, Vector<String> arityVars){
		
		Module projMod = getModBySort(projSort);
		String res="";
		
		
		if(rightHS.termToString(mod, this).equals(leftHS.termToString(mod, this))){
			res += projNoStateChangeTranslation(projSort, mod, project);
		}else{
			Vector<CafeTerm> chain = new Vector<CafeTerm>();
			buildChainFromTerm(chain,rightHS, mod,projSort,project.getOpName());
			
			//TODO
			Vector<CafeTerm> guards = buildGuardChain(rightHS, mod, projSort,project.getOpName());
			String  guardString = "";
			for(CafeTerm guard : guards){
				guardString += guard.termToString(projMod, this) + "&& ";
			}//end of looping through the guards
			if(guardString.endsWith("&& ")) guardString = StringHelper.remLastChar(StringHelper.remLastChar(guardString.trim()));
			
			Vector<CafeTerm> temp = new Vector<CafeTerm>();
			for(CafeTerm t : chain){
				temp.add(removeProjectSort(projSort, t));
			}
			chain  = temp;
			
			Vector<ObsValPair>  obsValP = jmod.getObsValAfterTransCh(chain, getModBySort(projSort));
			Vector<String> varList = getVarsOfObsList(obsValP);
			//TODO cleanUP, i.e. remove variables which appear in the arity of the method
			for(String var1: varList){
				for(String arVar : arityVars){
					if(arVar.equals(var1)){
						varList.remove(var1);
						break;
					}
				}
			}//end of looping through the varList
			
			res += "@" +guardString + "==>" +'\n';
			res += "@ (\\forall ";
			BasicTerm b = new BasicTerm(false);
			for(String var : varList){
				b.setOpName(var);
				res += TermParser.cafe2JavaSort(getTermSort(b)) + " " + var + ", ";
			}
			
			if(res.endsWith(", ")) res = StringHelper.remLastChar(res.trim()) + "; " + '\n';
			
			
			
			
			for(int j =0 ; j< obsValP.size(); j++){
				ObsValPair p  = obsValP.get(j);
				res += "@ " + project.termToString(mod, this)+"."+p.getObs().termToString(mod, this);
				
				if(!isObjectByOpName(p.getObs().getOpName(), projMod)){
					res +=  "==" +" \\old(" + p.getValue().termToString(mod, this) + ")";
				}else{
					res +=  ".equals(" +" \\old(" + p.getValue().termToString(mod, this) + "))";
				}//end if it is not an object		
						
				if(j <  obsValP.size()-1) res += " &&"+'\n';
				
			}//end of looping through  the new values of the observers
			res += ")" ;
			
			
			
		}//end if the transitions of the rightHs system sorted term 
		
		return res;
	}//end of translateProjectEquation
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param sort the sort of the module we are searching for
	 * @return the Module object which defines the given sort 
	 */
	public Module getModBySort(String sort){
		
		//System.out.println("looking for sort " + sort);
		for(Module mod : modules){
			if(mod.getClassSort() != null)
			if(mod.getClassSort().equals(sort)){   
				//System.out.println("found it a " + mod.getName()) ;
				return mod;
			}
		}
		return null;
	}//end of getModBySort
	
	/**
	 * 
	 * @param sort the sort of the module we are searching for
	 * @return the JmlModule object which defines the given sort 
	 */
	private JmlModule getJModBySort(String sort){
		
		//System.out.println("looking for sort " + sort);
		for(JmlModule mod : jModules){
			if(mod.getSort() != null)
			if(mod.getSort().equals(sort)){   
				return mod;
			}
		}
		return null;
	}//end of getModBySort
	
	
	
	
	
	
	
	/**
	 * Generates a chain of cafeTerms from the given term 
	 * @param chain a Vector<CafeTerm>
	 * @param term a CafeTerm
	 * @param projModSort the sort of the module we are projecting to
	 * @param projName 	the name of the projection operator
	 * @return a Vector<CafeTerm> which contains all the arguments of the given term  including the term itself
	 */
	public Vector<CafeTerm> buildChainFromTerm(Vector<CafeTerm> chain, CafeTerm term, 
			Module mod, String projModSort, String projName){
		if( getTermSort(term).equals(projModSort) 
				&& !(term).getOpName().equals(projName))
		{ chain.add(term);}
		
		for(Object arg: term.getArgs() ){
			if(arg instanceof CafeTerm){ 
				if( getTermSort((CafeTerm)arg).equals(projModSort))
				{	
					buildChainFromTerm(chain, (CafeTerm) arg,mod, projModSort,projName);
				}
			}
		}//end for loop
		return chain;
	}//end of buildChainFromTerm
	
	
	
	//TODO
	public Vector<CafeTerm> buildGuardChain(CafeTerm term, Module mod, String projSort, String projName){
		Module projMod = getModBySort(projSort);
		
		Vector<CafeTerm> guardChain =new Vector<CafeTerm>();
		Vector<CafeTerm> terms =  new Vector<CafeTerm>();
		buildChainFromTerm(terms, term, mod, projSort, projName);
		
		for(CafeTerm t : terms){
			Vector<CafeEquation> matchEqs =  projMod.getMatchingLeftEqs(t.getOpName());
			
			for(CafeEquation eq : matchEqs){
				if(eq.getCondition()!= null){ 
					guardChain.add(eq.getCondition());
				}
			}//end of looping through the equations that have on their lhs the term 
		}//end of looping through the given terms
		

		for(int i=1; i < guardChain.size() -1; i++){
			if(guardChain.get(i).isEqual(guardChain.get(i+1))){
				guardChain.remove(i);
			}
		}//end of cleaning up the guards vector
		
		
		
		return guardChain;
	}//end of buildGuardChain
	
	
	
	
	
	
	/**
	 * @param mod the module which contains the parsing of the CafeOBJ file
	 * @param jmod an JmlModule object that will store info about the translation
	 * @return a String containing the translation of the transition
	 * operations of the given module
	 */
	public String translateTransition(Module mod, JmlModule jmod){
		Vector<CafeOperator> actions = mod.getActions(); 
		Vector<CafeEquation> transEq; 
		
		String res ="";
		String forallStart="";
		
		
		TransObserValues valOfObser ; 
		
		for(CafeOperator bop : actions){
			transEq = mod.getMatchingLeftEqs(bop.getName());
			
			//first we set the transition to be the current transition
			valOfObser = new TransObserValues();
			
			res +="/*@ ensures " + '\n'; 
			for(int i =0; i< transEq.size();i++){
				forallStart = (i == 0)?forallDecl(mod,"",transEq.get(i),bop):forallDecl(mod,"@ ",transEq.get(i),bop) ;
				if(!forallStart.equals("@ "))res +=  forallStart;
				
				CafeTerm left = transEq.get(i).getLeftTerm();
				CafeTerm right = transEq.get(i).getRightTerm();
				CafeTerm cond = transEq.get(i).getCondition();
				
				int leftPos = TermParser.getPositionOfSystemSort(left,mod);
				int rightPos = TermParser.getPositionOfSystemSort(left,mod);
				
				if(cond!= null){
					if(!res.endsWith("@ ")) res +="@";
					//res += "(" + cond.printTermSkipArg(condPos,mod).replace("c-","c") + " ==> "+ '\n';
					res += "(" + cond.termToString(mod, this).replace("c-","c") + " ==> "+ '\n';
				}
				
				if(!mod.isProjection(modules, left.getOpName())){
					res += translateSimpleEquation(mod, left, right, leftPos, rightPos,  
							valOfObser, rightPos, jmod, transEq, forallStart,cond); 
				}else{
					
					String projSort = mod.getTermSort(left);
					CafeEquation eq = new CafeEquation();
					eq.setLeftTerm(left);
					eq.setRightTerm(right);
					Vector<String> arityVars = mod.getVariableOfEq(eq);
					//System.out.println("LEEEEFFFFT " + left.getOpName()+ left.termToString(null, null));
					
					res += translateProjectEquation(right, left,rightPos,mod,projSort,left,
							getJModBySort(projSort), arityVars);
					
					if(cond!=null){
						res += ");" + '\n';
					}else{
						res += ";" + '\n';
					}
					
				}//end if the leftHS main operator (i.e. observer) of the equation is a projection
			
			}//end of looping through the transition equations
			res +="@*/" + '\n';
			
			if(transEq.size() > 0 && mod.getVariableOfEq(transEq.get(0)).size() >0 ){
				res += "public void " + bop.getName() +"("+ 
						opArgsToString(bop, mod, mod.getVariableOfEq(transEq.get(0))) +"){}" +'\n' +'\n';
			}else{
				res += "public void " + bop.getName() +"("+ 
						opArgsToString(bop, mod, modVarsToStringVect(mod)) +"){}" +'\n' +'\n';
			}
		}//end of looping through the transitions 
		
		return res;
	}//end of translateTransition
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @param vars a Vector<String> denoting all the variables that must be
	 * quantified
	 * @param initString a String denoting how the for all declaration must start
	 * @param mod the module inside which we want to retrieve the variables
	 * @param action the CafeOperator which is the main operator of the lhs of the equation
	 * @return a String denoting a universal quantification of variables
	 * 			such that they appear in the given equation lhs but NOT INSIDE
	 * 			of the given operator action
	 */
	private String forallDecl(Module mod, String initString, 
			CafeEquation eq, CafeOperator action){
		
		String forallDecl="";
		forallDecl += (initString.equals("initially"))? "/*@" :"";
		forallDecl += initString +" (\\forall ";

		if(eq != null && action != null){
			
			Vector<String> varsToQuant = new Vector<String>();
			Vector<String> transVars = mod.getVarsOfOpinEq(action, eq);
			boolean add ;
			for(String var: mod.getVariableOfEq(eq)){
				add= true;
				for(String actVar : transVars){
					if(actVar.equals(var)){
						add = false;
					}
				}//end of looping through the transition variables
				if(add){varsToQuant.add(var);}
			}//end of looping through all the variables of the equation
			
			
			for(String var: varsToQuant){
				if(!getSortbyVarName(var, mod).equals(mod.getClassSort()))
					forallDecl = forallDecl + " "+ 
							getSortbyVarName(var, mod) + " " +
							var + ", ";
			}//end of looping through the variables we want to quantify!!
			
			if(forallDecl.endsWith(", "))forallDecl = 
					StringHelper.remLastChar(forallDecl.trim());
			
			forallDecl += ";" + '\n';
			return (varsToQuant.size() > 0)? forallDecl:initString;
		}else{
			if(eq != null){
				Vector<String> eqVars = mod.getVariableOfEq(eq);
				for(String var: eqVars){
					if(!getSortbyVarName(var, mod).equals(mod.getClassSort()))
						forallDecl = forallDecl + " "+ 
								getSortbyVarName(var, mod) + " " +
								var + ", ";
				}//end of looping through the variables we want to quantify!!
				
				if(forallDecl.endsWith(", "))forallDecl = 
						StringHelper.remLastChar(forallDecl.trim());
				
				forallDecl += ";" + '\n';
				return (eqVars.size() > 0)? forallDecl:"";
				
			}//end if eq != null but action == null
			return "NOEQ";
		}//end if (eq != null && action != null)
	}//end of forallDecl
	
	
	/**
	 * translates the effective conditions of the transition rules
	 * @param mod
	 * @return
	 */
	public String translateGuards(Module mod){
		String res ="";
		String startOfEqTrans ="";
		Vector<CafeOperator> guards = mod.getEffectiveConditions();
		
		
		for(CafeOperator guard : guards){
			Vector<CafeEquation> guardEqs = mod.getEqsForOp(guard.getName().trim());
		
			for(CafeEquation eq : guardEqs){
				//System.out.println("!!!!!" + guard.getName());
				//int rightSortPos = TermParser.getPositionOfSystemSort(eq.getRightTerm(), mod);
				startOfEqTrans = forallDecl(mod, "/*@ensures",eq,guard);
				res += startOfEqTrans +'\n';
				if(!isObjectByOpName(eq.getLeftTerm().getOpName(), mod)){
					//res += "@ \\result == " + eq.getRightTerm().printTermSkipArg(rightSortPos, mod);
					res += "@ \\result == " + eq.getRightTerm().termToString(mod, this);
				}else{
					//res += "@ \\result.equals(" + eq.getRightTerm().printTermSkipArg(rightSortPos, mod) ;
					res += "@ \\result.equals(" + eq.getRightTerm().termToString(mod, this) ;
				}
				res += (startOfEqTrans.equals("/*@ensures"))? '\n':")" +'\n';   //the extra parenth is added in case a (\forall is added to the satrt of the contract
			}//end of looping through the equations that have at the lhs an observer
			res += "@*/" + '\n' ;
			
			
			if(guardEqs.size() > 0 && mod.getVariableOfEq(guardEqs.get(0)).size() >0 ){
				res += "public "+ guard.getSort() + " " + guard.getName().replace("c-", "c") +"(" + 
						opArgsToString(guard, mod,mod.getVariableOfEq(guardEqs.get(0))) +"){}" + '\n' + '\n';
			}else{
				res += "public "+ guard.getSort() + " " + guard.getName().replace("c-", "c") +"(" + 
						opArgsToString(guard, mod,mod.getVariableOfEq(guardEqs.get(0))) +"){}" + '\n' + '\n';
			}
			
			
		}//end of looping through the observers

		return res;
	}//end of translateGuards
	
	
	
	/**
	 * 
	 * @return a string denoting the declaration of all the constants denoting
	 * an Object other than the initial system states, i.e. generates the declaration
	 * of the translation of hidden constants as freshly created Java objects of the appropriate 
	 * sort
	*/
	public String translateHiddenConstants(Module mod){
		String res="";
		
		Vector<CafeOperator> ops = mod.getConstants();
		Vector<CafeOperator> consts = new Vector<CafeOperator>();
		
		for(CafeOperator op: ops){
			//System.out.println("possibleconstnt " + op.getSort() + " " + op.getName());
			if(!op.getSort().equals(mod.getClassSort()) && isObjectByOpName(op.getName(), mod)){
				consts.add(op);
			}//end of if
		}//end of looping through the operators
		
		//System.out.println("num of constanst " + consts.size());
		for(CafeOperator con : consts){
			res += "public " + con.getSort() + " "+ con.getName() +"(" + 
					opArgsToString(con, mod, modVarsToStringVect(mod)) +"){}" + '\n' + '\n';
		}//end of looping throught the hidden constants
		
		
		return res;
	}//end of translateHiddenconstants
	
	
	
	
	
	
	
		
	/**
	 * @param a string which contains the variables already used in the signature of a term
	 * @param sort a string denoting a sort
	 * @param the_vars a Vector<String> which denotes from which vars to choose from
	 * @return a variable from that sort such that it does not appear in the existing variables 
	 * of the signature of the term
	 */
	private String getVarBySort(String sort, Module mod, String existing,
			Vector<String> the_vars){
		
		Vector<String> vars;
		
		if(the_vars!= null){
			vars = the_vars;
		}else{
			vars = modVarsToStringVect(mod);
		}
		
		for(String v: vars){
			if(getSortbyVarName(v, mod).equals(TermParser.cafe2JavaSort(sort)) 
					&& !existing.contains(" "+v) ){
				return v;
			}
		}//end of looping through the vars
		return null;
	}//end of getVarBySort
	
	
	
	/**
	 *@param name, the name of a Variable
	 *@param mod, the module the variable is declared in
	 *@return a String denoting the sort of the given variable
	 */
	private String getSortbyVarName(String name, Module mod){
		Vector<CafeVariable> vars = mod.getVars();
		
		for(CafeVariable v: vars){
			if(v.getName().equals(name) ){
				return TermParser.cafe2JavaSort(v.getSort());
			}//end if the name of the variable equals the given name
		}//end of looping through the variables of the module
		return "";
	}//end of getSortbyVarName
	
	
	
	/**
	 * 
	 * @param opName the name of the operator
	 * @param mod the module object this op is defined in
	 * @return true if the sort of the operator denotes an Object, 
	 * false if it denotes a primitive Java data-type
	 */
	public boolean isObjectByOpName(String opName, Module mod){
		String sort = mod.getOpSortByName(opName);
		sort = TermParser.cafe2JavaSort(sort);
		//System.out.println("NAME" + opName + "SORT" +sort);
		return !(sort.equals("int") || sort.equals("boolean")|| sort.equals("String"));
	}//end of isObjectByOpName
	
	
	
	/**
	 * 
	 * @param bop a CafeOperator Object
	 * @param mod the module the operator is defined in
	 * @param vars a Vector<String> from which the names of the variables are selected
	 * @return the arguments of the Operator skipping the sort of the module
	 */
	public String opArgsToString(CafeOperator bop, Module mod, Vector<String> vars){
		
		String stringArgs="";
		for(int i=0; i< bop.getArity().size(); i++){
			if(!bop.getArity().get(i).equals(mod.getClassSort())){ 
				stringArgs += 
						TermParser.cafe2JavaSort(bop.getArity().get(i))+ " " + 
							getVarBySort(bop.getArity().get(i), mod, stringArgs,vars) + ", ";
			}//end of if the argument is not the sort of the module
		}//end of looping  through the arguments of the observer
		if(stringArgs.trim().endsWith(",")) stringArgs = StringHelper.remLastChar(stringArgs.trim());
		
		return stringArgs;
	}//end of opArgsToString
	


	/**
	 * @para jmod an object that store the results of the translation
	 * @param mod
	 * @return
	 */
	public String translateSimpleModule(Module mod, JmlModule jmod){
		String res ="";
		jModules.add(jmod);
		
		
		res = "public class "+ mod.getClassSort() +"{" + '\n'
				+ translateHiddenConstants(mod)  + '\n' + 
				translateInitStates(mod) + '\n' + translateObservers(mod) + '\n'
				+ translateGuards(mod) + '\n'
				+ translateTransition(mod, jmod) + '\n' + "}" + '\n'+ '\n';
		return res;
	}//end of translateModule
	
	
	/**
	 * @param mod the module whose variables names we want to retrieve
	 * @return a vector of string containing the name of the variables of the module
	 */
	public Vector<String> modVarsToStringVect(Module mod){
		Vector<String> vars = new Vector<String>();
		for(CafeVariable cv : mod.getVars()){
			vars.add(cv.getName());
		}//end of looping through the variables of the module
		
		return vars;
	}//end of modVarsToStringVect
	
	
	

	/**
	 * returns whether or not the given term is an operator in any module of the spec file
	 * @param opName the name of the operator
	 * @return true if opName denotes an operator in any Module of the spec file
	 */
	public  boolean isOperator(String opName){
		
		//Vector<CafeOperator> allOps = new Vector<CafeOperator>();
		for(Module mod : modules){
			for(CafeOperator op: mod.getOps()){
				if(op.getName().equals(opName)){ return true;}
			}
		}
		
		return false;
	}//end of isOperator
	
	
	
	/**
	 * Returns the sort of a CafeTerm regardless of which module that term is defined in
	 * in the specification file 
	 * @param t a CafeTerm whose sort we are looking for
	 * @return the sort of the CafeTerm
	 */
	public String getTermSort(CafeTerm t){
		for(Module mod : modules){
			if(!mod.getOpSortByName(t.getOpName()).equals("")) return mod.getOpSortByName(t.getOpName()); 
		}
		return "noSortError";
	}//end of getTermSort
	
	
	
}//end of class
