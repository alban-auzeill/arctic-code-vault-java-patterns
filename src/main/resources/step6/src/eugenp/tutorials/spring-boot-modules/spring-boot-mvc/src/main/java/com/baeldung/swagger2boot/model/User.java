package com.baeldung.swagger2boot.model; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/spring-boot-modules/spring-boot-mvc/src/main/java/com/baeldung/swagger2boot/model/User.java

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.persistence.Entity;

@Entity
public class User {

    @Id
    private Long id;

    @NotNull(message = "First Name cannot be null")
    private String firstName;

    @Min(value = 15, message = "Age should not be less than 15")
    @Max(value = 65, message = "Age should not be greater than 65")
    private int age;

    @Email(regexp=".*@.*\\..*", message = "Email should be valid")
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

}
