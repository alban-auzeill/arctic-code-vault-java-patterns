package com.example.demo.controller; // (rank 639) copied from https://github.com/wuyouzhuguli/SpringAll/blob/f630cb95bcc4af0b9e9dda6c75c46a354246f35b/46.Spring-Boot-Hibernate-Validator/src/main/java/com/example/demo/controller/TestController.java

import com.example.demo.domain.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author MrBird
 */
@RestController
@Validated
public class TestController {

    @GetMapping("test1")
    public String test1(
            @NotBlank(message = "{required}") String name,
            @Email(message = "{invalid}") String email) {
        return "success";
    }

    @GetMapping("test2")
    public String test2(@Valid User user) {
        return "success";
    }
}

