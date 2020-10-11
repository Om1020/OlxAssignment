package testsuite.tests;

import apibuilder.APIRequestResponseBuilder;
import org.testng.annotations.Test;
import pageobjects.common.HomePage;
import pageobjects.common.MovieListPage;
import testsuite.basetest.BaseTestClass;

import java.util.Map;

import static utils.Constants.SEARCH_TEXT;

public class MovieSearchTest extends BaseTestClass {

    @Test(groups = {"sanity"}, description = "Verify that top 3 movie with highest rating displayed on UI")
    public void TC01_VerifyThatTopThreeMovieDisplayedOnUI() {
        APIRequestResponseBuilder apiRequestResponseBuilder = APIRequestResponseBuilder.getInstance();
        Map<String, Double> titleWithRating = apiRequestResponseBuilder.getTopThreeMovieWithTitleAndRating();
        HomePage homePage = HomePage.getInstance();
        homePage.searchForMovies(SEARCH_TEXT);
        MovieListPage movieListPage = MovieListPage.getInstance();
        movieListPage.clickOnMovieInSearchCategory();
        movieListPage.validateTopThreeMovieList(titleWithRating);

    }
}
