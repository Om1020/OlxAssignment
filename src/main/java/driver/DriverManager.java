package driver;

import executionengine.BrowserDTO;
import org.openqa.selenium.WebDriver;

/**
 * This class is used to set and return the driver instance which is assigned
 * to particular thread
 *
 * @author Ankit Gupta
 */
public class DriverManager {

    private static ThreadLocal<BrowserDTO> browserDTO = new ThreadLocal<>();

    public static BrowserDTO getbrowserDTO() {
        return browserDTO.get();
    }

    public static void setbrowserDTO(BrowserDTO browserDTO) {
        DriverManager.browserDTO.set(browserDTO);
    }

    public static WebDriver getDriver() {
        return getbrowserDTO().getDriver();
    }

    public static void setDriver(WebDriver driver) {
        getbrowserDTO().setDriver(driver);
    }

}
