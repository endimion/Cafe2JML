package test;

import static org.junit.Assert.*;

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
		jmod = new JmlModule(mod.getClassSort()); 
		gen.translateSimpleModule(mod,jmod);
	}
	
	
	
	
	@Test
	public void testGetObsValbyTrans(){
		BasicTerm trans1 = new BasicTerm(false);
		trans1.setOpName("setElementAt");
		trans1.addArg("I");
		trans1.addArg("V");
		Vector<ObsValPair> pairs  = jmod.getObsValbyTrans(trans1,mod);
		
		for(ObsValPair p : pairs){
			//System.out.println(p.getObs().getOpName() + " value " + p.getValue().termToString(mod));
			if(p.getObs().getOpName().equals("getSize")) assertEquals("",p.getValue().termToString(mod,null),"getSize()");	
			if(p.getObs().getOpName().equals("getElementAt")) assertEquals("",p.getValue().termToString(mod,null),"V");
		}
		
		trans1.setOpName("setSize");
		trans1.addArg("K");
		pairs  = jmod.getObsValbyTrans(trans1,mod);
		
		for(ObsValPair p : pairs){
			if(p.getObs().getOpName().equals("getSize")) assertEquals("",p.getValue().termToString(mod,null),"(d + getSize())");	
			if(p.getObs().getOpName().equals("getElementAt")) assertEquals("",p.getValue().termToString(mod,null),"getElementAt()");
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
		t5.addArg("h");
		t5.addArg("L");
		
		BasicTerm t6 = new BasicTerm(false);
		t6.setOpName("setSize");
		//t1.addArg("A");
		t6.addArg("W2");
		
		chain.removeAllElements();
		chain.add(t3);
		chain.addElement(t6);
		chain.add(t5);
		chain.add(t2);
		chain.add(t4);
		chain.add(t1);
		
		
		Vector<ObsValPair> newP = jmod.getObsValAfterTransCh(chain,mod);
		for(ObsValPair p: newP){
			//System.out.println("final val of " + p.getObs().termToString(mod) + " is " + p.getValue().termToString(mod));
			if(p.getObs().termToString(mod,null).equals("getSize()")) assertEquals("",p.getValue().termToString(mod,null),"(d + (d + getSize()))");	
			if(p.getObs().termToString(mod,null).equals("getElementAt(I)")) assertEquals("",p.getValue().termToString(mod,null),"V");
			if(p.getObs().termToString(mod,null).equals("getElementAt(J)")) assertEquals("",p.getValue().termToString(mod,null),"Q");
			if(p.getObs().termToString(mod,null).equals("getElementAt(h)")) assertEquals("",p.getValue().termToString(mod,null),"L");
		}
		
	}//end of testGetObsValAfterTransCh
	
	
	
	

}//end of JmlModuleTester
