package models;

public class Payment {
    private int paymentId;
    private String paymentType;
    private String paymentDescription;

    public Payment() {
    }

    public Payment(int paymentId, String paymentType, String paymentDescription) {
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.paymentDescription = paymentDescription;
    }
    
    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }
    
    
}
