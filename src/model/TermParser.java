package model;

import java.util.Vector;




/**
 * This class provides a variety of methods which can be used to parse a CafeOBJ term 
 * and store its contents in appropriate objects
 * @author nikos
 *
 */

public class TermParser {
	
	
	
	
	
	/**
	 * 
	 * @param term
	 * @param mainOp the name of the main operator of the term
	 * @param pos the position of the operator
	 * @return the inside part of the term (inner part of the main operator) if that exists
	 * otherwise the term as it is is returned
	 */
	public static String getInnerTerm(String term){
		String mainOp = getMainPos(term).getName();
		int pos = getMainPos(term).getPos();
		
		if(pos >= 0 && !(isBinary(mainOp))){
			//System.out.println(term);
			try{
				return term.substring(pos +2, term.length()-1).trim();
			}catch(Exception e){ //System.out.println(mainOp +" DUDDDDDEEE "+" "+term); 
				return term;
			}
			
		}else{
			return term;
		}
	}//end of getInnerTerm
	
	
	
	

	/**
	 * Takes an expression and determines if it contains an operator call
	 * or if it only contains constants or variables as arguments 
	 * @return true if the string has no '(' or if inside the initial (...) there exist 
	 * no other parenthesis or brackets
	 *  
	 */
	public static boolean isBasicTerm(String s){
		
		int fParPos = StringHelper.firstAppearOfChar(s, '(');
		
		OpNamePos mainOp = getMainPos(s);
		
		if(isBinary(mainOp.getName()) && !mainOp.getName().equals("=")){
			return isTermConsOrVar(s.substring(0,mainOp.getPos())) 
					&& 
					isTermConsOrVar(s.substring(mainOp.getPos()+1,s.length()) );
		}
		
		
		if(!s.contains("=")){
			if(fParPos >= 0){
				String inner = s.substring(fParPos+1, s.length()-1);
				return (StringHelper.numOf(inner, '(') == 0) && (StringHelper.numOf(inner, '[') == 0);
				
			}else{
				return true;
			}
		}else{
			return false;
		}
	}//end of isBasicExpr
	
	
	/**
	 * 
	 * @param term
	 * @return whether or not the given term is a constant or a variable,
	 * i.e. contains no arguments
	 */
	public static boolean isTermConsOrVar(String term){
		return !term.contains("(");
	}
	
	
	
	
	/**
	 * Parses a basic CafeOBJ expresion, i,e, an operator with values either variables or constants
	 * an operator is thus basic if the arguments are not non-constant operators this is easily found
	 * if we recall the for that to occur there must exist parenthesis of [] in the operator string  
	 * and stores the result to the given BasicOpExpr object
	 * @param exp
	 * @param op
	 */
	public static void parseBasicExpr(String exp, BasicTerm basic){
		//example1: belong5?(R , subL)
		//example2 a constant: e
		
		String  opName="";
		
		if(exp.split("[(]").length > 1){
			opName = exp.split("[(]")[0].trim();
			String arguments = exp.split("[(]")[1].replace(")","").trim();
			
			String[] args ;
			if(arguments.contains(",")){ //then the arguments of the operator are separated by commas ,
				args = arguments.split("[,]");
			}else{ //end if contains ","
				args = arguments.split("\\s+");
			}//arguments are split whit white spaces
			
			for(int i = 0; i < args.length; i++){
				basic.getArgs().add(StringHelper.remWhite(args[i]));
				//System.out.println("for op "+opName + " add args " +args[i] );
			}//end of adding the arguments loop
		}else{
			opName = exp;
		}
			
		basic.setOpName(opName);
	}//end of parseBasicExpr
	
	
	/**
	 * takes as input a term and returns the ending position of the first subterm
	 * if no subterm exists -1 is returned
	 * @param term
	 * @return the ending position of a subterm or if none exists -1
	*/
	public static int getSubTermPos(String term){
		
		char[] termAr ; 
		int pos = -1;
		int open = 0;
		char c;
		boolean skip = true;
		
		//String inner = getInnerTerm(term, getMainPos(term).getName(),
		//		getMainPos(term).getPos());
		term = term.trim();
		termAr  = term.toCharArray();
		
		if(!isBinary(getMainPos(term).getName()) ){
			
			for(int i=0; i< termAr.length; i++){
				
				c = termAr[i];
				
				if(!Character.isWhitespace(c) && c!=',' && c!='=') skip = false; //with this we ignore the first white spaces
				
				if(c == '('){
					open++;
				}else{
					if(c == ')'){
						open--;
						if(open == 0){
							pos = i;
							break;
						}//end of if open is zero 
					}else{
						
						if(Character.isWhitespace(c) && open == 0 && !skip){
							if(i < termAr.length -1 && !Character.isWhitespace(termAr[i+1])){
								pos = i;
								break;
							}
						}//end if c is a whitespace
					
						if(c ==',' 	&& open == 0 && !skip){
							pos = i-1;
							break;
						}//end if c is a comma
					
					
					
					
					}//end of if c is not a closing parenthesis
				}//end if char is not (
				
				if(pos == -1 && i == termAr.length-1 && open == 0){
					pos = termAr.length-1;
					//in the end if no open parenthesis remain and we have read all the
					// term then the position of the subterm is the end of the term
				}
				
			}//end of for loop
		}//if the main op is not an =
		else{
			pos = TermParser.getMainPos(term).getPos();
		}//end if main operator is an = operator
		
		
		
		return pos;
	}//end getSubTerm
	
	
	
