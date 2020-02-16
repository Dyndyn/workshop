package com.dyndyn.workshop.persist.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String username;
    private String email;
    private String city;
    private String postalCode;
    private String country;
}
