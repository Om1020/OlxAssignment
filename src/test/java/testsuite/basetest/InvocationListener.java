package testsuite.basetest;

import driver.DriverManager;
import global.GlobalData;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import reporting.extentreports.ExtentManager;

public class InvocationListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            DriverManager.setbrowserDTO(GlobalData.freePool.remove());
            ExtentManager.setDriver(DriverManager.getDriver());
        }
    }

    @Override
    public synchronized void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (method.isTestMethod()) {
            GlobalData.freePool.add(DriverManager.getbrowserDTO());
            System.gc();
        }
    }

}
