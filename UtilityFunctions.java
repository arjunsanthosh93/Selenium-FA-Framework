package utility;

//import org.apache.bcel.classfile.Method;
//import org.junit.internal.runners.model.EachTestNotifier;
import org.hamcrest.core.IsNull;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.Properties;

import org.apache.poi.hssf.util.HSSFColor.GREEN;
import org.apache.poi.hssf.util.HSSFColor.RED;
//import org.apache.poi.hwpf.usermodel.DateAndTime;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Exception.FilloException;
import Fillo.Connection;
import Fillo.Fillo;
import Fillo.Recordset;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIResults;





public class UtilityFunctions {
	
   

	public static void U_initialSettings() throws Throwable
	{
		Global.apikey=U_getKeyValue(Global.excelpth,Global.execsheet3,"API Key");	
		Global.url=U_getKeyValue(Global.excelpth,Global.execsheet3,"URL");
		Global.projectname=U_getKeyValue(Global.excelpth,Global.execsheet3,"Project Name");
		Global.tplan=U_getKeyValue(Global.excelpth,Global.execsheet3,"Test Plan");
		Global.testlinkflag="false";
		Global.browser=U_getKeyValue(Global.excelpth,Global.execsheet3,"Browser Name");
		
		if (Global.apikey!="")
		{
			Global.testlinkflag="true";
		}
	}
	
	public static String U_getKeyValue(String excelpath,String sheetname, String key) throws Throwable
	{
		Recordset rs=filterWorkSheet(excelpath, "", "", sheetname);	
		while (rs.next())
		{
		 if(rs.getField(Global.execsheet3Key).equalsIgnoreCase(key))
		 {
			 return rs.getField(Global.execsheet3Value);
		 }
		}
		return null;
	}
	
