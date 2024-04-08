package com.example.mj_motors;

import com.mongodb.Block;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;

public class MoreOptionsImportedController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");
    @FXML
    AnchorPane ownOptions;
    @FXML
    TableView<OwnerOptions> optionsTable;

    @FXML
    TableColumn<OwnerOptions, Integer> carId;
    @FXML
    TableColumn<OwnerOptions, String> carName;

    @FXML
    TableColumn<OwnerOptions, Integer> carPrice;

    @FXML
    TableColumn<OwnerOptions, Integer> manId;

    @FXML
    TableColumn<OwnerOptions, String> manName;

    @FXML
    TableColumn<OwnerOptions, Integer> manPhNo;
    @FXML
    TableColumn<OwnerOptions, Integer> custId;


    ObservableList<OwnerOptions> customerNotBuyCarList = FXCollections.observableArrayList();
    ObservableList<OwnerOptions> customerBuyMoreThan1List = FXCollections.observableArrayList();
    ObservableList<OwnerOptions> carCommissionList = FXCollections.observableArrayList();
    ObservableList<OwnerOptions> customerSoldCarList = FXCollections.observableArrayList();
    ObservableList<OwnerOptions> managerSoldCarList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ownOptions.styleProperty().set("-fx-background-color::#9BCCF6");
    }
    @FXML
    protected void back(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("checkStockOwn2.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void customerNotBuyCarCheck(ActionEvent actionEvent) throws IOException {
            custId.setCellValueFactory(new PropertyValueFactory<>("custId"));

            customerBuyMoreThan1List = FXCollections.observableArrayList();
            carCommissionList = FXCollections.observableArrayList();
            customerSoldCarList = FXCollections.observableArrayList();
            managerSoldCarList = FXCollections.observableArrayList();


            optionsTable.setItems(customerNotBuyCarList);


    }

    @FXML
    protected void customerBuyMoreThan1(ActionEvent actionEvent) throws IOException {

            custId.setCellValueFactory(new PropertyValueFactory<>("custId"));


            customerNotBuyCarList = FXCollections.observableArrayList();
            carCommissionList = FXCollections.observableArrayList();
            customerSoldCarList = FXCollections.observableArrayList();
            managerSoldCarList = FXCollections.observableArrayList();


            optionsTable.setItems(customerBuyMoreThan1List);

    }

    @FXML
    protected void carCommission(ActionEvent actionEvent) throws IOException {
        MongoCollection<Document> collection = database.getCollection("ImportedCommission");
        Block<Document> printBlock = document -> {
            int car_id = 0;
            String car_name;
            MongoCursor<Document> cursor = collection.find(document).iterator();
            while (cursor.hasNext()) {
                var doc = cursor.next();
                var owners = new ArrayList<>(doc.values());
                car_id = (int) owners.get(1);
                car_name = owners.get(2).toString();

                carCommissionList.add(new OwnerOptions(car_id, car_name));
            }
            System.out.println(document);

        };
        for (Document document : collection.aggregate(Arrays.asList(Aggregates.match(eq("Commission", 40000))))) {
            printBlock.apply(document);
        }
            carId.setCellValueFactory(new PropertyValueFactory<>("carId"));
            carName.setCellValueFactory(new PropertyValueFactory<>("carName"));

            customerNotBuyCarList = FXCollections.observableArrayList();
            customerBuyMoreThan1List = FXCollections.observableArrayList();
            customerSoldCarList = FXCollections.observableArrayList();
            managerSoldCarList = FXCollections.observableArrayList();

            optionsTable.setItems(carCommissionList);

    }

    @FXML
    protected void customerSoldCar(ActionEvent actionEvent) throws IOException {
        MongoCollection<Document> collection = database.getCollection("ImportedCustomerSale");
        Block<Document> printBlock = document -> {
            int car_id = 0;
            int car_price = 0;
            String car_name;
            MongoCursor<Document> cursor = collection.find(document).iterator();
            while (cursor.hasNext()) {
                var doc = cursor.next();
                var owners = new ArrayList<>(doc.values());
                car_id = (int)owners.get(1);
                car_name = owners.get(2).toString();
                car_price = (int) owners.get(3);

                customerSoldCarList.add(new OwnerOptions(car_name, car_price));
            }
            System.out.println(document);

        };
        for (Document document : collection.aggregate(Arrays.asList(Aggregates.match(eq("Car Type", "HatchBack"))))) {
            printBlock.apply(document);
        }
            carName.setCellValueFactory(new PropertyValueFactory<>("carName"));
            carPrice.setCellValueFactory(new PropertyValueFactory<>("carPrice"));

            customerNotBuyCarList = FXCollections.observableArrayList();
            customerBuyMoreThan1List = FXCollections.observableArrayList();
            carCommissionList = FXCollections.observableArrayList();
            managerSoldCarList = FXCollections.observableArrayList();


            optionsTable.setItems(customerSoldCarList);


    }

    @FXML
    protected void managerSoldCar(ActionEvent actionEvent) throws IOException {
        String name;
        int phone_no = 0;
        int man_id = 0;
        MongoCollection<Document> collection = database.getCollection("Managers");
        List<Bson> pipeline = Arrays.asList(new Document("$lookup", new Document()
                .append("from", "ImportedPurchase")
                .append("localField", "ManagerId")
                .append("foreignField", "ManagerId")
                .append("as", "Manager_Sold_Cars")), new Document("$project", new Document()
                .append("_id", 0)
                .append("ManagerId", 1)
                .append("Name", 1)
                .append("Phone No", 1)
                .append("Manager_Sold_Cars", 1)), new Document("$match", new Document("ManagerId", 21)));

        AggregateIterable<Document> aggregateIterable = collection.aggregate(pipeline);
        Document cursor = aggregateIterable.first();
        ArrayList<Document> arr = (ArrayList<Document>) cursor.get("Manager_Sold_Cars");

        name = cursor.getString("Name");
        phone_no = cursor.getInteger("Phone No");
        man_id = cursor.getInteger("ManagerId");
        managerSoldCarList.add(new OwnerOptions(man_id, name, phone_no));
            manId.setCellValueFactory(new PropertyValueFactory<>("manId"));
            manName.setCellValueFactory(new PropertyValueFactory<>("manName"));
            manPhNo.setCellValueFactory(new PropertyValueFactory<>("manPhNo"));

            customerNotBuyCarList = FXCollections.observableArrayList();
            customerBuyMoreThan1List = FXCollections.observableArrayList();
            carCommissionList = FXCollections.observableArrayList();
            customerSoldCarList = FXCollections.observableArrayList();

            optionsTable.setItems(managerSoldCarList);



    }
}
