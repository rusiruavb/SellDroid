package com.example.selldroid_final;

public class PaymentMethod {
    private String paymentMethodId;
    private String holderName;
    private String cardNumber;
    private String cardCVV;
    private String validationPeriod;
    private String phoneNumber;

    public PaymentMethod() {}

    public PaymentMethod(String paymentMethodId, String holderName, String cardNumber, String cardCVV, String validationPeriod, String phoneNumber) {
        this.paymentMethodId = paymentMethodId;
        this.holderName = holderName;
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.validationPeriod = validationPeriod;
        this.phoneNumber = phoneNumber;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardCVV() {
        return cardCVV;
    }

    public String getValidationPeriod() {
        return validationPeriod;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
