package com.dyndyn.workshop.persist.model;

import lombok.Data;

import java.util.List;

@Data
public class Workshop {
    private Integer id;
    private String companyName;
    private List<String> specialties;
    private String city;
    private String postalCode;
    private String country;
}
