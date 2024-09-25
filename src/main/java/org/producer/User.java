package org.producer;

public class User {
    private int id;
    private String name;

    private String email;

    private int age;

    private String gender;

    private String password;

    private boolean cookieConsent;

    private boolean dataConsent;

    User(int id, String name, String email, int age, String gender, String password, boolean cookieConsent, boolean dataConsent) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.password = password;
        this.cookieConsent = cookieConsent;
        this.dataConsent = dataConsent;
    }

    public boolean isDataConsent() {
        return dataConsent;
    }

    public void setDataConsent(boolean dataConsent) {
        this.dataConsent = dataConsent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCookieConsent() {
        return cookieConsent;
    }

    public void setCookieConsent(boolean cookieConsent) {
        this.cookieConsent = cookieConsent;
    }
}
