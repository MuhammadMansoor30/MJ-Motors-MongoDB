package com.example.mj_motors;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LocalOwnerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");

    @FXML
    AnchorPane owner1;
    @FXML
    TableView<OwnerRecord> localRecord;
    @FXML
    TableColumn<OwnerRecord, Integer> newCarId;
    @FXML
    TableColumn<OwnerRecord, Integer> oldCarId;
    @FXML
    TableColumn<OwnerRecord, Integer> manId;
    @FXML
    TableColumn<OwnerRecord, Integer> custId;
    @FXML
    TableColumn<OwnerRecord, String> carName;
    @FXML
    TableColumn<OwnerRecord, Integer> carCondition;
    @FXML
    TableColumn<OwnerRecord, Integer> carModel;

    ObservableList<OwnerRecord> listPurchase = FXCollections.observableArrayList();
    ObservableList<OwnerRecord> listCars = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        owner1.styleProperty().set("-fx-background-color::#9BCCF6");

        int car_id = 0;
        int cust_id = 0;
        int man_id = 0;
        int car_price = 0;
        int car_condition = 0;
        String car_name;
        String car_type;
        int car_model = 0;

        MongoCollection<Document> collection = database.getCollection("LocalPurchase");
        BasicDBObject searchQuery = new BasicDBObject();
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor = collection.find(searchQuery).iterator();
        while (cursor.hasNext()) {
            var doc = cursor.next();
            var owners = new ArrayList<>(doc.values());
            car_id = (int) owners.get(1);
            cust_id = (int) owners.get(2);
            man_id = (int) owners.get(3);

            listPurchase.add(new OwnerRecord(cust_id, man_id, car_id));
            //System.out.printf((String) owners.get(3), owners.get(3));
        }
        MongoCollection<Document> collection1 = database.getCollection("LocalNewCars");
        BasicDBObject searchQuery1 = new BasicDBObject();
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor1 = collection1.find(searchQuery1).iterator();
        while (cursor1.hasNext()) {
            var doc = cursor1.next();
            var owners = new ArrayList<>(doc.values());
            car_id = (int) owners.get(1);
            car_name = owners.get(2).toString();
            car_price = (int) owners.get(3);
            car_model = (int) owners.get(4);
            car_condition = (int) owners.get(5);
            car_type = owners.get(6).toString();

            listCars.add(new OwnerRecord(car_id, car_name, car_type, car_condition, car_model, car_price));
        }
        MongoCollection<Document> collection2 = database.getCollection("LocalOldCars");
        BasicDBObject searchQuery2 = new BasicDBObject();
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor2 = collection2.find(searchQuery2).iterator();
        while (cursor2.hasNext()) {
            var doc = cursor2.next();
            var owners = new ArrayList<>(doc.values());
            car_id = (int) owners.get(1);
            car_name = owners.get(2).toString();
            car_price = (int) owners.get(3);
            car_model = (int) owners.get(4);
            car_condition = (int) owners.get(5);
            car_type = owners.get(6).toString();

            listCars.add(new OwnerRecord(car_id, car_name, car_type, car_condition, car_model, car_price));

        }


        custId.setCellValueFactory(new PropertyValueFactory<>("custId"));
        manId.setCellValueFactory(new PropertyValueFactory<>("manId"));
        newCarId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        oldCarId.setCellValueFactory(new PropertyValueFactory<>("oldCarId"));
        carName.setCellValueFactory(new PropertyValueFactory<>("carName"));
        carCondition.setCellValueFactory(new PropertyValueFactory<>("carCondition"));
        carModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));

        localRecord.setItems(listCars);

    }

    @FXML
    protected void checkAllStock_1(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("checkStockOwn1.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
