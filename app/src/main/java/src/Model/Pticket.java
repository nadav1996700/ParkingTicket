package src.Model;

public class Pticket {
    private Long ticketId;
    private Long expirationDate;
    private Long IssueDate;

    public Pticket() {
        // empty constructor
    }

    public Pticket(Long ticketId) {
        this.ticketId = ticketId;
        this.IssueDate = System.currentTimeMillis();
        this.expirationDate = this.IssueDate + 94608000000L; // parking ticket expires after three years
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getIssueDate() {
        return IssueDate;
    }

    public void setIssueDate(Long issueDate) {
        IssueDate = issueDate;
    }
}
