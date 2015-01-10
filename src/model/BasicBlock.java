package model;

/**
 * This interface is meant to be implemented by the basic
 * building blocks of a CafeOBJ expression, i.e. variables and (constant) operators
 * the getName method should return the name of the op or var
 * and the getType its type , i.e. variable or operator
 * @author nikos
 *
 */
public interface BasicBlock {

	
	public String getName();
	public String getType();
	
}//end of interface
