package model;

import java.util.Vector;


/**
 * This class defines a sort of beans objects which contain all the 
 * parsed information from a module of a CafeOBJ file
 * and will be used to retrieve the information in order to create the JML contract for that class
 * @author nikos
 *
 */
public class Module {


	public String name;
	String classSort ;
	Vector<String> opNames = new Vector<String>();
	Vector<String> importNames = new Vector<String>();
	Vector<String> extendsNames = new Vector<String>();
	Vector<CafeOperator> ops = new Vector<CafeOperator>();
	Vector<CafeVariable> vars = new Vector<CafeVariable>();
	Vector<CafeEquation> eqs = new Vector<CafeEquation>();
	
	Vector<CafeOperator> constr = new Vector<CafeOperator>(); //the constructors of the module
	
	private int numOfOps ;
	private int numOfEq ;
	
	boolean isHidden;
	
	public  Module(){
		numOfOps = 0;
		numOfEq = 0;
	}


	public void setName(String name){this.name = name;}
	
	/**
	 * adds the given name to the "set" represented as a vector, which denotes the 
	 * modules imported using protecting or extending by the current module (Which is represented 
	 * by the class)
	 * @param name, the name as a String of a module imported by this one
	 */
	public void addImportName(String name){importNames.addElement(name);}
	public Vector<String> getImportNames(){return importNames;}


	/**
	 * adds the given name to the "set" represented as a vector, which denotes the 
	 * modules imported using protecting or extending by the current module (Which is represented 
	 * by the class)
	 * @param name, the name as a String of a module imported by this one
	 */
	public void addExtendsName(String name){extendsNames.addElement(name);}
	public Vector<String> getExtendsName(){return extendsNames;}


	/**
	 * 
	 * @param sort, the sort of the module
	 */
	public void setClassSort(String sort){
		this.classSort = sort;
	}
	public String getClassSort(){return this.classSort;}



	public void addOp(CafeOperator c){
		ops.addElement(c);
		numOfOps++;
	}
	public Vector<CafeOperator> getOps(){return ops;}

	/**
	 * 
	 * @param opName the name of an operator
	 * @return true if this operator is defined in this module
	 */
	public boolean hasOperator(String opName){
		for(CafeOperator op : getOps()){
			if(op.getName().equals(opName))
				return true;
		}
		return false;
	}
	

	public void addVar(CafeVariable v){
		vars.addElement(v);
	}
	public Vector<CafeVariable> getVars(){return vars;}
	
	
	
	
	public int getNumOfOps(){return numOfOps;}
	public int getNumOfEqs(){return numOfEq;}
	
	public String getName(){return this.name;}
	
	public boolean isHidden(){return this.isHidden;}
	public void setHidden(boolean hid){this.isHidden = hid;}
	
	
	
	
	
	/**
	 * 
	 * @return a vector containing the "constructors" of the modules, i.e.
	 * all the operators whose co-arity is the same sort as that of the module
	 */
	public Vector<CafeOperator> getConstructors(){
		
		Vector<CafeOperator> res = new Vector<CafeOperator>();
		
		for(CafeOperator op: getOps()){
			if(op.getSort().equals(getClassSort())){
				res.add(op);
			}
		}
		return res;
	}//end of getConstructors
	
	/**
	 * adds a new CafeEquation to the vector of the modules equations
	 * @param eq
	 */
	public void addEq(CafeEquation eq){
		eqs.add(eq);
	}
	/**
	 * 
	 * @return a vector containing the equations of the module
	 */
	public Vector<CafeEquation> getEqs(){return this.eqs;}
	
	
	/**
	 * returns the constructor operators,
	 * i.e. the operators which have as sort the same as that of the module
	 * 
	 */
	public Vector<CafeOperator> getConstr(){
		Vector<CafeOperator> opr = new Vector<CafeOperator>();
		
		for(CafeOperator op: getOps()){
			if(op.getSort().equals(getClassSort())){
				opr.add(op);
			}
		}//end of for loop
		
		return opr;
	}//end of getConstr
	
	
	
	/**
	 *  
	 * @return a Vector of constructor CafeOperators which have no arity,
	 * e.g. init : -> Sys
	 */
	public Vector<CafeOperator> getConstants(){
		
		Vector<CafeOperator> consts = new Vector<CafeOperator>();
		
		for(CafeOperator op: getOps()){
			if(op.getArity().size() == 0){
				consts.add(op);
				//System.out.println("Added " + op.getName());
			}
		}//end of for loop
		return consts;
	}//end of getHiddenConstants
	
	
	
	
	
	
	/**
	 * 
	 * @return all the hidden constants that have the same sort as the module,
	 * i.e. all the initial states of the system which is defined by the module
	 */
	public Vector<CafeOperator> getInitialStates(){
		Vector<CafeOperator> constants = getConstants();
		Vector<CafeOperator> init = new Vector<CafeOperator>();
		
		for(CafeOperator op: constants){
			if(op.getSort().equals(getClassSort()))init.add(op);
		}//end of for loop
		return init;
	}//end of getInitialStates
	
	
	
