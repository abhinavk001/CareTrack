package com.mongodb.rest;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

@ApplicationScoped
public class MongoProducer {

    @Inject
    @ConfigProperty(name = "mongo.user")
    String user;

    @Inject
    @ConfigProperty(name="mongo.pass")
    String pass;

    @Inject
    @ConfigProperty(name = "mongo.dbname", defaultValue = "testdb")
    String dbName;

    
    @Produces
    public MongoClient createClient(){
        String connectionString = String.format("mongodb+srv://%s:%s@taskapp.g7tme.mongodb.net/%s?retryWrites=true&w=majority",user,pass,dbName);
        MongoClient mongoClient = MongoClients.create(connectionString);

        return mongoClient;
    }

    @Produces
    public MongoDatabase createDB(
            MongoClient client) {
        return client.getDatabase(dbName);
    }

    public void close(
            @Disposes MongoClient toClose) {
        toClose.close();
    }
}
