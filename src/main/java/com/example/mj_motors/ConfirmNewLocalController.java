package com.example.mj_motors;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class ConfirmNewLocalController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");
    @FXML
    AnchorPane pane1, pane2, buyNewCar;
    @FXML
    TextField userPrice;
    @FXML
    Label price, carId;
    public int car_ID;
    public int cust_ID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buyNewCar.styleProperty().set("-fx-background-color:#ADD8E6");

        int car_price = 0;
        MongoCollection<Document> collection = database.getCollection("CarPrice");
        BasicDBObject searchQuery = new BasicDBObject();
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor = collection.find(searchQuery).iterator();
        while (cursor.hasNext()) {
            var doc = cursor.next();
            var owners = new ArrayList<>(doc.values());
            car_price = (int) owners.get(1);
            System.out.println(car_price);
            //System.out.printf((String) owners.get(3), owners.get(3));
        }
        int car_id = 0;
        MongoCollection<Document> collection1 = database.getCollection("CarId");
        BasicDBObject searchQuery1 = new BasicDBObject();
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor1 = collection1.find(searchQuery1).iterator();
        while (cursor1.hasNext()) {
            var doc = cursor1.next();
            var owners = new ArrayList<>(doc.values());
            car_id = (int) owners.get(1);
            car_ID = car_id;
            System.out.println(car_price);
        }
        int cust_id = 0;
        MongoCollection<Document> collection2 = database.getCollection("Customers");
        BasicDBObject searchQuery2 = new BasicDBObject();
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor2 = collection2.find(searchQuery2).iterator();
        while (cursor2.hasNext()) {
            var doc = cursor2.next();
            var owners = new ArrayList<>(doc.values());
            cust_id = (int) owners.get(1);
            cust_ID = cust_id;
            System.out.println(cust_id);
        }
        price.setText(String.valueOf(car_price));
    }

    @FXML
    protected void backToMenu(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("localCarsSection.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add("localSection.css");
        stage.show();

    }

    @FXML
    protected void confirmPurchase() throws SQLException {
//        pane1.setVisible(false);
//        pane2.setVisible(true);
        int labelPrice = parseInt(price.getText());
        int priceOfUser = parseInt(userPrice.getText());
        if (priceOfUser < labelPrice) {
            sorryDialog();
            userPrice.clear();
        } else if (priceOfUser >= labelPrice) {
            carsToCust();
            successDialog();
            userPrice.clear();

        }
    }

    protected void sorryDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Message");
            alert.setContentText("We Cannot Sell This Car for this price ");
            alert.showAndWait();
        });

    }

    protected void successDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Message");
            alert.setContentText("Congratulation You have Purchased this Car");
            alert.showAndWait();

        });

    }


    @FXML
    public void carsToCust() throws SQLException {
        if (car_ID != 0){
            int car_id = 0;
            int car_price = 0;
            int car_condition = 0;
            String car_name ;
            String car_type ;
            int car_model = 0;
            MongoCollection<Document> collection = database.getCollection("NewLocalCars");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("Car ID", car_ID);
            System.out.println("Retrieving specific Mongo Document");
            MongoCursor<Document> cursor = collection.find(searchQuery).iterator();
            while (cursor.hasNext()) {
                var doc = cursor.next();
                var owners = new ArrayList<>(doc.values());
                car_id = (int) owners.get(1);
                car_name = owners.get(2).toString();
                car_price = (int) owners.get(3);
                car_model = (int) owners.get(4);
                car_condition = (int) owners.get(5);
                car_type = owners.get(6).toString();

                addCar(car_id, car_name, car_price, car_model, car_condition, car_type);
                addPurchase(car_id, cust_ID, 11);
                deleteCar(car_id);

            }
        }

    }

    public void addCar(int carId, String carName, int carPrice, int carModel, int carCondition, String carType){
        MongoCollection<Document> collection1 = database.getCollection("LocalNewCars");
        Document document = new Document("Car ID", carId);
        document.append("Name", carName);
        document.append("Price", carPrice);
        document.append("Model", carModel);
        document.append("Condition", carCondition);
        document.append("Car Type", carType);
        collection1.insertOne(document);
        System.out.println("Car Added Success");
    }

    public void addPurchase(int carId, int custId, int manId){
        MongoCollection<Document> collection1 = database.getCollection("LocalPurchase");
        Document document = new Document("Car ID", carId);
        document.append("Customer ID", custId);
        document.append("Manager Id", manId);
        collection1.insertOne(document);
        System.out.println("Car Added Success");
    }
    public void deleteCar(int carId){
        MongoCollection<Document> collection1 = database.getCollection("NewLocalCars");
        collection1.deleteOne(Filters.eq("Car ID", carId));
        System.out.println("Car Deleted Success");
    }


}