	/**
	 * 
	 * @return the action operators of the specification
	 */
	public Vector<CafeOperator> getActions(){
		Vector<CafeOperator> act = new Vector<CafeOperator>();
		
		for(CafeOperator op:getConstr()){
			if( op.getArity().size() >0){
				act.add(op);
			}
		}//end for loop
		
		return act;
	}//end of getObservations()
	
	
	/**
	 * 
	 * @return a vector of CafeOperators which contains all the observations of
	 * the module
	 */
	public Vector<CafeOperator> getObservers(){
		Vector<CafeOperator> obs = new Vector<CafeOperator>();
		
		for(CafeOperator op : getOps()){
			//if teh op contains in the arity the same sort as that of the module
			if(isBehavioral(op) && !op.getSort().equals(classSort) && !op.getName().trim().startsWith("c-")){
				obs.add(op);
			}//end if the op is behavioral but not sorted as the module
			
		}//end of looping through the operators of the module
		return obs;
	}//end of getObservers
	
	
	
	
	/**
	 * 
	 * @param opName the name of an operator
	 * @return all the CafeEquations of the module which contain on the lhs the given operator
	 * in such a position that it gives the state of the object//systems
	 */
	public Vector<CafeEquation> getMatchingLeftEqs(String opName){
		
		Vector<CafeEquation> res = new Vector<CafeEquation>();
		
		int posOfSysState = -1; 
		
		for(CafeEquation eq: getEqs()){
			posOfSysState = TermParser.getPositionOfSystemSort(eq.getLeftTerm(),this);

			if(posOfSysState >= 0){
				Object critical = eq.getLeftTerm().getArgs().get(posOfSysState);
				if(critical instanceof String ){
					if(((String) critical).contains(opName))res.add(eq);
				}else{
					if(critical instanceof CafeTerm )
					{	if(((CafeTerm) critical).getOpName().equals(opName)){
							res.add(eq);
						}
					}
				}//end if the critical term is not a string
			}//end if equation has on its left hand side a system sorted term
		}//end of for loop
		
		return res;
	}//end of getMatchingLeftEqs
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * @param opName the name of an operator
	 * @return all equations of the module which give the value of the given
	 * operator
	 */
	public Vector<CafeEquation> getEqsForOp(String opName){
		Vector<CafeEquation> res = new Vector<CafeEquation>();
		
		for(CafeEquation eq: getEqs()){
			if(eq.getLeftTerm().getOpName().equals(opName)){
				res.add(eq);
			}
		}//end of for loop
		return res;
	}//end of getMatchingLeftEqs
	
	
	
	/**
	 * 
	 * @return all the effective condition operators of the module
	 * which by convention start with the prefix "c-"
	 */
	public Vector<CafeOperator> getEffectiveConditions(){
		Vector<CafeOperator> effectives = new Vector<CafeOperator>();
		
		for(CafeOperator op: getOps()){
			if(op.getName().trim().startsWith("c-")) effectives.add(op);
		}
		return effectives;
	}//end of getEffectiveConditions
	
	
	/**
	 * 
	 * @param op
	 * @return whether or not the operator is behavioral i.e. if it contains in its
	 * arity the sort of the module
	 */
	public boolean isBehavioral(CafeOperator op){
		Vector<String> args = op.getArity();
		for(String arg:args){
			if(arg.equals(getClassSort()))return true;
		}//end of for loop
		
		return false;
	}//end of isBehavioral
	
	
	/**
	 * 
	 * @param t a CafeTerm
	 * @return the sort for that term in this module
	 */
	public String getTermSort(CafeTerm t){
		Vector<CafeOperator> ops = getOps();
		String termMainOp = t.getOpName();
		
		for(CafeOperator op: ops){
			//System.out.println("checking..." + op.getName());
			if(termMainOp.equals(op.getName())) { return op.getSort();}
		}
		
		return "error";
	}//end of getTermSort
	
	
	
	
	
	/**
	 * takes as input the name of a CafeOperator Or Variable and returns its sort in
	 * this module
	 * if the operator is not found defined in this module "" is returned
	 * @param name the name of an operator or variable of the module
	 * @return
	 */
	public String getOpSortByName(String name){
		for(CafeOperator op: getOps()){
			if(op.getName().equals(name)) return op.getSort();
		}
		
		Vector<CafeVariable> vars = getVars();
		for(CafeVariable var : vars){
			if(var.getName().equals(name)) return var.getSort();
		}

		return "";
	}//end of getOpSortByName
	
	
	
