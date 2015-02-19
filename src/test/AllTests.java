package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FileHelperTester.class, TermParserTester.class, JmlGeneratorTester.class ,
	StringHelperTester.class, JmlModuleTester.class})



public class AllTests {

} 

