package listeners;

import global.GlobalData;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.GenericFunctions;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static global.GlobalData.*;

public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite iSuite) {
    }

    @Override
    public void onFinish(ISuite iSuite) {
        captueReportScreenShotWithPhantomJS();
    }

    private void captueReportScreenShotWithPhantomJS() {
        try {
            System.out.println("---------- Starting Phantom JS Driver ------------");
            WebDriverManager.phantomjs().setup();
            WebDriver driver = new PhantomJSDriver(getCapabilities_TurnOffLogsPhantomJS());
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            File file = new File(GlobalData.OUTPUT_FOLDER_REPORT + GlobalData.FILE_NAME_REPORT);
            driver.get(file.getAbsolutePath());
            Thread.sleep(2000);
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript(GlobalData.JS_OPENDASHBOARD_EXTENTREPORTS);
            String filePath = OUTPUT_FOLDER_REPORT + OUTPUT_FOLDER_SCREENSHOTS + FILE_NAME_REPORT_SCREENSHOT;
            File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(f, new File(filePath));
            driver.quit();
        } catch (Exception e) {
            System.out.println("XXXXX Unable to Get Report ScreenShot XXXXX");
        }
    }

    private DesiredCapabilities getCapabilities_TurnOffLogsPhantomJS() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        ArrayList<String> cliArgsCap = new ArrayList<>();
        cliArgsCap.add("--webdriver-loglevel=NONE");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
        return caps;
    }


}
