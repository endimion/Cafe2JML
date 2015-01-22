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
	 * @param opName the name of an operator
	 * @return all the CafeEquations of the module which contain on the lhs the given operator
	 */
	public Vector<CafeEquation> getMatchingLeftEqs(String opName){
		
		Vector<CafeEquation> res = new Vector<CafeEquation>();
		
		for(CafeEquation eq: getEqs()){
			if(eq.containsLeftOp(opName)){
				res.add(eq);
			}
		}//end of for loop
		
		return res;
	}//end of getMatchingLeftEqs
	
	
	
}//end of class
