package reporting.dataobjects;

public class PackageTestStatusDTO implements Comparable<PackageTestStatusDTO> {

    String packageName;
    int totalTest;
    int totalPass;
    int totalFail;
    int totalSkip;
    int productionBugs;

    public PackageTestStatusDTO(String packageName, int totalTest, int totalPass, int totalFail, int totalSkip, int productionBugs) {
        this.packageName = packageName;
        this.totalTest = totalTest;
        this.totalPass = totalPass;
        this.totalFail = totalFail;
        this.totalSkip = totalSkip;
        this.productionBugs = productionBugs;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
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

    @Override
    public int compareTo(PackageTestStatusDTO o) {
        return o.getTotalFail() - this.getTotalFail();
    }
}
