package models;

public class Complaint {
    private int complaintId;
    private String complaintDescription;
    private String file_date;
    private String accountUsername;

    public Complaint() {
    }

    public Complaint(int complaintId, String complaintDescription, String file_date, String accountUsername) {
        this.complaintId = complaintId;
        this.complaintDescription = complaintDescription;
        this.file_date = file_date;
        this.accountUsername = accountUsername;
    }
    
    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public String getFile_date() {
        return file_date;
    }

    public void setFile_date(String file_date) {
        this.file_date = file_date;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }
    
}