	public static void U_CreateResultFile() throws IOException{
		System.out.println("inside create result file");		
		String resultfilename=Global.resultFileName;
		String datafilenamepath=Global.dataFileName;
		if (resultfilename==null){
			String datafilename=datafilenamepath.substring(datafilenamepath.lastIndexOf("\\")+1, datafilenamepath.length());
			
			resultfilename = "Results_" + datafilename.replace(".XLS","").replace(".xls", "");
			Date dNow = new Date( );
		    SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd hh-mm-ss a");
		    resultfilename=resultfilename + "_" + dateFormat.format(dNow)+".xlsx";
			System.out.println("resultfilename Date: " + resultfilename);
			//Get the result path from data table
			U_Create_Folder(Global.resultpath);
			Global.resultFileName=Global.resultpath + resultfilename; 
			if (Global.mailAttachList==null)
			{
				Global.mailAttachList =new ArrayList<String>();
			}
			Global.mailAttachList.add(Global.resultFileName);
			ExcelFile excelFile=new ExcelFile();
			String[] sheetnamelist={Global.resultsheet1name,Global.resultsheet2name};
			String[][] sheetColumnHeader={
					{"Test Case Name","Status","Start Time","End Time","Duration"},
					{"Step ID","Status","Expected Result","Actual Result","Details"}
					
			};
			
			excelFile.CreateExcelFile(Global.resultpath, resultfilename,sheetnamelist);
			FileInputStream inputStream = excelFile.getInputStream(Global.resultpath, resultfilename);
			Sheet resultTCSheet= excelFile.getSheetToRead(inputStream, resultfilename, "Result Summary");
			U_Set_ResultFileHeaders(resultTCSheet,resultfilename,sheetColumnHeader[0]);
			//resultTCSheet.getWorkbook().close();
			inputStream.close();
			inputStream = excelFile.getInputStream(Global.resultpath, resultfilename);
			Sheet resultSummarySheet= excelFile.getSheetToRead(inputStream, resultfilename, "TC_Result");
			U_Set_ResultFileHeaders(resultSummarySheet,resultfilename,sheetColumnHeader[1]);
			inputStream.close();			
		}
	}
	public static void U_Create_Folder(String resultPath){
		File file = new File(resultPath);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Result Directory is created!");
			} else {
				System.out.println("Failed to create result directory!");
			}
		}
	}

	
    public static void  U_Set_ResultFileHeaders(Sheet ObjExcelSheet,String resultFile,String[] sheetColumnHeader) throws IOException
    {
          
      	CellStyle style = ObjExcelSheet.getWorkbook().createCellStyle();
        style.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font font = ObjExcelSheet.getWorkbook().createFont();
        font.setFontName("Arial");
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setBold(true);
        style.setFont(font);
        Row row=ObjExcelSheet.createRow(0);
    	for (int i = 0; i<=sheetColumnHeader.length-1;i++){
    		row.createCell(i).setCellValue(sheetColumnHeader[i]);
    		ObjExcelSheet.setColumnWidth(i, 8000);
    		row.getCell(i).setCellStyle(style);
  		 }

        FileOutputStream op =new FileOutputStream(Global.resultpath+resultFile);
        ObjExcelSheet.getWorkbook().write(op);
        //ObjExcelSheet.getWorkbook().close();
        
    }
    
    public static Recordset filterWorkSheet(String filepath, String Colname, String criteria, String sheet) throws Throwable   
    {                    
          
    	   Fillo fillo=new Fillo();
           System.out.println(filepath);
           Connection connection=null;
           Recordset recordset=null;
           String strwhereCondition=null;
           String strQuery=null;
		  connection = fillo.getConnection(filepath);
		  if(Colname.length() > 0 && criteria.length() > 0){
			strwhereCondition=" where " + Colname + " ='"+criteria+"'";
			strQuery="Select * from " + sheet + " " + strwhereCondition;
		  }
		  else{
			strQuery="Select * from "+sheet;
		   }
		  recordset=connection.executeQuery(strQuery);
		  System.out.println( recordset.getCount());
		  return recordset;
          }

  
	public static String U_Get_DataTableName(String testcasename) throws Throwable {
		
		System.out.println(testcasename);
		String execsettingpath=Global.datapath + "Execution Settings.xls";
		String strbatchfilename="";
		if (execsettingpath.length() > 0 ){
			//need to complete
			//filter worksheet 
			Recordset recordset=filterWorkSheet(execsettingpath,"TC_Name",testcasename,"TC_BatchFile");
			System.out.println(recordset.getCount());
			while( recordset.next()){
				strbatchfilename=recordset.getField(Global.execsheet2fileName);
				System.out.println(strbatchfilename);
			}
		//	strbatchfilename = sheet.cells(2,2)
			//need to complete
			if(strbatchfilename.length() > 0){
				return strbatchfilename;
			}else{
				return "";
			}
		}
		return "";
		/*If Trim(pathval) <> "" Then
			U_FilterWorkSheet pathval, "TC_BatchFile", strTCName, "TC_Name"
			Set objXL = CreateObject("Excel.Application")
			objXL.Workbooks.Open(Project.Path & "Temp\TC_BatchFile.xls")
			Set sheet=objXL.ActiveWorkbook.Worksheets("Temp_TC_BatchFile")
			strBatchFileName = sheet.cells(2,2)
			Set sheet=nothing
			objXL.Workbooks.close
			objXL.Quit
	    Set objXL = nothing
		End If
		
		If Trim(strBatchFileName) <> "" Then
			U_Get_DataTableName = Trim(strBatchFileName)
		Else
			U_Get_DataTableName = ""
		End If
		return "";*/
	}
	
	public static WebElement FindObject(String property1, String property1value,String property2,String property2value)
    {
        
        System.out.println("FindObject");
        By by; //The by type  can be name ,id ,class name - supported by the Selenium by class
        WebElement element; 
        by = null;
        element = null;
        try 
        {
        Class byClass = Class.forName(By.class.getName());
        java.lang.reflect.Method method1 = byClass.getMethod(property1, String.class);
        By newBy1 = (By) method1.invoke(by, property1value);
        WebDriverWait wait = new WebDriverWait(Global.driver, 30);
       // wait.until(ExpectedConditions.presenceOfElementLocated(newBy1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(newBy1));
        if(!(property2.length()==0))
        {
        	java.lang.reflect.Method method2 = byClass.getMethod(property2, String.class);
            By newBy2 = (By) method2.invoke(by, property2value); // type cast to 'By' type
            wait.until(ExpectedConditions.visibilityOfElementLocated(newBy2));
            element = Global.driver.findElement(newBy1).findElement(newBy2);
        }
        else
        {
            element = Global.driver.findElement(newBy1);
        }
      
        return element;
        }
 catch (ElementNotVisibleException env) //if element not found
        {
        System.out.println(env.getCause());
        throw env;
        }

        catch (Exception genException)//General exception
        {
        System.out.println(genException.getMessage());            
        }
            return element; //return the webelement
        }
	
	
	
	

