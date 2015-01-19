package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * This class provides the required functionality to parse a file containing a CafeOBJ specification
 * There is one limitation to the syntax of CafeOBJ expression in this version
 * In the specification file you are supposed to only have pre-fix operation (as opposed to a variety of 
 * infix, misfix and prefix) this does not affect at all the expressive power of the specification
 * it only makes the pasing of the operators easier as the left-most part will allways be an operator name
 *  
 * @author Nikos Triantafyllou
 *
 */

public class FileHelper {


	private File cafeFile ;
	private long size ;
	public Vector<Module> modules;
	
	/*
	 * this constructor takes as input the path to a what should be CafeOBJ file
	 */
	public FileHelper(String path){
		
		try{
			cafeFile = new File(path);

		}catch(Exception ex1){ex1.printStackTrace();}
		
	}//end of constructor
	
	
	/*
	 * returns the size of the cafeobj file
	*/
	public long getSize(){
		if(cafeFile != null){
			size = cafeFile.length(); 
		}else{
			size = -1;
		}
		return size;
	}//end of getSize
	
	

	/*
	 * reads the contents of a CafeOBJ file
	 * if a line starts with -- it should be ignored
	 * if a line starts with the word mod it should end when { is found
	 * if a line starts with [ it should end when ] is met or (*[ and ]* respectively)
	 * if a  line starts with pr( it should end when ) is  met
	 * if a line starts with op or ops or bop or bops it should end when either a "." is found 
	 * 			or at the end of line (EOL)
	 * if a line starts with var or vars it should end either when a "." is found of EOL
	 * if a line starts with eq or ceq or beq or bceq it should end when a "." is found  
	 * if a line starts with } it should end there
	 * 
	 */
	public void parseCafeFile(){
		
		modules = new Vector<Module>(); // the modules containing within the file under translation
		
		
		try {
			FileInputStream fis = new FileInputStream(cafeFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr); 
		
			String line = (br.readLine()).trim();
			
			 while(line != null){
				 
				 line = line.trim(); //we remove excessive white spaces from the start and end of the line
				 
				 // lines that start with -- are comments and should  be ignored
				 if(!line.startsWith("--")){
					 //System.out.println(line);
					 
					 // if a  new Class is found this should be processed  first and then
					 // continue to the rest of the classes
					 if(line.startsWith("mod") || line.startsWith("mod*") || line.startsWith("mod!")){
						 
						 Module newMod = new Module();
						 modules.addElement(newMod);
						 
						 parseModule(br, line, newMod);
						 
					 }//end of new module line found
				 }

				 line = br.readLine();
			 }
			
			br.close();
			 
		} catch (Exception e) {e.printStackTrace();}
	}//end of parseCafeFile
	
	
	/**
	 * processes a new module 
	 */
	public void parseModule(BufferedReader br, String currentLine, Module mod){
		
		boolean moduleParsed = false;
		int numOfOpenCurlies = 1 ;
		
		try{
			//first we split the first line to contain 
			
			String[] firstSplit = currentLine.split("\\{");
			
			
			if(firstSplit.length > 1){
				String rest = firstSplit[1];
				parseLine(firstSplit[0], mod);
				parseLine(rest,mod);
			}//end of if the first line does  not stop at {
			else{
					parseLine(currentLine,mod);
			}///end of parsing the first line
		
			currentLine = (br.readLine()).trim();
				
			while(currentLine != null && !moduleParsed ){
				
				if( !currentLine.startsWith("--")){
					
					currentLine = currentLine.trim(); //remove extra white spaces
					
					if(currentLine.contains("{")) {
						numOfOpenCurlies += StringHelper.numOf(currentLine, '{'); 
					}
					
					if(currentLine.contains("}")){
						numOfOpenCurlies -= StringHelper.numOf(currentLine, '}'); 
						moduleParsed = (numOfOpenCurlies == 0);
						parseLine(currentLine,mod);
						
						if(moduleParsed) {break; }
					}else{
						parseLine(currentLine,mod);
					}//end of parsing the line
				}//end if line is a comment
				
				currentLine = br.readLine();	
			}//end of parsing the module (END OF WHILE LOOP)
			
			
		}catch(Exception e){e.printStackTrace();}
	
	
	}//end of parseClass
	