	/**
	 * 
	 * @param eq a CafeEquation
	 * @return a vector of strings denoting all the variables which appear in 
	 * this equation but are not System variables
	 */
	public Vector<String> getVariableOfEq(CafeEquation eq){
		CafeTerm left = eq.getLeftTerm();
		Vector<String> vars  = new Vector<String>();
		
		for(String s: left.getVarsOfTerm()){
			//System.out.println("variable " + s + " of sort " + getOpSortByName(s));
			
			if(!getOpSortByName(s).equals(getClassSort())){
				vars.add(s);
			}
		}
		//System.out.println("!!!!!!!!sizee " + vars.size());
		return vars;
	}//end of getVariableOfEq
	
	
	
	
	/**
	 * 
	 * @param op  a CafeOperator
	 * @param eq  a CafeEquation
	 * @return a Vector of Strings denoting all the variable which 
	 * appear inside the operator op in eq
	 */
	public Vector<String> getVarsOfOpinEq(CafeOperator op, CafeEquation eq){
		
		CafeTerm t = eq.getTermInEqLeft(op.getName());
		return  t.getVarsOfTerm();
	}//end of getVarsOfOpinEq
	
	

	/**
	 * 
	 * @param opName
	 * @return
	 */
	public  boolean isOperator(String opName){
		
		for(CafeOperator op: getOps()){
			//System.out.println("Comparing " + opName +" with" + op.getName());
			if(op.getName().trim().equals(opName.trim())){
				//System.out.println("match found for "+ opName);
				return true;
			}
		}
		return false;
	}//end of isOperator
	
	
	
	
	
	/**
	 * This method returns all the projection operators of the module, i.e. all hidden sorted 
	 * 	operators whose hidden sort is that of another module of the specification
	 * @param specMods a vector<Module> which denotes the rest of the modules of the 
	 * specification
	 * @return a Vector<CafeOperator> which are projection operators
	 */
	public Vector<CafeOperator> getProjections(Vector<Module> specMods){
		
		Vector<CafeOperator> proj = new Vector<CafeOperator>();
		
		for(CafeOperator op: getOps()){
			for(Module m : specMods){
				if(op.getSort().equals(m.getClassSort()) && !op.getSort().equals(getClassSort()) ){
					proj.add(op);
					break;
				}//end if the op  has the same sort as anothe module
			}//end of looping through the other modules
		}//end of looping through the operators of the module
		
		return proj;
		
	}//end of getProjections
	
	
	/**
	 * 
	 * @param specMods a Vector<Module> denoting the other modules of the spec file
	 * @param opName the name of an operator
	 * @return true if the given operator is a projection operator for the given module
	 * under the given set of other modules
	 */
	public boolean isProjection(Vector<Module> specMods, String opName){
		for(CafeOperator op : getProjections(specMods)){
			if(op.getName().equals(opName)) return true;
		}//end of looping through the projection ops
		
		return false;
	}//end of isProjection
	
	
	/**
	 * 
	 * @param opName the name of an operator	
	 * @return true if the given operator declares a variable in the give module
	 */
	public boolean isVariable(String opName){
		for(CafeVariable var : getVars()){
			if(var.getName().equals(opName))
				return true;
		}//end of for loop
		return false;
	}//end of isVariable
	

	/**
	 * @param opName the name of an operator which is included in the given module
	 * @return a Vector<String> containing the names of the variables which are 
	 * declared in this module and whose sort match those of the given operator
	 */
	public Vector<String> getVarsForOps(String opName){
		Vector<String> result = new Vector<String>();
		
		Vector<CafeVariable> vars = new Vector<CafeVariable>();
		for(CafeVariable v: getVars()){
			vars.add(v);
		}//end of looping through the variables of the module
		
		
		CafeOperator op = null;
		for(CafeOperator o : getOps()){
			if(o.getName().equals(opName))
				op = o;
		}//end of looping throught the operators
		
		
		for(String sort : op.getArity()){
			for(CafeVariable v: vars){
				if(v.getSort().equals(sort) && !sort.equals(getClassSort())){
					result.add(v.getName());
					//vars.remove(v);
					break;
				}
			}//end of loopingthrough the variables of the module
		}//end of looping through the arity of the given operator
		
		return result;
	}//end of getVarsForOps
	
	

	/**
	 * 
	 * @param opName the name of an operator
	 * @return true the given op denotes an initial state in this module
	 */
	public boolean isInitial(String opName){
		
		Vector<CafeOperator> initStates = getInitialStates();
		for(CafeOperator op : initStates){
			if(op.getName().equals(opName)) return true;
		}
			return false;
	}//end of is initial
	
	
	/**
	 * 
	 * @param opName
	 * @return true if the given opName is the name of a Constant
	 * defined in this module
	 */
	public boolean isConstant(String opName){
		for(CafeOperator op: getConstants()){
			if(op.getName().equals(opName)){
				return true;
			}
		}//end of looping through the constants
		return false;
	}//end isConstants
	
	
	
}//end of class
