package test;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import model.BasicTerm;
import model.CafeTerm;
import model.CompTerm;
import model.StringHelper;
import model.TermParser;

import org.junit.Test;

public class TermParserTester {


	
	@Test
	public void testGetMainPos(){
		String s = "( g( f( about(C,  P)  = about(C,' P')) , asds) )";
		assertEquals("",  TermParser.getMainPos(s).getName()  , "g");
		assertEquals("",  TermParser.getMainPos(s).getPos()  , 0);
		
		s = "about(C,  P)  = about(C,' P')";
		assertEquals("",  TermParser.getMainPos(s).getName()  , "=");
		assertEquals("",  TermParser.getMainPos(s).getPos()  , 14);
		
		s = "f( about(C,  P)  = about(C,' P'))";
		assertEquals("",  TermParser.getMainPos(s).getName()  , "f");
		
		s = "cons3?(R , C about CPS) = cons0?(C)";
		assertEquals("",  TermParser.getMainPos(s).getName()  , "=");
		assertEquals("",  TermParser.getMainPos(s).getPos()  , 24);
		
		
		 s = "cons3?((R , C about CPS) = cons0?(C))";
		assertEquals("",  TermParser.getMainPos(s).getName()  , "cons3?");
		
		 s = "cons3";
		 assertEquals("",  TermParser.getMainPos(s).getPos()  , -1);
			
		 s = "cons3(";
		 assertEquals("",  TermParser.getMainPos(s).getPos()  , -1);
		
		 s= "R , about(C,  P)";
		 assertEquals("",TermParser.getMainPos(s).getPos(),-1);
		 
		 s = "black = black";
		 assertEquals("",  TermParser.getMainPos(s).getPos()  , 6);
			
		 s ="( g(f( about(C,  P)  = about(C,' P')) , asds) ) "; //OK
		 assertEquals("",  TermParser.getMainPos(s).getName()  , "g");
		 
		 s = StringHelper.remEnclosingParenthesis(s);	
		 assertEquals("",  TermParser.getMainPos(s).getName()  , "g");
		 assertEquals("",s,"g(f( about(C,  P)  = about(C,' P')) , asds)");
		 assertEquals("",TermParser.getMainPos(s).getPos(),0);
	
	
		 s = " adffdd and ssssv";
		 assertEquals("",  TermParser.getMainPos(s).getName()  , "and");
	
		 s =" ((c-request(S)) and (belong6?(R , L))) ";
		 assertEquals("",TermParser.getMainPos(s).getName(),"and");
		 
		 s ="not( c-request(S)) ";
		 assertEquals("",TermParser.getMainPos(s).getName(),"not");
		 
		 s=" (not((type3?(labelCP?(find3(R ,L))) = once )) or (type2?(labelCP?(find3(R ,L))) = true) ";
		 assertEquals("",s.contains(" or "),true);
		 assertEquals("",TermParser.getMainPos(s).getName(),"or");
	
		 
		 s=" (not((type3?(labelCP?(find3(R ,L))) = once )) + (type2?(labelCP?(find3(R ,L))) = true) ";
		 assertEquals("",s.contains(" + "),true);
		 assertEquals("",TermParser.getMainPos(s).getName(),"+");
		 
		 
		 s=" G( a + b) / G(d,e * f) ";
		 assertEquals("",TermParser.getMainPos(s).getName(),"/");
		 
		 s = "c-try(S,I)";
		 assertEquals("",TermParser.getMainPos(s).getName(),"c-try");
		 
		 
		s =" not(#(finalLic(choose(S))) > 1) ";
		//System.out.println(TermParser.getMainPos(s).getName());
		//System.out.println(TermParser.getMainPos(s).getPos());
					//t = TermParser.parseEqTerm(s);
		 assertEquals("",TermParser.getMainPos(s).getName(),"not");
		
		 
		 s = "p(x) + 1";
		 assertEquals("",TermParser.getMainPos(s).getName(),"+");
		 
		 s= "pivot(S) + 1";
		 assertEquals("",TermParser.getMainPos(s).getName(),"+");
		 
		 s =" pivot(S) - 1";
		 assertEquals("",TermParser.getMainPos(s).getName(),"-");
		 
	}//end of testParseTermContEq
	


