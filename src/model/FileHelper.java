package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
	public Vector<Module> parseCafeFile(){
		
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
	
		return modules;
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
				parseLine(firstSplit[0], mod,br);
				parseLine(rest,mod,br);
			}//end of if the first line does  not stop at {
			else{
					parseLine(currentLine,mod,br);
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
						parseLine(currentLine,mod,br);
						
						if(moduleParsed) {break; }
					}else{
						parseLine(currentLine,mod,br);
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
	public void parseLine(String line, Module mod,BufferedReader br){
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
			boolean continueReading = line.trim().endsWith(".")? false : true;
			
			while(continueReading){
				String newLine;
				try {
					newLine = br.readLine();
					
					if(newLine.endsWith(".")){
						newLine = StringHelper.remLastChar(newLine);
						continueReading = false;
					}
					line += newLine;
				
				} catch (IOException e) {
					continueReading = false;
					e.printStackTrace();
				}//end of catch
			}//end of while
			
			
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
		
		if(line.trim().startsWith("*")){
			mod.setHidden(true);
		}else{
			mod.setHidden(false);
		}
		
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

		opName = StringHelper.removeSpecialCharacters(StringHelper.replaceSpecialChars(opName));
		opName = opName.split("op\\s+")[1].trim();
		
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
		
		String[] arityArr = arity.split("(\\s)+");
		for(String arg : arityArr){
			if(!arg.equals(""))cop.addToArity(arg);
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
	 * Parses a line which contains an equation and stores the result of the parsing to the given eq object
	 * @param line the line we want to parse
	 * @param mod the module in which the result of the parsing is stored to
	 * @param eq the Object in which the parsing of the line should be stored in
	 */
	public void parseEq(String line, Module mod, CafeEquation eq){
		
		String condition="";
		line = line.replace(".","");
		
		if(line.startsWith("eq")){
			line = line.split("(eq)\\s+")[1];
		}else{
			if(line.startsWith("ceq")){
				//System.out.println("conditional line "+ line);
				condition = line.split("(\\s+)if(\\s+)")[1].trim();
				line = line.split("(\\s+)if(\\s+)")[0].split("(ceq)\\s+")[1].trim();
			}//end if it is a conditional equation
		}//end of if it is not an unconditional equation
		
		if(!condition.equals("")){
			//System.out.println("CONDITION!!!: " + condition.trim());
			eq.setCondition(TermParser.parseEqTerm(condition.trim()));
		}//end if the equation contains a conditional part
		
		try {
			eq.setLeftTerm(TermParser.parseEqTerm( TermParser.eqToTree(line).get(0)  ));
			eq.setRightTerm(TermParser.parseEqTerm( TermParser.eqToTree(line).get(1)    ));
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
		
	}//end of parseEq
	
	
	
	
	/**
	 * @return a vector containing the modules parsed by the given file
	 * used to create this object;
	 */
	public Vector<Module> getModules(){
		
		return parseCafeFile();
	}//end of getModules
	

	

	
	
	
	
	
	
	

}//end of class
