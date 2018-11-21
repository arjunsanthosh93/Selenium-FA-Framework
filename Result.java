package utility;

public class Result {
       private String ModuleName;
       private String TestCaseName;
       private String StepId;
       private String Status;
       private String ExpectedResult;
       private String ActualResult;
       private String Details;
       
       public Result() {  
                     
                this.ModuleName = "";  
                this.TestCaseName = "";  
                this.StepId = "";  
                this.Status = "";
                this.ExpectedResult = "";  
                this.ActualResult = "";
                this.Details = "";
       }  
       
       public Result(String StepId,String Status, String Details) {  
           
           this.TestCaseName = Global.testCaseName;  
           this.StepId = StepId;  
           this.Status = Status;
           this.Details =Details;
  }  

       public String getModuleName(){
              return ModuleName;
       }
       public String getTestCaseName(){
              return TestCaseName;
       }
       public String getStepId(){
              return StepId;
       }
       public String getStatus(){
              return Status;
       }
       public String getExpectedResult(){
              return ExpectedResult;
       }
       public String getActualResult(){
              return ActualResult;
       }
       public String getDetails(){
              return Details;
       }

       public void setModuleName(String ModuleName){
              this.ModuleName=ModuleName;
       }
       public void setTestCaseName(String TestCaseName){
              this.TestCaseName=TestCaseName;
       }
       public void setStepId(String StepId){
              this.StepId=StepId;
       }
       public void setStatus(String Status){
              this.Status=Status;
       }
       public void setExpectedResult(String ExpectedResult){
              this.ExpectedResult=ExpectedResult;
       }
       public void setActualResult(String ActualResult){
              this.ActualResult=ActualResult;
       }
       public void setDetails(String Details){
              this.Details=Details;
       }
}

