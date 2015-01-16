package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Vector;

import model.BasicTerm;
import model.CafeEquation;
import model.CafeOperator;
import model.CafeTerm;
import model.CompTerm;
import model.FileHelper;
import model.Module;
import model.OpNamePos;

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

	
	
	
	
	
	public void testRecursively(Vector<Object> args){
		for(Object o:args){
			
			if(o instanceof BasicTerm){
				System.out.println(((BasicTerm) o).getOpName());
				String basicargumnets="with arguments ";
				for(String s : ((BasicTerm) o).getArgs()){
					basicargumnets = basicargumnets + s  + " ";
				}
				System.out.println(basicargumnets);
			}else{
			System.out.println( "comp " +  ((CompTerm) o).getOpName());
				Vector<Object> innerArgs = (Vector<Object>) (((CompTerm) o).getArgs() );
				testRecursively(innerArgs);
			}//end of if it is a composite term
		}//end of the for loop of the arguments of the given term

	}//end of testRecursively
	
	
	@Test
	public void testGetMainPos(){
		String s = "( g( f( about(C,  P)  = about(C,' P')) , asds) )";
		assertEquals("",  fh.getMainPos(s).getName()  , "g");
		assertEquals("",  fh.getMainPos(s).getPos()  , 0);
		
		s = "about(C,  P)  = about(C,' P')";
		assertEquals("",  fh.getMainPos(s).getName()  , "=");
		assertEquals("",  fh.getMainPos(s).getPos()  , 14);
		
		s = "f( about(C,  P)  = about(C,' P'))";
		assertEquals("",  fh.getMainPos(s).getName()  , "f");
		
		s = "cons3?(R , C about CPS) = cons0?(C)";
		assertEquals("",  fh.getMainPos(s).getName()  , "=");
		
		 s = "cons3?((R , C about CPS) = cons0?(C))";
		assertEquals("",  fh.getMainPos(s).getName()  , "cons3?");
		
		 s = "cons3";
		 assertEquals("",  fh.getMainPos(s).getPos()  , -1);
			
		 s = "cons3(";
		 assertEquals("",  fh.getMainPos(s).getPos()  , -1);
		
		 s= "R , about(C,  P)";
		 assertEquals("",fh.getMainPos(s).getPos(),-1);
		 
		 
	}//end of testParseTermContEq
	
	
	@Test
	public void testGetInnerTerm(){
		String s = "cons3?((R , C about CPS) = cons0?(C))";
		OpNamePos main = fh.getMainPos(s);
		assertEquals("",  fh.getInnerTerm(s, main.getName(), main.getPos())  , 
											"(R , C about CPS) = cons0?(C)");
		
		s = "f( about(C,  P)  = about(C,' P'))";
		main = fh.getMainPos(s);
		assertEquals("",  fh.getInnerTerm(s, main.getName(), main.getPos())  , 
											"about(C,  P)  = about(C,' P')");
		s="find3(R , (subL , L))";
		main = fh.getMainPos(s);
		assertEquals("",  fh.getInnerTerm(s, main.getName(), main.getPos())  , 
											"R , (subL , L)");
		
		s="find3(R , (subL , L))";
		main = fh.getMainPos(s);
		assertEquals("",  fh.getInnerTerm(s, main.getName(), main.getPos())  , 
											"R , (subL , L)");
		
	}//end of testGetInnerTerm
	
	
	@Test
	public void testGetSubTermPos(){
		String s = "cons3?((R , C about CPS) = cons0?(C))";
		String inner = fh.getInnerTerm(s, fh.getMainPos(s).getName(),
				fh.getMainPos(s).getPos());
		assertEquals("",fh.getSubTermPos(inner),-1);
		
		s =  "find3(R , (subL , L))";
		inner = fh.getInnerTerm(s, fh.getMainPos(s).getName(),
				fh.getMainPos(s).getPos());
		assertEquals("",fh.getSubTermPos(inner),1);
		
		s =  "(R , C about CPS) = cons0?(C)";
		inner = fh.getInnerTerm(s, fh.getMainPos(s).getName(),
				fh.getMainPos(s).getPos());
		assertEquals("",fh.getSubTermPos(inner),-1);
		
		s =  "f( about(C,  P)  = about(C,' P'))";
		inner = fh.getInnerTerm(s, fh.getMainPos(s).getName(),
				fh.getMainPos(s).getPos());
		assertEquals("",fh.getSubTermPos(inner),-1);
		

		s =  "f( about(C,  P) , about(C,' P'))";
		inner = fh.getInnerTerm(s, fh.getMainPos(s).getName(),
				fh.getMainPos(s).getPos());
		assertEquals("",fh.getSubTermPos(inner),11);
	}//end of testGetSubTerm
	
	
	@Test
	public void testSplitTerm(){
		
		Vector<String> v = new Vector<String>();
		String s = "cons3?( (R , C about CPS) , cons0?(C), adbd,23)";
		fh.splitTerm(s, v,true);
		assertEquals("",v.get(0),"(R , C about CPS)");
		assertEquals("",v.get(1),"cons0?(C)");
		assertEquals("",v.get(2),"adbd");
		assertEquals("",v.get(3),"23");

		v = new Vector<String>();
		s = "R , about( C , CPS)";
		fh.splitTerm(s, v,false);
		assertEquals("",v.get(0),"R");
		assertEquals("",v.get(1),"about( C , CPS)");
	
		v = new Vector<String>();
		s = "g(R , about( C , CPS))";
		fh.splitTerm(s, v,false);
		assertEquals("",v.get(0),"R");
		assertEquals("",v.get(1),"about( C , CPS)");
	
	
	}//end of testSplitTerm
	
	
	
	
	
	@Test
	public void testParseSubTerm(){
		//CafeTerm ct = fh.parseSubTerm("cons3?( g(R , about( C , CPS)) , cons0?(C), adbd,23)");  OK
		// CafeTerm ct = fh.parseSubTerm("cons3?( R , about( C , CPS) , cons0?(C), adbd,23)"); OK
		ct.getArgs();
		//System.out.println(ct.getOpName());
		
		for(int i=0; i< ct.getArgs().size();i++){
			CafeTerm t = (CafeTerm) ct.getArgs().get(i);
			String arg ="";
			for(int j = 0; j < t.getArgs().size(); j++){
				if(t.getArgs().get(j) instanceof CafeTerm){
				 arg = arg + printTermsArgs((CafeTerm)t.getArgs().get(j));
				}
				else{
					arg += t.getArgs().get(j);
				}
					
			}
			System.out.println(t.getOpName() +" with arguments " + arg);
		}
		
	}//end of testParseSubTerm
	
	
	public String printTermsArgs(CafeTerm t){
		String name = t.getOpName();
		Vector<Object> v = (Vector<Object>) t.getArgs();
		
		String res = name + "( ";
		
		for(int i=0; i< v.size();i++){
			if(v.get(i) instanceof BasicTerm){
				res = res + ((BasicTerm)v.get(i)).termToString() + ", ";
			}else{
				if(v.get(i) instanceof String){
					res = res + (String)v.get(i) + ",";
				}else{
					res = res + printTermsArgs((CompTerm) v.get(i));
				}
			}
		}//end of for loop
		
		return res + ")";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Test
	public void testParseEq(){
		Module mod = new Module();
		CafeEquation eq = new CafeEquation();
		CafeTerm ct2;
		
		fh.parseEq("eq (black = black ) = true ", mod,eq);
		ct2 = eq.getLeftTerm();
		//System.out.println(ct2.termToString());
		
		fh.parseEq("eq Union(e , empty) = e .", mod,eq);
		ct2 = eq.getLeftTerm();
		//System.out.println(ct2.termToString());
		
		
		fh.parseEq("ceq ( about(C  P)  = about(C'  P')) = true  if (C = C') and (P = P') .", mod,eq);
		ct2 = eq.getLeftTerm();
		//System.out.println(ct2.termToString());
		
		
		fh.parseEq("ceq  ( g( f( about(C,  P)  = about(C,' P')) , asds) )   = true  if (C = C') and (P = P') .", mod,eq);
		ct2 = eq.getLeftTerm();
		//System.out.println(ct2.termToString());
		
		
		
		fh.parseEq("ceq find1(R , Union(CP1 , CPS)) = CP2 if (belong3?(R , CP)).",mod,eq);
		CafeTerm ct = eq.getLeftTerm();	
		
		if(ct instanceof CompTerm){
			//System.out.println(((CompTerm) ct).termToString());
		}
		
		
	}//end of testParseEq
	
	
	
}//end of FileHelperTester
