package pageobjects.common;

import org.openqa.selenium.By;
import reporting.assertions.CustomAssert;
import utils.ActionHelper;

public class HomePage {

    private static HomePage instance=null;
    private By searchIcon=By.id("suggestion-search");
    private By searchButton=By.id("suggestion-search-button");
    private By categorySearch=By.xpath("//*[text()='Category Search']");

    public static HomePage getInstance(){
        if (instance==null)
            instance=new HomePage();
        return instance;
    }

    public void searchForMovies(String movieName){
        ActionHelper.sendKeys(searchIcon,movieName);
        ActionHelper.click(searchButton);
        ActionHelper._waitForJStoLoad();
        CustomAssert.assertTrue(ActionHelper.isPresentWithWait(categorySearch),"category search title displayed");
    }

}
