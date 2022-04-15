package src.Model;

public class Pticket {
    private String ticketId;
    private Long expirationDate;
    private Long IssueDate;

    public Pticket() {
        // empty constructor
    }

    public Pticket(String ticketId, Long expirationDate, Long issueDate) {
        this.ticketId = ticketId;
        this.expirationDate = expirationDate;
        IssueDate = issueDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
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
