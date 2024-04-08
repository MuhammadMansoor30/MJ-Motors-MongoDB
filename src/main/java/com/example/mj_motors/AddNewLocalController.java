package com.example.mj_motors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class AddNewLocalController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField carNameAdd, carPriceAdd;
    @FXML
    private Spinner<Integer> spnModel, spnCondition;

    @FXML
    private ChoiceBox<String> chBoxType;

    private String[] carTypes = {"HatchBack", "Sedan", "Crossover", "SUV"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(9, 10, 1);
        spnCondition.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory1 = new SpinnerValueFactory.IntegerSpinnerValueFactory(2022, 2022);
        spnModel.setValueFactory(valueFactory1);

        chBoxType.getItems().addAll(carTypes);

    }

    @FXML
    protected void back(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("manLogin.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add("man.css");
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    protected void addNewLocalCar(ActionEvent actionEvent) throws IOException, SQLException {
        String carName = carNameAdd.getText();
        String carPrice = carPriceAdd.getText();
        String carModel = spnModel.getValue().toString();
        String carCondition = spnCondition.getValue().toString();
        String carType = chBoxType.getValue();
        int carId = (int) (Math.random() * 1000) + 1;

        Db_Connection add = new Db_Connection();
        if (carName.isEmpty() || carPrice.isEmpty() || chBoxType.getSelectionModel().isEmpty()) {
            carsEmptyCredentialsDialog();
        } else {

            if (checkInt(carPriceAdd)) {
                add.addManagerLocalCarsToDb(1, carId, carName, parseInt(carPrice), parseInt(carModel), parseInt(carCondition), carType);
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
}