	/**
	 * parses a term and returns an Object containing the position and name of the main Operator
	 * of the term
	 * @param term
	 * @return an Object containing the position and name of the main Operator
	 * if there exists no main operator -1 is returned as the position instead
	 */
	public static OpNamePos getMainPos(String term){
		
		term = StringHelper.remEnclosingParenthesis(term).trim();
		
		
		String mainOp ="";
		char[] termAr = term.toCharArray();
		
		int openPars = 0;
		int pos = -1;
		boolean addToMain = true;
		char c;
		boolean continueLooping = true;
		boolean lookForLogic  = term.contains(" and ")|| term.contains(" or ") 
				|| term.contains(" implies ")
				||term.contains(" > ")  || term.contains(" >= ") 
				|| term.contains(" < ") || term.contains(" <= ") 
				|| term.contains(" + ") || term.contains(" - ")
				|| term.contains(" / ") || term.contains(" * ");
		
		String logicConnect = ""; 
				
		if(!(StringHelper.numOf(term, '(') == 0 && StringHelper.numOf(term, '=') == 0)
				|| (term.contains(" and ") || term.contains(" or ")) || term.contains(" implies ")
				||term.contains(" > ")  || term.contains(" >= ") 
				|| term.contains(" < ") || term.contains(" <= ") 
				|| term.contains(" + ") || term.contains(" - ")
				|| term.contains(" / ") || term.contains(" * ") ){
			//"R , about(C,  P)"
			
			
			if(lookForLogic){
				for(int j = 0; j< term.toCharArray().length;j++){
					c = term.toCharArray()[j];
					
					if(!Character.isWhitespace(c)){
						if(c == '('){openPars++;}
						else{if(c == ')') openPars--;}
						
						logicConnect += c;
						
						if(isBinary(logicConnect.trim())){
							if(!(logicConnect.equals(">")||logicConnect.equals("<"))){
								mainOp = logicConnect;
								lookForLogic = false;
								if(openPars == 0){ 
									//System.out.println("THE LOCATION OF " + logicConnect + " in " +  
									//		term+" is " + j);
									return new OpNamePos(logicConnect, j);
								}
							}else{
								if(j < term.toCharArray().length -2  && term.toCharArray()[j+1] == '='){
										logicConnect += term.toCharArray()[j+1];
										lookForLogic = false;
										if(openPars == 0){ 
											return new OpNamePos(logicConnect, j+1);
										}//end if there are no open parenthesis
								}//end if the next char is an equals
								else{
									if(openPars == 0){ 
										return new OpNamePos(logicConnect, j);
									}//end if there are no open parenthesis
								}//end if the next char is not the equals char
								
							}//end if logicConnect is equal to < or >
						}//end if and or or has been found
					}else{
						logicConnect="";
						lookForLogic = true;
					}
				}//end for loop
				
			} //end of if lookForLogic
			
				mainOp = "";
				continueLooping = true;
				//if(term.startsWith("not"))System.out.println("HEYYYY!! "+term);
				
				for(int i =0; i< termAr.length; i++){
					
					if(continueLooping){
						c = termAr[i];
						
						if(c != '(' && !Character.isWhitespace(c) && c!=')' && c !=',' && addToMain && c != '='){
							mainOp = mainOp + c;
							pos = i;
							
							//if(term.startsWith("not"))System.out.println("HEYYYY!! "+term);
	
						}else{ //end of if c is not a special character for this purposes
							
							
							switch(c){
								case '(' :	openPars++;
											addToMain = false;
											break;
								
								case ')' :	openPars--;
											if(openPars == 0 && i !=0 && i != termAr.length-1){
												mainOp ="";
												pos = -1;
												addToMain = true;
											}//end if the closing of this parenthesis made the opened ones zero
											break;
								
								case '=' :  if(openPars == 0){
												addToMain = false;
												mainOp = ""+c;
												pos = i;
												continueLooping = false;
											}
											break;
								
								case ',' : if(openPars == 0){
												pos = -1;
												continueLooping = false;
											} 
											break;
							}//end of switch
							
							
							
							
							
						}//end of else if c is not a special char
			
					}//end of for loop
					
					
				}//end of continue looping
					
				if(openPars != 0 && !isBinary(mainOp)){
					mainOp ="";
					pos = -1;
				}//end of if some parenthesis remained open
				
			}//end if the term contains atleast a parenthesis or an = symbol
		
		//}//end of if not look for logic connectivity symbol
		
		return new OpNamePos(mainOp,pos);
	}//end of getMainPos
	
	
	
	
	/**
	 * takes as input a term and gets its inner part of the main operator 
	 * splits that into arguments and returns a vector containing those arguments
	 * i.e. f(a(b,c),g) --> [a(b,c) , g]
	 * @param term
	 * @param args the vector in which the subterms are stored
	 * @return
	 */
	public static void splitTerm(String term, Vector<String> args, boolean makeSub){
		
		int pos = TermParser.getSubTermPos(term);
		int start = 0;
		
		if(pos == term.length()-1) { makeSub = true;}
		
		if(makeSub){
			
			String inner = TermParser.getInnerTerm(term).trim();

			pos = TermParser.getSubTermPos(inner) ;
			
			
			if(pos>=0 && !(pos >= inner.length())){
				
				if(!isBinary(getMainPos(inner).getName())){
					if(inner.charAt(0)  == ','){start = 1;}
					args.addElement( inner.substring(start,pos+1).trim());
					splitTerm(inner.substring(pos+1 ,inner.length()).trim(),args,false);
					
					//System.out.println("not binary " +term);
				}else{
					args.addElement(inner.trim());
				}//end if the main operator is not the = symbol
			}//end if the position of the main operator is not the end of the term
			
		}else{
			
			term = term.trim();
		
			pos = TermParser.getSubTermPos(term);
			
			if(term.charAt(0) == ',' ){ start = 1;}
			
			if(pos > 0 && pos+1 < term.length()){
				
				if(term.charAt(pos+1) ==','){
					args.addElement( term.substring(start,pos+1).trim());
					splitTerm(term.substring(pos+2,term.length()),args,false);
				}else{
					args.addElement( term.substring(start,pos).trim());
					splitTerm(term.substring(pos+1,term.length()),args,false);
				}
				
			
			}else{
				//if(term.startsWith(",")){term = StringHelper.remFirstChar(term);}
				//if(term.endsWith(",")){term = StringHelper.remLastChar(term);}
				args.addElement(term);
			}//end of else
			
		}
		
	}//end of split term
	
	

