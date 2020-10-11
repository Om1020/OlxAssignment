package utils;

import driver.DriverManager;
import global.GlobalData;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import reporting.logging.Logger;

import java.util.concurrent.TimeUnit;


public class ActionHelper {

    public static void waitUntilElementVisible_NoCustomMessage(By by) {
        setImplicitWait(0);
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), (long)GlobalData.ELEMENT_TIMEOUT);
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Exception var5) {
            throw new ElementNotVisibleException("Element is not visible");
        } finally {
            setImplicitWait(GlobalData.DEFAULT_IMPLICITWAIT);
        }

    }

    public static void openURL(String url) {
        DriverManager.getDriver().get(url);
        Logger.logPass("Opening Url : " + url);
        gotoSleep(3000);
    }

    public static void click(By by) {
        findElement(by).click();
    }

    public static void quitDriver() {
        DriverManager.getDriver().quit();
        Logger.logPass("Closing Browser !!");
    }

    public static void gotoSleep(int miliSeconds) {
        try {
            Thread.sleep(miliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setImplicitWait(int seconds) {
        DriverManager.getDriver().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    public static void waitUntilElementVisible(By by) {
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), GlobalData.ELEMENT_TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static WebElement findElement(By by) {
        waitUntilElementVisible(by);
        return DriverManager.getDriver().findElement(by);
    }


    public static String getAttribute(By by, String attribute) {
        return findElement(by).getAttribute(attribute);
    }

    public static String getValue(By by) {
        String value = findElement(by).getAttribute("name");
        Logger.logPass("Fetching value of " + getCallingMethodName() + " [ " + value + " ]");
        return value;
    }

    public static boolean isPresentWithWait(By by) {
        WebDriver driver = DriverManager.getDriver();
        try {
            waitUntilElementVisible(by);
        } catch (Exception e) {

        }
        setImplicitWait(0);
        if (driver.findElements(by).size() > 0 && driver.findElement(by).isDisplayed()) {
            return true;
        } else {
            setImplicitWait(GlobalData.DEFAULT_IMPLICITWAIT);
            return false;
        }
    }


    public static String getCallingMethodName() {
        String methodName;
        try {
            String[] str = Thread.currentThread().getStackTrace()[3].getMethodName().split("_");
            String newStr = "";
            for (int i = 0; i < str.length; i++) {
                if (i == 0 || i == str.length - 1)
                    continue;
                else
                    newStr = newStr + str[i] + "_";
            }

            methodName = newStr.substring(0, newStr.length() - 1);
        } catch (Exception e) {
            methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        }
        return methodName;
    }


    public static void sendKeys(By by, String input) {
        DriverManager.getDriver().findElement(by).sendKeys(input);
    }

    public static void sendKeysWithClear(By by, String input) {
        sendKeys(by, input);
    }

    public static void clear(By by) {
        while (getAttribute(by, "value").length() > 0) {
            findElement(by).sendKeys(Keys.BACK_SPACE);
            gotoSleep(30);
        }
    }


    public static boolean isPresent(By by) {
        boolean flag = false;
        WebDriver driver = DriverManager.getDriver();
        ActionHelper.setImplicitWait(0);
        if (driver.findElements(by).size() > 0) {
            if (driver.findElement(by).isDisplayed()) {
                flag = true;
            }
        }
        ActionHelper.setImplicitWait(GlobalData.DEFAULT_IMPLICITWAIT);
        return flag;
    }

    public static boolean _waitForJStoLoad() {
        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            try {
                return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active")
                        == 0);
            } catch (Exception e) {
                return true;
            }
        };

        /**
         * wait for JavaScript to load
         */
        ExpectedCondition<Boolean> jsLoad = driver -> {
            Object rsltJs = ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState");
            if (rsltJs == null) {
                rsltJs = "";
            }
            return rsltJs.toString().equals("complete") || rsltJs.toString().equals("loaded");
        };

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), 30);
        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    public static Boolean isElelementInteractable(By by) {
        waitUntilElementVisible(by);
        WebElement element = DriverManager.getDriver().findElement(by);
        return element.isDisplayed();
    }

    public static void javascriptButtonClick(WebElement webElement) {
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        js.executeScript("arguments[0].click();", webElement);
    }

    public static void scrollPageThroughWebElement(WebElement element) {
        Actions actions = new Actions(DriverManager.getDriver());
        actions.moveToElement(element);
        actions.perform();
    }

    public static void javaScriptScroll(WebElement element) {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView();", element);
    }
}



