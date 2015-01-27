package model;

import java.util.Vector;

public class JmlGenerator {

	Vector<Module> modules;
	
	
	public JmlGenerator(Vector<Module> mod){
		this.modules = mod;
	}//end of constructor
	
	
	//TODO
	//if an operator has no arity and returns a sort different then the
	// sor of the module then it denotes a new object not a method!!!!
	
	/**
	 * Generates the signature of the Java method from the CafeOBJ operator
	 * @param op, a CafeOperator
	 * @param mod, the module this Operator belongs to
	 * @return a string representing the signature of the Java method from the CafeOBJ operator
	 */
	public String genMethodSig(CafeOperator op, Module mod){
		String res="";
		
		if(!op.getSort().equals(mod.getClassSort())){ //observer found
			if(op.getArity().size() == 0){
				//res = res +"public /*@ pure @*/" + op.getSort() +" "+ op.getName() +"(){}";
				res = op.getSort()+" "+ op.getName() + " = new " + op.getSort()+"();";

			}else{
				String args ="";
				for(String varSort : op.getArity()){
					if(!varSort.equals(mod.getClassSort()))
						args += varSort + " " +getVarBySort(varSort, mod,args) +", ";
				}
				if(args.length()>=1) args = StringHelper.remLastChar(args.trim());
				res = res +"public /*@ pure @*/ " + op.getSort() +" "+ op.getName() +"(" +args+"){}";
				
			}//end if the arity of the operator is greater than zero
		
		}else{ //end if the sort is not that of the module
			
			if(op.getArity().size() > 0){// action operator found
				
				CafeEquation eq = mod.getMatchingLeftEqs(op.getName()).get(0);
				
				String args ="";
				for(String n: eq.getLeftArguments(op.getName()) ){
					String varSort = getSortOfVar(n,mod);
					if(!varSort.equals(mod.getClassSort()))
						args += getSortOfVar(n,mod)+" "+ n +", ";
				}
				if(args.length()>=1) args = StringHelper.remLastChar(args.trim());
				
				res = res +"public void "+ op.getName() +"("+ args+"){}";
				
			}else{//constructor found
				res = res +"public " + op.getName() +"(){}";
			}//end of constructor found

		}//end if the sort of the operator is the same as that of the module

		return res;
		
	}//end of genMethodSig
	
	
	/**
	 * @param  mod a Module Object
	 * @return  a string consisting of the translation of the initial states
	 * for the given module
	 */
	public String translateInitStates(Module mod){
		String res = "";
		Vector<CafeOperator> initStates = mod.getInitialStates();
		
		String forallDecl="";
		
		forallDecl = forallDecl(mod, "initially");
		
		for(CafeOperator init : initStates){
			//1)get the equations matching that have on the left hand side the intial state operator
			//2) get the variables for each such equation
			//2') cheat: just add all the variables which might not appear on the term!!!!
			//3) add to the output an expression: (\forall D1 x1... Dn xn; mainOperatorOfEquation(x1,..xn)
			// == with the translation of the right part of the equation)
			
			Vector<CafeEquation> matchingEqs = mod.getMatchingLeftEqs(init.getName());
		
			res += forallDecl;
			
			for(CafeEquation eq :matchingEqs){
				CafeTerm left = eq.getLeftTerm();
				CafeTerm right = eq.getRightTerm();
				
				res += "  @ "+ left.printTermSkipArg(0) + " == " + right.termToString() + '\n';
				
			}//end of looping through the matching equations
			res +=  ") @*/"+ '\n' +"public "+init.getName() + "(){}"  + '\n' + '\n';
		
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
			observerArguments = "(" + opArgsToString(bop, mod);
			res += start + bop.getSort()+ " "+ bop.getName() + observerArguments +"){}" +'\n';
			
		}//end of looping through the observers
		
		
		return res;
	}//end of translateObservers
	
	
	
	
	/**
	 * //TODO
	 * @param mod
	 * @return a String containing the translation of the transition
	 * operations of the given module
	 */
	public String translateTransition(Module mod){
		Vector<CafeOperator> actions = mod.getActions(); 
		Vector<CafeEquation> transEq; // = mod.getMatchingLeftEqs("");
		
		String res ="";
		boolean isObject;
		
		for(CafeOperator bop : actions){
			transEq = mod.getMatchingLeftEqs(bop.getName());
			
			res ="/*@ " +  forallDecl(mod,"ensures") ;
			for(int i =0; i< transEq.size();i++){
				CafeTerm left = transEq.get(i).getLeftTerm();
				CafeTerm right = transEq.get(i).getRightTerm();
				CafeTerm cond = transEq.get(i).getCondition();
				
				int leftPos = mod.getPositionOfSystemSort(left);
				int rightPos = mod.getPositionOfSystemSort(left);
				
				//res += forallDecl(mod," @ ensures") ;
				//System.out.println("left part is " + left.getOpName() + " and it has " + left.getArgs().size() +" stuff");		
				
				if(cond!= null){
					int condPos = mod.getPositionOfSystemSort(cond);
					res += "@ (" + cond.printTermSkipArg(condPos)+ " ==> "+ '\n';
				}
				
				res += "@ "+ left.printTermSkipArg(leftPos) ;
				isObject =isObjectByOpName(left.getOpName(), mod);
				if(isObject){
					res += ".equals("; 
							
				}else{
					res += " == ";
				}
				
				if(StringHelper.numOf(right.printTermSkipArg(rightPos), '(') > 0){
						res += "\\old("+right.printTermSkipArg(rightPos) +")" ;
				}else{
					res += right.printTermSkipArg(rightPos)  ;		
				}
				
				if(isObject){res += ")";}
				if(i != transEq.size()-1){
					res += ") &&" +'\n';
				}else{
					res += ");" +'\n';
				}
				//right.termToString() + '\n';
				
				//System.out.println( left.printTermSkipArg(pos) + "==" + right.termToString() );
			}//end of looping through the transition equations
			res +="@*/" + '\n';
			res += "public void " + bop.getName() +"("+ opArgsToString(bop, mod) +"){}" +'\n';
		
		}//end of looping through the transitions 
		
		return res;
	}//end of translateTransition
	
	
	
	
	/**
	 * 
	 * @param mod
	 * @return a String containing the forall variables declared in the module
	 * declaration
	 */
	public String forallDecl(Module mod, String initString){
		String forallDecl="";
		forallDecl += (initString.equals("initially"))? "/*@" :"";
		forallDecl += initString +" (\\forall ";
		for(CafeVariable var: mod.getVars()){
			if(!var.getSort().equals(mod.getClassSort()))
				forallDecl = forallDecl + " "+ var.getSort() + " " +var.getName() + ", ";
		}
		if(forallDecl.endsWith(", "))forallDecl = StringHelper.remLastChar(forallDecl.trim());
		forallDecl += ";" + '\n';
		
		return forallDecl;
	}//forallDecl
	
	
	
	
	
	
	
