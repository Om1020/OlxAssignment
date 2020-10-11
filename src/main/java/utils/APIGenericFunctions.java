package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Reporter;
import reporting.extentreports.ExtentManager;

import java.io.File;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.requestSpecification;

public class APIGenericFunctions {

    private APIGenericFunctions.MethodType method;
    private Response response;
    private RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
    private Object body;
    private ContentType contentType;
    private String baseUri;
    private Map<String, Object> queryParams = new HashMap();
    private Map<String, Object> params = new HashMap();
    private String basePath;
    private Map<String, Object> headers = new HashMap();
    private Boolean isCaptureExtent = true;

    public Response execute() {
        Response response;
        switch (this.method) {
            case GET:
                response = RestAssured.given().config(RestAssured.config()).spec(requestSpecification).when().get();
                break;
            default:
                throw new RuntimeException("API method not specified");
        }

        this.response = response;
        this.captureAPIDetails();
        this.printResponse(response);
        return response;
    }

    public void captureAPIDetails() {
        if (this.baseUri != null && this.basePath != null) {
            ExtentManager.addLogsInReport("Url is " + this.baseUri + this.basePath);
        }

        if (this.method != null) {
            ExtentManager.addLogsInReport("Method is " + this.method);
        }

        if (this.headers != null && this.headers.size() != 0) {
            ExtentManager.addLogsInReport("Header are " + this.headers);
        }

        if (this.queryParams != null && this.queryParams.size() != 0) {
            ExtentManager.addLogsInReport("Query Params are " + this.queryParams);
        }

        if (this.body != null) {
            ExtentManager.addLogsInReport("Request body is " + this.body);
        }

        if (this.response != null) {
            ExtentManager.addLogsInReport("Response is " + this.response.asString());
        }

    }

    private void printResponse(Response response) {
        String contentType = response.contentType();
        if (contentType.toLowerCase().contains("text/html")) {
            DateFormat timeFormat = new SimpleDateFormat("MM.dd.yyyy HH-mm-ss");
            String fileName = Reporter.getCurrentTestResult().getMethod().getMethodName() + "_" + timeFormat.format(new Date()) + ".html";
            String outputDir = Reporter.getCurrentTestResult().getTestContext().getOutputDirectory();
            outputDir = outputDir.substring(0, outputDir.lastIndexOf(File.separator)) + "/html";
            File file = new File(outputDir + File.separator + fileName);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            PrintWriter writer = null;

            try {
                file.createNewFile();
                writer = new PrintWriter(file);
                writer.write(response.asString());
                writer.flush();
                Reporter.log("<a href=\"" + fileName + "\" target=\"_blank\"><b>API Response</b></a><br>");
            } catch (Throwable var12) {
                throw new RuntimeException(var12);
            } finally {
                writer.close();
            }
        } else {
            Reporter.log("<br>API Response:" + response.getBody().prettyPrint());
        }

    }

    public enum MethodType {
        GET;
        MethodType() {
        }
    }


}
