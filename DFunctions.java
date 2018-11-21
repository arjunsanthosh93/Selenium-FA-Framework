package driverFunctions;

import Exception.FilloException;
import Fillo.Recordset;
import utility.Global;
import utility.ObjectRepository;
import utility.Result;
import utility.UtilityFunctions;

import java.awt.Frame;
import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import businessFunctions.BusinessFunctions;




/*import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;*/

public class DFunctions {

	BusinessFunctions businessFunctions=new BusinessFunctions();
	
	
	
	
   	public  void D_InitTCExecution(String testcase_name, String batch_name) throws Throwable {
   		System.out.println("inside dfunctions");
   		if (testcase_name.length() == 0 && batch_name.length() == 0 ) {
				D_InitBatchExecution();
		}
		else if (testcase_name.length() == 0  && batch_name.length() > 0){
			if (D_InitTestSuiteExecution(batch_name) == false) {
					//Runner.Halt "No data present in Mapper sheet in the " & Project.Variables.gStrDataFileName
		        System.out.println("D_InitTestSuiteExecution -if staement2");
			}
		}
		else if (testcase_name .length() > 0){
			 System.out.println("D_InitTestSuiteExecution -if staement3 " +Global.resultpath );
			 //Initiate a single Test Case execution
				if (batch_name.length() == 0 ){
					Global.dataFileName= Global.datapath +UtilityFunctions.U_Get_DataTableName(testcase_name);
					
				}
				else{
					Global.dataFileName=Global.datapath +batch_name;
				}
				
	      //Create Excel result file
		   UtilityFunctions.U_CreateResultFile();
	      //Start Test Case execution
	       D_Start_TCExecution(testcase_name);
	       

		}
   		
}
	
	public void D_InitBatchExecution() throws Throwable{
		try
		{
		Recordset recordset=UtilityFunctions.filterWorkSheet(Global.excelpth, Global.execsheet1Excecute, "Yes", Global.execsheet1);
		Boolean blnExecute;
		System.out.println(recordset.getCount());
		int totalrows =recordset.getCount();
		String batchname=null;
		ArrayList <String> batchList = new ArrayList();
		while (recordset.next())
			{
				blnExecute=true;
				batchname=recordset.getField(Global.execsheet1BatchName);
				if(batchname == ""){
					System.out.println( "Batch_Name column in Exec_Setting sheet of Execution Settings.xls is not correct");
				}
				else
				{
					if (!batchList.contains(batchname)){ //avoid duplication of the Batch Name while adding into the batch list
						batchList.add(batchname);
						 D_InitTCExecution("", batchname);
				         Global.resultFileName=null;
					}
					
				}
				//recordset.next();
			}
		recordset.close();
		
		
		}
		catch(FilloException e)	//Error condition if recordset is null
		{
			System.out.println(e.getMessage());
			Global.dataFileName=Global.excelpth;
			Global.testCaseName=Global.excelpth.substring(Global.excelpth.lastIndexOf("\\")+1, Global.excelpth.length());
			Global.startTime=LocalDateTime.now();
			UtilityFunctions.U_CreateResultFile();
			Result resultObject=new Result("1","Fail",e.getMessage());
			ArrayList <Result> arrListResult=new ArrayList <Result>();
			arrListResult.add(resultObject);
			UtilityFunctions.U_writeResult(arrListResult);
		 }

	}



