package pageobjects.common;

import driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import reporting.assertions.CustomAssert;
import reporting.logging.Logger;
import utils.ActionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieListPage {

    private static MovieListPage instance = null;
    private By categorySearchMovie = By.xpath("//a[text()='Movie']");
    private By searchTitle = By.className("findSearchTerm");
    private By listOfMovie = By.xpath("//table[@class='findList']/tbody/tr/td[@class='result_text']/a");

    public static MovieListPage getInstance() {
        if (instance == null)
            instance = new MovieListPage();
        return instance;
    }

    public void clickOnMovieInSearchCategory() {
        ActionHelper.scrollPageThroughWebElement(DriverManager.getDriver().findElement(categorySearchMovie));
        ActionHelper.click(categorySearchMovie);
        CustomAssert.assertTrue(ActionHelper.isPresentWithWait(searchTitle), "Searched item list is displayed");
    }

    public void validateTopThreeMovieList(Map<String, Double> map) {
        boolean flag = false;
        List<String> title = new ArrayList<>();
        List<WebElement> element=DriverManager.getDriver().findElements(listOfMovie);

        //Loop runs element.size()/2 because only top 3 movies displayed in first five on UI table

        for (int i = 0; i < element.size() / 2; i++) {
            String movieTitle = DriverManager.getDriver().findElement(By.xpath("//table[@class='findList']/tbody/tr[" + (i + 1) + "]/td[@class='result_text']/a")).getText();
            title.add(movieTitle);
        }
        for (Map.Entry<String, Double> keyset : map.entrySet()) {
            if (title.contains(keyset.getKey())) {
                flag = true;
                CustomAssert.assertTrue(flag," ## Title displayed");
                Logger.logPass("Title -->"+keyset.getKey()+"  ==rating "+keyset.getValue()+ "  verified");
                flag = false;
            } else CustomAssert.assertFail(keyset.getKey() + " ## This title was not displayed on UI");
        }
    }

}
