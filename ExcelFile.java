package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.*;


public class ExcelFile {
	@SuppressWarnings("resource")
	
	public FileInputStream getInputStream(String filePath,String fileName ) throws FileNotFoundException{
		//Create a object of File class to open xlsx file
		File file =	new File(filePath+fileName);
		//Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		return inputStream;
	}
	public Sheet getSheetToRead(FileInputStream inputStream ,String fileName,String sheetName) throws IOException{
	
		Workbook workbook = null;
		//Find the file extension by spliting file name in substing and getting only extension name
		String fileExtensionName = fileName.substring(fileName.indexOf("."));
		//Check condition if the file is xlsx file
		if(fileExtensionName.equals(".xlsx")){
		//If it is xlsx file then create object of XSSFWorkbook class
			workbook = new XSSFWorkbook(inputStream);
		}
		//Check condition if the file is xls file
		else if(fileExtensionName.equals(".xls")){
		//If it is xls file then create object of XSSFWorkbook class
			workbook = new HSSFWorkbook(inputStream);
		}
		//Read sheet inside the workbook by its name
		Sheet  sheet = workbook.getSheet(sheetName);
		return sheet;	
	}
	public void CreateExcelFile(String filePath,String fileName, String[] sheetListName) throws IOException{
		//Create blank workbook
		Workbook workbook =null;
	      workbook = new XSSFWorkbook(); 
	     // workbook.setHidden(true);
	      //Create a blank sheet
	      for(int i=0;i<sheetListName.length;i++){
	    	  Sheet spreadsheet = workbook.createSheet(sheetListName[i]);
	      }
	  	FileOutputStream out =new FileOutputStream(new File(filePath+fileName));
	    try {
			workbook.write(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
