package model;

import java.util.Vector;


public class BasicTerm implements CafeTerm{
	
	String opName;
	boolean isVarConst;
	//Vector<BasicBlock> args = new Vector<BasicBlock>();
	//Maybe it is better instead of a BasicBlock to have a vector of strings
	Vector<String> args  = new Vector<String>();
	
	public BasicTerm(boolean isVar){
		isVarConst = isVar;
	}
	
	public boolean isVar(){return isVarConst;}
	public void setIsVar(boolean t){isVarConst = t;}
	
	public void setOpName(String name){this.opName = name;}
	
	@Override
	public String getOpName(){return this.opName;}
	
	@Override
	public Vector<String> getArgs(){return args;}
	
	@Override
	public void addArg(Object arg) { args.addElement((String)arg);}
	
	
	@Override
	public CafeTerm getSubTerm(String op){
		if(op.equals(getOpName())){
			return this;
		}else{
			for(String s: getArgs()){
				if(s.equals(op)){
					//if the argument contains a parenthesis then it is not a constant or variable
					BasicTerm t = (s.contains("("))?  new BasicTerm(false):new BasicTerm(true);
					t.setOpName(op);
					return t;
				}//end of if the name of the op is the same as that of the argument 
			}//end of loopinig through the arguments 
		}//end if the name of the op is not the same as that of the term
		return null;
	}// end of getSubTerm
	
	
	/**
	 * !!!!! ATTENTION, if the term is a constant, the name of the term will
	 * be returned here!! additional check 
	 * must hence be implemented in the caller method
	 * 
	 * @return a Vector containing the names of all the variables
	 * which appear in this term
	 * if the term is it shelf a variable return that term
	 * 
	 */
	@Override
	public Vector<String> getVarsOfTerm(){
		if(getArgs().size() > 0){
			return getArgs();
		}else{
			Vector<String> res = new Vector<String>();
			res.add(getOpName());
			return res;
		}
	}//end of getVarsOfTerm
	
	
	
	/**
	 * 
	 * @return a string representation of the term
	 */
	@Override
	public String termToString(){
		String print="";
		
		if(!TermParser.isBinary(getOpName())){
			print = getOpName();
			String extra="";
			
			for(String s: getArgs()){
				extra += " "+s +",";
			}//end of looping through the term argumnets
			
			if(extra.endsWith(","))	extra = StringHelper.remLastChar(extra); //we remove the last comma
			if(!isVar()) print  += "(" + extra +")";
			//print  += "(" + extra +")";
			
		}else{
			String leftTerm =  (String)getArgs().get(0);  
			String rightTerm =  getArgs().get(1);
			print = " (" + leftTerm + " "+ getOpName() + " " + rightTerm + ")";
		}
		
		return print;
	}//end of termToString
	
	
	
	/**
	 * 
	 * @param pos
	 * @return a string representation of the term skipping the 
	 * argument in the given position
	 */
	public String printTermSkipArg(int pos, Module mod){
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
	public String printBinaryOpTermSkipArg(int pos, Module mod){
		
		if(getArgs().size() == 2){
			String res = " (" + getArgs().get(0) + " "+ 
					TermParser.cafe2JavaSort(getOpName()) + " "+ getArgs().get(1) + " )";
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

	
	
	
	@Override
	public CafeTerm replaceArg(Object newArg, int pos) {
	
		if(newArg instanceof String){
			BasicTerm nt = new BasicTerm(isVarConst);
			nt.setOpName(this.getOpName());
			
			//Vector<String> args = new Vector<String>();
			for(int i = 0; i < getArgs().size(); i++){
				if(i != pos){
					nt.getArgs().add(getArgs().get(i));
				}else{
					nt.getArgs().add((String)newArg);
				}
				
			}//end of looping through the original arguments
			
			return nt;
		}else{
			if(newArg instanceof CafeTerm){
				CompTerm nt = new CompTerm();
			  //System.out.println("SSSS " + getOpName());	
				nt.setOpName(getOpName());
				for(int i = 0; i < getArgs().size(); i++){
					if(i != pos){
						nt.getArgs().add(getArgs().get(i));
						//System.out.println("KKKK " + getArgs().get(i));
					}else{
						nt.getArgs().add(newArg);
						//System.out.println("DDDaaa " + ((CafeTerm) newArg).termToString());
					}
					
				}//end of looping through the original arguments

				return nt;
			}//end if the new argument is not a variable or constant
		}
		return null;
	}//end of replaceArg
	
	
	
	public CafeTerm replaceAllMatching(Vector<ObsValPair> newValues){
		return this;
	}//end of replaceAllMatching
	
	
	/**
	 * returns true whether or not the given terms are equal
	 */
	public boolean isEqual(CafeTerm t){
		if(!(t instanceof BasicTerm || !t.getOpName().equals(getOpName()))){
			return false;
		}else{
			Vector<String> foreignArgs = ((BasicTerm)t).getArgs();
			int max = (foreignArgs.size() >= getArgs().size())?getArgs().size():foreignArgs.size();
			for(int i =0; i < max; i++){
				if( !foreignArgs.get(i).equals(getArgs().get(i))) return false;
			}//end of looping through the arguments
		
			return true;
		}//end of if the names match and the given term is a BasicTerm
		
	}//end of equals
	
	
	
	
	@Override
	public CafeTerm replaceTerm(Object orig , Object repl){
		
		//System.out.println("should replace " + orig + " with " + repl + " in "+ getOpName());
		
		boolean v = ( !((String)repl).contains("(") && !isVar())? true:false;
		BasicTerm returnTerm = new BasicTerm(v);
		
		if(getArgs().size()>0) {
			returnTerm.setOpName(getOpName());
		}else{
			if(repl instanceof CafeTerm){
				return (CafeTerm)repl;
			}else{
				returnTerm.setOpName((String)repl);
				return returnTerm;
			}
		}//end if the current term has no arguments
		
		
		for(String arg: getArgs()){
			//System.out.println("should replace " + orig + " with " + repl + " and found " +arg);
			if(!arg.equals(orig)){
				returnTerm.addArg(arg);
			}else{
				returnTerm.addArg(repl);
			}
		}
		return returnTerm;
	}//end of replaceTerm
	
	
}//end of BasicOpExpr