public static void U_writeResult(ArrayList<Result> result) throws IOException
{
	FileInputStream inputStream=new FileInputStream(Global.resultFileName);
	Workbook workbook = new XSSFWorkbook(inputStream);
	Sheet sheet=workbook.getSheet(Global.resultsheet2name);
	int rowNum=sheet.getLastRowNum()+1;
	int passCount=0;
	int failCount=0;
	Global.testCaseResult="Pass";
	XSSFCellStyle cellstyle = (XSSFCellStyle)sheet.getWorkbook().createCellStyle();
  	XSSFColor myColor = new XSSFColor(Color.decode("#BDBDBD"));
	cellstyle.setFillForegroundColor(myColor);
  	cellstyle.setFillPattern(cellstyle.SOLID_FOREGROUND);
    Font font = sheet.getWorkbook().createFont();
    font.setColor(IndexedColors.DARK_RED.getIndex());
    font.setBold(true);
    cellstyle.setFont(font);
   	Cell cell=sheet.createRow(rowNum).createCell(0);
	cell.setCellValue(Global.testCaseName);
	cell.setCellStyle(cellstyle);
	XSSFCellStyle style;
	XSSFFont fnt; 
	
	for(Result array:result)
	{
		
		rowNum=rowNum+1;
		Row row=sheet.createRow(rowNum);
		row.createCell(0).setCellValue(array.getStepId());
		Cell statuscell = row.createCell(1);
		if(array.getStatus().equalsIgnoreCase("pass")) 
		{
			passCount=	passCount+1;
			style = (XSSFCellStyle)sheet.getWorkbook().createCellStyle();
			fnt = (XSSFFont) sheet.getWorkbook().createFont();
			fnt.setColor(GREEN.index);
		}
		else
		{
			failCount=failCount+1;
			style = (XSSFCellStyle)sheet.getWorkbook().createCellStyle();
			fnt =(XSSFFont) sheet.getWorkbook().createFont();
			fnt.setColor(RED.index);
			Global.testCaseResult="Fail";
		}
		style.setFont(fnt);
		statuscell.setCellStyle(style);
		statuscell.setCellValue(array.getStatus());
		row.createCell(2).setCellValue(array.getExpectedResult());
		row.createCell(3).setCellValue(array.getActualResult());
		row.createCell(4).setCellValue(array.getDetails());
	}
	Row row=sheet.createRow(rowNum+1);
	String string=Global.testCaseName+"-"+Global.testCaseResult+"(Passed-"+passCount+" Failed-"+failCount+")"; 
	row.createCell(0).setCellValue(string);
	U_writeResultSummary(workbook);
	FileOutputStream outputStream=new FileOutputStream(Global.resultFileName);
	workbook.write(outputStream);
}
	
