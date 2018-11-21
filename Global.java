package utility;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


import org.openqa.selenium.WebDriver;

public class Global {
	
	//Constants
	
	public static String resultpath=System.getProperty("user.dir") + "\\Result\\";
    public static String datapath=System.getProperty("user.dir")+"\\DataTable\\";
    public static String resultsheet1name="Result Summary";
    public static String resultsheet2name="TC_Result";
    public static String excelpth=System.getProperty("user.dir")+"\\DataTable\\Execution Settings.xls";
    //data sheet names
    public static String execsheet1="Exec_Settings";
    public static String execsheet2="TC_BatchFile";
    public static String execsheet3="Initial_Settings";
    public static String datasheet1="App_Settings";
    public static String datasheet2="Mapper";
    public static String datasheet3="ObjectDetails";
    public static String datasheet4="InputData";
    public static String datasheet5="ExpectedResult";
    //execsheet1 column headers
    public static String execsheet1BatchName="Batch_Name";
    public static String execsheet1Excecute="Exe";
    //execsheet2 column headers
    public static String execsheet2TCName="TC_Name";
    public static String execsheet2fileName="Batch_FileName";
    //execsheet3 column headers
    public static String execsheet3Key="KeyName";
    public static String execsheet3Value="KeyValue";
    // datasheet2 column headers
    public static String datasheet2TCName="TestCaseName";
    public static String datasheet2Excecute="Exe";
    public static String datasheet2Dependency="Dependency";
    
    // datasheet3 column headers
    public static String datasheet3ScreenName="ScreenName";
    public static String datasheet3PageName="PageName";
    public static String datasheet3Fieldid="FieldId";
    public static String datasheet3FieldName="FieldName";
    public static String datasheet3FieldType="FieldType";
    public static String datasheet3Property1="Property1";
    public static String datasheet3Property1Value="Property1Value";
    public static String datasheet3Property2="Property2";
    public static String datasheet3Property2Value="Property2Value";
    
    //datasheet4 column headers
    public static String datasheet4TLID="TestLinkID";
    public static String datasheet4TCName="TestCaseID";
    public static String datasheet4StepNo="TC_SLNO";
    public static String datasheet4Field_ID="Field_ID";
    public static String datasheet4Field_Value="Field_Value";
    
    //datasheet5 column headers
    
    public static String datasheet5TCName="TestCaseID";
    public static String datasheet5StepNo="TC_SLNO";
    public static String datasheet5ExpectedResult="ExpectedResult";
    public static String datasheet5PassDescription="Pass_Description";
    public static String datasheet5FailDescription="Fail_Description";
    
     
    
    //Global Variables
    public static String testCaseResult;
    public static String testCaseName;
    public static String testLinkId;
    public static LocalDateTime startTime;
    public static String dataFileName;
    public static String resultFileName;
    public static WebDriver driver;
    public static ArrayList<String> mailAttachList;
    public static HashMap<Integer, ObjectRepository> hashmap;
    public static String apikey;
    public static String url;
    public static String projectname;
    public static String tplan;
    public static String testlinkflag;
    public static String browser;
  
}
