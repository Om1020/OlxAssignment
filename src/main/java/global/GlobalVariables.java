package global;

import java.util.ResourceBundle;

public class GlobalVariables {

    //Global Variables
    private static ResourceBundle masterProperties;

    public static ResourceBundle getMasterProperties() {
        return masterProperties;
    }

    public static void setMasterProperties(ResourceBundle masterProperties) {
        GlobalVariables.masterProperties = masterProperties;
    }

}