public static void U_writeResultSummary(Workbook workbook)
{
	Sheet sheet=workbook.getSheet(Global.resultsheet1name);
	int rowNum=sheet.getLastRowNum()+1;
	Row row=sheet.createRow(rowNum);
	row.createCell(0).setCellValue(Global.testCaseName);
	XSSFCellStyle cellstyle;
	XSSFColor color;
	if(Global.testCaseResult.equalsIgnoreCase("Pass"))
	{
		cellstyle=(XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		color=new XSSFColor(Color.decode("#32CD32"));
	}
	else
	{
		cellstyle=(XSSFCellStyle) sheet.getWorkbook().createCellStyle();
		color=new XSSFColor(Color.decode("#EE4000"));
	}
	cellstyle.setFillForegroundColor(color);
	cellstyle.setFillPattern(cellstyle.SOLID_FOREGROUND);
  	row.createCell(1).setCellStyle(cellstyle);
  	row.getCell(1).setCellValue(Global.testCaseResult);
  	DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd/MM/yyy hh:mm:ss a");
  	String startTime=Global.startTime.format(formatter).toString();
  	row.createCell(2).setCellValue(startTime);
  	LocalDateTime endTime = LocalDateTime.now();
   	row.createCell(3).setCellValue(endTime.format(formatter).toString());
  	row.createCell(4).setCellValue(U_Get_TestDuration(Global.startTime, endTime));
  	
  	
}
public static String U_Get_TestDuration(LocalDateTime start,LocalDateTime endTime)
{
	Integer seconds=(int) LocalDateTime.from(Global.startTime).until(endTime, ChronoUnit.SECONDS);
	Integer hours= (seconds/60/60);
	Integer minutes=(seconds - ((hours * 60)*60))/60;
	Integer s=seconds - (((hours * 60) * 60) + (minutes * 60));
	String testDuration=hours+"h:"+minutes+"m:"+s+"s";
	return testDuration;
	
}


public static String U_SetObjectValue(WebElement ObjField,String strFieldType,String strFieldValue)

{
 try {
        
       switch  (strFieldType.toUpperCase()) {
                    case "BUTTON" :                                                  
                       ObjField.click();
                       break;
                    case "TEXTBOX" : 
                       ObjField.clear(); //uncomment for wand
                       ObjField.sendKeys(strFieldValue);
                       break;
                    case "TEXTAREA" :
                       ObjField.sendKeys(strFieldValue);
                       break;
                    case "LINK" :
                       ObjField.click();
                       break;
                    case "COMBOBOX" :
                    	ObjField.click();
                    	Thread.sleep(500);
                        Select dropdown = new Select(ObjField);
                    	dropdown.selectByVisibleText(strFieldValue);
                        //ObjField.sendKeys(Keys.ENTER); //uncomment for wand
                        break;
                    case "CHECKBOX" :
                       ObjField.click();
                       break;
                    case "RADIOBUTTON" :
                        ObjField.click();
                        break;                 
                     default:
                    throw new IllegalArgumentException("Invalid fieldType " + strFieldType);

       }
       
 }
        catch (Exception e)
        
        {
              return e.getMessage();
        }
 
              return "";
        }

public static void U_OpenBrowser()
{
	if (isBrowserClosed()==false)
			{
	
	switch(Global.browser.toUpperCase()){
	
		case "FIREFOX":
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"\\geckodriver.exe");
			Global.driver=new FirefoxDriver();
			break;
		case "CHROME":
			
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\chromedriver.exe");
			Global.driver = new ChromeDriver();
		  	break;
		case "IE":
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\IEDriverServer.exe");
		    Global.driver = new InternetExplorerDriver();
		  	break;
	  	default:
	  		throw new IllegalArgumentException("Invalid browser name"+Global.browser);
	}
	
	
	}

	
}
         

public static void CloseBrowser() 
{
	Global.driver.close();
	Global.driver.quit();
}

public static Boolean isBrowserClosed() {
    try 
    {
        Global.driver.getCurrentUrl();
        return true;
    } 
    catch (Exception ex) 
    {
        return false;
    }
}


public static String U_CheckExpectedResult(String testCase,String actualRetVal,Recordset expectRecordSet,Result resultObject) throws FilloException{
	
	if(actualRetVal.equalsIgnoreCase(expectRecordSet.getField(Global.datasheet5ExpectedResult).toString())){
		resultObject.setStatus("Pass");
		resultObject.setActualResult("Return value is " + actualRetVal );
		resultObject.setExpectedResult("Need to return " + expectRecordSet.getField(Global.datasheet5ExpectedResult).toString());
		resultObject.setDetails(expectRecordSet.getField(Global.datasheet5PassDescription).toString());
	}
	else{
		resultObject.setStatus("Fail");
		resultObject.setActualResult("Return value is " + actualRetVal );
		resultObject.setExpectedResult("Need to return " + expectRecordSet.getField(Global.datasheet5ExpectedResult).toString());
		resultObject.setDetails(expectRecordSet.getField(Global.datasheet5FailDescription).toString());
	}
	return (resultObject.getStatus());
}

