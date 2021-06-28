package test.java.ToDo;

import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import test.java.base.BaseTest;

import java.io.IOException;

public class NegativeTests extends BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void start() throws IOException {
        test = rep.startTest("01_Open ToDo_List Page and verify PageTitle");
        openBrowser("Chrome");
        navigate(prop.getProperty("Todo_URL"));

       if (verifyPageTitle() == true && isElementPresent("ToDoEntry_xpath"))
           reportlog("ToDo_List Page is displayed and Page Title is correct: "+driver.getTitle());
       else
           Assert.fail("ToDo_List Page is not loaded correctly");

        takeScreenShot();

    }

    @Test (priority=1)
    public void CreateanEmptyEntry() throws IOException {
        test = rep.startTest("08_To Create an Empty Entry in ToDo List");

        reportlog("Create Some New Entries: ");
        NewEntry("1 Book Car Service Appointment");
        NewEntry("1 Buy Cycle Stand");
        sa.assertEquals(NoOfActiveEntries(),2);

        reportlog("Create an entry with spaces/No values");
        NewEntry("   ");
        sa.assertEquals(NoOfActiveEntries(),2);
            }

    @Test (priority=2)
    public void DeleteANonExistingEntry() throws IOException {
        test = rep.startTest("09_To Delete a Non-Existing Entry in ToDo List");
        reportlog("Delete a Non-Existing Entry");
        DeleteAnEntry("Non-Existing Entry");
        sa.assertEquals(NoOfActiveEntries(),2);
    }

    @Test (priority=3)
    public void CompleteANonExistingEntry() throws IOException {
        test = rep.startTest("10_To Complete a Non-Existing Entry in ToDo List");
        reportlog("Complete a Non-Existing Entry");
        CompleteAnEntry("Non-Existing Entry");
        sa.assertEquals(NoOfActiveEntries(),2);
        sa.assertEquals(NoOfCompletedEntries(),0);
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
