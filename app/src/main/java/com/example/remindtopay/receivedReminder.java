package com.example.remindtopay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class receivedReminder {
    String receiverEmail;
    String bankName;
    String accountName;
    String accountNumber;
    String amount;
    String date;
    String status;
    String notes;

    public receivedReminder(){
        //empty constructor
    };

    public receivedReminder(String receiverEmail, String bankName, String accountName, String accountNumber, String amount, String date, String status,String notes) {
        this.receiverEmail = receiverEmail;
        this.bankName = bankName;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = date;
        this.status = status;
        this.notes = notes;
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
    public String getDate() {
        return date;
    }
    public String getStatus() {
        return status;
    }
    public String getNotes() {
        return notes;
    }
}