	/**
	 * parses the given line argument
	 * @param line, a String representing a line read from the file
	 */
	public void parseLine(String line, Module mod){
		String beforeModName = "( mod|mod\\*|mod! )";
		String importString = "(pr|ext)[(]";
		String sortDeclString = "\\[|\\*\\[";
		String sortDeclEndString = "]";
		
		line = line.trim();
		
		if(line.split(beforeModName).length > 1){
			parseNameLine(line, beforeModName, mod);
		}//end of parsing the first module line
		
		//TODO attention the line may not end after pr(...) smn could have written code there...
		// for now we ignore this
		// also we have to take care of the cases where renaming in CafeOBJ takes place
		// via some signature morphism defined inside a pr clause
		if(line.split(importString).length > 1){
			parseImportLine(line, importString, mod);
		}//end of parsing the line if it contains a pr or ext
		

		if(line.split(sortDeclString).length > 1 && 
					(line.endsWith(sortDeclEndString) || line.endsWith("]*"))){
			parseSortDecLine(line, mod);
		}//end of if the line contains a sort declaration
		
		
		if( ( line.startsWith("op") || (line.trim().startsWith("bop"))  ) 
				&& ( !( (line.trim()).startsWith("ops") || (line.trim().startsWith("bops"))  ))
				&& (StringHelper.numOf(line,':') == 1)
			){

			parseOpLine(line,mod);
			
		}//end if line contains an operator declaration
		
	
		if(line.startsWith("ops") || line.startsWith("bops")
				&& (StringHelper.numOf(line,':') == 1)){
			
			parseOpsLine(line,mod);
			
		}//end if line contains a single line declaration of  
	
		
		if(line.startsWith("var") || line.startsWith("vars")){
			parseVarLine(line,mod);
		}//end if line contains a single variable declaration
	
		if(line.startsWith("eq") || line.startsWith("ceq")){
			CafeEquation eq = new CafeEquation();
			parseEq(line,mod,eq);
			mod.addEq(eq);
		}//end if line contains a single variable declaration
		
	}//end of parseLine
	
	
	
	/**
	 * 
	 * @param line, the line we want to parse
	 * @param before, a string which denotes  mod|mod\\*|mod!
	 * @param mod, the module object where the result from parsing the line should be saved
	*/
	public void parseNameLine(String line, String before, Module mod){
		line = line.trim();

		if(line.contains("::")){ //this is required for parametric modules(i.e., SET(X::TRIV))
			line = line.split("[(]")[0];
		}
		
		if(line.endsWith("{")){ 
			line = StringHelper.remLastChar(line);
		}
		
		mod.setName(line.split(before)[1].trim());
	}//end of parseNameLine
	
	/**
	 * method responsible for parsing a line which contains a CafeOBJ import statement (i.e. pr(...))
	 * @param line , the line we want to parse
	 * @param importString, a string denoting the regular expression for CafeOBJ declaration of an import
	 * @param mod, the Module object which will store the information stored by the parsing
	 */
	public void parseImportLine(String line, String importString,Module mod ){
		line = line.split(importString)[1].trim();
		String[] imports = line.split("[+]");
		for(String impName : imports){
			if(impName.endsWith(")")){impName =  StringHelper.remLastChar(impName);}
			mod.addImportName(impName.trim());
		}//end of for loop, which adds the import names to the module object
	}//end of parseImportLine
	
	
	/**
	 * method responsible for parsing a line which contains a CafeOBJ sort declaration (i.e. [...] or *[...]*)
	 * @param line the line we want to parse
	 * @param mod the Module object which will store the information stored by the parsing
	 */
	public void parseSortDecLine(String line, Module mod){
		line = line.replace("[","");
		line = line.replace("]", "");
		line = line.replace("*","");
		line = line.replace("*", "");
		
		line = line.trim();
		
		if(StringHelper.numOf(line, '<') > 1){
			System.out.println("no successfull parse too many < is sort declaration");
		}else{
			
			String extendsNames = ""; 
			String sort ="";
			if(line.split("<").length > 1){
				extendsNames = line.split("<")[0].trim();
				sort = line.split("<")[1].trim();
				
			}//end if line contains a <
			else{
				sort = line;
			}//end if line does not contain a <
			
			mod.setClassSort(sort);
			
			if( extendsNames.split("\\s+").length > 1){
				System.out.println("Error you cannot extend multiple classes in Java");
			}else{
				mod.addExtendsName(extendsNames);
			}//end of else
		}//end of else
	}//end of parseSortDeclLine
	
	
	
	
	/**
	 * 
	 * @param line, a string containing a line which defines a CafeOBJ operator declacration
	 * @param mod
	*/
	public void parseOpLine(String line, Module mod){
		CafeOperator cop = new CafeOperator();
		String opName  = line.split(":")[0].trim();
			   opName = StringHelper.removeSpecialCharacters(opName.split("_")[0].trim());
		
		opName = opName.substring(StringHelper.getWhitePos(opName), opName.length()).replace("_","").trim();
		cop.setName(opName);
		
		String opSig = line.split(":")[1].trim();
		String arity = opSig.split("->")[0].trim();
		
		if(arity.equals("")){
			cop.setType("constant");
		}else{
			cop.setType("operator");
		}//end of discriminating the type
		
		String coArity = opSig.split("->")[1].trim();
		
		cop.setSort(StringHelper.cutStringAtWhite(coArity));
		
		String[] arityArr = arity.split("\\s+");
		for(String arg : arityArr){
			cop.addToArity(arg);
		}//end of for

		mod.addOp(cop);
	}//end of parseOpLine
	
	
	
