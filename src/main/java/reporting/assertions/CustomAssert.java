package reporting.assertions;

import com.aventstack.extentreports.markuputils.ExtentColor;
import org.testng.Assert;
import org.testng.ITestResult;
import reporting.listeners.TestNGReportListener;
import reporting.logging.Logger;


import java.util.List;

public class CustomAssert {
    private List<Throwable> m_errors;

    private CustomAssert() {
    }

    public static void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
        Logger.logPass(message + " = <b>Pass</b>");
    }

    public static void assertContainsIgnoreCase(String completeString, String subString, String message) {
        String str = message + "<br><b>Complete String : </b>" + completeString + "<br><b>Substring : </b>" + subString;
        String errorMessage = message + " -- \nComplete String : " + completeString + "\nSubstring : " + subString + "\n\n";
        Assert.assertTrue(completeString.toLowerCase().contains(subString.toLowerCase()), errorMessage);
        Logger.logPass(str);
    }

    public static void assertEquals(String actual, String expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertEqualsIgnoreCase(String actual, String expected, String message) {
        Assert.assertEquals(actual.toLowerCase(), expected.toLowerCase(), message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertEquals(double actual, double expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);
    }

    public static void assertEquals(int actual, int expected, String message) {
        Assert.assertEquals(actual, expected, message);
        String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
        Logger.logPass(str);

    }

    public static void assertContains(String completeString, String subString, String message) {
        String str = message + "<br><b>Complete String : </b>" + completeString + "<br><b>Substring : </b>" + subString;
        String errorMessage = message + " -- \nComplete String : " + completeString + "\nSubstring : " + subString + "\n\n";
        Assert.assertTrue(completeString.contains(subString), errorMessage);
        Logger.logPass(str);
    }

    public static void assertFail(String message) {
        Assert.fail(message);
        Logger.logDebug(message + " = <b>Fail</b>", ExtentColor.RED);
    }

    public static void assertTrue(boolean condition, String message, String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);

        try {
            Assert.assertTrue(condition, message);
            Logger.logPass(message + " = <b>Pass</b>");
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.assertTrue(condition, message);
        }
    }

    public static void assertContainsIgnoreCase(String completeString, String subString, String message
            , String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);
        String str = message + "<br><b>Complete String : </b>" + completeString + "<br><b>Substring : </b>" + subString;
        String errorMessage = message + " -- \nComplete String : " + completeString + "\nSubstring : " + subString + "\n\n";

        boolean flag = completeString.toLowerCase().contains(subString.toLowerCase());
        try {
            Assert.assertTrue(flag, errorMessage);
            Logger.logPass(str);
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.assertTrue(flag, errorMessage);
        }
    }

    public static void assertEquals(String actual, String expected, String message, String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);

        try {
            Assert.assertEquals(actual, expected, message);
            String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
            Logger.logPass(str);
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEqualsIgnoreCase(String actual, String expected, String message, String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);

        try {
            Assert.assertEquals(actual.toLowerCase(), expected.toLowerCase(), message);
            String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
            Logger.logPass(str);
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.assertEquals(actual.toLowerCase(), expected.toLowerCase(), message);
        }
    }

    public static void assertEquals(double actual, double expected, String message, String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);

        try {
            Assert.assertEquals(actual, expected, message);
            String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
            Logger.logPass(str);
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.assertEquals(actual, expected, message);
        }
    }

    public static void assertEquals(int actual, int expected, String message, String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);

        try {
            Assert.assertEquals(actual, expected, message);
            String str = message + "<br><b>Actual : </b>" + actual + "<br><b>Expected : </b>" + expected;
            Logger.logPass(str);
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.assertEquals(actual, expected, message);
        }

    }

    public static void assertContains(String completeString, String subString, String message, String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);
        String str = message + "<br><b>Complete String : </b>" + completeString + "<br><b>Substring : </b>" + subString;
        String errorMessage = message + " -- \nComplete String : " + completeString + "\nSubstring : " + subString + "\n\n";

        try {
            Assert.assertTrue(completeString.contains(subString), errorMessage);
            Logger.logPass(str);
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.assertTrue(completeString.contains(subString), errorMessage);
        }
    }

    public static void assertFail(String message, String testCaseName, String description, String jiraID) {
        TestNGReportListener.createChildTest(testCaseName, description);

        try {
            Assert.fail(message);
            Logger.logDebug(message + " = <b>Fail</b>", ExtentColor.RED);
            TestNGReportListener.updateChildTestStatus(ITestResult.SUCCESS, null);
        } catch (Exception e) {
            TestNGReportListener.updateChildTestStatus(ITestResult.FAILURE, e.getCause());
            Assert.fail(message);
        }
    }


}
