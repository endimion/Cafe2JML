package test;


import static org.junit.Assert.assertEquals;
import model.CafeEquation;
import model.CafeTerm;
import model.FileHelper;
import model.Module;

import org.junit.Test;
public class CompTermTester {

	
	public CompTermTester(){};
	
	@Test
	public void testPrintTermSkipArg(){
		
		FileHelper fh = new FileHelper("src/tests.cafe");
		CafeEquation eq = new CafeEquation();
		Module mod  =new Module();
		
		String line = "ceq right(searchleft(S)) = pivot(S) - 1 if c-searchleft(S) .";
		fh.parseEq(line, mod, eq);
		
		CafeTerm t =  eq.getRightTerm();
		
		assertEquals("",t.getOpName(),"-");
		
	}//end of testPrintTermSkipArg
	
	
	
	
	
}
