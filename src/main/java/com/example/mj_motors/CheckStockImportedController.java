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

public class CheckStockImportedController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");
    @FXML
    AnchorPane checkStock;
    @FXML
    TableView<Cars> newCarImported, oldCarImported;
    @FXML
    TableColumn<Cars, Integer> carIdNew, carIdOld;
    @FXML
    TableColumn<Cars, String> carNameNew, carNameOld;
    @FXML
    TableColumn<Cars, String> carTypeNew, carTypeOld;
    @FXML
    TableColumn<Cars, Integer> carConditionNew, carConditionOld;
    @FXML
    TableColumn<Cars, Integer> carModelNew, carModelOld;
    @FXML
    TableColumn<Cars, Integer> carPriceNew, carPriceOld;
    @FXML
    TableColumn<Cars, Integer> carYearImportNew, carYearImportOld;
    @FXML
    TableColumn<Cars, Integer> carAuctionNew, carAuctionOld;
    @FXML
    TableColumn<Cars, Integer> carGradeNew, carGradeOld;

    ObservableList<Cars> newList = FXCollections.observableArrayList();
    ObservableList<Cars> oldList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkStock.styleProperty().set("-fx-background-color::#9BCCF6");

        int car_id = 0;
        int car_price = 0;
        int car_condition = 0;
        String car_name;
        String car_type;
        int car_model = 0;
        int importYear = 0;
        String auction_sheet;
        int car_grade = 0;

        MongoCollection<Document> collection = database.getCollection("NewImportedCars");
        BasicDBObject searchQuery = new BasicDBObject();
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
            importYear = (int) owners.get(7);
            auction_sheet = owners.get(8).toString();
            car_grade = (int)owners.get(9);

            newList.add(new Cars(car_id, car_condition, car_name, car_model, car_price, car_type, importYear, auction_sheet, car_grade));
            //System.out.printf((String) owners.get(3), owners.get(3));
        }
        carIdNew.setCellValueFactory(new PropertyValueFactory<>("carId"));
        carNameNew.setCellValueFactory(new PropertyValueFactory<>("carName"));
        carPriceNew.setCellValueFactory(new PropertyValueFactory<>("carPrice"));
        carModelNew.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carConditionNew.setCellValueFactory(new PropertyValueFactory<>("carCondition"));
        carTypeNew.setCellValueFactory(new PropertyValueFactory<>("carType"));
        carYearImportNew.setCellValueFactory(new PropertyValueFactory<>("carImportYear"));
        carAuctionNew.setCellValueFactory(new PropertyValueFactory<>("carAuction"));
        carGradeNew.setCellValueFactory(new PropertyValueFactory<>("carGrade"));

        newCarImported.setItems(newList);

        int car_id1 = 0;
        int car_price1 = 0;
        int car_condition1 = 0;
        String car_name1;
        String car_type1;
        int car_model1 = 0;
        int importYear1 = 0;
        String auction_sheet1;
        int car_grade1 = 0;

        MongoCollection<Document> collection1 = database.getCollection("OldImportedCars");
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

            oldList.add(new Cars(car_id, car_condition, car_name, car_model, car_price, car_type, importYear, auction_sheet, car_grade));
            //System.out.printf((String) owners.get(3), owners.get(3));
        }

        carIdOld.setCellValueFactory(new PropertyValueFactory<>("carId"));
        carNameOld.setCellValueFactory(new PropertyValueFactory<>("carName"));
        carPriceOld.setCellValueFactory(new PropertyValueFactory<>("carPrice"));
        carModelOld.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carConditionOld.setCellValueFactory(new PropertyValueFactory<>("carCondition"));
        carTypeOld.setCellValueFactory(new PropertyValueFactory<>("carType"));
        carYearImportOld.setCellValueFactory(new PropertyValueFactory<>("carImportYear"));
        carAuctionOld.setCellValueFactory(new PropertyValueFactory<>("carAuction"));
        carGradeOld.setCellValueFactory(new PropertyValueFactory<>("carGrade"));

        oldCarImported.setItems(oldList);

    }

    @FXML
    protected void back_1(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("owner2.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    protected void moreOptions(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("importedOwnOptions.fxml"));
        root = loader.load();
        //root = FXMLLoader.load(getClass().getResource("importedOwnOptions.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
