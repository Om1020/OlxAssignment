package reporting.listeners;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.testng.*;
import reporting.constants.Constants;
import reporting.dataobjects.PackageTestStatusDTO;
import reporting.extentreports.ExtentManager;
import reporting.utils.GlobalData;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;

public class TestNGReportListener implements ITestListener {
    private static ITestStatusListener testStatusListener = null;

    public static synchronized void initStatusListener(ITestStatusListener testStatusListener) {
        TestNGReportListener.testStatusListener = testStatusListener;
    }

    public static synchronized String[][] getParameterArray(HashMap<String, String> hm) {
        String[][] parameters = new String[hm.size()][2];
        int row = 0;
        int column = 0;
        for (String str : hm.keySet()) {
            parameters[row][column] = "<b>" + str + "</b>";
            column++;
            parameters[row][column] = hm.get(str);
            row++;
            column = 0;
        }
        return parameters;
    }

    public static synchronized void addADBLogsInReport(ITestResult result, String logs) {
        try {
            String methodName = result.getName();
            String className = result.getMethod().getRealClass().getSimpleName();
            DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM_dd_yyyy_HH_mm_ss_SSS");
            String timeStamp = formatter.print(DateTime.now());
            String logsFilePathForReport = Constants.OUTPUT_FOLDER_LOGS + className + "_" + methodName + "_" + timeStamp + Constants.LOGSFILE_EXTENTION;
            String logsFile = ExtentManager.getReportBaseDirectory() + Constants.OUTPUT_FOLDER_LOGS + className + "_" + methodName + "_" + timeStamp + Constants.LOGSFILE_EXTENTION;
            FileUtils.writeStringToFile(new File(logsFile), logs, Charset.forName("UTF-8"));
            ExtentManager.getTest().get().log(Status.DEBUG, "<b> ADB Logs : </b><a href='" + logsFilePathForReport + "'>Click here</a>");
        } catch (Exception e) {
            System.out.println("XXXXX Unable to add ADB Logs to Report XXXXX");
        }
    }

    public static synchronized void createChildTest(String name) {
        ExtentManager.setChildTest(ExtentManager.getTest().get().createNode(name));
        if (ExtentManager.getCategoryName().get() != null)
            ExtentManager.getChildTest().get().assignCategory(ExtentManager.getCategoryName().get());
    }

    public static synchronized void createChildTest(String name, String description) {
        ExtentManager.setChildTest(ExtentManager.getTest().get().createNode(name, description));
        if (ExtentManager.getCategoryName().get() != null)
            ExtentManager.getChildTest().get().assignCategory(ExtentManager.getCategoryName().get());
    }

    public static synchronized void updateChildTestStatus(int statusCode, Throwable throwable) {
        switch (statusCode) {
            case ITestResult.SUCCESS:
                ExtentManager.getChildTest().get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
                break;
            case ITestResult.FAILURE:
                ExtentManager.getChildTest().get().debug(throwable);
                ExtentManager.getChildTest().get().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
                break;
        }
        updateTestStatusInSuiteDTO(statusCode);
    }

    public synchronized static void updateTestStatusInSuiteDTO(int statusCode) {
        String packageName = ExtentManager.getTestPackageName().get();
        if (packageName != null) {
            GlobalData.suiteTestStatusDTO.incrementTotalTest();

            switch (statusCode) {
                case ITestResult.SUCCESS:
                    GlobalData.suiteTestStatusDTO.incrementTotalPass();
                    break;

                case ITestResult.FAILURE:
                    GlobalData.suiteTestStatusDTO.incrementTotalFail();
                    break;

                case ITestResult.SKIP:
                    GlobalData.suiteTestStatusDTO.incrementTotalSkip();
                    break;
            }

            HashMap<String, PackageTestStatusDTO> packageWiseTestStatusMap = GlobalData.suiteTestStatusDTO.getPackageTestStatusDTOHashMap();

            if (packageWiseTestStatusMap.get(packageName) == null) {
                PackageTestStatusDTO packageTestStatusDTO = new PackageTestStatusDTO(packageName, 0, 0, 0, 0, 0);
                packageWiseTestStatusMap.put(packageName, updatePackageDTO(packageTestStatusDTO, statusCode));
            } else {
                PackageTestStatusDTO packageTestStatusDTO = packageWiseTestStatusMap.get(packageName);
                packageWiseTestStatusMap.put(packageName, updatePackageDTO(packageTestStatusDTO, statusCode));
            }
        }
    }

