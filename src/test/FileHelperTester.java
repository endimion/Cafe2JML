package test;

import static org.junit.Assert.*;
import model.BasicTerm;
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
			
			
			/*System.out.println(mod.name + 
					" with sort "+ mod.getClassSort()+" which imports" + prString + " " + "and extends" + extString);
			System.out.println("and has  " + mod.getNumOfOps() +" operators "+ op);
			*/
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
	
		 /*fh.parseOpLine("op _about_ : consSet setofCP -> subLic", mod);
		 	assertEquals("",mod.getOps().get(0).getName() ,"about");
		 	assertEquals("",mod.getOps().get(0).getType() ,"operator");
		 	assertEquals("",mod.getOps().get(0).getSort(),"subLic");
		 	assertEquals("",mod.getOps().get(0).getArity().get(0),"consSet");
		 	assertEquals("",mod.getOps().get(0).getArity().get(1),"setofCP");
		  */
		 
		 fh.parseOpLine("op count[_] : Nat -> cons", mod);
		 assertEquals("",mod.getOps().get(0).getName() ,"count");
		 assertEquals("",mod.getOps().get(0).getType() ,"operator");
		 assertEquals("",mod.getOps().get(0).getSort(),"cons");
		 assertEquals("",mod.getOps().get(0).getArity().get(0),"Nat");
		 
		 mod = new Module();
		 fh.parseOpLine("op belong2?(_,_) : req permSet -> Bool", mod);
		 assertEquals("",mod.getOps().get(0).getName() ,"belong2?");
		 assertEquals("",mod.getOps().get(0).getType() ,"operator");
		 assertEquals("",mod.getOps().get(0).getSort(),"Bool");
		 assertEquals("",mod.getOps().get(0).getArity().get(0),"req");
		 assertEquals("",mod.getOps().get(0).getArity().get(1),"permSet");
		 
		 
		 mod = new Module();
		 fh.parseOpLine(" op belong3?(_,_) : req cPerm -> Bool", mod);
		 assertEquals("",mod.getOps().get(0).getName() ,"belong3?");
		 assertEquals("",mod.getOps().get(0).getType() ,"operator");
		 assertEquals("",mod.getOps().get(0).getSort(),"Bool");
		 assertEquals("",mod.getOps().get(0).getArity().get(0),"req");
		 assertEquals("",mod.getOps().get(0).getArity().get(1),"cPerm");
		 
		
		 
	}//end of testParsOpLine
	
	
	@Test
	public void testParseOpsLine(){
		Module mod = new Module();
		fh.parseOpsLine("ops op1_,_  op2[_,_] : Nat1 Nat2 -> Nat", mod);
		
		 assertEquals("",mod.getOps().get(0).getName() ,"op1");
		 assertEquals("",mod.getOps().get(0).getType() ,"operator");
		 assertEquals("",mod.getOps().get(0).getSort(),"Nat");
		 assertEquals("",mod.getOps().get(0).getArity().get(0),"Nat1");
		 assertEquals("",mod.getOps().get(0).getArity().get(1),"Nat2");
		 assertEquals("",mod.getOps().get(1).getName() ,"op2");
		 assertEquals("",mod.getOps().get(1).getType() ,"operator");
		 assertEquals("",mod.getOps().get(1).getSort(),"Nat");
		 assertEquals("",mod.getOps().get(1).getArity().get(0),"Nat1");
		 assertEquals("",mod.getOps().get(1).getArity().get(1),"Nat2");
		
		mod = new Module();
		fh.parseOpsLine("ops op1(_ _)  op2 : Nat1 Nat2 -> Nat", mod);
		assertEquals("",mod.getOps().get(0).getName() ,"op1");
		assertEquals("",mod.getOps().get(0).getType() ,"operator");
		assertEquals("",mod.getOps().get(0).getSort(),"Nat");
		assertEquals("",mod.getOps().get(0).getArity().get(0),"Nat1");
		assertEquals("",mod.getOps().get(0).getArity().get(1),"Nat2");
	}//end of testParseOpsLine
	
	
	
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
		BasicTerm basic = new BasicTerm();
		
		fh.parseBasicExpr("belong5?(R , subL)", basic);
		assertEquals("", basic.getOpName(), "belong5?");
		assertEquals("",basic.getArgs().get(0),"R");
		assertEquals("",basic.getArgs().get(1),"subL");
		
		basic = new BasicTerm();
		
		fh.parseBasicExpr("find4(R ,  L)", basic);
		assertEquals("", basic.getOpName(), "find4");
		assertEquals("",basic.getArgs().get(0),"R");
		assertEquals("",basic.getArgs().get(1),"L");
		
		
		fh.parseBasicExpr("find4(R   L)", basic);
		assertEquals("", basic.getOpName(), "find4");
		assertEquals("",basic.getArgs().get(0),"R");
		assertEquals("",basic.getArgs().get(1),"L");
		
		
	}//end of parseBasicExpr

	
	
	@Test
	public void testParseEq(){
		Module mod = new Module();
		
		fh.parseEq("eq (black = black ) = true ", mod);
		fh.parseEq("eq Union(e , empty) = e .", mod);
		fh.parseEq("ceq (( C about P)  = ( C' about P')) = true  if (C = C') and (P = P') .", mod);
		fh.parseEq("ceq find1(R , Union(CP , CPS)) = CP if (belong3?(R , CP)).",mod);
		
		
		/*BasicTerm t1 = new BasicTerm();
		BasicTerm t2 = new BasicTerm();
		parseBasicExpr(leftHS, t1);
		parseBasicExpr(rightHS, t2);
		
		CafeEquation e1 = new CafeEquation();
		e1.setLeftTerm(t1);
		e1.setRightTerm(t2);
		
		String ar1 = "";
		for(Object s:e1.getLeftTerm().getArgs()){ ar1 = ar1 + " " + (String)s;}
		
		
		System.out.println("line "+ line +" left op name '" + e1.getLeftTerm().getOpName() 
				+" with arguments " + ar1 
				+ "'  right '" + e1.getRightTerm().getOpName()+"'");
		*/
		
	}//end of testParseEq
	
	
	
	
	
}//end of FileHelperTester
