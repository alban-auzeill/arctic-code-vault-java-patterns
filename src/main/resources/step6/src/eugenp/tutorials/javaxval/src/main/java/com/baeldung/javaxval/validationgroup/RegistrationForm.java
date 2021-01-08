package com.baeldung.javaxval.validationgroup; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/javaxval/src/main/java/com/baeldung/javaxval/validationgroup/RegistrationForm.java

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegistrationForm {
    @NotBlank(groups = BasicInfo.class)
    private String firstName;
    @NotBlank(groups = BasicInfo.class)
    private String lastName;
    @Email(groups = BasicInfo.class)
    private String email;
    @NotBlank(groups = BasicInfo.class)
    private String phone;

    @NotBlank(groups = { BasicInfo.class, AdvanceInfo.class })
    private String captcha;

    @NotBlank(groups = AdvanceInfo.class)
    private String street;
    @NotBlank(groups = AdvanceInfo.class)
    private String houseNumber;
    @NotBlank(groups = AdvanceInfo.class)
    private String zipCode;
    @NotBlank(groups = AdvanceInfo.class)
    private String city;
    @NotBlank(groups = AdvanceInfo.class)
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
