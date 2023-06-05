package classes;

import java.util.Date;

public class DataRow implements Comparable<DataRow>{
    String issueType;
    String key;
    String status;
    Date created;
    Date transitionDateFrom;
    Date transitionDateTo;
    String transitionFrom;
    String transitionTo;
    public DataRow() {}

    public DataRow(String issueType, String key, String status, Date created, Date transitionDateFrom, Date transitionDateTo, String transitionFrom, String transitionTo) {
        this.issueType = issueType;
        this.key = key;
        this.status = status;
        this.created = created;
        this.transitionDateFrom = transitionDateFrom;
        this.transitionDateTo = transitionDateTo;
        this.transitionFrom = transitionFrom;
        this.transitionTo = transitionTo;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getTransitionDateFrom() {
        return transitionDateFrom;
    }

    public void setTransitionDateFrom(Date transitionDateFrom) {
        this.transitionDateFrom = transitionDateFrom;
    }

    public Date getTransitionDateTo() {
        return transitionDateTo;
    }

    public void setTransitionDateTo(Date transitionDateTo) {
        this.transitionDateTo = transitionDateTo;
    }

    public String getTransitionFrom() {
        return transitionFrom;
    }

    public void setTransitionFrom(String transitionFrom) {
        this.transitionFrom = transitionFrom;
    }

    public String getTransitionTo() {
        return transitionTo;
    }

    public void setTransitionTo(String transitionTo) {
        this.transitionTo = transitionTo;
    }

    @Override
    public int compareTo(DataRow o) {
        return getKey().compareTo(o.getKey());
    }
}
