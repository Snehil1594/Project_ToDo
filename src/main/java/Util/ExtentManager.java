package main.java.Util;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

import java.io.File;
import java.util.Date;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            Date d=new Date();
            String fileName=d.toString().replace(":", "_").replace(" ", "_")+".html";
            extent = new ExtentReports("Report//"+fileName, true, DisplayOrder.NEWEST_FIRST);

            extent.loadConfig(new File("src\\ReportsConfig.xml"));


        }
        return extent;

    }

}

