package io.micronaut.inject.configproperties; // (rank 317) copied from https://github.com/micronaut-projects/micronaut-core/blob/74b67f092917e52b27b918f90cc05f30eb05ae78/inject-java/src/test/groovy/io/micronaut/inject/configproperties/Pojo.java

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Introspected
public class Pojo {

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

