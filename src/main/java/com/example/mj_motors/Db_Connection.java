package com.example.mj_motors;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.sql.*;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Db_Connection extends Application {
    public static String uri;
    public static MongoClient mongoClient;
    public static MongoDatabase database;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage.setTitle("MJ_MOTORS");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.show();

        try {
            uri = "mongodb://localhost:27017";
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("MJ_MOTORS");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }

    public void getRegistered(int userId, String userName, String userPass, int userAddress, int userPhNo, int userCNIC) {
        MongoCollection<Document> collection = database.getCollection("Customers");
        Document doc = new Document("Customer ID", userId);
        doc.append("Name", userName);
        doc.append("Password", userPass);
        doc.append("Address", userAddress);
        doc.append("Phone No", userPhNo);
        doc.append("CNIC", userCNIC);
        collection.insertOne(doc);
        System.out.println("Customer Inserted Successfully");

    }

    public boolean checkCustomerLoginCredentials(String cusName, String custPass, int custId) {
        try {
            MongoCollection<Document> collection = database.getCollection("Customers");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("Customer Id", custId);
            searchQuery.put("Name", cusName);
            searchQuery.put("Password", custPass);
            System.out.println("Retrieving specific Mongo Document");
            MongoCursor<Document> cursor = collection.find(searchQuery).iterator();
            while (cursor.hasNext()) {
                var doc = cursor.next();
                var owners = new ArrayList<>(doc.values());
                System.out.println(owners.get(1));
                System.out.println(owners.get(2));
                System.out.println(owners.get(3));
                //System.out.printf((String) owners.get(3), owners.get(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public boolean checkOwnerLoginCredentials(int ownerId, String ownerName, String ownerPass) {
        try {
            MongoCollection<Document> collection = database.getCollection("Owners");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("OwnerId", ownerId);
            searchQuery.put("Name", ownerName);
            searchQuery.put("Password", ownerPass);
            System.out.println("Retrieving specific Mongo Document");
            MongoCursor<Document> cursor = collection.find(searchQuery).iterator();
            while (cursor.hasNext()) {

                var doc = cursor.next();
                var owners = new ArrayList<>(doc.values());
                System.out.println(owners.get(3));
                System.out.println(owners.get(1));
                System.out.println(owners.get(2));
                //System.out.printf((String) owners.get(3), owners.get(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    public boolean checkManagerLoginCredentials(int ownID, int manId) {
        try {
            MongoCollection<Document> collection = database.getCollection("Managers");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("OwnerId", ownID);
            searchQuery.put("Manager Id", manId);
            System.out.println("Retrieving specific Mongo Document");
            MongoCursor<Document> cursor = collection.find(searchQuery).iterator();
            while (cursor.hasNext()) {

                var doc = cursor.next();
                var owners = new ArrayList<>(doc.values());
                System.out.println(owners.get(1));
                System.out.println(owners.get(2));
                //System.out.printf((String) owners.get(3), owners.get(3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public void addManagerLocalCarsToDb(int no, int carId, String carName, int carPrice, int carModel, int carCondition, String carType) {
        if (no == 1) {
            MongoCollection<Document> collection = database.getCollection("NewLocalCars");
            Document doc = new Document("Car ID", carId);
            doc.append("Name", carName);
            doc.append("Price", carPrice);
            doc.append("Model", carModel);
            doc.append("Condition", carCondition);
            doc.append("Car Type", carType);
            collection.insertOne(doc);
            System.out.println("Customer Inserted Successfully");
        } else if (no == 2) {
            MongoCollection<Document> collection = database.getCollection("OldLocalCars");
            Document doc = new Document("Car ID", carId);
            doc.append("Name", carName);
            doc.append("Price", carPrice);
            doc.append("Model", carModel);
            doc.append("Condition", carCondition);
            doc.append("Car Type", carType);
            collection.insertOne(doc);
            System.out.println("Customer Inserted Successfully");
        }
    }

    public void addManagerImportedCarsToDb(int no, int carId, String carName, int carPrice, int carModel, int carCondition, String carType,
                                           int carYearOfImport, String carAuctionSheet, int carGrade) {
        if (no == 1) {
            MongoCollection<Document> collection = database.getCollection("NewImportedCars");
            Document doc = new Document("Car ID", carId);
            doc.append("Name", carName);
            doc.append("Price", carPrice);
            doc.append("Model", carModel);
            doc.append("Condition", carCondition);
            doc.append("Car Type", carType);
            doc.append("Import Year", carYearOfImport);
            doc.append("Auction Sheet", carAuctionSheet);
            doc.append("Grade", carGrade);
            collection.insertOne(doc);
            System.out.println("Car Inserted Successfully");
        } else if (no == 2) {
            MongoCollection<Document> collection = database.getCollection("OldImportedCars");
            Document doc = new Document("Car ID", carId);
            doc.append("Name", carName);
            doc.append("Price", carPrice);
            doc.append("Model", carModel);
            doc.append("Condition", carCondition);
            doc.append("Car Type", carType);
            doc.append("Import Year", carYearOfImport);
            doc.append("Auction Sheet", carAuctionSheet);
            doc.append("Grade", carGrade);
            collection.insertOne(doc);
            System.out.println("Car Inserted Successfully");
        }
    }

    public void importedCarSale(int carId, String carName, int carPrice, int carModel, int carCondition, String carType,
                                int carYearOfImport, String carAuctionSheet, int carGrade) {
        MongoCollection<Document> collection = database.getCollection("ImportedCustomerSale");
        Document doc = new Document("Car ID", carId);
        doc.append("Name", carName);
        doc.append("Price", carPrice);
        doc.append("Model", carModel);
        doc.append("Condition", carCondition);
        doc.append("Car Type", carType);
        doc.append("Import Year", carYearOfImport);
        doc.append("Auction Sheet", carAuctionSheet);
        doc.append("Grade", carGrade);
        collection.insertOne(doc);
        System.out.println("Car Inserted Successfully");

    }

    public void placeImportedCarSale(int carId, String carName, int carModel, int carCondition, String carType,
                                     int carYearOfImport, String carAuctionSheet, int carGrade, int carCommission) {
        MongoCollection<Document> collection = database.getCollection("ImportedCommission");
        Document doc = new Document("Car ID", carId);
        doc.append("Name", carName);
        doc.append("Model", carModel);
        doc.append("Condition", carCondition);
        doc.append("Car Type", carType);
        doc.append("Import Year", carYearOfImport);
        doc.append("Auction Sheet", carAuctionSheet);
        doc.append("Grade", carGrade);
        doc.append("Commission", carCommission);
        collection.insertOne(doc);
        System.out.println("Car Inserted Successfully");

    }

    public void localCarSale(int carId, String carName, int carPrice, int carModel, int carCondition, String carType) {
        MongoCollection<Document> collection = database.getCollection("LocalCustomersSale");
        Document doc = new Document("Car ID", carId);
        doc.append("Name", carName);
        doc.append("Price", carPrice);
        doc.append("Model", carModel);
        doc.append("Condition", carCondition);
        doc.append("Car Type", carType);
        collection.insertOne(doc);
        System.out.println("Car Inserted Successfully");

    }

    public void placeLocalCarSale(int carId, String carName, int carModel, int carCondition, String carType, int carCommission) {
        MongoCollection<Document> collection = database.getCollection("LocalCommission");
        Document doc = new Document("Car ID", carId);
        doc.append("Name", carName);
        doc.append("Model", carModel);
        doc.append("Condition", carCondition);
        doc.append("Car Type", carType);
        doc.append("Commission", carCommission);
        collection.insertOne(doc);
        System.out.println("Car Inserted Successfully");

    }

    //    public static Connection provideConnection() throws  SQLException{
//
//        Connection connection = DriverManager.getConnection(dbURL, username, password);
//
//        return connection;
//
//    }
    public void addCartoLocalCustomerDb(int no, int carId, String carName, int carPrice, int carModel, int carCondition, String carType) {


    }


}