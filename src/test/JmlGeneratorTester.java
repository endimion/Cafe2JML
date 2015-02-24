package test;

import static org.junit.Assert.*;

import java.util.Vector;

import model.BasicTerm;
import model.CafeOperator;
import model.CafeTerm;
import model.CompTerm;
import model.FileHelper;
import model.JmlGenerator;
import model.JmlModule;
import model.Module;

import org.junit.Before;
import org.junit.Test;

public class JmlGeneratorTester {

	FileHelper fh;
	Vector<Module> mods ;
	Vector<JmlModule> jmods ;
	JmlGenerator gen;
	
	@Before
	public void intialize(){
		fh = new FileHelper("src/tests.cafe");
		mods = fh.getModules();
		gen = new JmlGenerator(mods);
	}
	
	
	@Test
	public void testGenMethodSig(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		//Module mod = mods.get(mods.size()-1);
		
		//for(CafeOperator oper : mod.getOps()){
			//System.out.println(gen.genMethodSig(oper, mod));
		//}
	
	
	}//end of testGenMethodSig


	
	
	
	@Test
	public void testTranslateInitialStates(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		//Module mod = mods.get(mods.size()-1);
		//System.out.println(gen.translateInitStates(mod));
		
	}
	
	
	@Test
	public void testTranslateObservers(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		//Module mod = mods.get(mods.size()-1);
		//System.out.println(gen.translateObservers(mod));
		//System.out.println("translated observers");
	}
	
	@Test
	public void testTranslateTransitions(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		//Module mod = mods.get(mods.size()-2);
		//Module mod = mods.get(mods.size()-1);
		//System.out.println(gen.translateObservers(mod));
		//System.out.println(gen.translateTransition(mod));
		//System.out.println("translated TRANSITIONS");
	}
	
	@Test
	public void testTranslateSimpleModule(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		//Module mod = mods.get(mods.size()-2);
		Module mod = mods.get(mods.size()-1);
		JmlModule jmod = new JmlModule(mod.getClassSort()); 
		
		//System.out.println(gen.translateObservers(mod));
		//System.out.println(gen.translateGuards(mod));
		//System.out.println("translated effective conditions!!!!!");
		
		//System.out.println(gen.translateHiddenConstants(mod) );
		//System.out.println(gen.translateInitStates(mod) );
		//System.out.println(gen.translateObservers(mod));
		//System.out.println(gen.translateGuards(mod));
		//System.out.println(gen.translateTransition(mod) );
		System.out.println(gen.translateSimpleModule(mod,jmod));
		
		mod = mods.get(mods.size()-2);
		jmod = new JmlModule(mod.getClassSort()); 
		gen.translateSimpleModule(mod, jmod);
		
	}//end of testTranslateSimpleModule
	
	@Test
	public void testGetProjections(){
		
		Module mod = mods.get(mods.size()-2);
		
		Vector<CafeOperator> proj = mod.getProjections(mods); 
		
		for(CafeOperator op: proj){
			System.out.println("projection "+ op.getName());
		}
	}//end of testGetProjections
	
	
	@Test
	public void testBuildChainFromTerm(){
		
		Vector<CafeTerm> chain = new Vector<CafeTerm>();
		Module mod = mods.get(mods.size()-1);
		
		BasicTerm t2 = new BasicTerm(false);
		t2.setOpName("t2");
		t2.addArg("S2");
		
		CompTerm t3 = new CompTerm();
		t3.setOpName("t3");
		t3.addArg(t2);
		
		CompTerm t1 = new CompTerm();
		t1.setOpName("t1");
		t1.addArg(t3);
		
		chain = gen.buildChainFromTerm(chain, t1,mod,"","");
		
		int i=0;
		for(CafeTerm t : chain){
			//System.out.println("element " + i + "of chain is "+ t.termToString(mod));
			if(i == 0) assertEquals("", t.getOpName(),"t1" );
			if(i == 1) assertEquals("", t.getOpName(),"t3" );
			if(i == 2) assertEquals("", t.getOpName(),"t2" );
			i++;
		}
		
	}//end testBuildChainFromTerm
	
	
	
}//end of test class
