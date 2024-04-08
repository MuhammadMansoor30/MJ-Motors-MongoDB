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
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class AddOldImportController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public static String uri = "mongodb://localhost:27017";
    public static MongoClient mongoClient = MongoClients.create(uri);
    public static MongoDatabase database = mongoClient.getDatabase("MJ_MOTORS");

    @FXML
    AnchorPane pane1, pane2;

    @FXML
    private Spinner<Integer> spnModel, spnCondition, spnCarGrade, spnYearOfImport;

    @FXML
    private ChoiceBox<String> chBoxType, chBoxAuctionSht;

    @FXML
    private TextField carNameAdd, carPriceAdd, userCarId;
    @FXML
    TableView<Cars> oldCarImported;
    @FXML
    TableColumn<Cars, Integer> carId;
    @FXML
    TableColumn<Cars, String> carName;
    @FXML
    TableColumn<Cars, Integer> carModel;
    @FXML
    TableColumn<Cars, Integer> carPrice;
    @FXML
    TableColumn<Cars, String> carType;
    @FXML
    TableColumn<Cars, Integer> carCondition;
    @FXML
    TableColumn<Cars, Integer> carImportYear;
    @FXML
    TableColumn<Cars, String> carAuction;
    @FXML
    TableColumn<Cars, Integer> carGrade;


    private String[] carTypes = {"HatchBack", "Sedan", "Crossover", "SUV", "Sports"};

    private String[] options = {"Yes! Available", "Not Available"};

    ObservableList<Cars> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        spnCondition.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(2008, 2020, 1);
        spnModel.setValueFactory(valueFactory1);

        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(2012, 2022, 1);
        spnYearOfImport.setValueFactory(valueFactory2);

        SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        spnCarGrade.setValueFactory(valueFactory3);

        chBoxType.getItems().addAll(carTypes);

        chBoxAuctionSht.getItems().addAll(options);

        carId.setCellValueFactory(new PropertyValueFactory<>("carId"));
        carName.setCellValueFactory(new PropertyValueFactory<>("carName"));
        carPrice.setCellValueFactory(new PropertyValueFactory<>("carPrice"));
        carModel.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carCondition.setCellValueFactory(new PropertyValueFactory<>("carCondition"));
        carType.setCellValueFactory(new PropertyValueFactory<>("carType"));
        carImportYear.setCellValueFactory(new PropertyValueFactory<>("carImportYear"));
        carAuction.setCellValueFactory(new PropertyValueFactory<>("carAuction"));
        carGrade.setCellValueFactory(new PropertyValueFactory<>("carGrade"));

        oldCarImported.setItems(list);
    }

    @FXML
    protected void back(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manLoginImported.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("man.css");
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    protected void addCarFromTable(){
        pane1.setVisible(false);
        pane2.setVisible(true);
        int car_id = 0;
        int car_price = 0;
        int car_condition = 0;
        String car_name;
        String car_type;
        int car_model = 0;
        int importYear = 0;
        String auction_sheet ;
        int car_grade = 0;

        MongoCollection<Document> collection = database.getCollection("ImportedCustomerSale");
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
            car_grade = (int) owners.get(9);

            list.add(new Cars(car_id, car_condition, car_name, car_model, car_price, car_type, importYear, auction_sheet, car_grade));
        }
    }

    @FXML
    protected void goBack(){
        pane1.setVisible(true);
        pane2.setVisible(false);
    }

    @FXML
    protected void addOldImportCar(ActionEvent actionEvent) throws IOException, SQLException {

        String carName = carNameAdd.getText();
        String carPrice = carPriceAdd.getText();
        String carModel = spnModel.getValue().toString();
        String carCondition = spnCondition.getValue().toString();
        String carType = chBoxType.getValue();
        String carAuctionSheet = chBoxAuctionSht.getValue();
        String carGrade = spnCarGrade.getValue().toString();
        String carYearOfImport = spnYearOfImport.getValue().toString();
        int carId = (int) (Math.random() * 2000) + 1; // generate between 1001 and 2000

        Db_Connection add = new Db_Connection();
        if (carName.isEmpty() || carPrice.isEmpty() || chBoxType.getSelectionModel().isEmpty() || chBoxAuctionSht.getSelectionModel().isEmpty()) {
            carsEmptyCredentialsDialog();
        } else {

            if (checkInt(carPriceAdd)) {
                add.addManagerImportedCarsToDb(2, carId, carName, parseInt(carPrice), parseInt(carModel), parseInt(carCondition),
                        carType, parseInt(carYearOfImport), carAuctionSheet, parseInt(carGrade));
                carAddSuccessDialog();
                carNameAdd.clear();
                carPriceAdd.clear();


            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Caution!");
                    alert.setContentText("Oops ! Looks like some entered Values are not a number. Please Try Again ");
                    alert.showAndWait();
                });
                carNameAdd.clear();
                carPriceAdd.clear();

            }
        }

    }

    @FXML
    protected void carsEmptyCredentialsDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Message");
            alert.setContentText("Please fill the required Credentials");
            alert.showAndWait();
        });

    }

    @FXML
    private boolean checkInt(TextField carPrice) {
        try {
            Integer.parseInt(carPrice.getText());
            return true;
        } catch (NumberFormatException e) {

            return false;
        }
    }

    @FXML
    protected void carAddSuccessDialog() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Message");
            alert.setContentText("CAR HAS BEEN ADDED SUCCESSFULLY ");
            alert.showAndWait();
        });

    }
    @FXML
    protected void addCartoOldStock() throws SQLException {
        int car_id = 0;
        int car_price = 0;
        int car_condition = 0;
        String car_name;
        String car_type;
        int car_model = 0;
        int importYear = 0;
        String auction_sheet ;
        int car_grade = 0;

        MongoCollection<Document> collection = database.getCollection("ImportedCustomerSale");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("Car ID", parseInt(userCarId.getText()));
        System.out.println("Retrieving specific Mongo Document");
        MongoCursor<Document> cursor = collection.find(searchQuery).iterator();

        while (cursor.hasNext()) {
            var doc = cursor.next();
            var owners = new ArrayList<>(doc.values());
            car_id = (int)owners.get(1);
            car_name = owners.get(2).toString();
            car_price = (int) owners.get(3);
            car_model = (int) owners.get(4);
            car_condition = (int) owners.get(5);
            car_type = owners.get(6).toString();
            importYear = (int) owners.get(7);
            auction_sheet = owners.get(8).toString();
            car_grade = (int)owners.get(9);

            addCar(car_id, car_name, car_price, car_model, car_condition, car_type, importYear, auction_sheet, car_grade);

            //System.out.printf((String) owners.get(3), owners.get(3));
        }
    }

    public void addCar(int carId, String carName, int carPrice, int carModel, int carCondition, String carType, int importYear, String auctionSheet, int carGrade){
        MongoCollection<Document> collection1 = database.getCollection("OldImportedCars");
        Document document = new Document("Car ID", carId);
        document.append("Name", carName);
        document.append("Price", carPrice);
        document.append("Model", carModel);
        document.append("Condition", carCondition);
        document.append("Car Type", carType);
        document.append("Import Year", importYear);
        document.append("Auction Sheet", auctionSheet);
        document.append("Grade", carGrade);
        collection1.insertOne(document);
        System.out.println("Car Added Success");
    }
}
