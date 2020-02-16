package com.dyndyn.workshop.persist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class Appointment {
    private Integer id;
    private String username;
    private String trademark;
    private String companyName;
    private ZonedDateTime date;
    @JsonIgnore
    private Workshop workshop;
    @JsonIgnore
    private User user;
}