	@Test
	public void testParseBasicExpr(){
		BasicTerm basic = new BasicTerm();
		
		TermParser.parseBasicExpr("belong5?(R , subL)", basic);
		assertEquals("", basic.getOpName(), "belong5?");
		assertEquals("",basic.getArgs().get(0),"R");
		assertEquals("",basic.getArgs().get(1),"subL");
		
		basic = new BasicTerm();
		
		 TermParser.parseBasicExpr("find4(R ,  L)", basic);
		assertEquals("", basic.getOpName(), "find4");
		assertEquals("",basic.getArgs().get(0),"R");
		assertEquals("",basic.getArgs().get(1),"L");
		
		
		 TermParser.parseBasicExpr("find4(R   L)", basic);
		assertEquals("", basic.getOpName(), "find4");
		assertEquals("",basic.getArgs().get(0),"R");
		assertEquals("",basic.getArgs().get(1),"L");
		
		
	}//end of parseBasicExpr


	@Test
	public void testGetInnerTerm(){
		String s = "cons3?((R , C about CPS) = cons0?(C))";
		assertEquals("",   TermParser.getInnerTerm(s)  , 
											"(R , C about CPS) = cons0?(C)");
		
		s = "f( about(C,  P)  = about(C,' P'))";
		assertEquals("",   TermParser.getInnerTerm(s)  , 
											"about(C,  P)  = about(C,' P')");
		s="find3(R , (subL , L))";
		assertEquals("",  TermParser.getInnerTerm(s)  , 
											"R , (subL , L)");
		
		s="find3(R , (subL , L))";
		assertEquals("",   TermParser.getInnerTerm(s)  , 
											"R , (subL , L)");
		
		
		s =  "(R , C about CPS) = cons0?(C)";
		assertEquals("", TermParser.getInnerTerm(s) ,"(R , C about CPS) = cons0?(C)");
		
		s ="c-try(S,I)";
		assertEquals("",TermParser.getInnerTerm(s), "S,I");
		
	}//end of testGetInnerTerm


