package reporting.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.KlovReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.WebDriver;
import reporting.constants.Constants;
import reporting.listeners.TestNGReportListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ExtentManager {
    private static KlovReporter klovReporter = null;
    private static ExtentReports extent = null;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<ExtentTest> childTest = new ThreadLocal<>();
    private static ThreadLocal<String> categoryName = new ThreadLocal<>();
    private static ThreadLocal<String> testPackageName = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static String reportBaseDirectory;
    private static boolean addScreenshotsToReport;
    private static boolean updateJIRA;
    private static boolean removeRetriedTests;

    private ExtentManager() {
    }

    public static ThreadLocal<String> getTestPackageName() {
        return testPackageName;
    }

    public static void setTestPackageName(String testPackageName) {
        getTestPackageName().set(testPackageName);
    }

    public static ThreadLocal<String> getCategoryName() {
        return categoryName;
    }

    public static void setCategoryName(String categoryName) {
        getCategoryName().set(categoryName);
    }

    public synchronized static ThreadLocal<ExtentTest> getChildTest() {
        return childTest;
    }

    public synchronized static void setChildTest(ExtentTest test) {
        getChildTest().set(test);
    }

    public synchronized static ThreadLocal<ExtentTest> getTest() {
        return test;
    }

    public synchronized static void setTest(ExtentTest test) {
        getTest().set(test);
    }

    public synchronized static ExtentReports createInstance(String reportBaseDirectory, String documentTitle, String reportName, Theme theme,
                                                            boolean updateJIRA, boolean addScreenshotsToReport, boolean removeRetriedTests) {
        setReportBaseDirectory(reportBaseDirectory);
        setUpdateJIRA(updateJIRA);
        setRemoveRetriedTests(removeRetriedTests);
        setAddScreenshotsToReport(addScreenshotsToReport);
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(getReportBaseDirectory() + Constants.FILE_NAME_REPORT);
        htmlReporter.config().setTheme(theme);
        htmlReporter.config().setReportName(reportName);
        htmlReporter.config().setDocumentTitle(documentTitle);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setEncoding(Constants.REPORT_ENCODING);
        htmlReporter.config().setJS(Constants.JS_CHANGE_NAME_EXTENT_REPORTS);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        if (klovReporter != null)
            extent.attachReporter(klovReporter);
        return extent;
    }

    public synchronized static void startKlovReporter(String projectName, String remoteIP, int mongoDBPort, int klovServerPort) {
        klovReporter = new KlovReporter();
        klovReporter.initMongoDbConnection(remoteIP, mongoDBPort);
        klovReporter.setProjectName(projectName);
        klovReporter.setReportName(getCurrentDate());
        klovReporter.setKlovUrl("http://" + remoteIP + ":" + klovServerPort);
    }

    public synchronized static String getCurrentDate() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss a");
        return formatter.format(date);
    }

    public synchronized static void deleteCurrentTest() {
        try {
            extent.removeTest(ExtentManager.getTest().get());
        } catch (Exception e) {
            System.out.println("XXXXX Unable to Delete Extent Test XXXXX");
        }
    }

    public synchronized static void flush() {
        extent.flush();
    }

    public synchronized static void setPassPercentage(String value) {
        extent.setSystemInfo("Pass %", MarkupHelper.createLabel(value, ExtentColor.GREEN).getMarkup());
        flush();
    }

    public synchronized static void addSystemInfo(String key, String value) {
        extent.setSystemInfo(key, value);
        flush();
    }

    public synchronized static void setTestRunnerOutput(String log) {
        extent.setTestRunnerOutput(log);
    }

    public synchronized static void createTest(String testName, String description) {
        setTest(extent.createTest(testName, description));
    }

    public synchronized static boolean isUpdateJIRA() {
        return updateJIRA;
    }

    public synchronized static void setUpdateJIRA(boolean updateJIRA) {
        ExtentManager.updateJIRA = updateJIRA;
    }

    public synchronized static boolean isRemoveRetriedTests() {
        return removeRetriedTests;
    }

    public synchronized static void setRemoveRetriedTests(boolean removeRetriedTests) {
        ExtentManager.removeRetriedTests = removeRetriedTests;
    }

    public synchronized static boolean isAddScreenshotsToReport() {
        return addScreenshotsToReport;
    }

    public synchronized static void setAddScreenshotsToReport(boolean addScreenshotsToReport) {
        ExtentManager.addScreenshotsToReport = addScreenshotsToReport;
    }

    public synchronized static String getReportBaseDirectory() {
        return reportBaseDirectory;
    }

    public synchronized static void setReportBaseDirectory(String reportBaseDirectory) {
        ExtentManager.reportBaseDirectory = reportBaseDirectory;
    }

    public synchronized static WebDriver getDriver() {
        return driver.get();
    }

    public synchronized static void setDriver(WebDriver driver) {
        ExtentManager.driver.set(driver);
    }

    public synchronized static void addGroupNamesSystemInfo(String key, String value) {
        ExtentManager.addSystemInfo(key, getFomattedGroupNames(value));
        flush();
    }

    private synchronized static String getFomattedGroupNames(String groupNames) {
        String finalStr = "";
        for (String str : groupNames.split(",")) {
            if (str.contains(".")) {
                finalStr = finalStr + str.split("\\.")[1] + "<br>";
            } else {
                finalStr = finalStr + str + "<br>";
            }
        }
        return finalStr;
    }

    public static synchronized void addDeviceInfoToReport(HashMap<String, String> deviceDetails) {
        ExtentManager.getTest().get().info(MarkupHelper.createTable(TestNGReportListener.getParameterArray(deviceDetails)));
    }

    public static synchronized void addLogsInReport(String logs) {
        try {
            getTest().get().log(Status.DEBUG, logs);
        } catch (Exception e) {
            System.out.println("XXXXX Unable to add ADB Logs to Report XXXXX");
        }
    }
}
