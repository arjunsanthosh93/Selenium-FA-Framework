package driverFunctions;

import org.testng.annotations.Test;

import utility.UtilityFunctions;

public class Main {
	@Test
	 public void driver() throws Throwable  {
		 System.out.println("inside main");
		 UtilityFunctions.U_initialSettings();
		 DFunctions dfunctionsobj=new DFunctions();
		  dfunctionsobj.D_InitTCExecution("", "");
		  //UtilityFunctions.sendMail();
		  //UtilityFunctions.updateTestResult();
		  UtilityFunctions.CloseBrowser();  // Closing the Browser after executing all
	  }

}
