package com.example.calendarv3.Models;

public class Member {
    private String name;
    private String lastname;
    private String login;
    private String password;
    private String familyID;

    public Member() {}

    public Member(String name, String lastname, String login, String password, String familyID) {
        this.name = name;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
        this.familyID = familyID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFamilyID() {
        return familyID;
    }

    public void setFamilyID(String familyID) {
        this.familyID = familyID;
    }
}
