package test;

import java.util.Vector;

import model.CafeOperator;
import model.FileHelper;
import model.JmlGenerator;
import model.Module;

import org.junit.Before;
import org.junit.Test;

public class JmlGeneratorTester {

	FileHelper fh;
	Vector<Module> mods ;
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
		Module mod = mods.get(mods.size()-1);
		
		for(CafeOperator oper : mod.getOps()){
			//System.out.println(gen.genMethodSig(oper, mod));
		}
	
	
	}//end of testGenMethodSig


	
	
	
	@Test
	public void testTranslateInitialStates(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		Module mod = mods.get(mods.size()-1);
		System.out.println(gen.translateInitStates(mod));
		
	}
	
	
	@Test
	public void testTranslateObservers(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		Module mod = mods.get(mods.size()-1);
		System.out.println(gen.translateObservers(mod));
		System.out.println("translated observers");
	}
	
	@Test
	public void testTranslateTransitions(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		Module mod = mods.get(mods.size()-1);
		//System.out.println(gen.translateObservers(mod));
		gen.translateTransition(mod);
		System.out.println("translated TRANSITIONS");
	}
	
	
	
	
	
	
}//end of test class