    private synchronized static PackageTestStatusDTO updatePackageDTO(PackageTestStatusDTO packageTestStatusDTO, int statusCode) {
        packageTestStatusDTO.setTotalTest(packageTestStatusDTO.getTotalTest() + 1);

        switch (statusCode) {
            case ITestResult.SUCCESS:
                packageTestStatusDTO.setTotalPass(packageTestStatusDTO.getTotalPass() + 1);
                break;

            case ITestResult.FAILURE:
                packageTestStatusDTO.setTotalFail(packageTestStatusDTO.getTotalFail() + 1);
                break;

            case ITestResult.SKIP:
                packageTestStatusDTO.setTotalSkip(packageTestStatusDTO.getTotalSkip() + 1);
                break;
        }

        return packageTestStatusDTO;
    }

    @Override
    public synchronized void onStart(ITestContext context) {
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        System.out.println(GlobalData.suiteTestStatusDTO.toString());
        removeRetriedTestsFromTestNG(context);
        addConsoleLogsToReport();
    }

    public synchronized void clearOldTestDetails() {
        ExtentManager.setChildTest(null);
        ExtentManager.setTestPackageName(null);
        ExtentManager.setCategoryName(null);
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        clearOldTestDetails();
        ExtentManager.createTest(getMethodNameWithParams(result), getTestDescription(result));
        ExtentManager.setCategoryName(getSimpleClassName(result));
        ExtentManager.setTestPackageName(getPackageNameFromTestMethod(result));
        addParametersInReport(result);
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        logStatusToConsole(result);
        assignCategoryToTest(result);
        addExtentLabelToTest(result);
        updateTestStatusInSuiteDTO(result);
        ExtentManager.flush();
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        logStatusToConsole(result);
        assignCategoryToTest(result);
        assignExceptionToTest(result);
        addExtentLabelToTest(result);
        updateTestStatusInSuiteDTO(result);
        ExtentManager.flush();
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        if (result.wasRetried() && ExtentManager.isRemoveRetriedTests()) {
            removeRetriedTest(result);
        } else {
            logSkippedTest(result);
        }
    }

    @Override
    public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    public synchronized void removeRetriedTest(ITestResult result) {
        logRetryStatusToConsole(result);
        deleteCurrentTestFromReport();
        ExtentManager.flush();
    }

    public synchronized void logSkippedTest(ITestResult result) {
        if (testStatusListener != null)
            logStatusToConsole(result);
        assignCategoryToTest(result);
        assignExceptionToTest(result);
        addExtentLabelToTest(result);
        updateTestStatusInSuiteDTO(result);
        ExtentManager.flush();
    }


    private synchronized void addParametersInReport(ITestResult result) {
        if (result.getParameters().length > 0 && result.getParameters()[0] instanceof HashMap) {
            ExtentManager.getTest().get().log(Status.PASS, MarkupHelper.createTable(getParameterArray((HashMap<String, String>) result.getParameters()[0])));
        }
    }

    private synchronized String getMethodNameWithParams(ITestResult result) {
        String methodName = result.getName();
        String nextLineCharacter = "<br>";
        if (result.getParameters().length > 0 && result.getParameters()[0] instanceof HashMap) {
            methodName = methodName + nextLineCharacter + result.getParameters()[0].toString();
        }

        return methodName;
    }

    private synchronized void logStatusToConsole(ITestResult result) {
        String status = "";

        if (result.getStatus() == ITestResult.SUCCESS)
            status = "Pass";

        else if (result.getStatus() == ITestResult.FAILURE)
            status = "Fail";

        else if (result.getStatus() == ITestResult.SKIP)
            status = "Skip";

        System.out.println(getSimpleMethodName(result) + " = [" + status + "]" + System.lineSeparator());
        Reporter.log(getSimpleMethodName(result) + " = [" + status + "]<br>");
    }

