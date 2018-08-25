package quoters;

public class TerminatorQuoter implements Quoter {

    private String message;

    public void sayQuote() {
        System.out.println("message = " + message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
