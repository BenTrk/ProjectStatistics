package classes;

import java.util.List;

public class IssueType {
    String issueType;
    List<StatusTime> statusTimeList;
    int counter = 0;

    public IssueType() {
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public IssueType(String issueType, List<StatusTime> statusTimeList) {
        this.issueType = issueType;
        this.statusTimeList = statusTimeList;
    }

    public IssueType(String issueType, List<StatusTime> statusTimeList, int counter) {
        this.issueType = issueType;
        this.statusTimeList = statusTimeList;
        this.counter = counter;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public List<StatusTime> getStatusTimeList() {
        return statusTimeList;
    }

    public void setStatusTimeList(List<StatusTime> statusTimeList) {
        this.statusTimeList = statusTimeList;
    }
}
