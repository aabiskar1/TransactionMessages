package com.example.remindtopay;

public class UserDetails {
    String first_name;
    String last_Name;
    String email;
    String dateofbirth;
    String registered_date;

    public UserDetails() {
        //Empty Constructor
    }

    public UserDetails(String first_name, String last_Name, String email, String dateofbirth, String registered_date) {
        this.first_name = first_name;
        this.last_Name = last_Name;
        this.email = email;
        this.dateofbirth = dateofbirth;
        this.registered_date = registered_date;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public String getEmail() {
        return email;
    }

    public String getDateofbirth() {
        return dateofbirth;
    }

    public String getRegistered_date() {
        return registered_date;
    }
}