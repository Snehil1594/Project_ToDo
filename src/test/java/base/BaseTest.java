package test.java.base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import main.java.Util.ExtentManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.By.*;

public class BaseTest {

    public WebDriver driver;
    public Properties prop;
    public ExtentReports rep = ExtentManager.getInstance();
    public ExtentTest test;
    public SoftAssert sa = new SoftAssert();


    public void openBrowser(String bType)  {

        if (prop==null){
            prop = new Properties(); //Code to get data from application.properties file
            try {
                FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//application.properties");
                prop.load(fs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(bType.equals("Chrome")) // Open Browser Either Chrome or IE
        {
            System.setProperty("webdriver.chrome.driver", prop.getProperty("chromedriver_exe"));
            driver = new ChromeDriver();
        }
        else if((bType.equals("IE")))
        {
            System.setProperty("webdriver.ie.driver", prop.getProperty("iedriver_exe"));
            driver = new InternetExplorerDriver();
        }
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize() ; // To Maximize the browser

    }

    public void navigate(String urlKey) {
        driver.get(urlKey);
    }

    /*Reusable Function to getElements by passing locatorKey available in Properties file*/
    public WebElement getElement(String locatorKey){
        WebElement e = null;
        try {
            if (locatorKey.endsWith("_id")) {
                e = driver.findElement(id(prop.getProperty(locatorKey)));
            } else if (locatorKey.endsWith("_name")) {
                e = driver.findElement(name(prop.getProperty(locatorKey)));
            } else if (locatorKey.endsWith("_xpath")) {
                e = driver.findElement(xpath(prop.getProperty(locatorKey)));
            } else if (locatorKey.endsWith("_css")) {
                e = driver.findElement(cssSelector(prop.getProperty(locatorKey)));
            }else if (locatorKey.endsWith("_class")) {
                e = driver.findElement(className(prop.getProperty(locatorKey)));
            }
            else {
                //reportFailure("Locator is not correct - " + locatorKey);
                Assert.fail("Locator is not correct - " + locatorKey);
            }

        }catch (Exception ex)     /*fail the test and report the error*/
        {
            //reportFailure(ex.getMessage());
            ex.printStackTrace();
            Assert.fail("Failed the test - "+ex.getMessage());
        }
        return e;
    }

    public void click(String locatorKey){
        getElement(locatorKey).click();
    }

    public void type(String locatorKey, String data){
        getElement(locatorKey).sendKeys(data);
    }

    public void implicitwait(int time){
        driver.manage().timeouts().implicitlyWait(time,TimeUnit.SECONDS);
    }

    public void terminateBrowser() {
        driver.close();
        Reporter.log("Driver is closed after testing");
    }



    /*-----------------------------------------------Validation Functions--------------------------------------------*/
    public boolean isElementPresent(String locatorKey) throws IOException {
        List<WebElement> elementList=null;
        if(locatorKey.endsWith("_id"))
            elementList = driver.findElements(id(prop.getProperty(locatorKey)));
        else if(locatorKey.endsWith("_name"))
            elementList = driver.findElements(name(prop.getProperty(locatorKey)));
        else if(locatorKey.endsWith("_xpath"))
            elementList = driver.findElements(xpath(prop.getProperty(locatorKey)));
        else if (locatorKey.endsWith("_css"))
            elementList = driver.findElements(cssSelector(prop.getProperty(locatorKey)));
        else{
            reportFailure("Locator of given element is not correct - " + locatorKey);
            Assert.fail("Locator of given element is not correct - " + locatorKey);
        }

        if(elementList.size()==0)
            return false;
        else
            return true;
    }


    public boolean verifyText(String locatorKey,String expectedTextKey){
        String actualText=getElement(locatorKey).getText().trim();
        String expectedText=prop.getProperty(expectedTextKey);
        if(actualText.equals(expectedText))
            return true;
        else
            return false;

    }

    public boolean verifyPageTitle(){
        String actualText=driver.getTitle();
        String expectedText="Vue.js • TodoMVC";
              if(actualText.equals(expectedText))
                  return true;
              else
        return false;
    }


    /*---------------------------------------------------Report Functions--------------------------------------------*/


    public void reportPass(String msg){
        test.log(LogStatus.PASS, msg);
    }

    public void reportFailure(String msg) throws IOException {
        test.log(LogStatus.FAIL, msg);
        takeScreenShot();
        Assert.fail(msg);
    }

    public void reportlog(String msg){
        System.out.println(msg);
        test.log(LogStatus.INFO,msg);
    }

    public String capture() throws IOException {

        String screenshotPath = null;
        try{
            //take screenshot and save it in a file
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            //copy the file to the required path
            File destinationFile = new File(System.getProperty("user.dir")+"/Report/image_" + System.currentTimeMillis()+ ".png");
            FileHandler.copy(sourceFile, destinationFile);
            String[] relatvePath = destinationFile.toString().split("Report");
            screenshotPath = ".\\" + relatvePath[1];
        }
        catch(Exception e){
            System.out.println("Failure to take screenshot "+e);
        }
        return screenshotPath;
    }

    public void takeScreenShot() throws IOException {
        test.log(LogStatus.INFO,"Screenshot -> "+ test.addScreenCapture(capture()));
    }

    public void takeFullPageScreenShot(WebDriver driver) throws IOException {

        JavascriptExecutor jsExec = (JavascriptExecutor)driver;
        //Returns a Long, Representing the Height of the window’s content area.
        Long windowHeight = (Long) jsExec.executeScript("return window.innerHeight;");
        //Returns a Long, Representing the Height of the complete WebPage a.k.a. HTML document.
        Long webpageHeight = (Long) jsExec.executeScript("return document.body.scrollHeight;");
        //Marker to keep track of the current position of the scroll point
        //Long currentWindowScroll = Long.valueOf(0);
        //Using java's boxing feature to create a Long object from native long value.
        long currentWindowScroll = 0L;
        do{
            //System.out.println(windowHeight + ", " + webpageHeight + ", " + currentWindowScroll);

            jsExec.executeScript("window.scrollTo(0, " + currentWindowScroll + ");");

            Actions act = new Actions(driver);
            act.pause(5000).perform();

            test.log(LogStatus.INFO,"Screenshot -> "+ test.addScreenCapture(capture()));

            currentWindowScroll = currentWindowScroll + windowHeight;
        }while(currentWindowScroll <= webpageHeight);
    }


    /*-----------------------------Others-------------------------*/

    public void NewEntry(String Entry) {
        type("ToDoEntry_xpath",Entry);
        getElement("ToDoEntry_xpath").sendKeys(Keys.ENTER);

        List<WebElement> t = driver.findElements(By.xpath("//*[contains(text(),'"+Entry+"')]//parent::div/child::label"));
        if (!(t.size() ==0))
            reportlog("ToDo_List Entry '"+Entry+"' is created");
        else
            reportlog("ToDo_List Entry '"+Entry+"' couldn't be created");
            }

    public void DeleteAnEntry(String Entry) throws IOException {
        click("All_xpath") ;
        try {
        driver.findElement(By.xpath("//*[contains(text(),'"+Entry+"')]//parent::div/child::label")).click();
        driver.findElement(By.xpath("//*[contains(text(),'"+Entry+"')]//parent::div/child::button")).click();
        }catch (Exception ex)
        {reportFailure(ex.getMessage());
        }

        List<WebElement> t = driver.findElements(By.xpath("//*[contains(text(),'"+Entry+"')]//parent::div/child::label"));
        if ((t.size() ==0))
            reportlog("ToDo_List Entry '"+Entry+"' is deleted");
        else
            reportlog("ToDo_List Entry '"+Entry+"' couldn't be deleted");
    }

    public void CompleteAnEntry(String Entry) throws IOException {
        click("All_xpath") ;
        try {
        List <WebElement> t=driver.findElements(xpath("//*[contains(text(),'"+Entry+"')]//parent::div/child::input"));
        t.get(0).click();
        }catch (Exception ex)
        { reportFailure(ex.getMessage());
        }

        click("Completed_xpath") ;
        List<WebElement> l = driver.findElements(By.xpath("//*[contains(text(),'"+Entry+"')]//parent::div/child::label"));
        if (!(l.size() ==0))
            reportlog("ToDo_List Entry '"+Entry+"' is marked as completed");
        else
            reportlog("ToDo_List Entry '"+Entry+"' couldn't be marked as completed");
    }

    public void CompleteAll(){
        click("CompleteAll_xpath");
    }

    public void ClearCompleted(){
        click("ClearCompleted_css");
    }

    public void AllEntries() throws IOException {
        click("All_xpath") ;

        java.util.List<WebElement> lst1 = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo completed']"));
        int a = lst1.size();
        java.util.List<WebElement> lst2 = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo']"));
        int b = lst2.size();

        reportlog("----------------No. of All Entries in To Do list is "+(a+b)+" ----------------");
        takeFullPageScreenShot(driver);

        if((a+b)!=0) {
            reportlog("All Entries in ToDo List are below: ");
            for (WebElement webElement : lst1) {
                String name = webElement.getText();
                reportlog(name);
            }
            for (WebElement webElement : lst2) {
                String name = webElement.getText();
                reportlog(name);
            }
        }

    }

    public int NoOfAllEntries() throws IOException {
        click("All_xpath") ;

        java.util.List<WebElement> lst1 = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo completed']"));
        int a = lst1.size();
        java.util.List<WebElement> lst2 = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo']"));
        int b = lst2.size();

        reportlog("No. of All Entries in To Do list is "+(a+b));
        takeFullPageScreenShot(driver);
        return a+b;

    }

    public void ActiveEntries() throws IOException {
        click("Active_xpath");
        List<WebElement> lst = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo']"));
        int a = lst.size();
        reportlog("----------------No. of active Entries in To Do list is " + a+" ----------------");
        takeFullPageScreenShot(driver);

        if (a != 0) {
            reportlog("Active Entries in ToDo List are below: ");
            for (WebElement webElement : lst) {
                String name = webElement.getText();
                reportlog(name);
            }
        }
    }

    public int NoOfActiveEntries() throws IOException {
        click("Active_xpath") ;
        java.util.List<WebElement> lst = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo']"));
        int a = lst.size();
        reportlog("No. of active Entries in To Do list is "+a);
        takeFullPageScreenShot(driver);
        return a;
    }

    public void CompletedEntries() throws IOException {
        click("Completed_xpath") ;
        java.util.List<WebElement> lst = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo completed']"));
        int a = lst.size();
        reportlog("----------------No. of completed Entries in To Do list is "+a+" ----------------");
        takeFullPageScreenShot(driver);

        if(a!=0) {
         reportlog("Completed Entries in ToDo List are below: ");
         for (WebElement webElement : lst) {
             String name = webElement.getText();
             reportlog(name);
         }
              }
    }

    public int NoOfCompletedEntries() throws IOException {
        click("Completed_xpath") ;
        java.util.List<WebElement> lst = driver.findElements(cssSelector("ul[class='todo-list']>li[class='todo completed']"));
        int a = lst.size();
        reportlog("No. of completed Entries in To Do list is "+a);
        takeFullPageScreenShot(driver);
        return a;
             }


}

