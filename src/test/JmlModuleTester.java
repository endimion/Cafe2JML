package test;

import java.util.Vector;

import model.BasicTerm;
import model.CafeTerm;
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
	
	
	
	
	//Test
	public void testGetObsValbyTrans(){
		BasicTerm trans1 = new BasicTerm(false);
		trans1.setOpName("setElementAt");
		Vector<ObsValPair> pairs  = jmod.getObsValbyTrans(trans1,mod);
		
		for(ObsValPair p : pairs){
		//	System.out.println(p.getObs().getOpName() + " value " + p.getValue().termToString(mod));
		}
		
		trans1.setOpName("setSize");
		pairs  = jmod.getObsValbyTrans(trans1,mod);
		
		for(ObsValPair p : pairs){
			//System.out.println(p.getObs().getOpName() + " value " + p.getValue().termToString(mod));
		}
		
	}//end of testGetValofObs
	
	@Test
	public void testGetObsValAfterTransCh(){
		Vector<CafeTerm> chain = new Vector<CafeTerm>();
		
		chain.add(jmod.getTransObsVals().get(0).getTransition());
		chain.add(jmod.getTransObsVals().get(2).getTransition());
		chain.add(jmod.getTransObsVals().get(2).getTransition());
		chain.add(jmod.getTransObsVals().get(0).getTransition());
		//jmod.getObsValAfterTransCh(chain,mod);
		
		
		BasicTerm t1 = new BasicTerm(false);
		t1.setOpName("setElementAt");
		//t1.addArg("A");
		t1.addArg("I");
		t1.addArg("K");
		
		BasicTerm t2 = new BasicTerm(false);
		t2.setOpName("setElementAt");
		//t2.addArg("A");
		t2.addArg("J");
		t2.addArg("Q");
		
		
		BasicTerm t3 = new BasicTerm(false);
		t3.setOpName("setElementAt");
		//t1.addArg("A");
		t3.addArg("I");
		t3.addArg("V");

		
		BasicTerm t4 = new BasicTerm(false);
		t4.setOpName("setSize");
		//t1.addArg("A");
		t4.addArg("W");
		
		BasicTerm t5 = new BasicTerm(false);
		t5.setOpName("setElementAt");
		t5.addArg("A");
		t5.addArg("L");
		
		
		chain.removeAllElements();
		chain.add(t3);
		chain.add(t5);
		chain.add(t2);
		chain.add(t4);
		chain.add(t1);
		
		
		jmod.getObsValAfterTransCh(chain,mod);
		
		
	}//end of testGetObsValAfterTransCh
	
	
	
	

}//end of JmlModuleTester
