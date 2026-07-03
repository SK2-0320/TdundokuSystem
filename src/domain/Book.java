package domain;

import java.util.Date;

public class Book {
    private String isbnCode;
    private String bookName;
    private String writerName;
    private String status;
    private int price;
    private String registeredDate;
    private int rating;
    private String review;
    private Date purchaseDate;

    public Book() {
    }

    public String getIsbnCode() {
        return isbnCode;
    }

    public void setIsbnCode(String isbnCode) {
        this.isbnCode = isbnCode;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getTitle() {
        return bookName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "Book{"
                + "isbnCode='" + isbnCode + '\''
                + ", bookName='" + bookName + '\''
                + ", writerName='" + writerName + '\''
                + ", status='" + status + '\''
                + ", price=" + price
                + ", registeredDate='" + registeredDate + '\''
                + ", rating=" + rating
                + ", review='" + review + '\''
                + ", purchaseDate=" + purchaseDate
                + '}';
    }
}
