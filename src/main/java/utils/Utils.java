package utils;

import global.GlobalData;
import org.aeonbits.owner.ConfigFactory;

public class Utils {
    public static TestData TESTDATA;

    static {
        ConfigFactory.setProperty("env", GlobalData.ENVIRONMENT);
        TESTDATA = ConfigFactory.create(TestData.class);
    }


}
