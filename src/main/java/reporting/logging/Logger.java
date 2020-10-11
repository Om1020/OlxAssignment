package reporting.logging;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import reporting.extentreports.ExtentManager;

public class Logger {

    public synchronized static void logPass(String log) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().log(Status.PASS, log);
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().log(Status.PASS, log);
        }
    }

    public synchronized static void logFail(String log) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().log(Status.FAIL, log);
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().log(Status.FAIL, log);
        }
    }

    public synchronized static void logSkip(String log) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().log(Status.SKIP, MarkupHelper.createLabel(log, ExtentColor.ORANGE));
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().log(Status.SKIP, MarkupHelper.createLabel(log, ExtentColor.ORANGE));
        }
    }

    public synchronized static void logDebug(String log) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().log(Status.DEBUG, MarkupHelper.createLabel(log, ExtentColor.LIME));
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().log(Status.DEBUG, MarkupHelper.createLabel(log, ExtentColor.LIME));
        }
    }

    public synchronized static void logDebug(String log, ExtentColor color) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().log(Status.DEBUG, MarkupHelper.createLabel(log, color));
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().log(Status.DEBUG, MarkupHelper.createLabel(log, color));
        }
    }

    public synchronized static void logWarning(String log) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().log(Status.WARNING, log);
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().log(Status.WARNING, log);
        }
    }

    public synchronized static void logException(Status status, Throwable throwable) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().log(status, throwable);
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().log(status, throwable);
        }
    }

    public synchronized static void logCategory(String category) {
        if (ExtentManager.getChildTest().get() != null)
            ExtentManager.getChildTest().get().assignCategory(category);
        else {
            if (ExtentManager.getTest().get() != null)
                ExtentManager.getTest().get().assignCategory(category);
        }
    }
}
