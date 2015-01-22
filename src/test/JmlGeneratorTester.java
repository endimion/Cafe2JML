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
		Module mod = mods.get(2);
		
		for(CafeOperator oper : mod.getOps()){
			System.out.println(gen.genMethodSig(oper, mod));
		}
	
	
	}//end of testGenMethodSig


	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of test class
