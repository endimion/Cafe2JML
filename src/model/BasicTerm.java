package model;

import java.util.Vector;

public class BasicTerm implements CafeTerm{
	
	String opName;
	//Vector<BasicBlock> args = new Vector<BasicBlock>();
	//Maybe it is better instead of a BasicBlock to have a vector of strings
	Vector<String> args  = new Vector<String>();
	
	public BasicTerm(){}
	
	public void setOpName(String name){this.opName = name;}
	
	@Override
	public String getOpName(){return this.opName;}
	
	@Override
	public Vector<String> getArgs(){return args;}
	
	@Override
	public void addArg(Object arg) { args.addElement((String)arg);}
	
	
	
	/**
	 * 
	 * @return a string representation of the term
	 */
	@Override
	public String termToString(){
		String print = getOpName();
		String extra="";
		
		for(String s: getArgs()){
			extra += " "+s +",";
		}//end of looping through the term argumnets
		
		if(extra.length() > 0){
			extra = StringHelper.remLastChar(extra); //we remove the last comma
			print  += "(" + extra +")";
		}

		return print;
	}//end of termToString
	
	
	
	/**
	 * 
	 * @param pos
	 * @return a string representation of the term skipping the 
	 * argument in the given position
	 */
	public String printTermSkipArg(int pos){
		String res ="";
		if(!TermParser.isBinary(getOpName())){
			res = (getArgs().size() ==0)?getOpName() :getOpName() +"(";
			//System.out.println("aaaa " + getOpName());
			if(getArgs().size()>0){
				//res += "(";
				for(int i= 0; i < getArgs().size();i++){
					if(i != pos) res += getArgs().get(i) + ", ";
				}
				//System.out.println("RES " + res);
				if(!res.endsWith("("))res = StringHelper.remLastChar(res.trim());
				res +=")";
			}
			//System.out.println("RES2 " + res);
		}else{
			res +="BASIC BINARRRUUUU";
		}
		return res;
	}//end of printTermSkippingArg
	
	
	
	/**
	 * 
	 * @param pos
	 * @return a string representation of the term skipping the 
	 * argument in the given position
	 */
	public String printBinaryOpTermSkipArg(int pos){
		
		if(getArgs().size() == 2){
			String res = " (" + getArgs().get(0) + " "+ getOpName() + " "+ getArgs().get(1) + " )";
			return res;
		}else{
			return "This is not a binary operator!!! something went wrong!!!! "+ getOpName();
		}
	}//end of printTermSkippingArg

	
	
	
	
	
	
	
	/**
	 * 
	 * @param op the name of an operator
	 * @return true if the given name appears in this term
	 */
	public boolean containsOp(String op){
		if(op.equals(opName)){
			return true;
		}else{
			for(String arg:getArgs()){
				if(arg.equals(op))return true;
			}
		}
		return false;
	}//end of containsOp
	
	
	
	
	
	
}//end of BasicOpExpr