	/**
	 * takes as input a term and returns a CafeTerm object which contains its parsing, 
	 * i.e. operator and a vector of the arguments of the term
	 * @return
	*/
	public static CafeTerm parseSubTerm(String term){
		
		term = StringHelper.remEnclosingParenthesis(term);
		OpNamePos mainOp = TermParser.getMainPos(term);
		
		if(TermParser.isBasicTerm(term)){
			BasicTerm t = (term.contains("("))? new BasicTerm(false):new BasicTerm(true);
			TermParser.parseBasicExpr(term, t);
			return t;
		}else{ //end if it is a basic term
			CompTerm ct = new CompTerm();
			ct.setOpName(mainOp.getName());
			
			//System.out.println("term "+ term + "main " + mainOp.getName());
			if(!isBinary(mainOp.getName())){
				//String inner = getInnerTerm(term, mainOp.getName(),mainOp.getPos());
				
				Vector<String> args = new Vector<String>(); 
				
				if(TermParser.getMainPos(term).getPos() > 0){
					TermParser.splitTerm(term, args, true);
				}else{
					TermParser.splitTerm(term, args, false);
				}
				
				for(String t: args){
					ct.addArg(parseSubTerm(t));
				}
				
				return ct;
			}else{
				
				int eqPos = mainOp.getPos();
				String lhs = "" ; //term.substring(0, eqPos).trim();
				
				if(mainOp.getName().equals("=")){
					lhs = term.substring(0, eqPos).trim();
				}else{
					//System.out.println("TERM :" + term);
					try{
						lhs = term.substring(0, eqPos-mainOp.getName().length()).trim();
					}catch(StringIndexOutOfBoundsException e){
						System.out.println(term+ "eqPos" + eqPos + " mainOp" + mainOp.getName().length());
						e.printStackTrace();
					}
				}
				
				String rhs = term.substring(eqPos+1, term.length()).trim();
				
				if(mainOp.getName().equals("=")){ ct.setOpName("equals");}
				
				ct.addArg(parseSubTerm(lhs));
				ct.addArg(parseSubTerm(rhs));
				
				return ct;
			}//end if the main operator is not an = symbol
			
		}//end if it is not a basic term

		
		
	}//end of parseSubTerm
	
	
	/**
	 * 
	 * @param eqTerm a String representing a equation 
	 * @return a Vector (containing one or two Strings) representing
	 * the left and right part of the equation term 
	 * 
	 */
	public static Vector<String> eqToTree(String eqTerm) throws Exception{
		
		Vector<String> result = new Vector<String>();
		String leftHS;
		String rightHS;
		
		if(StringHelper.numOf(eqTerm, '=') > 1){
			OpNamePos main = getMainPos(eqTerm);
			if(main.getName().equals("=")){
				leftHS = eqTerm.substring(0, main.getPos()).trim();
				rightHS = eqTerm.substring(main.getPos()+1, eqTerm.length()).trim();
			}else{
				throw new Exception("The term parsed should be an equation but " 
									+ eqTerm + "is not" + main.getName() );
			}
		}else{//the line contains only one =
			leftHS = eqTerm.split("=")[0].trim();
			//System.out.println("the term is" + eqTerm);
			rightHS = eqTerm.split("=")[1].trim();
		}
		
		result.add(leftHS);
		result.add(rightHS);
		
		return result;
	}//end of getLR
	
	
	
	
	/**
	 * parses a left or right hand side term  of an equation
	 * and returns a CafeTerm object which contains the result of the parsing
	 * @param the term
	 * @return a CafeTerm object containing the result of the parsing
	 */
	public static CafeTerm parseEqTerm(String line){
		
		line = StringHelper.remEnclosingParenthesis(line);
		
		if(TermParser.isBasicTerm(line)){
			BasicTerm t = (line.contains("("))?new BasicTerm(false):new BasicTerm(true);
			TermParser.parseBasicExpr(line, t);
			//System.out.println("Basic term " + line);
			return t;
		
		}else{
			
			return TermParser.parseSubTerm(line);
			
		}
	}//end of parseLeftHS
	
