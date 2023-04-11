package com.mongodb.rest;

import java.util.Set;

import org.bson.Document;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.Json;

@Path("/patient")
public class PatientController {

    @Inject
    MongoDatabase db;

    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String ping(){
        return "Hello World";
    }

    @Inject
    Validator validator;

    private JsonArray getViolations(Patient patient) {
        Set<ConstraintViolation<Patient>> violations = validator.validate(
                patient);

        JsonArrayBuilder messages = Json.createArrayBuilder();

        for (ConstraintViolation<Patient> v : violations) {
            messages.add(v.getMessage());
        }

        return messages.build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Successfully added patient."),
        @APIResponse(
            responseCode = "400",
            description = "Invalid patient configuration.") })
    @Operation(summary = "Add a new patient to the database.")
    public Response add(Patient patient) {
        JsonArray violations = getViolations(patient);

        if (!violations.isEmpty()) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(violations.toString())
                    .build();
        }

        MongoCollection<Document> patientCollection = db.getCollection("patients");

        Document newPatient = new Document();
        newPatient.put("Name", patient.getName());
        newPatient.put("PatientID", patient.getPatientID());

        patientCollection.insertOne(newPatient);

        return Response
            .status(Response.Status.OK)
            .entity(newPatient.toJson())
            .build();
    }

}