	/**
	 * takes as input a Vector of CafeVariables and a string denoting a sort
	 * and returns the first variable matching that sort
	 * @return
	 */
	private String getSortOfVar(String varName, Module mod){
		Vector<CafeVariable> vars = mod.getVars();
		
		for(CafeVariable v: vars){
			if(v.getName().equals(varName)){
				return v.getSort();
			}
		}
		
		return null;
	}//end of getSortOfVar
	
	
	
	/**
	 * @param a string which contains the variables already used in the signature of a term
	 * @param sort a string denoting a sort
	 * @return a variable from that sort such that it does not appear in the existing variables 
	 * of the signature of the term
	 */
	private String getVarBySort(String sort, Module mod, String existing){
		Vector<CafeVariable> vars = mod.getVars();
		
		for(CafeVariable v: vars){
			if(v.getSort().equals(sort) && !existing.contains(" "+v.getName()) ){
				return v.getName();
			}
		}
		return null;
	}//end of getVarBySort
	
	
	/**
	 * 
	 * @param opName the name of the operator
	 * @param mod the module object this op is defined in
	 * @return true if the sort of the operator denotes an Object, 
	 * false if it denotes a primitive Java data-type
	 */
	public boolean isObjectByOpName(String opName, Module mod){
		String sort = mod.getOpSortByName(opName);
		return !(sort.equals("Int") || sort.equals("Bool")|| sort.equals("String"));
	}//end of isObjectByOpName
	
	
	
	/**
	 * 
	 * @param bop a CafeOperator Object
	 * @param mod the module the operator is defined in
	 * @return the arguments of the Operator skipping the sort of the module
	 */
	public String opArgsToString(CafeOperator bop, Module mod){
		
		String stringArgs="";
		for(int i=0; i< bop.getArity().size(); i++){
			if(!bop.getArity().get(i).equals(mod.getClassSort())){ 
				stringArgs += bop.getArity().get(i) + " " + 
							getVarBySort(bop.getArity().get(i), mod, stringArgs) + ", ";
			}//end of if the argument is not the sort of the module
		}//end of looping  through the arguments of the observer
		if(stringArgs.trim().endsWith(",")) stringArgs = StringHelper.remLastChar(stringArgs.trim());
		
		return stringArgs;
	}//end of opArgsToString
	


	
	
	
	
}//end of class
