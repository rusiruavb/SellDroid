package com.example.selldroid_final;

public class ConformedPayment {
    private String paymentId;
    private String date;
    private String totalPrice;

    public ConformedPayment() {}

    public ConformedPayment(String paymentId, String date, String totalPrice) {
        this.paymentId = paymentId;
        this.date = date;
        this.totalPrice = totalPrice;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getDate() {
        return date;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
