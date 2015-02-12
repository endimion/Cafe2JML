package test;

import java.util.Vector;

import model.BasicTerm;

import model.FileHelper;
import model.JmlGenerator;
import model.JmlModule;
import model.Module;
import model.ObsValPair;

import org.junit.Before;
import org.junit.Test;

public class JmlModuleTester {
	FileHelper fh;
	Vector<Module> mods ;
	JmlGenerator gen;
	Module mod;
	JmlModule jmod;
	
	@Before
	public void intialize(){
		fh = new FileHelper("src/tests.cafe");
		mods = fh.getModules();
		gen = new JmlGenerator(mods);
		mod = mods.get(mods.size()-1);
		jmod = new JmlModule(); 
		gen.translateSimpleModule(mod,jmod);
	}
	
	
	
	
	@Test
	public void testGetObsValues(){
		BasicTerm trans1 = new BasicTerm(false);
		trans1.setOpName("setElementAt");
		Vector<ObsValPair> pairs  = jmod.getObsValues(trans1);
		
		for(ObsValPair p : pairs){
			System.out.println(p.getObs().getOpName() + " value " + p.getValue().termToString());
		}
		
		trans1.setOpName("setSize");
		pairs  = jmod.getObsValues(trans1);
		
		for(ObsValPair p : pairs){
			System.out.println(p.getObs().getOpName() + " value " + p.getValue().termToString());
		}
		
	}//end of testGetValofObs
	
	
	
	
	
	

}//end of JmlModuleTester
