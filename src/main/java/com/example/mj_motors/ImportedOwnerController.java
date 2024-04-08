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

public class ImportedOwnerController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");

    @FXML
    AnchorPane owner2;
    @FXML
    TableView<OwnerRecord> importedRecord;
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
    TableColumn<OwnerRecord, String> carType;
    @FXML
    TableColumn<OwnerRecord, Integer> carCondition;
    @FXML
    TableColumn<OwnerRecord, Integer> carModel;
    @FXML
    TableColumn<OwnerRecord, Integer> carPrice;
    @FXML
    TableColumn<OwnerRecord, Integer> carImportYear;
    @FXML
    TableColumn<OwnerRecord, String> carAuction;
    @FXML
    TableColumn<OwnerRecord, Integer> carGrade;

    ObservableList<OwnerRecord> listPurchase = FXCollections.observableArrayList();
    ObservableList<OwnerRecord> listCars = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        owner2.styleProperty().set("-fx-background-color::#9BCCF6");
        int car_id = 0;
        int cust_id = 0;
        int man_id = 0;
        int car_price = 0;
        int car_condition = 0;
        String car_name;
        String car_type;
        int car_model = 0;
        int importYear = 0;
        String auction_sheet;
        int car_grade = 0;

        MongoCollection<Document> collection = database.getCollection("ImportedPurchase");
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
            importYear = (int) owners.get(7);
            auction_sheet = owners.get(8).toString();
            car_grade = (int)owners.get(9);

            listCars.add(new OwnerRecord(cust_id, car_name, car_type, car_condition, car_model, car_price, importYear, auction_sheet, car_grade));
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
            importYear = (int) owners.get(7);
            auction_sheet = owners.get(8).toString();
            car_grade = (int)owners.get(9);

            listCars.add(new OwnerRecord(cust_id, car_name, car_type, car_condition, car_model, car_price, importYear, auction_sheet, car_grade));

        }
            newCarId.setCellValueFactory(new PropertyValueFactory<>("newCarId"));
            oldCarId.setCellValueFactory(new PropertyValueFactory<>("oldCarId"));
            carName.setCellValueFactory(new PropertyValueFactory<>("carName"));
            carPrice.setCellValueFactory(new PropertyValueFactory<>("carPrice"));
            carModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
            carCondition.setCellValueFactory(new PropertyValueFactory<>("carCondition"));
            carType.setCellValueFactory(new PropertyValueFactory<>("carType"));
            carImportYear.setCellValueFactory(new PropertyValueFactory<>("carImportYear"));
            carAuction.setCellValueFactory(new PropertyValueFactory<>("carAuction"));
            carGrade.setCellValueFactory(new PropertyValueFactory<>("carGrade"));
            custId.setCellValueFactory(new PropertyValueFactory<>("custId"));
            manId.setCellValueFactory(new PropertyValueFactory<>("manId"));

            importedRecord.setItems(listCars);

    }
    @FXML
    protected void checkAllStock_2(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("checkStockOwn2.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