	/**
	 * takes as input the name of an operator and returns wheter or not it should be considered
	 * a binary operator
	 * @param opName
	 * @return
	 */
	public static boolean isBinary(String opName){
		return opName.equals("=")
		||opName.equals("and")||opName.equals("or") 
		||opName.equals(">")||opName.equals(">=")||opName.equals("<")
		||opName.equals("<=")||opName.equals("+")||opName.equals("-")
		||opName.equals("*")||opName.equals("/") || opName.equals("equals");
	}//end of isBinary
	

	
	/**
	 * 
	 * @param t
	 * @return the position of the system sort, i.e. the sort which is 
	 * defined by the module in the term. If no such sort exists -1 is returned
	 */
	public static int getPositionOfSystemSort(CafeTerm t, Module mod){
		
		@SuppressWarnings("unchecked")
		Vector<Object> args = (Vector<Object>) t.getArgs();
		
		for(int i = 0; i < args.size(); i++){
			Object arg = args.get(i);
			
			if(arg instanceof String &&  arg != null 
					&& mod.getOpSortByName((String)arg).equals(mod.getClassSort())){
				return i;
			}else{
				if(arg instanceof CafeTerm &&  
						mod.getOpSortByName(((CafeTerm) arg).getOpName()).equals(mod.getClassSort()))
				{return i;}
			}//end if the argument is not a string
		}//end of looping through the arguments of the term r
		return -1;
	}//end of getPositionOfSystemSort
	

	/**
	 * translates the given CafeOBJ sort to the corresponding Java sort
	 * @param sort
	 * @return
	 */
	public static String cafe2JavaSort(String sort){
		
		switch(sort){
		case "Int" : return "int";
					 
		case "Bool" : return "boolean";
					  
		case "and"	: return "&&";

		case "or"   : return "||";
		}
		return sort;
	}//end of cafe2JavaSort


}
