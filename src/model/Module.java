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
	public Vector<CafeOperator> getHiddenConstants(){
		
		Vector<CafeOperator> consts = new Vector<CafeOperator>();
		
		for(CafeOperator op: getConstr()){
			if(op.getArity().size() == 0){
				consts.add(op);
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
		Vector<CafeOperator> constants = getHiddenConstants();
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
			if(isBehavioral(op) && !op.getSort().equals(classSort)){
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
			posOfSysState = getPositionOfSystemSort(eq.getLeftTerm());
			
			if(posOfSysState >= 0){
				Object critical = eq.getLeftTerm().getArgs().get(posOfSysState);
				
				if(critical instanceof String && critical.equals(opName)){
					res.add(eq);
				}else{
					//System.out.println("CRITICAL " + ((CafeTerm)critical).getOpName() + " "+opName);
					if(critical instanceof CafeTerm && ((CafeTerm) critical).getOpName().equals(opName))
					{	
						res.add(eq);
					}
				}//end if the critical term is not a string
			}//end if equation has on its left hand side a system sorted term
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
			if(op.getName().startsWith("c-")) effectives.add(op);
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
			if(termMainOp.equals(op.getName())) return op.getSort();
		}
		
		return null;
	}//end of getTermSort
	
	
	/**
	 * 
	 * @param t
	 * @return the position of the system sort, i.e. the sort which is 
	 * defined by the module in the term. If no such sort exists -1 is returned
	 */
	public int getPositionOfSystemSort(CafeTerm t){
		
		@SuppressWarnings("unchecked")
		Vector<Object> args = (Vector<Object>) t.getArgs();
		
		for(int i = 0; i < args.size(); i++){
			Object arg = args.get(i);
			
			if(arg instanceof String &&  arg != null 
					&& getOpSortByName((String)arg).equals(getClassSort())){
				return i;
			}else{
				if(arg instanceof CafeTerm &&  
						getOpSortByName(((CafeTerm) arg).getOpName()).equals(getClassSort()))
				{return i;}
			}//end if the argument is not a string
		}//end of looping through the arguments of the term r
		return -1;
	}//end of getPositionOfSystemSort
	
	
	/**
	 * takes as input the name of a CafeOperator and returns its sort in
	 * this module
	 * @return
	 */
	public String getOpSortByName(String name){
		for(CafeOperator op: getOps()){
			if(op.getName().equals(name)) return op.getSort();
		}
		return "";
	}//end of getOpSortByName
	
	
	//TODO getVariablesOfEquations which
	//gets a set of CafeEquations and returns all the variables which appears in them
	
	
}//end of class
