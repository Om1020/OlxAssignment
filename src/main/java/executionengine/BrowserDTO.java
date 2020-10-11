package executionengine;

import org.openqa.selenium.WebDriver;

public class BrowserDTO {

    private WebDriver driver;
    private String mobileNumber;
    private String password;

    public BrowserDTO() {

    }

    public BrowserDTO(WebDriver driver) {
        this.driver = driver;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }


}
