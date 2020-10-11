package testsuite.basetest;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import reporting.listeners.TestNGReportListener;
import utils.GenericFunctions;

@Listeners({InvocationListener.class, TestNGReportListener.class})
public class BaseTestClass {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) throws Exception {
        System.out.println("--------------------- INSIDE BEFORE SUITE ---------------------");
        GenericFunctions.initExtentReport();
        GenericFunctions.initDirectories();
        GenericFunctions.initDrivers();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite(ITestContext context) {
        System.out.println("--------------------- INSIDE AFTER SUITE ---------------------");
        GenericFunctions.updateTestCaseStatsGlobalData();
        GenericFunctions.addExecutionDetails_ExtentReport();
        GenericFunctions.quitAllBrowsers();
    }

}
