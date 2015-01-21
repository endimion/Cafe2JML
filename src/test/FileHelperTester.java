package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import model.BasicTerm;
import model.CafeEquation;
import model.CafeOperator;
import model.CafeTerm;
import model.CompTerm;
import model.FileHelper;
import model.Module;

import org.junit.Before;
import org.junit.Test;

public class FileHelperTester {

	FileHelper fh;
	
	
	@Before
	public void setUpTests(){
		fh = new FileHelper("src/tests.cafe");
		//fh = new FileHelper("src/cafe2athena.test.txt");
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
	public void testParseEq(){
		Module mod = new Module();
		CafeEquation eq = new CafeEquation();
		CafeTerm ct2;
		
		fh.parseEq("eq (black = black) = true ", mod,eq);
		ct2 = eq.getLeftTerm();
		assertEquals("",ct2.getOpName(),"equals" );
		assertEquals("",((BasicTerm)ct2.getArgs().get(0)).getOpName(), "black");
		assertEquals("",((BasicTerm)ct2.getArgs().get(1)).getOpName(), "black");
		//System.out.println(ct2.termToString());
		
		fh.parseEq("eq (c1 = c2) = true", mod, eq);
		ct2 = eq.getLeftTerm();
		assertEquals("",ct2.getOpName(),"equals" );
		assertEquals("",((BasicTerm)ct2.getArgs().get(0)).getOpName(), "c1");
		assertEquals("",((BasicTerm)ct2.getArgs().get(1)).getOpName(), "c2");
		//System.out.println(ct2.termToString());
		
		
		fh.parseEq("eq Union(e , empty) = e .", mod,eq);
		ct2 = eq.getLeftTerm();
		assertEquals("",ct2.getOpName(),"Union" );
		assertEquals("",(String)ct2.getArgs().get(0), "e");
		assertEquals("",(String)ct2.getArgs().get(1), "empty");
		//System.out.println(ct2.termToString());
		
		
		fh.parseEq("ceq ( about(C  P)  = about(C'  P')) = true  if (C = C') and (P = P') .", mod,eq);
		ct2 = eq.getLeftTerm();
		assertEquals("",ct2.getOpName(),"equals" );
		assertEquals("",((BasicTerm)ct2.getArgs().get(0)).getOpName(), "about");
		assertEquals("",((BasicTerm)ct2.getArgs().get(0)).getArgs().get(0), "C");
		assertEquals("",((BasicTerm)ct2.getArgs().get(0)).getArgs().get(1), "P");
		assertEquals("",((BasicTerm)ct2.getArgs().get(1)).getOpName(), "about");
		assertEquals("",((BasicTerm)ct2.getArgs().get(1)).getArgs().get(0), "C'");
		assertEquals("",((BasicTerm)ct2.getArgs().get(1)).getArgs().get(1), "P'");
		//System.out.println(ct2.termToString());
		
		
		fh.parseEq("ceq  ( g( f( about(C,  P)  = about(C,' P')) , asds) )   = true  if (C = C') and (P = P') .", mod,eq);
		ct2 = eq.getLeftTerm();
		assertEquals("",ct2.getOpName(),"g" );
		assertEquals("",((CompTerm)ct2.getArgs().get(0)).getOpName(), "f");
		assertEquals("",((BasicTerm)ct2.getArgs().get(1)).getOpName(), "asds");
		assertEquals("",((CompTerm)((CompTerm)ct2.getArgs().get(0)).getArgs().get(0)).getOpName(), "equals");
		//assertEquals("",eq.getCondition().getOpName(),"and");
		

		
		fh.parseEq("ceq find1(R , Union(CP1 , CPS)) = CP2 if (belong3?(R , CP)).",mod,eq);
		CafeTerm ct = eq.getLeftTerm();	
		//assertEquals("",eq.getCondition().getOpName(),"belong3?");
		
		if(ct instanceof CompTerm){
			System.out.println(((CompTerm) ct).termToString());
		}
		
		fh.parseEq("eq (L = L) = true",mod,eq);
		ct = eq.getLeftTerm();
		assertEquals("",ct.getOpName(),"equals");
		
		fh.parseEq("eq c-try(S,I) = ( (pc(S , I) = rs) and (not locked(S)) ) .", mod,eq);
		ct = eq.getRightTerm();
		System.out.println(ct.getOpName());
		
		ct = eq.getLeftTerm();
		System.out.println(ct.getOpName());
		//System.out.println( "Name "+ ((CompTerm)ct.getArgs().get(0)).getOpName());
		
	}//end of testParseEq
	
	
	
}//end of FileHelperTester
