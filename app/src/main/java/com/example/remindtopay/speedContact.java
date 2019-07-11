package com.example.remindtopay;

public class speedContact {
    String receiverEmail;
    String bankName;
    String accountName;
    String accountNumber;
    String amount;


    public speedContact(){
        //empty constructor
    };

    public speedContact(String receiverEmail, String bankName, String accountName, String accountNumber, String amount) {
        this.receiverEmail = receiverEmail;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.amount = amount;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAmount() {
        return amount;
    }
}
