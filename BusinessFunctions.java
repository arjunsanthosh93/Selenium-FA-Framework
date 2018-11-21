package businessFunctions;


import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;

import utility.Global;
import utility.ObjectRepository;
import utility.UtilityFunctions;

public class BusinessFunctions {
	
	public String navigate(String url) throws Throwable 
	{
		try
		{
		Global.driver.get(url);
		Global.driver.manage().window().maximize();
		
		//setCount("16","3" );
	    return "true";
		}
		catch(Exception e)
		{
			
			return (e.getLocalizedMessage());
		}
     }
	
	public String setCombobox(String id1,String id2, String value) throws InterruptedException
	{
		try
		{
		Thread.sleep(4000); 
		 ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
		 WebElement combobox=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		 if (combobox!=null && combobox.isDisplayed())
		 {   
			 combobox.click();
			 combobox.clear();
			 combobox.sendKeys(value);
			 Thread.sleep(2000);
			 objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));
			 WebElement listbox=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
			 if(listbox!=null )//&& listbox.isDisplayed())
			 {
				 combobox.sendKeys(Keys.RETURN);
				 return "true";
				
			 }
			 else
			 {
				 return "false";
			 }
		 }
		 else
		 {
			 System.out.println("Combobox not exists");
			 return "Combobox with id "+id1+" not exists";
		 }
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
		
		 
	}
	
	
	public String setRoundTripDate(String id1,String id2,String id3,String id4,String id5,String fromdate,String todate) throws Throwable
	{

		try
		{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date1=LocalDate.now();
	    LocalDate date2 = LocalDate.parse(fromdate, formatter);
	    LocalDate date3 = LocalDate.parse(todate, formatter);
	    String returnValue=setDate(id1,id2,id3,id4,date1,date2);
		 if(returnValue.equalsIgnoreCase("true"))
		 {
	       return(setDate(id5,id2,id3,id4,date2,date3));
		 }
		 else
		 {
			 return returnValue;
		 }
	    }
		catch(DateTimeParseException e)
		{
			return "Date should be in yyy-mm-dd format ";
		}
	}
	public String setCheckInandOutDate(String id1,String id2,String id3,String id4,String id5,String fromdate,String todate) throws Throwable
	{

		try
		{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date1=LocalDate.now().plusDays(2);
	    LocalDate date2 = LocalDate.parse(fromdate, formatter);
	    LocalDate date3 = LocalDate.parse(todate, formatter);
	    String returnValue=setDate(id1,id2,id3,id4,date1,date2);
		 if(returnValue.equalsIgnoreCase("true"))
		 {
	       return(setDate(id5,id2,id3,id4,date2,date3));
		 }
		 else
		 {
			 return returnValue;
		 }
	    }
		catch(DateTimeParseException e)
		{
			return "Date should be in yyy-mm-dd format ";
		}
	}
	public String setDate(String id1,String id2,String id3,String id4,LocalDate date1,LocalDate date2) throws Throwable
	{
		try
		{
		   ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
		   WebElement calender=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	       calender.click();
	       Integer monthdiff=(int) LocalDate.from(date1).until(date2, ChronoUnit.MONTHS);
	       if(date2.isAfter(date1))
		   {
        	 if(date2.getDayOfMonth()<date1.getDayOfMonth())
	         {
        		 monthdiff=monthdiff+1;  
	         }
        	 objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));	
        	 for (int i=0;i<monthdiff;i++)
        	 {
				WebElement nextMonth =UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
				if (isElementEnabled(nextMonth))
					{ nextMonth.click();}
			 }
	       }
		   else
		   {
			 if(date2.getDayOfMonth()>date1.getDayOfMonth())
			 {
			     monthdiff=monthdiff-1;  
		     }
			if (monthdiff<0)
			{
				objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id3));
				WebElement previousMonth =UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
				if (isElementEnabled(previousMonth))
				{ return "true";}
				else
				{
					return "false";
				}
			}
		   }
	        objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id4));
	        Thread.sleep(1000);
	        WebElement dateWidget = UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	        Integer day=date2.getDayOfMonth();
	        List<WebElement> columns=dateWidget.findElements(By.tagName("td"));  
	        for (WebElement cell: columns){  
	        	if (cell.getText().equals(day.toString())){  
	        		if(isElementEnabled(cell))
	    	  	    {
	    	  	    	cell.findElement(By.linkText(day.toString())).click();
	    	  	    	return "true";
	    	  	    	
	    	  	    }
	    	  	    else
	    	  	    {
	    	  	    	return "false";
	    	  	    	 
	    	  	    }
	    	  	
	    	  	   }  
	        	 
        	}
	        return "invalid date";
	        
		}
		
		catch(NullPointerException e)
		{
			return "Object not found";
		}
	}
	public String setCount(String id1,String count)
	{
		try
		{
		ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
	    WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	    List<WebElement> list=element.findElements(By.tagName("a"));
	    for (WebElement a: list){  
        	if (a.getText().equals("+")){ 
        		for(int i=1; i <Integer.parseInt(count);i++)
        		{
        			a.click();  
        		}
    	  	    
    	  	    break;  }  
        	}
	    return "true";
	    }
		catch(Exception e)
		{
			return(e.getMessage());
		}
		
	}
	public String selectFromDropdown(String id1,String id2,String value)
	{
		try{
		ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
	    WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	    element.click();
	    objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));
	    WebElement dropdown=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	    List<WebElement> list=dropdown.findElements(By.tagName("a"));
	    for (WebElement a: list){  
        	if (a.getText().equals(value))
        	{ 
        		a.click();  
        	    break; 
        	}  
        }
	    return "true";
		}
		catch(Exception e)
		{
			return(e.getMessage());
		}
	}
	public String clickifExist(String id1,String id2)
	{
		try
		{
			ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
		    WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		    if (element!=null && element.isDisplayed())
		    {
		    	System.out.println(element.getTagName());
		    	if(element.getTagName().equalsIgnoreCase("iframe"))
		    	{
		    		Global.driver.switchTo().frame(element);	
		    	}
		    	objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));
			    element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
			    if (element!=null)
			    {
			     element.click();
			     Global.driver.switchTo().defaultContent();
			    }
			    else
			    {
			    	return "false";
			    }
			   
		    }
		    return "true";
		}
		catch(Exception e)
		{
			return(e.getMessage());
		}
	}
	public static Boolean isElementEnabled(WebElement element) {
	    String className = element.getAttribute("class");
	    return !className.contains("disabled");
	}
	
	public static String scrollintoView(String id)
	{
	try
	{
	ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id));
	Thread.sleep(2000);
	WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	((JavascriptExecutor) Global.driver).executeScript("arguments[0].scrollIntoView(true);", element);
	//WebElement element1=Global.driver.findElement(By.className("widget_tabs_info"));
	//Global.driver.findElement(By.partialLinkText("Hotels")).click();
	return "true";
	}
	catch(Exception e)
	{
		return (e.getMessage());
	}
	
	}
	public static String wait(String timeMS){
        try {
               long time = Long.parseLong(timeMS);
               Thread.sleep( time);
               System.out.println("wait function");
               return "true";
        } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               return e.getMessage();
               
        }
     
 }
	public String GetWindowHandle() 
    {
           try
           {
           
                  for(String winHandle : Global.driver.getWindowHandles()){
                      Global.driver.switchTo().window(winHandle);
                  }
           return "true";
           }
           catch(Exception e)
           {
                  
                  return (e.getLocalizedMessage());
           }
     }
  


	   public static String selectFirstValue(String id1)
       {
		   ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
		   WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		   element.click();
		   element.sendKeys(Keys.ARROW_DOWN);
		   //element.sendKeys(Keys.ARROW_DOWN);
		   element.sendKeys(Keys.ENTER);
		   
		   return "true";
       }
	   public static String selectSecondValue(String id1)
       {
		   ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
		   WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		   element.click();
		   element.sendKeys(Keys.ARROW_DOWN);
		   element.sendKeys(Keys.ARROW_DOWN);
		   //element.sendKeys(Keys.ARROW_DOWN);
		   element.sendKeys(Keys.ENTER);
		   
		   return "true";
       }
	   public static String setInternalCode(String id1,String id2,String value) throws Throwable
       {
		   ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
		   WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		   element.click();
		   Thread.sleep(3000);
		   element.click();
		   objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));
		   element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		   element.sendKeys(value);
		   Thread.sleep(3000);
		   //element.sendKeys(Keys.ARROW_DOWN);
		   element.sendKeys(Keys.ENTER);
		   
		   return "true";
       }
	   public static String verifynSubmit(String id1,String id2,String value) throws Throwable
       {
		   ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
		   WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		   String totalHours=element.getText().substring(15, 17);
		   if(totalHours.equalsIgnoreCase(value))
		   {
			    objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));
			    element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		        File scrFile = ((TakesScreenshot)Global.driver).getScreenshotAs(OutputType.FILE);
		        String filename=System.getProperty("user.dir")+"\\"+Global.testCaseName+"_total_hours.png";
	 	// Now you can do whatever you need to do with it, for example copy somewhere
	 		    FileUtils.copyFile(scrFile, new File(filename));
	 		    Global.mailAttachList.add(filename);
			    element.click();
			    return "true";
		   }
		   else
		   {
			   return "verification failed";
		   }
		   
		  
       }
	   public static String setPassengerDetails(String firstNameid,String lastNameid,String ageid,String maleGenderid,String femaleGenderid,String passengerdetails){
           try{
                  String listsplit[]=passengerdetails.split("\\:");
                  
                  String Names[]=new String[listsplit.length];
                  String Gender[]=new String[listsplit.length];
                  String Age[]=new String[listsplit.length];
                  for(int i=0;i<listsplit.length;i++){
                        Names[i]=listsplit[i].substring(0, listsplit[i].indexOf('-'));
                        Gender[i]=listsplit[i].substring(listsplit[i].indexOf('-')+1,listsplit[i].indexOf('-')+2);
                        Age[i]=listsplit[i].substring(listsplit[i].lastIndexOf('-')+1);
                  }
                  
                  ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(firstNameid));
                  List<WebElement> firstNameElements=UtilityFunctions.FindObjects(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  String firstNameType=objectRepository.getFieldType();
                  
                  objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(lastNameid));
                  List<WebElement> lastNameElements=UtilityFunctions.FindObjects(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  String lastNameType=objectRepository.getFieldType();
                  
                  objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(ageid));
                  List<WebElement> ageElements=UtilityFunctions.FindObjects(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  String ageType=objectRepository.getFieldType();
                  
                  objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(maleGenderid));
                  List<WebElement> maleElements=UtilityFunctions.FindObjects(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  String maleType=objectRepository.getFieldType();
                  
                  objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(femaleGenderid));
                  List<WebElement> femaleElements=UtilityFunctions.FindObjects(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  String femaleType=objectRepository.getFieldType();
                               
               for (int i = 0; i < firstNameElements.size(); i++){
                  if(listsplit[i].indexOf(' ') > 0){
                         UtilityFunctions.U_SetObjectValue(firstNameElements.get(i),firstNameType, Names[i].substring(0, listsplit[i].indexOf(' ')));
                         UtilityFunctions.U_SetObjectValue(lastNameElements.get(i),lastNameType, Names[i].substring(listsplit[i].indexOf(' ')+1));
                  }else{
                         UtilityFunctions.U_SetObjectValue(firstNameElements.get(i),firstNameType, Names[i]);
                  }
                  UtilityFunctions.U_SetObjectValue(ageElements.get(i),ageType, Age[i]);
                  if(Gender[i].equalsIgnoreCase("F")){
                         UtilityFunctions.U_SetObjectValue(femaleElements.get(i),femaleType, "");
           
                  }else{
                         UtilityFunctions.U_SetObjectValue(maleElements.get(i),maleType, "");
              
                  }
               
                  }
                  return "true";
           }
           catch(Exception e)
           {
                  return (e.getMessage());
           }
    }
	   public String BookOnline(String id1)
       {
              try
              
              {
                     ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
                  WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  List<WebElement> columns=element.findElements(By.tagName("input"));  
           
           for (WebElement cell: columns){  
               
                     if(isElementEnabled(cell))
                  {
                           if (cell.getAttribute("value").equalsIgnoreCase("Book Online"))
                     
                           {
                                  cell.click();
                                  break;
                           }
                     } 
               
              
                  }
          
           
           return "true";
              }
       catch(Exception e)
       {
              return "Object not found";
       }

       }
	   public String IsWarningDisplayed(String id1)
       {
              try
              
              {      String msg="";
                  ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
                  WebElement msgbox=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),"","");
                  
                 if ( msgbox!=null && msgbox.isDisplayed())
                     { 
                        msg= msgbox.getText();
                        System.out.println(msg);
                        return "false";
                     }
                 else
                        
                        return "true";
              }
              
              catch (Exception e)
              {
                     return e.getMessage();
              }
       }



	   public String hitEnter(String id1)
       {
              try
              
              {     
            	  ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
                  WebElement txtbox=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),"","");
                  txtbox.sendKeys(Keys.ENTER);
                  return "true";
                 
              }
              
              catch (Exception e)
              {
                     return e.getMessage();
              }
       }
	
	   public String enableElement(String id1)
       {
              try
              
              {     
            	  ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
            	  String property=objectRepository.getProperty1Value();
            	  JavascriptExecutor executor = (JavascriptExecutor)Global.driver;
            	  executor.executeScript("document.getElementById('"+property+"').style.display='inline-block';");
                  return "true";
                 
              }
              
              catch (Exception e)
              {
                     return e.getMessage();
              }
       }
	   
	   public String mouseOvernClick(String id1,String id2)
       {
              try
              
              {     
            	  ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
                  WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  Actions actions= new Actions(Global.driver);
                  actions.moveToElement(element).perform();
                  objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));
                  element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  actions.moveToElement(element).click().perform();
                  return "true";
                 
              }
              
              catch (Exception e)
              {
                     return e.getMessage();
              }
       }
	   
	   public String setExpiry(String id1,String data)
       {
              try
              
              {     
            	  ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id1));
                  WebElement cmbbox=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
                  Thread.sleep(2000);
                  cmbbox.click();
                  cmbbox.sendKeys(data);
                  cmbbox.sendKeys(Keys.TAB);
                  Thread.sleep(1500);
                  return "true";
                 
              }
              
              catch (Exception e)
              {
                     return e.getMessage();
              }
       }
	
	 //------------------------------------FOURTH AMBIT----------------------------------------//
	   
	   /*  ==================================================================================================================================
		   Function Name			  :	verifyElement
		   'Description			      :	Function to verify whether the element is enabled or not
		   'Function Parameter	      :	
		   'Date Created		      : 11th Jan 2017
		   'Date Modified			  :
		   '==================================================================================================================================*/ 
	       
	   public String verifyElement(String id1)
	       	{
	              try
	              
	              {     
	            	  ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
	                  WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	                  if(element.isEnabled())
	                  {
	                  return "true";
	                  }
	                  else
	                  {
	                  return "false";  
	                  }
	              }
	              
	              catch (Exception e)
	              {
	                     return e.getMessage();
	              }
	       }
	     
	      
	       public String verifyPost(String id1,String data)
	     {
	     	 try{
	     		 ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
	     		// WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	     		// List<WebElement> list=element.findElements(By.partialLinkText("Anto Manjooran"));
	     		 WebElement list=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	     		 //System.out.println(list.size());
	     		 String actualdata=list.findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).findElement(By.xpath("..")).getText().toUpperCase();
	     		 String[] expData=data.split("#");
	     		 for(int i=0; i<expData.length;i++)
	     		 {
	     			 if(actualdata.contains(expData[i].toUpperCase()))
	     			 {
	     				continue; 
	     			 }
	     			 else
	     			 {
	     				 System.out.println(expData[i]+" doesnot contains on the post");
	     				 return "false";
	     			 }
	     		 }
	     		 return "true";
	     	 }
	     	   
	       catch (Exception e)
	       {
	    	   System.out.println(e.getMessage());   
	    	   return "false";
	       }
	     }
	    
	       public String uploadFile1(String id1,String file) {
			   	  try {
			   		  ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
			   		  String property=objectRepository.getProperty1Value();
			   		  JavascriptExecutor executor = (JavascriptExecutor)Global.driver;
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.display='inline-block';");
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.visibility='visible';");
			       	  executor.executeScript("document.getElementById('"+property+"').click();");
			       	  Runtime.getRuntime().exec(System.getProperty("user.dir") +"\\tools\\FileUpload1.exe "+System.getProperty("user.dir")+"\\tools\\"+file);
			       	   return "true";
			   	  	} catch (Exception e) {
			   	  		System.out.println(e.getMessage());
			   	  		return "false";
			   	} 
			   	   
			    }
	   	   
	       public String uploadFile2(String id1,String file) {
			   	  try {
			   		  ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
			   		  String property=objectRepository.getProperty1Value();
			   		  JavascriptExecutor executor = (JavascriptExecutor)Global.driver;
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.display='inline-block';");
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.visibility='visible';");
			       	  executor.executeScript("document.getElementById('"+property+"').click();");
			       	  Runtime.getRuntime().exec(System.getProperty("user.dir") +"\\tools\\FileUpload2.exe "+System.getProperty("user.dir")+"\\tools\\"+file);
			       	   return "true";
			   	  	} catch (Exception e) {
			   	  		System.out.println(e.getMessage());
			   	  		return "false";
			   	} 
			   	   
			    }
	   	  
	       
	       public String uploadResume(String id1,String file) {
			   	  try {
			   		  ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
			   		  String property=objectRepository.getProperty1Value();
			   		  JavascriptExecutor executor = (JavascriptExecutor)Global.driver;
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.display='inline-block';");
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.visibility='visible';");
			       	  executor.executeScript("document.getElementById('"+property+"').click();");
			       	  Runtime.getRuntime().exec(System.getProperty("user.dir") +"\\tools\\ResumeUpload.exe "+System.getProperty("user.dir")+"\\tools\\"+file);
			       	   return "true";
			   	  	} catch (Exception e) {
			   	  		System.out.println(e.getMessage());
			   	  		return "false";
			   	} 
			   	   
			    }
	       
	       
	       public String uploadVideo(String id1,String file) {
			   	  try {
			   		  ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
			   		  String property=objectRepository.getProperty1Value();
			   		  JavascriptExecutor executor = (JavascriptExecutor)Global.driver;
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.display='inline-block';");
			       	  executor.executeScript("document.getElementById('"+property+"').parentElement.style.visibility='visible';");
			       	  executor.executeScript("document.getElementById('"+property+"').click();");
			       	  Runtime.getRuntime().exec(System.getProperty("user.dir") +"\\tools\\VideoUpload.exe "+System.getProperty("user.dir")+"\\tools\\"+file);
			       	   return "true";
			   	  	} catch (Exception e) {
			   	  		System.out.println(e.getMessage());
			   	  		return "false";
			   	} 
			   	   
			    }
	   
	       
	     public String uploadFile3(String id1,String file) {
		   	  try {
		   		ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
		   Actions actions= new Actions(Global.driver);
		   		WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());

               actions.moveToElement(element).sendKeys("user.dir"+"\\tools\\"+file).perform();
               
                return "true";
		   	  	} catch (Exception e) {
		   	  		System.out.println(e.getMessage());
		   	  		return "false";
		   	} 
		   	 
		    }
	     
	     
	   
	     public String UserTag(String id1,String tag) {
		   	  try {
		   		ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
		   		
		   		WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
		   		Actions actions= new Actions(Global.driver);
		   		actions.click(element).sendKeys(tag).perform();
               return "true";
		   	  	} catch (Exception e) {
		   	  		System.out.println(e.getMessage());
		   	  		return "false";
		   	} 
		   	 
		    }
	     
	     
	     
	     public String tinyMCE(String id1,String id2,String file) 
	     {
	    	 
	    	 try {
	   
	    		 
	    		    Global.driver.switchTo().frame(id1);
	    		    
	    		    ObjectRepository objectRepository=(ObjectRepository)Global.hashmap.get(Integer.parseInt(id2));
	    		    WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	    		 
                 element.sendKeys(file);
                 return "true";
	   
		   	  	} catch (Exception e) {
		   	  		System.out.println(e.getMessage());
		   	  		return "false";
		   	} 
		   	
	     }
	    	 public String enterTextAutoCompleteField(String id1, String sText) {
	       	  try {
	       		  ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
	   	          WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	   	          element.clear();
	   	          element.sendKeys(sText);
	   	          Thread.sleep(5000);
	   	          element.sendKeys(Keys.DOWN);
	   	          element.sendKeys(Keys.ENTER);          
	   	    	  return "true";
	       	  	} catch (Exception e) {
	       	  		System.out.println(e.getMessage());
	       	  		return "false";
	   		} 
	       	   
	          }
	    	 
	    	 
	    	 public String verifyPageTitle(String ExpectTitle) {
		       	  
		       		  String actualTitle=Global.driver.getTitle();      
		       		  if(actualTitle.equals(ExpectTitle))
		   	    	  {
		       		  return "true";
		   	    	  }
		   	    	  else
		   	    	  {
					return "false";
		   	    	  }
	    	 }
	    	 
	    	 
	    	 public String verifyTextInElement(String id1, String text){
	        	 try {
	        		wait("5000");
	        		 ObjectRepository objectRepository=Global.hashmap.get(Integer.parseInt(id1));
	    	         WebElement element=UtilityFunctions.FindObject(objectRepository.getProperty1(), objectRepository.getProperty1Value(),objectRepository.getProperty2(),objectRepository.getProperty2Value());
	    	         if(element.getText().contains(text)){
	    	        	 return "true";
	    	         }
	    	         
	    	         else{
	    	      
	    	        	 return "false";
	    	         }
	        		
	    		} catch (Exception e) {
	    			return e.getMessage();
	    		}
	         } 	        
	     



}
	