public static String U_Get_Field_Description(ObjectRepository objectRepository){
	String strMessage="";
	strMessage = "Object : ";
	
	if (objectRepository.getFieldName().length() > 0){
		strMessage =strMessage + objectRepository.getFieldName();
	}
	else{
		strMessage =strMessage + objectRepository.getProperty1Value();
	}
	
	strMessage= strMessage + '\n' + "Object Type : " + objectRepository.getFieldType();
	strMessage= strMessage + '\n' + "Screen : " + objectRepository.getScreenName();

	return strMessage;
}
 public static void sendMail() throws IOException
 {
	 
	 String to = "hanson.j@katalyti.com,jithesh.rs@katalyti.com,ananthu.m@katalyti.com";
	 //String to = "antomanjooran@gmail.com";
	 String host = " smtp.gmail.com";
     Properties props = new Properties();
     props.put("mail.smtp.auth", true);
     props.put("mail.smtp.starttls.enable", true);
     props.put("mail.smtp.host", "smtp.gmail.com");
     props.put("mail.smtp.port", "587");
     Session session = Session.getInstance(props,
             new javax.mail.Authenticator() {
                 protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication("automationteam16@gmail.com","createapassword");
                 }
             });
     try {
    	 
    	 Multipart multipart = new MimeMultipart();
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("automationteam16@gmail.com"));
         message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
         message.setSubject("WAND");
         MimeBodyPart messageBodyPart = new MimeBodyPart();
         messageBodyPart.setText(" Hai,\n\n WAND Execution Result is attached.Please find the attachment.\n\n Thanks\n Automation Testing Team");
         multipart.addBodyPart(messageBodyPart);
                  
         FileDataSource source=null;
         MimeBodyPart mbp2=null;
         for (String array:Global.mailAttachList)
         {
         source =null;
         mbp2=null;
         mbp2=new MimeBodyPart();
         source = new FileDataSource(array);
         mbp2.setDataHandler(new DataHandler(source));
         mbp2.setFileName(new File(array).getName());
        // mbp2.setFileName(array);
         multipart.addBodyPart(mbp2); 
         }
         message.setContent(multipart);
         System.out.println("Sending");
         Transport.send(message);
         System.out.println("Done");

     } catch (MessagingException e) {
         e.printStackTrace();
     }
  
 }
 public static List<WebElement> FindObjects(String property1, String property1value,String property2,String property2value)
 {
               System.out.println(property1);
             
      System.out.println("FindObject");
      By by; //The by type  can be name ,id ,class name - supported by the Selenium by class
      List<WebElement>  element; 
      by = null;
      element = null;
      try 
      {
      Class byClass = Class.forName(By.class.getName());
      java.lang.reflect.Method method1 = byClass.getMethod(property1, String.class);
      By newBy1 = (By) method1.invoke(by, property1value);
      if(!(property2.length()==0))
      {
        java.lang.reflect.Method method2 = byClass.getMethod(property2, String.class);
          By newBy2 = (By) method2.invoke(by, property2value); // type cast to 'By' type
          element = ((WebElement) Global.driver.findElements(newBy1)).findElements(newBy2);
      }
      else
      {
         element = Global.driver.findElements(newBy1);
      }
      return element;
      }
      catch (ElementNotVisibleException env) //if element not found
      {
      System.out.println(env.getCause());
      throw env;
      }
      catch (Exception genException)//General exception
      {
      System.out.println(genException.getMessage());            
      }
          return element; //return the webelement
      }
        
 
 
 
 public static void updateTestResult(String testcaseid) throws TestLinkAPIException
 {
     String build=null;
	 String notes="";
	 String result="";
	 if(Global.testCaseResult.equalsIgnoreCase("pass"))
	 {
		  result= TestLinkAPIResults.TEST_PASSED;
	 }
	 else
	 {
		  result= TestLinkAPIResults.TEST_FAILED;
	 }
	// String result=null;
     TestLinkAPIClient api=new TestLinkAPIClient(Global.apikey, Global.url);
	 api.reportTestCaseResult(Global.projectname,Global.tplan,testcaseid,build,notes,result);

 }
 


   
        

  }
	




