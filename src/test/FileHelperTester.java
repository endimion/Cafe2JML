package test;

import static org.junit.Assert.*;
import model.CafeOperator;
import model.FileHelper;
import model.ModuleClass;

import org.junit.Before;
import org.junit.Test;

public class FileHelperTester {

	FileHelper fh;
	
	
	@Before
	public void setUpTests(){
		fh = new FileHelper("src/tests.cafe");
	}
	
	
	@Test
	public void testFileHelper() {
		//System.out.println(fh.getSize());
		assertNotNull("fh object is not null!!", fh);
	}//end of test
	
	
	@Test
	public void testParseCafeFile(){
		fh.parseCafeFile();
		
		for(ModuleClass mod : fh.modules ){
			String prString = "";
			String extString ="";
			String op ="";
			
			switch(mod.name){
			
				case	"Content" : 			assertEquals("", 1,mod.getNumOfOps());
												break;
				
				case	"Permission" : 			assertEquals("", 7,mod.getNumOfOps());
												break;

				case	"Request" : 			assertEquals("", 4,mod.getNumOfOps());
												break;	
				case	"ConstraintPermission" : assertEquals("", 7,mod.getNumOfOps());
												 break;							
									
				default: 			break;
		
			};
			
			
			
			
			for(String prName : mod.getImportNames()){
				prString = prString + " " + prName;
			}//end of for loop
			
			for(String n : mod.getExtendsName()){
				extString += " " + n;
			}
			
			for(int i= 1 ; i <= mod.getOps().size(); i++){
				
				CafeOperator co =  mod.getOps().get(i-1);
				String arity ="";
				for(String ar:co.getArity()){
					arity += " "+ar;
				}
				
				op = op + " "+ i +" " + co.getName() + " : " + arity + " -> " + co.getSort();
			}//end of for loop
			
			
			System.out.println(mod.name + 
					" with sort "+ mod.getClassSort()+" which imports" + prString + " " + "and extends" + extString);
			System.out.println("and has  " + mod.getNumOfOps() +" operators "+ op);
			
		}//end of for loop
		
	}//end of testParseCafeFile
	
	
	
	

}//end of FileHelperTester