	public void parseOpsLine(String line, Module mod){
		
		Vector<CafeOperator> cops = new Vector<CafeOperator>();
		
		
		String opName  = line.split(":")[0].trim();
		opName = opName.substring(StringHelper.getWhitePos(opName), opName.length()).replace("_","").trim();
		
		
		String[] arrayOfOps = opName.split("\\s+");
		//System.out.println(arrayOfOps.length + " !!!!!!");
		for(String s:arrayOfOps){
			CafeOperator cop = new CafeOperator();
			
			s = StringHelper.removeSpecialCharacters(s.trim());
			cop.setName(s);
			cops.add(cop);
		}
		
		String opSig = line.split(":")[1].trim();
		String arity = opSig.split("->")[0].trim();
		String coArity = opSig.split("->")[1].trim();
		
		
		for(CafeOperator cop: cops){
			cop.setSort(StringHelper.cutStringAtWhite(coArity));
			
			if(arity.equals("")){
				cop.setType("constant");
			}else{
				cop.setType("operator");
			}//end of discriminating the type
			
			String[] arityArr = arity.split("\\s+");
			for(String arg : arityArr){
				cop.addToArity(arg);
			}//end of for
			mod.addOp(cop);
		}//end of for loop
		
	}//end of parseOpsLine
	
	
	/**
	 * This method parses a line which should contain a variable declaration in CafeOBJ and store 
	 * the result  in the module object
	 * @param line the line we want to parse
	 * @param mod  the object we store the result of the parsing
	 */
	public void parseVarLine(String line, Module mod){
		
		line = line.trim();
		
		String varName = line.split("[:]")[0];
		String sort = (line.split("[:]")[1].replace(".","")).trim();
		
		varName = varName.trim();
		int start = StringHelper.getWhitePos(varName);//varName
		varName = varName.substring(start, varName.length()).trim();
		
		
		if(!line.startsWith("vars")){//single variable declaration
			
			CafeVariable var = new CafeVariable();
			var.setName(varName);
			var.setSort(sort);
			
			mod.addVar(var);
			
		}else{ //multiple variables declaration
			//vars C C' : consSet
			String[] vars = varName.split("\\s+");
			
			for(String v : vars){
				CafeVariable var = new CafeVariable();
				var.setName(v.trim());
				var.setSort(sort);
			
				mod.addVar(var);
			}//end of for var names loop
		}//end of else
	}//end of parseVarLine
	
	
	/**
	 * Parses a line which contains an equation and stores the result of the parsing
	 * @param line the line we want to parse
	 * @param mod the module in which the result of the parsing is stored to
	 */
	public void parseEq(String line, Module mod, CafeEquation eq){
		
		String condition="";
		line = line.replace(".","");
		
		if(line.startsWith("eq")){
			line = line.split("(eq)\\s+")[1];
		}else{
			if(line.startsWith("ceq")){
				condition = line.split("if")[1].trim();
				line = line.split("if")[0].split("(ceq)\\s+")[1].trim();
			}//end if it is a conditional equation
		}//end of if it is not an unconditional equation
		
		if(!condition.equals("")){
			//TODO parse the effectiveCondition;
		}
		
		eq.setLeftTerm(parseEqTerm( eqToTree(line).get(0)  ));
		eq.setRightTerm(parseEqTerm( eqToTree(line).get(1)    ));
	}//end of parseEq
	
	
	/**
	 * 
	 * @param eqTerm a String representing a equation 
	 * @return a Vector (containing one or two Strings) representing
	 * the left and right part of the equation term 
	 * 
	 */
	public Vector<String> eqToTree(String eqTerm){
		
		Vector<String> result = new Vector<String>();
		String leftHS;
		String rightHS;
		
		if(StringHelper.numOf(eqTerm, '=') > 1){
			//if the line contains many equals we have to use parenthesis to parse it
			int firstPar = StringHelper.firstAppearOfChar(eqTerm,'(');
			int pos = StringHelper.colsingParPosition(eqTerm);

			if( firstPar == 0){
				leftHS = eqTerm.substring(1, pos).trim(); //we remove the opening parenthesis
			}else{
				String upToFirstParen = eqTerm.substring(0,firstPar-1);
				leftHS = (upToFirstParen + eqTerm.substring(firstPar+1,pos)).trim();
			}
				  
			rightHS = eqTerm.substring(pos+1,eqTerm.length()).trim();
			if(rightHS.startsWith("=")){
				rightHS = StringHelper.remFirstChar(rightHS).trim();
			}
		}else{//the line contains only one =
			leftHS = eqTerm.split("=")[0].trim();
			rightHS = eqTerm.split("=")[1].trim();
		}
		
		result.add(leftHS);
		result.add(rightHS);
		
		return result;
	}//end of getLR
	
	
	
	
	
	
	/**
	 * parses a left or right hand side term  of an equation
	 * and returns a CafeTerm object which contains the result of the parsing
	 * @param lhs the left/right hand side string represting the term
	 * @return a CafeTerm object containing the result of the parsing
	 */
	public CafeTerm parseEqTerm(String line){
		
		line = StringHelper.remEnclosingParenthesis(line);
		
		if(isBasicTerm(line)){
			BasicTerm t = new BasicTerm();
			parseBasicExpr(line, t);
			//System.out.println("Basic term " + line);
			return t;
		
		}else{
			
			return parseSubTerm(line);
			
		}
	}//end of parseLeftHS
	
	

	
	
	
	
	
	/**
	 * parses a term and returns an Object containing the position and name of the main Operator
	 * of the term
	 * @param term
	 * @return an Object containing the position and name of the main Operator
	 * if there exists no main operator -1 is returned as the position instead
	 */
	public OpNamePos getMainPos(String term){
		
		term = StringHelper.remEnclosingParenthesis(term).trim();
		
		String mainOp ="";
		char[] termAr = term.toCharArray();
		
		int openPars = 0;
		int pos = -1;
		boolean addToMain = true;
		char c;
		boolean continueLooping = true;
		
		
		if(!(StringHelper.numOf(term, '(') == 0 && StringHelper.numOf(term, '=') == 0)){
			//"R , about(C,  P)"
			for(int i =0; i< termAr.length; i++){
				
				if(continueLooping){
					c = termAr[i];
					
					if(c != '(' && !Character.isWhitespace(c) && c!=')' && c !=',' && addToMain && c != '='){
						mainOp = mainOp + c;
						pos = i;
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
				
			if(openPars != 0){
				mainOp ="";
				pos = -1;
			}//end of if some parenthesis remained open
			
		}//end if the term contains atleast a parenthesis or an = symbol
		
		return new OpNamePos(mainOp,pos);
	}//end of getMainPos
	
	
	
	/**
	 * takes as input a term and returns a CafeTerm object which contains its parsing, 
	 * i.e. operator and a vector of the arguments of the term
	 * @return
	*/
	public CafeTerm parseSubTerm(String term){
		
		term = StringHelper.remEnclosingParenthesis(term);
		OpNamePos mainOp = getMainPos(term);
		
		if(isBasicTerm(term)){
			BasicTerm t = new BasicTerm();
			parseBasicExpr(term, t);
			return t;
		}else{ //end if it is a basic term
			CompTerm ct = new CompTerm();
			ct.setOpName(mainOp.getName());
			
			if(!mainOp.getName().equals("=")){
				//String inner = getInnerTerm(term, mainOp.getName(),mainOp.getPos());
				Vector<String> args = new Vector<String>(); 
				
				if(getMainPos(term).getPos() > 0){
					splitTerm(term, args, true);
				}else{
					splitTerm(term, args, false);
				}
				
				for(String t: args){
					ct.addArg(parseSubTerm(t));
				}
				
				return ct;
			}else{
				
				int eqPos = mainOp.getPos();
				String lhs = term.substring(0, eqPos).trim();
				String rhs = term.substring(eqPos+1, term.length()).trim();
				ct.setOpName("equals");
				ct.addArg(parseSubTerm(lhs));
				ct.addArg(parseSubTerm(rhs));
				
				return ct;
			}//end if the main operator is not an = symbol
			
		}//end if it is not a basic term

		
		
	}//end of parseSubTerm
	
	/**
	 * 
	 * @param term
	 * @param mainOp the name of the main operator of the term
	 * @param pos the position of the operator
	 * @return the inside part of the term (inner part of the main operator) if that exists
	 * otherwise the term as it is is returned
	 */
	public String getInnerTerm(String term, String mainOp, int pos){
		if(pos >= 0 && !mainOp.equals("=")){
			//System.out.println(term);
			try{
				return term.substring(pos +2, term.length()-1).trim();
			}catch(Exception e){System.out.println(term); return term;}
			
		}else{
			return term;
		}
	}//end of getInnerTerm
	
	
	
	/**
	 * takes as input a term and gets its inner part of the main operator 
	 * splits that into arguments and returns a vector containing those arguments
	 * i.e. f(a(b,c),g) --> [a(b,c) , g]
	 * @param term
	 * @param args the vector in which the subterms are stored
	 * @return
	 */
	public void splitTerm(String term, Vector<String> args, boolean makeSub){
		
		int pos = getSubTermPos(term);
		int start = 0;
		
		if(pos == term.length()-1) { makeSub = true;}
		
		if(makeSub){
			
			String inner = getInnerTerm(term, getMainPos(term).getName(),
					getMainPos(term).getPos()).trim();

			pos = getSubTermPos(inner) ;
			
			
			if(pos>=0 && !(pos >= inner.length())){
				
				if(!getMainPos(inner).getName().equals("=")){
					if(inner.charAt(0)  == ','){start = 1;}
					args.addElement( inner.substring(start,pos+1).trim());
					splitTerm(inner.substring(pos+1 ,inner.length()).trim(),args,false);
				}else{
					args.addElement(inner.trim());
				}//end if the main operator is not the = symbol
			}//end if the position of the main operator is not the end of the term
			
		}else{
			
			term = term.trim();
		
			pos = getSubTermPos(term);
			
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
	 * takes as input a term and returns the ending position of the first subterm
	 * if no subterm exists -1 is returned
	 * @param term
	 * @return the ending position of a subterm or if none exists -1
	*/
	public int getSubTermPos(String term){
		
		char[] termAr ; 
		int pos = -1;
		int open = 0;
		char c;
		boolean skip = true;
		
		//String inner = getInnerTerm(term, getMainPos(term).getName(),
		//		getMainPos(term).getPos());
		int start;
		term = term.trim();
		termAr  = term.toCharArray();
		
		if(!getMainPos(term).getName().equals("=")){
			
			start = 0;
			
			for(int i=start; i< termAr.length; i++){
				
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
							pos = i;
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
			pos = getMainPos(term).getPos();
		}//end if main operator is an = operator
		
		
		
		return pos;
	}//end getSubTerm
	
	
	
	
	
	
	/**
	 * Takes an expression and determines if it contains an operator call
	 * or if it only contains constants or variables as arguments 
	 * @return true if the string has no '(' or if inside the initial (...) there exist 
	 * no other parenthesis or brackets
	 *  
	 */
	public boolean isBasicTerm(String s){
		
		int fParPos = StringHelper.firstAppearOfChar(s, '(');
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
	 * Parses a basic CafeOBJ expresion, i,e, an operator with values either variables or constants
	 * an operator is thus basic if the arguments are not non-constant operators this is easily found
	 * if we recall the for that to occur there must exist parenthesis of [] in the operator string  
	 * and stores the result to the given BasicOpExpr object
	 * @param exp
	 * @param op
	 */
	public void parseBasicExpr(String exp, BasicTerm basic){
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
	
	
	
	
	
	
}//end of class
