package APITests.utils;

public class Order {
    private int bookId;
    private String customerName;

    public Order(int bookId, String customerName) {
        this.bookId = bookId;
        this.customerName = customerName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

}
