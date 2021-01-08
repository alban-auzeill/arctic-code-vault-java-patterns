package com.vaadin.tests.data.bean; // (rank 209) copied from https://github.com/vaadin/framework/blob/2e8b9d2b5b40652321aaa9d2bb2dba1041da7fc4/uitest/src/main/java/com/vaadin/tests/data/bean/BeanToValidate.java

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class BeanToValidate {
    @NotNull
    @Size(min = 3, max = 16)
    private String firstname;

    @NotNull(message = "Last name must not be empty")
    private String lastname;

    @Min(value = 18, message = "Must be 18 or above")
    @Max(150)
    private int age;

    @Digits(integer = 3, fraction = 2)
    private String decimals;

    @Pattern(regexp = "V*", message = "Must start with letter V")
    @Size(min = 3, max = 6, message = "Must contain 3 - 6 letters")
    private String nickname;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDecimals() {
        return decimals;
    }

    public void setDecimals(String decimals) {
        this.decimals = decimals;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