    private synchronized void logRetryStatusToConsole(ITestResult result) {
        Reporter.log("-------- Retry = " + getSimpleMethodName(result) + "--------<br>");
        System.out.println("-------- Retry = " + getSimpleMethodName(result) + "--------" + System.lineSeparator());
    }

    private synchronized String getSimpleClassName(ITestResult result) {
        return result.getMethod().getRealClass().getSimpleName();
    }

    private synchronized String getSimpleMethodName(ITestResult result) {
        return result.getName();
    }

    private synchronized String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription();
    }

    private synchronized void assignCategoryToTest(ITestResult result) {
        if (ExtentManager.getChildTest().get() == null)
            ExtentManager.getTest().get().assignCategory(getSimpleClassName(result));
    }

    private synchronized void assignExceptionToTest(ITestResult result) {
        ExtentManager.getTest().get().debug(result.getThrowable());
    }

    private synchronized void addExtentLabelToTest(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS)
            ExtentManager.getTest().get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));

        else if (result.getStatus() == ITestResult.FAILURE) {
            ExtentManager.getTest().get().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
        } else
            ExtentManager.getTest().get().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));

    }

    private synchronized void deleteCurrentTestFromReport() {
        ExtentManager.deleteCurrentTest();
    }

    private synchronized void removeRetriedTestsFromTestNG(ITestContext context) {
        Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
        for (ITestResult temp : skippedTests) {
            ITestNGMethod method = temp.getMethod();
            if (context.getFailedTests().getResults(method).size() > 0) {
                skippedTests.remove(temp);
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    skippedTests.remove(temp);
                }
            }
        }
    }

    private synchronized void addConsoleLogsToReport() {
        for (String s : Reporter.getOutput()) {
            ExtentManager.setTestRunnerOutput(s + System.lineSeparator());
        }
    }

    public synchronized void updateTestStatusInSuiteDTO(ITestResult result) {
        if (ExtentManager.getChildTest().get() == null) {
            String packageName = getPackageNameFromTestMethod(result);
            GlobalData.suiteTestStatusDTO.incrementTotalTest();

            switch (result.getStatus()) {
                case ITestResult.SUCCESS:
                    GlobalData.suiteTestStatusDTO.incrementTotalPass();
                    break;

                case ITestResult.FAILURE:
                    GlobalData.suiteTestStatusDTO.incrementTotalFail();
                    break;

                case ITestResult.SKIP:
                    GlobalData.suiteTestStatusDTO.incrementTotalSkip();
                    break;
            }

            HashMap<String, PackageTestStatusDTO> packageWiseTestStatusMap = GlobalData.suiteTestStatusDTO.getPackageTestStatusDTOHashMap();

            if (packageWiseTestStatusMap.get(packageName) == null) {
                PackageTestStatusDTO packageTestStatusDTO = new PackageTestStatusDTO(packageName, 0, 0, 0, 0, 0);
                packageWiseTestStatusMap.put(packageName, updatePackageDTO(packageTestStatusDTO, result));
            } else {
                PackageTestStatusDTO packageTestStatusDTO = packageWiseTestStatusMap.get(packageName);
                packageWiseTestStatusMap.put(packageName, updatePackageDTO(packageTestStatusDTO, result));
            }
        }
    }

    private synchronized PackageTestStatusDTO updatePackageDTO(PackageTestStatusDTO packageTestStatusDTO, ITestResult result) {
        packageTestStatusDTO.setTotalTest(packageTestStatusDTO.getTotalTest() + 1);

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                packageTestStatusDTO.setTotalPass(packageTestStatusDTO.getTotalPass() + 1);
                break;

            case ITestResult.FAILURE:
                packageTestStatusDTO.setTotalFail(packageTestStatusDTO.getTotalFail() + 1);
                break;

            case ITestResult.SKIP:
                packageTestStatusDTO.setTotalSkip(packageTestStatusDTO.getTotalSkip() + 1);
                break;
        }

        return packageTestStatusDTO;
    }

    private synchronized String getPackageNameFromTestMethod(ITestResult result) {
        String[] strList = result.getTestClass().getRealClass().getPackage().getName().split("\\.");
        return strList[strList.length - 1];
    }


}
