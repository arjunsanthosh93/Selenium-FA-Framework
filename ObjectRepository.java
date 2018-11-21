package utility;

public class ObjectRepository {
	
    
 private String ScreenName;
 private String PageName;
 private Integer FieldID;
 private String FieldName;
 private String FieldType;
 private String Property1;
 private String Property1Value;
 private String Property2;
 private String Property2Value;

 
 public ObjectRepository(String ScreenName,String PageName,Integer FieldID,String FieldName,String FieldType,String Property1,String Property1Value,String Property2,String Property2Value) {  
     
     this.ScreenName = ScreenName;  
     this.PageName = PageName;  
     this.FieldID = FieldID;  
     this.FieldName = FieldName;
     this.FieldType = FieldType;  
     this.Property1 = Property1;
     this.Property1Value = Property1Value;
     this.Property2 = Property2;
     this.Property2Value = Property2Value;
   }  


public String getScreenName(){
   return ScreenName;
}
public String getPageName(){
      System.out.println("test worked "+PageName);
   return PageName;
}
public Integer getFieldID(){
   return FieldID;
}
public String getFieldName(){
   return FieldName;
}
public String getFieldType(){
   return FieldType;
}
public String getProperty1(){
   return Property1;
}
public String getProperty1Value(){
   return Property1Value;
}
public String getProperty2(){
	   return Property2;
	}
public String getProperty2Value(){
	   return Property2Value;
	}
public void setScreenname(String ScreenName){
   this.ScreenName=ScreenName;
}
public void setPageName(String PageName){
   this.PageName=PageName;
}
public void setFieldID(Integer FieldID){
   this.FieldID=FieldID;
}
public void setFieldName(String FieldName){
   this.FieldName=FieldName;
}
public void setFieldType(String FieldType){
   this.FieldType=FieldType;
}
public void setProperty1(String Property1){
   this.Property1=Property1;
}
public void setProperty1Value(String Property1Value){
   this.Property1Value=Property1Value;
}
}



