package test;

import static org.junit.Assert.*;
import model.BasicOpExpr;
import model.CafeOperator;
import model.FileHelper;
import model.Module;

import org.junit.Before;
import org.junit.Test;

public class FileHelperTester {

	FileHelper fh;
	
	
	@Before
	public void setUpTests(){
		fh = new FileHelper("src/tests.cafe");
	}
	
	
	@Test
	public void testFileHelper() {
		//System.out.println(fh.getSize());
		assertNotNull("fh object is not null!!", fh);
	}//end of test
	
	
	@Test
	public void testParseCafeFile(){
		fh.parseCafeFile();
		
		for(Module mod : fh.modules ){
			String prString = "";
			String extString ="";
			String op ="";
			
			switch(mod.name){
			
				case	"Content" : 			assertEquals("", 1,mod.getNumOfOps());
												break;
				
				case	"Permission" : 			assertEquals("", 7,mod.getNumOfOps());
												break;

				case	"Request" : 			assertEquals("", 4,mod.getNumOfOps());
												break;	
				case	"ConstraintPermission" : assertEquals("", 7,mod.getNumOfOps());
												 break;							
									
				default: 			break;
		
			};
			
			
			
			
			for(String prName : mod.getImportNames()){
				prString = prString + " " + prName;
			}//end of for loop
			
			for(String n : mod.getExtendsName()){
				extString += " " + n;
			}
			
			for(int i= 1 ; i <= mod.getOps().size(); i++){
				
				CafeOperator co =  mod.getOps().get(i-1);
				String arity ="";
				for(String ar:co.getArity()){
					arity += " "+ar;
				}
				
				op = op + " "+ i +" " + co.getName() + " : " + arity + " -> " + co.getSort();
			}//end of for loop
			
			
			System.out.println(mod.name + 
					" with sort "+ mod.getClassSort()+" which imports" + prString + " " + "and extends" + extString);
			System.out.println("and has  " + mod.getNumOfOps() +" operators "+ op);
			
		}//end of for loop
		
	}//end of testParseCafeFile
	
	
	
	@Test
	public void testParseNameLine(){
		 Module mod = new Module();
		 String line = "mod! ConstraintSet{";
		 String before ="( mod|mod\\*|mod! )";
	
		 fh.parseNameLine(line, before, mod);
		 assertEquals("", mod.getName(),"ConstraintSet");
	
		 fh.parseNameLine("mod! PermissionSET{", before, mod);
		 assertEquals("",mod.getName(),"PermissionSET");
		 
		 fh.parseNameLine("mod* SET(X :: TRIV){", before, mod);
		 assertEquals("",mod.getName(),"SET");
		 
	}//end of parseNameLine
	
	
	@Test
	public void testParsOpLine(){
		 Module mod = new Module();
	
		 fh.parseOpLine("op _about_ : consSet setofCP -> subLic", mod);
		 assertEquals("",mod.getOps().get(0).getName() ,"about");
		 assertEquals("",mod.getOps().get(0).getType() ,"operator");
		 assertEquals("",mod.getOps().get(0).getSort(),"subLic");
		 assertEquals("",mod.getOps().get(0).getArity().get(0),"consSet");
		 assertEquals("",mod.getOps().get(0).getArity().get(1),"setofCP");
	
	}//end of testParsOpLine
	
	
	
	
	
	@Test
	public void testParseVarLine(){
		 Module mod = new Module();
		 String line = "var n : Nat";
		
		 fh.parseVarLine(line, mod);  
		 assertEquals("", mod.getVars().get(0).getName() ,"n");
		 assertEquals("", mod.getVars().get(0).getSort() ,"Nat");
		 assertEquals("", mod.getVars().get(0).getType() ,"var");
		 
	
		 fh.parseVarLine("var CS : consSet", mod);
		 assertEquals("", mod.getVars().get(1).getName() ,"CS");
		 assertEquals("", mod.getVars().get(1).getSort() ,"consSet");
		 
		 mod = new Module();
		 ////
		 fh.parseVarLine("vars C C' : consSet", mod);
		 assertEquals("", mod.getVars().get(0).getName() ,"C");
		 assertEquals("", mod.getVars().get(0).getSort() ,"consSet");
		 assertEquals("", mod.getVars().get(1).getName() ,"C'");
		 assertEquals("", mod.getVars().get(1).getSort() ,"consSet");
		 
		////
		 mod = new Module();
		 fh.parseVarLine("vars P P' : permSet", mod);
		 assertEquals("", mod.getVars().get(0).getName() ,"P");
		 assertEquals("", mod.getVars().get(0).getSort() ,"permSet");
		 assertEquals("", mod.getVars().get(1).getName() ,"P'");
		 assertEquals("", mod.getVars().get(1).getSort() ,"permSet");
		 
		
		////
		 mod = new Module();
		 fh.parseVarLine("vars A1 A2 : action", mod);
		 assertEquals("", mod.getVars().get(0).getName() ,"A1");
		 assertEquals("", mod.getVars().get(0).getSort() ,"action");
		 assertEquals("", mod.getVars().get(1).getName() ,"A2");
		 assertEquals("", mod.getVars().get(1).getSort() ,"action");
	}//end of parseNameLine
	
	
	@Test
	public void testParseBasicExpr(){
		BasicOpExpr basic = new BasicOpExpr();
		
		fh.parseBasicExpr("belong5?(R , subL)", basic);
		
	}//end of parseBasicExpr

}//end of FileHelperTester
