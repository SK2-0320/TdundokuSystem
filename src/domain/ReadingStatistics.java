package domain;

public class ReadingStatistics {
    private int unreadCount;
    private int readingCount;
    private int finishedCount;
    private int totalCount;
    private double averageBooksPerMonth;

    public int getUnreadCount() {
        return unreadCount;
    }

    public int getReadingCount() {
        return readingCount;
    }

    public int getFinishedCount() {
        return finishedCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public double getAverageBooksPerMonth() {
        return averageBooksPerMonth;
    }
}
