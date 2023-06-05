package classes;

public class StatusTime {
    String status;
    long timeSpent;

    int counter = 0;
    public StatusTime() {
    }

    public StatusTime(String status, long timeSpent, int counter) {
        this.status = status;
        this.timeSpent = timeSpent;
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public StatusTime(String status, long timeDifference) {
        this.status = status;
        this.timeSpent = timeDifference;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }
}
