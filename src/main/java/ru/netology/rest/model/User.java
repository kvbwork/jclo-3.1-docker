package ru.netology.rest.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
public class User {

    @NotNull
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 100)
    private final String password;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
