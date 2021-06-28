package test.java.ToDo;

import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import org.testng.annotations.*;
import test.java.base.BaseTest;

import java.io.IOException;

public class NegativeTests extends BaseTest {

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

    @Test (priority=7)
    public void CreateanEmptyEntry() throws IOException {
        test = rep.startTest("07_To Create an Empty Entry in ToDo List");

        reportlog("Create Some New Entries: ");
        NewEntry("1 Book Car Service Appointment");
        NewEntry("1 Buy Cycle Stand");
        sa.assertEquals(NoOfActiveEntries(),2);

        reportlog("********Create an entry with spaces/No values********");
        NewEntry("   ");
        sa.assertEquals(NoOfActiveEntries(),2);
            }

    @Test (priority=8) //Just To Check Code Errors/Not Possible on GUI
    public void DeleteANonExistingEntry() throws IOException {
        test = rep.startTest("08_To Delete a Non-Existing Entry in ToDo List");
        reportlog("This test is to check Code Issues/Action Not possible on GUI: Expected to be Failed(To demonstrate failure in Report)");
        reportlog("Delete a Non-Existing Entry");
        DeleteAnEntry("Non-Existing Entry");
        sa.assertEquals(NoOfActiveEntries(),2);
    }

    @Test (priority=9)//Just To Check Code Errors/Not Possible on GUI
    public void CompleteANonExistingEntry() throws IOException {
        test = rep.startTest("09_To Complete a Non-Existing Entry in ToDo List");
        reportlog("This test is to check Code Issues/Action Not possible on GUI: Expected to be Failed(To demonstrate failure in Report)");
        reportlog("Complete a Non-Existing Entry");
        CompleteAnEntry("Non-Existing Entry");
        sa.assertEquals(NoOfActiveEntries(),2);
        sa.assertEquals(NoOfCompletedEntries(),0);
    }

    @AfterClass(alwaysRun = true)
    public void CloseBrowser()
    {
        terminateBrowser();
    }

    }
