package listeners;

import driver.DriverManager;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import reporting.extentreports.ExtentManager;
import utils.ActionHelper;
import utils.GenericFunctions;

public class ExceptionListener {

    private synchronized boolean checkSessionCrash(Throwable throwable) {
        return throwable instanceof NoSuchSessionException ||
                throwable instanceof WebDriverException && throwable.toString().contains("Failed to connect to localhost");
    }

    private synchronized void startFreshDriver() {
        System.out.println("------ Restarting Fresh Driver ------");
        try {
            ActionHelper.quitDriver();
        } catch (Exception e) {

        }
        WebDriver driver = GenericFunctions.startDriver();
        DriverManager.setDriver(driver);
        ExtentManager.setDriver(driver);
    }

}

