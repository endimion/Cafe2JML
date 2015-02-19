package model;

import java.util.Vector;


//TODO 
/* 0) projection operators are simply translated to pure methods
 * 
 * 1) Hidden constants are translated to 
 * public static final S objects!!!! of the class that is 
 * the sort of the projection op
 * 
 * 2.1)composite object initial states, projected to composing object constants
 * 2.2) composite object initial states, projected to composing object initial states
 *
 *3)transitions:
 */



public class JmlGenerator {

	Vector<Module> modules;
	
	
	public JmlGenerator(Vector<Module> mod){
		this.modules = mod;
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
			String forallStart){
		
		String res ="";
		boolean isObject;
		
		
		
		res += "@ "+ left.printTermSkipArg(leftPos,mod) ;
		isObject =isObjectByOpName(left.getOpName(), mod);
		if(isObject){
			res += ".equals("; 
					
		}else{
			res += " == ";
		}
		
		if(StringHelper.numOf(right.printTermSkipArg(rightPos,mod), '(') > 0){
			res += "\\old("+right.printTermSkipArg(rightPos,mod) +")" ;
		}else{
			res += right.printTermSkipArg(rightPos,mod)  ;		
		}
		
		if(isObject){res += ")";}
		//res += rightHS;
		
		/* !!!!!!!!!!!!!!!!!!!!!!!!!!! */
		//now since the rightHS of the equation has been translated it can be stored
		CafeTerm leftObs =  left;
		CafeTerm trans = (CafeTerm)left.getArgs().get(leftPos); //we retrieve the sub term that denotes the transition rule
		int sysPos = TermParser.getPositionOfSystemSort(trans, mod); //find the system sort in the transition term
		trans.getArgs().remove(sysPos); //end remove it
		
		leftObs.getArgs().remove(leftPos); //we remove the system sorted argument
		
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
			res += (forallStart.contains("forall"))? ")) &&" +'\n':") &&"+'\n';
		}
		
		return res;
	}//end of translateSimpleEquation
	
	
	
	
	//TODO
	/**
	 * 
	 * @return a JML contract for translating a projection operator equation
	 */
	public String translateProjectEquation(CafeTerm rightHS,  int rightPos, Module mod){
		String res="";
		
		Object transS = rightHS.getArgs().get(rightPos);
		if(transS instanceof String){
			//System.out.println("the transitions chain is a String" );
			//System.out.println("rightHs is " + rightHS.termToString(mod) + "and chain is "+ transS );
			if(!getSortbyVarName((String)transS, mod).equals(mod.getClassSort())){
				System.out.println("problem found");
			}
		}else{
			//it is a CafeTerm
			System.out.println("the transitions chain is a CafeTerm");
			System.out.println("rightHs is " + rightHS.termToString(mod,this) + "and chain is "+ ((CafeTerm)transS).termToString(mod,this) );
			
			Vector<CafeTerm> chain = new Vector<CafeTerm>();
			buildChainFromTerm(chain,(CafeTerm)transS, mod);
			int i= 0;
			for(CafeTerm t : chain){
				System.out.println(i + " "+t.termToString(mod,this));
				i++;
			}
			//TODO add in this class a method to check if a term denotes an 
			//operator in ANY of the spec file modules
			
			
			
		}//end if the transitions of the rightHs system sorted term 
		
		return res;
	}//end of translateProjectEquation
	
	
	
	/**
	 * Generates a chain of cafeTerms from the given term 
	 * @param chain a Vector<CafeTerm>
	 * @param term a CafeTerm
	 * @return a Vector<CafeTerm> which contains all the arguments of the given term  including the term itself
	 */
	public Vector<CafeTerm> buildChainFromTerm(Vector<CafeTerm> chain, CafeTerm term, Module mod){
		chain.add(term);
		
		for(Object arg: term.getArgs() ){
			if(arg instanceof CafeTerm){ 
				if( mod.getTermSort((CafeTerm)arg).equals(mod.getClassSort()))
				buildChainFromTerm(chain, (CafeTerm) arg,mod);
			}
		}//end for loop
		return chain;
	}//end of buildChainFromTerm
	
	
	
	
	
	
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
				res +=  forallStart;
				
				CafeTerm left = transEq.get(i).getLeftTerm();
				CafeTerm right = transEq.get(i).getRightTerm();
				CafeTerm cond = transEq.get(i).getCondition();
				
				int leftPos = TermParser.getPositionOfSystemSort(left,mod);
				int rightPos = TermParser.getPositionOfSystemSort(left,mod);
				
				if(cond!= null){
					int condPos = TermParser.getPositionOfSystemSort(cond,mod);
					res += "@ (" + cond.printTermSkipArg(condPos,mod).replace("c-","c") + " ==> "+ '\n';
				}
				
				if(!mod.isProjection(modules, left.getOpName())){
					res += translateSimpleEquation(mod, left, right, leftPos, rightPos,  valOfObser, rightPos, jmod, transEq, forallStart); 
				}else{
					//TODO
					//translate the projection equation
					translateProjectEquation(right, rightPos,mod);
					
					
					
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
	public String forallDecl(Module mod, String initString, 
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
				int rightSortPos = TermParser.getPositionOfSystemSort(eq.getRightTerm(), mod);
				startOfEqTrans = forallDecl(mod, "/*@ensures",eq,guard);
				res += startOfEqTrans +'\n';
				if(!isObjectByOpName(eq.getLeftTerm().getOpName(), mod)){
					res += "@ \\result == " + eq.getRightTerm().printTermSkipArg(rightSortPos, mod);
				}else{
					res += "@ \\result.equals(" + eq.getRightTerm().printTermSkipArg(rightSortPos, mod) ;
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
	 * 
	 * @param opName
	 * @return
	 */
	public  boolean isOperator(String opName){
		
		Vector<CafeOperator> allOps = new Vector<CafeOperator>();
		for(Module mod : modules){
			for(CafeOperator op: mod.getOps()){
				allOps.add(op);
			}
		}
		
		for(CafeOperator op: allOps){
			if(op.getName().equals(opName)){
				return true;
			}
		}
		return false;
	}//end of isOperator
	
	
	
}//end of class
