package com.example.phenomenon.newapp;

/**
 * Created by PHENOMENON on 22/12/2015.
 */
public class Customer {
    String acct;
    String name;
    String location;

    public Customer() {
    }

    public Customer(String acct, String name, String location) {
        this.acct = acct;
        this.name = name;
        this.location = location;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