	public boolean D_InitTestSuiteExecution(String strOptionalParam1) throws Throwable  
	{
		try
		{
			String datafilename=Global.datapath+strOptionalParam1;
			Global.dataFileName=datafilename;
			Recordset recordset=UtilityFunctions.filterWorkSheet(datafilename, Global.datasheet2Excecute, "Yes", Global.datasheet2);
			int totalrows =recordset.getCount();
			System.out.println("total rows"+totalrows);		
	    	while(recordset.next())
				{
					String dependency=recordset.getField(Global.datasheet2Dependency);
					if (dependency.length()>0)
					{
						String[] arrDependency=dependency.split(",");
						for(int i=0; i< arrDependency.length;i++)
						{
							System.out.println("Dependency"+arrDependency[i]);
							D_InitTCExecution(arrDependency[i], strOptionalParam1);
						}
					}
					
					D_InitTCExecution(recordset.getField(Global.datasheet2TCName), strOptionalParam1);
				}
			recordset.close();
			UtilityFunctions.CloseBrowser();
		    return true;
		    
		}
		catch(FilloException e)
		{
			Global.testCaseName=Global.dataFileName.substring(Global.dataFileName.lastIndexOf("\\")+1, Global.dataFileName.length());
			Global.startTime=LocalDateTime.now();
			UtilityFunctions.U_CreateResultFile();
			Result resultObject=new Result("1","Fail",e.getMessage());
			ArrayList <Result> arrListResult=new ArrayList <Result>();
			arrListResult.add(resultObject);
			UtilityFunctions.U_writeResult(arrListResult);
			System.out.println(e.getMessage());
			return false;	
		}
	}
	public void D_Start_TCExecution(String testcase_name) throws Throwable{
		System.out.println("D_Start_TCExecution");
		Global.testCaseName=testcase_name;
		if(D_LoadObjectRepository(Global.dataFileName)==true)
		{
			System.out.println(testcase_name);
			D_CheckInputData(testcase_name);
			
					
		}
		
	}
	public void D_CheckInputData(String testCase) throws Throwable {
	    
		System.out.println("inside dcheckinputdata");
        WebElement element;
        Global.startTime=LocalDateTime.now();
        String datafilename=Global.dataFileName;
        ArrayList <Result> arrListResult=new ArrayList <Result>();
        Result resultObject=null;
        try
        {
        resultObject=new Result();
        Recordset recordset=UtilityFunctions.filterWorkSheet(datafilename, Global.datasheet4TCName, testCase, Global.datasheet4);
        Recordset expectResultRecordset=UtilityFunctions.filterWorkSheet(datafilename, Global.datasheet5TCName, testCase, Global.datasheet5);
        System.out.println(expectResultRecordset.getCount());
        int rowCount=recordset.getCount();
        System.out.println(Global.browser);
        UtilityFunctions.U_OpenBrowser();
        if (rowCount>0 )
        {
            while(recordset.next())
               {
            	
                   String returnValue = null;
            	   System.out.println(recordset.getField(Global.datasheet4StepNo).toString());
            	   resultObject=new Result();
            	   resultObject.setModuleName(datafilename.substring(datafilename.lastIndexOf("\\")+1, datafilename.length())); //modulename
            	   resultObject.setStepId(recordset.getField(Global.datasheet4StepNo).toString());
            	   resultObject.setTestCaseName(testCase);  
            	   expectResultRecordset.moveNext();  
            	   Global.testLinkId=recordset.getField(Global.datasheet4TLID);
            	   // Result=new result();
                     if(recordset.getField(Global.datasheet4Field_ID).equalsIgnoreCase("F"))
                     {
                            String fieldValue=recordset.getField(Global.datasheet4Field_Value);
                            String methodName=fieldValue.substring(0, fieldValue.indexOf("("));
                            String arguments=fieldValue.substring(fieldValue.indexOf("(")+1, fieldValue.indexOf(")"));
                            String[] parameters=arguments.split(",");
                            Method[] declaredMethods = BusinessFunctions.class.getDeclaredMethods();
                            if (arguments.length()==0)
			                   {
			                          parameters=null;
			                   }
		                    for (Method declaredMethod : declaredMethods) {
		                        if (declaredMethod.getName().equals(methodName)) {
		                          returnValue=(String) declaredMethod.invoke(businessFunctions,parameters); 
		                          break;
		                        }
		                       }
		                    if(returnValue==null)
		                    {
		                    	resultObject.setStatus("Fail");
	                        	resultObject.setDetails("Function " + methodName + "is not declared in the Business Functions"); 
	                        	arrListResult.add(resultObject); 
	                        	break;
		                    }
		                    else if(returnValue.equalsIgnoreCase("true")||returnValue.equalsIgnoreCase("false"))
		                    {
		                    	System.out.println(expectResultRecordset.getField(Global.datasheet5StepNo));
		                    	System.out.println(expectResultRecordset.getField(Global.datasheet5ExpectedResult));
		                    	if(UtilityFunctions.U_CheckExpectedResult(testCase,returnValue,expectResultRecordset,resultObject).equalsIgnoreCase("Fail")||returnValue.equalsIgnoreCase("false"))
                        	    {
                        	    	arrListResult.add(resultObject); 
                                	break;
                        	    }
		                    }
		                    else//error condition
	                        {
	                        	resultObject.setStatus("Fail");
	                        	resultObject.setDetails("Function " + methodName + " failed due to" + returnValue); //change returnvalue to string type
	                        	resultObject.setActualResult("Function return failed ");
	                        	resultObject.setExpectedResult("Business Function call is failed");
	                        	arrListResult.add(resultObject); 
	                        	break;
	                        }
	                      
                         
                     	}
                     else //field id case
                     {
                       System.out.println(recordset.getField(Global.datasheet4Field_ID));
                       ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(recordset.getField(Global.datasheet4Field_ID)));
                       if(objectRepository!=null)
                       {
                    	   element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                           if(element!=null && element.isDisplayed())
                            {
                            	returnValue = null;
                            	returnValue = UtilityFunctions.U_SetObjectValue(element, objectRepository.getFieldType(), recordset.getField(Global.datasheet4Field_Value).toString());
                            	if(returnValue.length() > 0) //error condition
                            	{
                            		
                            		resultObject.setStatus("Fail");
    	                        	resultObject.setDetails(UtilityFunctions.U_Get_Field_Description(objectRepository));
    	                        	resultObject.setActualResult("Unable to perform an action on the object which was identified " + returnValue );
    	                        	resultObject.setExpectedResult("Perform input action");
    	                        	arrListResult.add(resultObject); 
    	                        	break;
                            		
                            	}else{ //true condition
                            		System.out.println(expectResultRecordset.getField(Global.datasheet5ExpectedResult));
                            		resultObject.setStatus("Pass");
                            		resultObject.setActualResult("Return value is TRUE");
                            		resultObject.setExpectedResult("Need to return " + expectResultRecordset.getField(Global.datasheet5ExpectedResult).toString());
                            		resultObject.setDetails(expectResultRecordset.getField(Global.datasheet5PassDescription).toString());
                            	     }
                            	  
                            }
                            else //element is not existing
                            {
                            	String returnValueMsgDetails=UtilityFunctions.U_Get_Field_Description(objectRepository);
                            	resultObject.setStatus("Fail");
                            	resultObject.setDetails(returnValueMsgDetails); //change return value to string type
                            	resultObject.setActualResult("The element identification failed for FieldID : " + objectRepository.getFieldID().toString());
                            	resultObject.setExpectedResult("Perform object identification");
                            	arrListResult.add(resultObject); 
                            	break;
          				
                            } 
                       }
                       else //object repository doesn't contain the specified object
                       {
                    	resultObject.setStatus("Fail");
                       	resultObject.setDetails("Object Repository sheet doesn't contain the object with field_id "+recordset.getField(Global.datasheet4Field_ID)); //change return value to string type
                       	resultObject.setActualResult("The element identification failed") ;
                       	resultObject.setExpectedResult("Perform object identification");
                       	arrListResult.add(resultObject); 
                       	break;
                       }
                      
                     }
                     
                     arrListResult.add(resultObject);  //adding result object to Arraylist
                 
               }
            // 
        }
	 	}
	
       
        catch(Exception e)
        {
        	
            if(resultObject.getStatus().length()== 0)
            {
                resultObject.setStatus("Fail");
                resultObject.setDetails(e.getMessage()); //change return value to string type
                //System.out.println(e.getCause().getMessage());
            }              

            arrListResult.add(resultObject);
            System.out.println("failed");
        }
	 	finally{
	 		  
	 			  UtilityFunctions.U_writeResult(arrListResult);
	 			 File scrFile = ((TakesScreenshot)Global.driver).getScreenshotAs(OutputType.FILE);
	 			// Now you can do whatever you need to do with it, for example copy somewhere
	 			 FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"\\"+Global.testCaseName+".png"));
	 			 Global.mailAttachList.add(System.getProperty("user.dir")+"\\"+Global.testCaseName+".png");
	 			
	 			 //Global.driver.close(); // Close browser after Completing each test cases  
	 			// Global.driver.quit(); // Quit browser after Completing each test cases
	 			if (Global.testlinkflag.equalsIgnoreCase("true"))
				{
					UtilityFunctions.updateTestResult(Global.testLinkId);
				}
	 		
	 		
	 	}
        
        

}
	
	
	public boolean D_LoadObjectRepository(String filepath) throws Throwable
	{
        try
        {
		Global.hashmap =  new HashMap <Integer, ObjectRepository>();
        Recordset recordset=UtilityFunctions.filterWorkSheet(filepath,"","", Global.datasheet3);
        System.out.println( recordset.getCount());
        while(recordset.next()){
        	
			String ScreenName=recordset.getField(Global.datasheet3ScreenName);
		    String PageName=recordset.getField(Global.datasheet3PageName);
		    Integer FieldId=Integer.parseInt(recordset.getField(Global.datasheet3Fieldid));
		    String FieldName=recordset.getField(Global.datasheet3FieldName);                   
		    String Fieldtype=recordset.getField(Global.datasheet3FieldType);
		    String Property1=recordset.getField(Global.datasheet3Property1);
		    String Property1Value=recordset.getField(Global.datasheet3Property1Value);
		    String Property2=recordset.getField(Global.datasheet3Property2);
		    String Property2Value=recordset.getField(Global.datasheet3Property2Value); 
            ObjectRepository objectRepository= new ObjectRepository(ScreenName,PageName,FieldId,FieldName,Fieldtype,Property1,Property1Value,Property2,Property2Value);
            Global.hashmap.put(FieldId, objectRepository);
            
            }
            recordset.close();
            return true;
        
       }
        catch(Exception e)
        {
        	System.out.println(e.getMessage());
        	System.out.println(e.getCause());
        	System.out.println(e.getLocalizedMessage());
        	System.out.println(e.getStackTrace());
        	Global.startTime=LocalDateTime.now();
        	ArrayList <Result> arrListResult=new ArrayList <Result>();
        	Result resultObject=new Result("1","Fail",e.getMessage()+" error in the " +Global.datasheet2+" sheet");
			arrListResult.add(resultObject);
			UtilityFunctions.U_writeResult(arrListResult);
			return false;
        
        	
        }
	}
}
	
