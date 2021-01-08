package com.example.demo.domain; // (rank 639) copied from https://github.com/wuyouzhuguli/SpringAll/blob/f630cb95bcc4af0b9e9dda6c75c46a354246f35b/46.Spring-Boot-Hibernate-Validator/src/main/java/com/example/demo/domain/User.java

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author MrBird
 */
public class User implements Serializable {
    private static final long serialVersionUID = -2731598327208972274L;

    @NotBlank(message = "{required}")
    private String name;

    @Email(message = "{invalid}")
    private String email;

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
}
