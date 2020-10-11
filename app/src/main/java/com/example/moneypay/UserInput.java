package com.example.moneypay;

public class UserInput {
   public String firstname,lastname,email,mobile,address;

    public UserInput(String firstnameC, String lastnameC, String emailC, String mobileC, String addressC) {
        this.firstname = firstnameC;
        this.lastname = lastnameC;
        this.email = emailC;
       this.mobile = mobileC;
        this.address = addressC;
    }

    public UserInput() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
