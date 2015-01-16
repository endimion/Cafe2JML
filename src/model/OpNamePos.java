package model;

/**
 * trivial class used store the name of an operator and its position in a given String
 * @author nikos
 *
 */

public class OpNamePos {

	String name;
	int pos;
	
	public OpNamePos(String name, int pos){
		this.name = name;
		this.pos  =pos;
	}
	
	public String getName(){return name;}
	public int getPos(){return pos;}
	
	
}//end of class
