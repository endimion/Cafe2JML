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
		if(!op.getSort().equals(mod.getClassSort())){
			res = res +"public " + op.getSort() +" "+ op.getName() +"(){}";
		}

		return res;
	}//end of genMethodSig
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class
