package com.example.mj_motors;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.Objects;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class OldLocalSectionController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");

    @FXML
    AnchorPane pane1, pane2, backgroundPane;

    @FXML
    AnchorPane paneOld1, paneOld2;

    @FXML
    public TableView<Cars> oldCarLocal;

    @FXML
    public TableColumn<Cars, Integer> carId;
    @FXML
    public TableColumn<Cars, String> carName;
    @FXML
    public TableColumn<Cars, Integer> carPrice;
    @FXML
    public TableColumn<Cars, String> carType;
    @FXML
    public TableColumn<Cars, Integer> carModel;
    @FXML
    public TableColumn<Cars, Integer> carCondition;
    @FXML
    public TextField searchOld, userPrice, carIdUserOld;
    @FXML
    public Label price;

    ObservableList<Cars> list = FXCollections.observableArrayList();
    ObservableList<Cars> searchList = FXCollections.observableArrayList();
    ObservableList<Cars> selectedItem = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backgroundPane.styleProperty().set("-fx-background-color:#ADD8E6");
        int car_id = 0;
        int car_price = 0;
        int car_condition = 0;
        String car_name;
        String car_type;
        int car_model = 0;

        MongoCollection<Document> collection = database.getCollection("OldLocalCars");
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

            list.add(new Cars(car_id, car_condition, car_name, car_model, car_price, car_type));
            //System.out.printf((String) owners.get(3), owners.get(3));
        }
            carId.setCellValueFactory(new PropertyValueFactory<>("carId"));
            carName.setCellValueFactory(new PropertyValueFactory<>("carName"));
            carCondition.setCellValueFactory(new PropertyValueFactory<>("carCondition"));
            carModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
            carPrice.setCellValueFactory(new PropertyValueFactory<>("carPrice"));
            carType.setCellValueFactory(new PropertyValueFactory<>("carType"));


            oldCarLocal.setItems(list);

    }

    @FXML
    protected void proceedOld() throws SQLException {
        paneOld1.setVisible(false);
        paneOld2.setVisible(true);
        System.out.println(carIdUserOld.getText());
        selectedItem.add(oldCarLocal.getSelectionModel().getSelectedItem());
        MongoCollection<Document> collection = database.getCollection("CarPrice");
        Document doc = new Document("Car Price", parseInt(String.valueOf(selectedItem.get(0).carPrice)));
        collection.insertOne(doc);
        System.out.println("Price Inserted Successfully");

        MongoCollection<Document> collection1 = database.getCollection("CarId");
        Document doc1 = new Document("Car ID", parseInt(String.valueOf(selectedItem.get(0).carId)));
        collection1.insertOne(doc1);
        System.out.println("Price Inserted Successfully");

        for(int i = 0 ; i<selectedItem.size() ; i++){
            System.out.println(selectedItem.get(i).carId);
        }
        System.out.println(selectedItem.get(0).carId);

    }


    @FXML
    protected void buyOldCar(ActionEvent actionEvent) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("buyOldCar.fxml"));
        root = loader.load();
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    protected void searchOtherOldCar(){
        paneOld2.setVisible(false);
        paneOld1.setVisible(true);
        searchOld.clear();
        oldCarLocal.setItems(list);

    }

    @FXML
    protected void back(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("localCarsSection.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void searchCar() throws SQLException{
        int car_id = 0;
        int car_price = 0;
        int car_condition = 0;
        String car_name;
        String car_type;
        int car_model = 0;

        MongoCollection<Document> collection = database.getCollection("OldLocalCars");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("Name", searchOld.getText());
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor = collection.find(searchQuery).iterator();
        if (searchOld.getText().isEmpty()) {
            carsEmptyCredentialsDialog();
        }
        while (cursor.hasNext()) {
            var doc = cursor.next();
            var owners = new ArrayList<>(doc.values());
            car_id = (int) owners.get(1);
            car_name = owners.get(2).toString();
            car_price = (int) owners.get(3);
            car_model = (int) owners.get(4);
            car_condition = (int) owners.get(5);
            car_type = owners.get(6).toString();

            searchList = FXCollections.observableArrayList();
            searchList.add(new Cars(car_id, car_condition, car_name, car_model, car_price, car_type));
            //System.out.printf((String) owners.get(3), owners.get(3));
        }

    }
    @FXML
    protected void carsNotFoundDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Message");
            alert.setContentText("Car Not Found! Please check your input text");
            alert.showAndWait();
            oldCarLocal.setItems(list);
        });

    }

    @FXML
    protected void carsEmptyCredentialsDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Message");
            alert.setContentText("Please fill the required Credentials");
            alert.showAndWait();
            oldCarLocal.setItems(list);
        });

    }

    @FXML
    protected void invalidCarDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message");
            alert.setContentText("Invalid Car Id. Try Again ");
            alert.showAndWait();
        });

    }

}
