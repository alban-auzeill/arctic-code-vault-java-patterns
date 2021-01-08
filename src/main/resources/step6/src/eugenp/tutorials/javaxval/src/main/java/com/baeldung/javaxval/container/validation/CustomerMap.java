package com.baeldung.javaxval.container.validation; // (rank 448) copied from https://github.com/eugenp/tutorials/blob/3fb80e088c2d523eeed5836ac8cd4871d19989b6/javaxval/src/main/java/com/baeldung/javaxval/container/validation/CustomerMap.java

import java.util.Map;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class CustomerMap {

    private Map<@Email(message = "Must be a valid email") String, @NotNull Customer> customers;

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(Map<String, Customer> customers) {
        this.customers = customers;
    }
}
