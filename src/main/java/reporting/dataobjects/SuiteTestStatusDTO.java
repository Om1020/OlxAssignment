package reporting.dataobjects;

import java.util.HashMap;

public class SuiteTestStatusDTO {

    int totalTest;
    int totalPass;
    int totalFail;
    int totalSkip;
    int productionBugs;
    HashMap<String, PackageTestStatusDTO> packageTestStatusDTOHashMap;

    public SuiteTestStatusDTO(int totalTest, int totalPass, int totalFail, int totalSkip, int productionBugs, HashMap<String, PackageTestStatusDTO> packageTestStatusDTOHashMap) {
        this.totalTest = totalTest;
        this.totalPass = totalPass;
        this.totalFail = totalFail;
        this.totalSkip = totalSkip;
        this.productionBugs = productionBugs;
        this.packageTestStatusDTOHashMap = packageTestStatusDTOHashMap;
    }

    public int getTotalTest() {
        return totalTest;
    }

    public void setTotalTest(int totalTest) {
        this.totalTest = totalTest;
    }

    public int getTotalPass() {
        return totalPass;
    }

    public void setTotalPass(int totalPass) {
        this.totalPass = totalPass;
    }

    public int getTotalFail() {
        return totalFail;
    }

    public void setTotalFail(int totalFail) {
        this.totalFail = totalFail;
    }

    public int getTotalSkip() {
        return totalSkip;
    }

    public void setTotalSkip(int totalSkip) {
        this.totalSkip = totalSkip;
    }


    public HashMap<String, PackageTestStatusDTO> getPackageTestStatusDTOHashMap() {
        return packageTestStatusDTOHashMap;
    }

    public void setPackageTestStatusDTOHashMap(HashMap<String, PackageTestStatusDTO> packageTestStatusDTOHashMap) {
        this.packageTestStatusDTOHashMap = packageTestStatusDTOHashMap;
    }

    public void incrementTotalTest(){
        this.totalTest++;
    }

    public void incrementTotalPass(){
        this.totalPass++;
    }

    public void incrementTotalFail(){
        this.totalFail++;
    }

    public void incrementTotalSkip(){
        this.totalSkip++;
    }

    @Override
    public String toString() {
        return "SuiteTestStatusDTO{" +
                "totalTest=" + totalTest +
                ", totalPass=" + totalPass +
                ", totalFail=" + totalFail +
                ", totalSkip=" + totalSkip +
                ", productionBugs=" + productionBugs +
                ", packageTestStatusDTOHashMap=" + packageTestStatusDTOHashMap +
                '}';
    }
}
