package test.java.ToDo;

import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.*;
import test.java.base.BaseTest;

import javax.swing.*;
import java.io.IOException;

public class PositiveTests extends BaseTest {

    @BeforeClass(alwaysRun = true)
    public void start() throws IOException {
        test = rep.startTest("Open ToDo_List Page");
        openBrowser("Chrome");
        navigate(prop.getProperty("Todo_URL"));

       if (verifyPageTitle() == true && isElementPresent("ToDoEntry_xpath"))
           reportlog("ToDo_List Page is displayed and Page Title is: "+driver.getTitle());
       else
           Assert.fail("ToDo_List Page is not loaded correctly");

        takeScreenShot();
    }

    @Test (priority=1)
    public void CreateToDoList() throws IOException {
        test = rep.startTest("01_To Create Multiple Entries in ToDo List");

        NewEntry("1 Pay Childcare Fee"); // To Create a single entry in the List
        NewEntry("2 Make A Doctor Appointment");
        NewEntry("3 Submit timeSheet");
        NewEntry("4 Buy Gym Membership");

        sa.assertEquals(NoOfAllEntries(),4);
            }

    @Test (priority=2)
    public void DeleteEntries() throws IOException {
        test = rep.startTest("02_To Delete specific Entries from ToDo List");

        reportlog("Adding 2 new entries in ToDo_List");
        NewEntry("5 Get Internet Connection");
        NewEntry("6 To send Email to Recruiter");
        sa.assertEquals(NoOfAllEntries(),6);

        reportlog("Deleting 2 newly created entries from ToDo_List");
        DeleteAnEntry("5 Get Internet Connection");
        DeleteAnEntry("6 To send Email to Recruiter");
        sa.assertEquals(NoOfAllEntries(),4);
            }

    @Test (priority=3)
    public void CompleteEntries() throws IOException {
        test = rep.startTest("03_To Complete specific Entries from ToDo List");

        reportlog("Adding 2 new entries in ToDo_List");
        NewEntry("7 Call to Anna");
        NewEntry("8 Buy YogaPants");
        sa.assertEquals(NoOfAllEntries(),6);

        reportlog("Marking 2 newly created entries as completed from ToDo_List");
        CompleteAnEntry("7 Call to Anna");
        CompleteAnEntry("8 Buy YogaPants");
        sa.assertEquals(NoOfAllEntries(),6);
        sa.assertEquals(NoOfCompletedEntries(),2);
        sa.assertEquals(NoOfActiveEntries(),4);
    }

    @Test (priority=4)
    public void GetEntriesList() throws IOException {
        test = rep.startTest("04_To Get Entries list from ToDo_List_Tabs: All, Active, Completed");
        AllEntries();
        ActiveEntries();
        CompletedEntries();
    }

    @Test (priority=5)
    public void VerifyTabs() throws IOException {
        test = rep.startTest("05_To Verify Tabs: All, Active, Completed");

        reportlog("**********Initial overview of Tabs**********");

        sa.assertEquals(NoOfAllEntries(),6);
        sa.assertEquals(NoOfCompletedEntries(),2);
        sa.assertEquals(NoOfActiveEntries(),4);

        reportlog("**********Making below Changes**********"); //Changes
        NewEntry("13 Visit Grandma");
        DeleteAnEntry("3 Submit timeSheet");
        CompleteAnEntry("4 Buy Gym Membership");

        reportlog("**********New overview of Tabs**********"); //Verifications
        sa.assertEquals(NoOfAllEntries(),6);
        sa.assertEquals(NoOfCompletedEntries(),3);
        sa.assertEquals(NoOfActiveEntries(),3);

       }

    @Test (priority=6)
    public void VerifyFeatures() throws IOException {
        test = rep.startTest("06_To Verify Features: ClearCompleted, Mark all Completed ");

        reportlog("**********Clearing All Completed Entries**********"); //Changes
        ClearCompleted();

        reportlog("Overview of All,Completed Tabs after clearing all completed entries"); //Verifications
        sa.assertEquals(NoOfAllEntries(),3);
        sa.assertEquals(NoOfCompletedEntries(),0);


        reportlog("**********Marking all activities as completed**********"); //Changes
        CompleteAll();

        reportlog("Overview of Tabs after marking all entries as completed "); //Verifications
        sa.assertEquals(NoOfAllEntries(),3);
        sa.assertEquals(NoOfCompletedEntries(),3);
        sa.assertEquals(NoOfActiveEntries(),0);

        CompleteAll(); //To make all entries Active again
    }

    @AfterClass(alwaysRun = true)
    public void CloseBrowser()
    {
        terminateBrowser();
    }

    @AfterSuite(alwaysRun = true)
    public void quit () {
        try {
            sa.assertAll();
        }catch(Error e) {
            test.log(LogStatus.FAIL, e.getMessage());
        }
        rep.endTest(test);
        rep.flush();
        System.out.println("report is flushed");

    }

}
