package com.example.mj_motors;

import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class OldLocalGuestController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");

    @FXML
    public TableView<Cars> oldLocalTable;

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
    public TextField searchOld;

    ObservableList<Cars> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        carPrice.setCellValueFactory(new PropertyValueFactory<>("carPrice"));
        carModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carCondition.setCellValueFactory(new PropertyValueFactory<>("carCondition"));
        carType.setCellValueFactory(new PropertyValueFactory<>("carType"));


        oldLocalTable.setItems(list);

        FilteredList<Cars> searchedCar = new FilteredList<>(list, b -> true);
        searchOld.textProperty().addListener((observable, oldValue, newValue) -> {

            searchedCar.setPredicate(Cars -> {

                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {

                    return true;

                }
                String searchval = newValue.toLowerCase(Locale.ROOT);
                if (Cars.getCarName().toLowerCase(Locale.ROOT).indexOf(searchval) > -1) {
                    return true;

                } else {
                    return false;
                }

            });


        });
        SortedList<Cars> sortedSearch = new SortedList<>(searchedCar);
        sortedSearch.comparatorProperty().bind(oldLocalTable.comparatorProperty());
        oldLocalTable.setItems(sortedSearch);

    }

    @FXML
    protected void backLocal(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("checkoutMenuLocal.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("localSection.css");
        stage.setScene(scene);
        stage.show();

    }

}
