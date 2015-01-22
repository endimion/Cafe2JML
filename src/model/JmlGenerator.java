package model;

import java.util.Vector;

public class JmlGenerator {

	Vector<Module> modules;
	
	
	public JmlGenerator(Vector<Module> mod){
		this.modules = mod;
	}//end of constructor
	
	
	
	
	
	/**
	 * Generates the signature of the Java method from the CafeOBJ operator
	 * @param op, a CafeOperator
	 * @param mod, the module this Operator belongs to
	 * @return a string representing the signature of the Java method from the CafeOBJ operator
	 */
	public String genMethodSig(CafeOperator op, Module mod){
		String res="";
		
		if(!op.getSort().equals(mod.getClassSort())){ //observer found
			res = res +"public /*@ pure @*/" + op.getSort() +" "+ op.getName() +"(){}";
			//TODO the variables for the declacration of the pure methods
		
		
		}else{ //end if the sort is not that of the module
			
			if(op.getArity().size() > 0){// action operator found
				
				CafeEquation eq = mod.getMatchingLeftEqs(op.getName()).get(0);
				
				String args ="";
				for(String n: eq.getLeftArguments(op.getName()) ){
					args += getSortOfVar(n,mod)+" "+ n +", ";
				}
				args = StringHelper.remLastChar(args.trim());
				res = res +"public void "+ op.getName() +"("+ args+"){}";
				
			}else{//constructor found
				res = res +"public " + op.getName() +"(){}";
			}//end of constructor found

		}//end if the sort of the operator is the same as that of the module

		return res;
		
	}//end of genMethodSig
	
	
	
	
	
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
	
	
	
	
	
	
	
	
}//end of class
