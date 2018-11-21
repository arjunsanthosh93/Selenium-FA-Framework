package driverFunctions;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import utility.UtilityFunctions;

public class Master {
  @Test
  @Parameters({"testcase_name","batch_name"})
  public void driver(String testcase_name,String batch_name) throws Throwable   {
	  System.out.println("tc_name" + testcase_name);
	  UtilityFunctions.U_initialSettings();
	  DFunctions dfunctionsobj=new DFunctions();
	  dfunctionsobj.D_InitTCExecution(testcase_name,batch_name);
	 // UtilityFunctions.sendMail();
	  UtilityFunctions.CloseBrowser();  // Closing the Browser after executing all
	 
  }
}
