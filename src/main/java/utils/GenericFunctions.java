package utils;

import com.aventstack.extentreports.reporter.configuration.Theme;
import driver.DriverManager;
import executionengine.BrowserDTO;
import global.GlobalData;
import io.github.bonigarcia.wdm.PhantomJsDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import reporting.dataobjects.SuiteTestStatusDTO;
import reporting.extentreports.ExtentManager;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static global.GlobalData.*;
import static global.GlobalData.TOTAL_PASS;
import static global.GlobalData.TOTAL_PASS;

public class GenericFunctions {

    public static synchronized WebDriver startDriver() {
        WebDriver driver = null;

        try {
            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("Chrome")) {
                System.out.println("--------Starting Chrome Driver-------");
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                driver = new ChromeDriver(options);
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                driver.get("https://www.imdb.com/");
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("ChromeHeadless")) {
                System.out.println("--------Starting Chrome Driver-------");
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.setHeadless(true);
                options.addArguments("window-size=1920x1080");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(options);
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                driver.get("https://www.imdb.com/");
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("Firefox")) {
                System.out.println("--------Starting Firefox Driver-------");
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                driver.get("https://www.imdb.com/");
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("FirefoxHeadless")) {
                System.out.println("--------Starting Firefox Driver-------");
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                options.setHeadless(true);
                options.addArguments("window-size=1920x1080");
                driver = new FirefoxDriver(options);
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                driver.get("https://www.imdb.com/");
            }

            if (GlobalData.DRIVER_TYPE.equalsIgnoreCase("PhantomJS")) {
                System.out.println("--------Starting PhantomJS Driver-------");
                WebDriverManager.phantomjs().setup();
                PhantomJsDriverManager.phantomjs().setup();
                driver = new PhantomJSDriver(getCapabilities_TurnOffLogsPhantomJS());
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                driver.get("https://www.imdb.com/");
            }

            driver.manage().timeouts().implicitlyWait(GlobalData.DEFAULT_IMPLICITWAIT, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return driver;

    }

    public static synchronized void initDrivers() throws Exception {
        for (int i = 0; i < GlobalData.NOOFTHREADS; i++) {
            BrowserDTO browserDTO = new BrowserDTO(startDriver());
            GlobalData.freePool.put(browserDTO);
        }
    }

    public synchronized static void initDirectories() {
        try {
            File reportDirectory = new File(GlobalData.OUTPUT_FOLDER_REPORT);
            FileUtils.forceMkdir(reportDirectory);
            FileUtils.cleanDirectory(reportDirectory);
            File screenshotsDirectory = new File(GlobalData.OUTPUT_FOLDER_REPORT + GlobalData.OUTPUT_FOLDER_SCREENSHOTS);
            FileUtils.forceMkdir(screenshotsDirectory);
            FileUtils.cleanDirectory(screenshotsDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void quitAllBrowsers() {
        for (BrowserDTO device : GlobalData.freePool) {
            device.getDriver().quit();
        }
    }

    public static synchronized void initExtentReport() {
        ExtentManager.createInstance(GlobalData.OUTPUT_FOLDER_REPORT, "Automation Report", "Automation Reports - Web",
                Theme.STANDARD, GlobalData.FLAG_UPDATEJIRA, true, true);
    }

    public static synchronized void updateTestCaseStatsGlobalData() {
        SuiteTestStatusDTO suiteTestStatusDTO = reporting.utils.GlobalData.suiteTestStatusDTO;
        TOTAL_PASS = suiteTestStatusDTO.getTotalPass();
        TOTAL_FAIL = suiteTestStatusDTO.getTotalFail();
        TOTAL_SKIP = suiteTestStatusDTO.getTotalSkip();
        TOTAL_TESTCASES = suiteTestStatusDTO.getTotalTest();

        Double passPercentage = (TOTAL_PASS * 100.0) / (TOTAL_TESTCASES - PRODUCTION_BUGS);
        PASS_PERCENTAGE_INTVALUE = passPercentage.intValue();

        GlobalData.PASSPERCENTAGE = PASS_PERCENTAGE_INTVALUE + "%";
    }


    public static synchronized void addExecutionDetails_ExtentReport() {
        ExtentManager.setPassPercentage(GlobalData.PASSPERCENTAGE);
        ExtentManager.addSystemInfo("Environment", StringUtils.capitalize(GlobalData.ENVIRONMENT));
        ExtentManager.addSystemInfo("ExecutionType", GlobalData.EXECUTION_TYPE);

        if (!GlobalData.EXECUTION_TYPE.equalsIgnoreCase("System"))
            ExtentManager.addGroupNamesSystemInfo("Groups", GlobalData.GROUP_NAMES);

        ExtentManager.addSystemInfo("No of Browsers", "" + GlobalData.NOOFTHREADS);
        ExtentManager.addSystemInfo("Language", GlobalData.LANGUAGE);
    }

    private static synchronized DesiredCapabilities getCapabilities_TurnOffLogsPhantomJS() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        ArrayList<String> cliArgsCap = new ArrayList<>();
        cliArgsCap.add("--webdriver-loglevel=NONE");
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
        return caps;
    }

    public static synchronized void restartFreshDriver() {
        System.out.println("------ Restarting Fresh Driver for Logout Case ------");
        ActionHelper.quitDriver();
        WebDriver driver = startDriver();
        DriverManager.setDriver(driver);
        ExtentManager.setDriver(driver);
    }

}
