package test;

import static org.junit.Assert.*;
import model.StringHelper;

import org.junit.Test;

public class StringHelperTester {

	
	
	
	
	
	@Test
	public void testReplaceSpecialChars(){
		
		String res = StringHelper.replaceSpecialChars("op belongs(_,_)");
		assertEquals("",res,"op belongs()");
		
		res =StringHelper.replaceSpecialChars("ops belongs(_,_)");
		res= StringHelper.replaceSpecialChars("op belo(_,_)");
		res=StringHelper.replaceSpecialChars("op belongs");
		
		res =StringHelper.replaceSpecialChars("op belongs4(_,_,_,_)");
		assertEquals("",res,"op belongs4()");
		
		res =StringHelper.replaceSpecialChars("op _,_");
		assertEquals("",res,"op comma");
		
		res =StringHelper.replaceSpecialChars("op _,_,_,_,_");
		assertEquals("",res,"op comma");
		
		res =StringHelper.replaceSpecialChars("op _&_");
		assertEquals("",res,"op add");
		
		res =StringHelper.replaceSpecialChars("op _&_&_&_&_");
		assertEquals("",res,"op add");
		
		res =StringHelper.replaceSpecialChars("op _&&_");
		assertEquals("",res,"op add");
	}//testReplaceSpecialChars
	
	
	
	
	
	
}
