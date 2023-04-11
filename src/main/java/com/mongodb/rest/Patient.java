package com.mongodb.rest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class Patient {

    @NotEmpty(message = "All patients must have name.")
    private String name;

    @Pattern(regexp = "^\\d+$",
            message = "ID Number must be a non-negative integer!")
    private String patientID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
    
}