	@Test
	public void testGetSubTermPos(){
		String s = "cons3?((R , C about CPS) = cons0?(C))";
		String inner =  TermParser.getInnerTerm(s);
		assertEquals("",TermParser.getSubTermPos(inner),18);
		
		s =  "find3(R , (subL , L))";
		inner = TermParser.getInnerTerm(s);
		assertEquals("",TermParser.getSubTermPos(inner),1);
		
		s =  "(R , C about CPS) = cons0?(C)";
		inner =  TermParser.getInnerTerm(s);
		//assertEquals("",fh.getSubTermPos(inner),18);
		assertEquals("",inner,"(R , C about CPS) = cons0?(C)");
		
		s =  "f(about(C,  P) = about(C,' P'))";
		inner =  TermParser.getInnerTerm(s);
		assertEquals("",TermParser.getSubTermPos(inner),13);
		
		s ="c-try(S,I)";
		inner = TermParser.getInnerTerm(s);
		assertEquals("",inner, "S,I");
		assertEquals("",TermParser.getSubTermPos(inner),0);
		
	}//end of testGetSubTerm


	
	@Test
	public void testSplitTerm(){
		
		Vector<String> v = new Vector<String>();
		String s = "cons3?( (R , C about CPS) , cons0?(C), adbd,23)";
		TermParser.splitTerm(s, v,true);
		assertEquals("",v.get(0),"(R , C about CPS)");
		assertEquals("",v.get(1),"cons0?(C)");
		assertEquals("",v.get(2),"adbd");
		assertEquals("",v.get(3),"23");

		v = new Vector<String>();
		s = "R , about( C , CPS)";
		TermParser.splitTerm(s, v,false);
		assertEquals("",v.get(0),"R");
		assertEquals("",v.get(1),"about( C , CPS)");
	
		v = new Vector<String>();
		s = "g(R , about( C , CPS))";
		TermParser.splitTerm(s, v,false);
		assertEquals("",v.get(0),"R");
		assertEquals("",v.get(1),"about( C , CPS)");
	
		v = new Vector<String>();
		s = "g(f( about(C,  P)  = about(C,' P')) , asds)";
		TermParser.splitTerm(s, v,false);
		assertEquals("",v.get(0),"f( about(C,  P)  = about(C,' P'))");
		assertEquals("",v.get(1),"asds");
		
		v = new Vector<String>();
		s = "f( about(C,  P)  = about(C,' P'))";
		TermParser.splitTerm(s, v,false);
		assertEquals("",v.get(0),"about(C,  P)  = about(C,' P')");
		//assertEquals("",v.get(1),"about(C,' P')");
		
		
		v = new Vector<String>();
		s = " about(C,  P)  = about(C,' P')";
		TermParser.splitTerm(s, v,false);
		assertEquals("",v.get(0),"about(C,  P)");
		assertEquals("",v.get(1),"about(C,' P')");
		
		v = new Vector<String>();
		s ="c-try(S,I)";
		TermParser.splitTerm(s, v,false);
		assertEquals("",v.get(0),"S");
		assertEquals("",v.get(1),"I");
		
		
	}//end of testSplitTerm
	
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testParseSubTerm(){
		//CafeTerm ct = fh.parseSubTerm("cons3?( g(R , about( C , CPS)) , cons0?(C), adbd,23)");  OK
		// CafeTerm ct = fh.parseSubTerm("cons3?( R , about( C , CPS) , cons0?(C), adbd,23)"); OK
		//CafeTerm ct = fh.parseSubTerm("find3(R , union(subL , L))"); //OK
		//CafeTerm ct = fh.parseSubTerm("build(R , uni(L ,LS)) = (build(R , LS))"); //OK
		
		CafeTerm ct = TermParser.parseSubTerm(" ( g(f( about(C,  P)  = about(C', P')) , asds) ) "); //NOT OK!!
		assertEquals("",ct.getOpName(),"g");
		Vector<CafeTerm> args = (Vector<CafeTerm>) ct.getArgs();
		CafeTerm arg0 = args.get(0);
		CafeTerm arg1 = args.get(1);
		assertEquals("",args.size(),2);
		assertEquals("",arg0.getOpName(),"f");
		assertEquals("",arg1.getOpName(),"asds");
		
		args = (Vector<CafeTerm>) arg0.getArgs();
		assertEquals("",args.get(0).getOpName(),"equals");
		assertEquals("",args.size(),1);
		arg0 = args.get(0);
		
		args = (Vector<CafeTerm>) arg0.getArgs();
		assertEquals("",args.get(0).getOpName(),"about");
		assertEquals("",args.get(1).getOpName(),"about");
		assertEquals("",args.size(),2);
		arg0 = args.get(0);
		arg1 = args.get(1);
		
		Vector<String> basicArgs = (Vector<String>) arg0.getArgs();
		assertEquals("",basicArgs.get(0),"C");
		assertEquals("",basicArgs.get(1),"P");
		
		
		//test n02 
		
		ct = TermParser.parseSubTerm("cons3?( g(R , about( C , CPS)) , cons0?(C), adbd,23)");
		assertEquals("",ct.getOpName(),"cons3?");
		args = (Vector<CafeTerm>) ct.getArgs();
		arg0 = args.get(0);
		arg1 = args.get(1);
		CafeTerm arg3 = args.get(2);
		assertEquals("",args.size(),4);
		
		assertEquals("",arg0.getOpName(),"g");
		assertEquals("",arg1.getOpName(),"cons0?");
		assertEquals("",arg3.getOpName(),"adbd");	
		assertEquals("",args.get(3).getOpName(),"23");
		
		
		Vector<Object> arugm = (Vector<Object>)args.get(0).getArgs();
		assertEquals("",((CafeTerm)arugm.get(0)).getOpName(),"R");
		assertEquals("",((CafeTerm)arugm.get(1)).getOpName(),"about");
		
		
		
		ct = TermParser.parseSubTerm(" (pc(S , I) = rs) and (not (locked(S)) )");
		assertEquals("",ct.getOpName(),"and");
		assertEquals("",ct.getArgs().size(),2);
		assertEquals("",((CompTerm)ct.getArgs().get(0)).getOpName(),"equals");
		assertEquals("",((CompTerm)ct.getArgs().get(1)).getOpName(),"not");
	}//end of testParseSubTerm
	
	
	@Test
	public void testParseEqTerm(){
		
		String s = "c-try(S,I)";
		CafeTerm t = TermParser.parseEqTerm(s);
		
		assertEquals("",t.getOpName(),"c-try");
		assertEquals("",t.getArgs().get(0),"S");

		
		//s =" (not(#(finalLic(choose(S))) > 1)) and c-choose(S)";
		//System.out.println(TermParser.getMainPos(s).getName());
		//System.out.println(TermParser.getMainPos(s).getPos());
		//t = TermParser.parseEqTerm(s);
		s ="not((I = J) and c-exit(S,I)) ";
		t = TermParser.parseEqTerm(s);
		assertEquals("", t.getOpName(),"not");
		assertEquals("",((CompTerm)t.getArgs().get(0)).getOpName(),"and");		
		assertEquals("",((BasicTerm)((CompTerm)t.getArgs().get(0)).getArgs().get(1)).getOpName(),"c-exit");
		
	}//end of testParseEqTerm
	
	
	@Test
	public void testEqToTree(){
		String s = "c-try(S,I) = (pc(S , I) = rs) and (not (locked(S)))";
		try{
			Vector<String> tree = TermParser.eqToTree(s);
			assertEquals("",tree.get(0),"c-try(S,I)");
			assertEquals("",tree.get(1),"(pc(S , I) = rs) and (not (locked(S)))");
		}catch(Exception e){e.printStackTrace();}
	}//end of EqToTree test
	
	
}//end of testerClass
