package test;

import java.util.Vector;

import model.BasicTerm;
import model.FileHelper;
import model.JmlGenerator;
import model.JmlModule;
import model.Module;
import model.TransObserValues;
import model.TransObserValues.ObsValPair;

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
	public void testTranslateGuards(){
		//CafeOperator op = mods.get(0).getOps().get(0); 
		//Module mod = mods.get(mods.size()-2);
		Module mod = mods.get(mods.size()-1);
		JmlModule jmod = new JmlModule(); 
		
		//System.out.println(gen.translateObservers(mod));
		//System.out.println(gen.translateGuards(mod));
		//System.out.println("translated effective conditions!!!!!");
		
		//System.out.println(gen.translateHiddenConstants(mod) );
		//System.out.println(gen.translateInitStates(mod) );
		//System.out.println(gen.translateObservers(mod));
		//System.out.println(gen.translateGuards(mod));
		//System.out.println(gen.translateTransition(mod) );
		System.out.println(gen.translateSimpleModule(mod,jmod));
		
		
		for(TransObserValues v : jmod.getTransObsVals()){
			String trans_args = "";
			
			for(Object t: v.getTransitionArgs()){
				trans_args += t + ", ";
			}//end of for loop
			
			System.out.println("for transition " + v.getTransitionName() + "with arguments " +trans_args );
			for(ObsValPair pair: v.getObserversValues()){
				String arguments ="";
				for(Object arg : pair.getObs().getArgs()){
					arguments += ((BasicTerm)arg).getOpName() + ", ";
				}
			 System.out.println("observer "	+ pair.getObs().getOpName() + 
					 " with arguments "+ arguments +" and value " + pair.getValue());
			}

		}
	}
	
	
	
	
	
	
}//end of test class
