package reporting.utils;

import executionengine.BrowserDTO;
import org.apache.commons.lang3.StringUtils;
import reporting.dataobjects.SuiteTestStatusDTO;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class GlobalData {

    public static SuiteTestStatusDTO suiteTestStatusDTO = new SuiteTestStatusDTO(0, 0, 0, 0, 0, new LinkedHashMap<>());

    public static final String OUTPUT_FOLDER_REPORT = "extentreport/";
    public static final String OUTPUT_FOLDER_SCREENSHOTS = "screenshots/";
    public static final String FILE_NAME_REPORT_SCREENSHOT = "reportscreenshot.png";
    public static final String FILE_NAME_REPORT = "extentreport.html";

    public static final int ELEMENT_TIMEOUT = 30;
    public static final int DEFAULT_IMPLICITWAIT = 20;

    public static int TOTAL_TESTCASES = 0;
    public static int TOTAL_PASS = 0;
    public static int PRODUCTION_BUGS = 0;
    public static int TOTAL_FAIL = 0;
    public static int TOTAL_SKIP = 0;
    public static int PASS_PERCENTAGE_INTVALUE = 0;
    public static int NOOFTHREADS = Integer.parseInt(System.getProperty("threads", "1"));
    public static String DRIVER_TYPE =System.getProperty("drivertype", "Chrome");
    public static String ENVIRONMENT = System.getProperty("environment", "live");
    public static String EXECUTION_TYPE = StringUtils.capitalize(System.getProperty("suitetype", "System"));
    public static String GROUP_NAMES = System.getProperty("gname", "");
    public static String LANGUAGE = StringUtils.capitalize(System.getProperty("language", "English"));
    public static boolean FLAG_UPDATEJIRA = Boolean.valueOf(System.getProperty("updatejira", "false"));
    public static String PASSPERCENTAGE = "";
    public static String JS_OPENDASHBOARD_EXTENTREPORTS = "$('a[view=\"dashboard-view\"]').click();";

    static {
        System.out.println("Environment = " + global.GlobalData.ENVIRONMENT);
        System.out.println("Execution Type = " + global.GlobalData.EXECUTION_TYPE);
        System.out.println("Language = " + global.GlobalData.LANGUAGE);
    }

}
