package test.java.ToDo;



import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import test.java.base.BaseTest;

import java.io.IOException;

public class Tests extends BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void start() throws IOException {
        openBrowser("Chrome");
        navigate(prop.getProperty("Todo_URL"));
           }

    @Test (priority=1)
    public void CreateToDoList() throws IOException {
        test = rep.startTest("To Create Multiple Entries in ToDo List");
        takeScreenShot();

        NewEntry("1 Pay Childcare Fee"); // To Create a single entry in the List
        NewEntry("2 Make A Doctor Appointment");
        NewEntry("3 Submit timeSheet");
        NewEntry("4 Buy Gym Membership");
        NewEntry("5 Join PTM");
        NewEntry("6 Buy Grocery");
        NewEntry("7 Wish Old Friend for birthday");
        NewEntry("8 Donate to Giving.sg");

        takeScreenShot();
    }

    @Test (priority=2)
    public void DeleteEntries() throws IOException {
        test = rep.startTest("To Delete sepecific Entries from ToDo List");
        reportlog("Creating and Deleting Entries 9 & 10");

        NewEntry("9 Get Internet Connection");
        NewEntry("10 To send Email to Recruiter");

        takeScreenShot();

        DeleteAnEntry("9 Get Internet Connection");
        DeleteAnEntry("10 To send Email to Recruiter");

        takeScreenShot();

    }

    @Test (priority=3)
    public void CompleteEntries() throws IOException {
        test = rep.startTest("To Complete sepecific Entries from ToDo List");
        reportlog("Creating Entry 11 and completing Entries 1 and 11");

        NewEntry("11 Get SP Services Connection");
        takeScreenShot();
        CompleteAnEntry("11 Get SP Services Connection");
       CompleteAnEntry("1 Pay Childcare Fee");
        takeScreenShot();

    }

    @Test (priority=4)
    public void AllEntriesTab() throws IOException {
        test = rep.startTest("To Verify All Entries Tab");

        reportlog("Intial overview of All Entries Tab: ");
        AllEntries();
        takeScreenShot();

        reportlog("Creating a New Entry - Visit Grandma");
        NewEntry("12 Visit Grandma");
        reportlog("Deleting an Entry - Wish Old Friend for birthday");
        DeleteAnEntry("7 Wish Old Friend for birthday");
        reportlog("Marking an entry as completed - Donate to Giving.sg");
        CompleteAnEntry("8 Donate to Giving.sg");

        reportlog("Intial overview of All Entries Tab: ");
        AllEntries();
        takeScreenShot();

        reportlog("Clear all completed entries");
        ClearCompleted();

        reportlog("Overview of All Entries Tab after clearing all completed entries ");
        AllEntries();
        takeScreenShot();


        reportlog("Marking all activities a completed");
        CompleteAll();

        reportlog("Overview of All Entries Tab after marking all entries as completed ");
        AllEntries();
        takeScreenShot();

        CompleteAll(); //To make all entries Active again


          }

    @Test (priority=5)
    public void ActiveEntriesTab() throws IOException {
        test = rep.startTest("To Verify Active Entries Tab");

        reportlog("Intial overview of Active Entries Tab: ");
        ActiveEntries();
        takeScreenShot();

        reportlog("Creating a New Entry - Buy Curtains");
        NewEntry("13 Buy Curtains");
        reportlog("Deleting an Entry - Buy Grocery");
        DeleteAnEntry("6 Buy Grocery");
        reportlog("Marking an entry as completed - Submit timeSheet");
        CompleteAnEntry("3 Submit timeSheet");


        reportlog("Overview of Active Entries Tab after above actions: ");
        ActiveEntries();
        takeScreenShot();

        reportlog("Marking all activities a completed");
        CompleteAll();

        reportlog("Overview of Active Entries Tab after marking all entries as completed ");
        ActiveEntries();
        takeScreenShot();

        CompleteAll(); //To make all entries Active again

    }

    @Test (priority=6)
    public void CompletedEntriesTab() throws IOException {
        test = rep.startTest("To Verify Completed Entries Tab");

        reportlog("Intial overview of Completed Entries Tab: ");
        CompletedEntries();
        takeScreenShot();

        reportlog("Marking 2 Entries as Completed");
        CompleteAnEntry("5 Join PTM");
        CompleteAnEntry("4 Buy Gym Membership");

        reportlog("Overview of Completed Entries Tab after marking 2 entries as completed: ");
        CompletedEntries();
        takeScreenShot();

        reportlog("Clear all completed entries");
        ClearCompleted();

        reportlog("Overview of Completed Entries Tab after clearing all completed entries ");
        CompletedEntries();
        takeScreenShot();

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
        terminateBrowser();
    }

}
