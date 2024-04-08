package com.example.mj_motors;


public class OwnerRecord {

    public int custId;
    public int manId;
    public int carId;
    public String carName;
    public int carCondition;
    public int carModel;
    public String carType;
    public int carImportYear;
    public String carAuction;
    public int carPrice;
    public int carGrade;


    public OwnerRecord(int carId,
                       String carName,
                       String carType,
                       int carCondition,
                       int carModel, int carPrice) {

        this.carId = carId;
        this.carName = carName;
        this.carCondition = carCondition;
        this.carType = carType;
        this.carModel = carModel;
        this.carPrice = carPrice;

    }

    public OwnerRecord(int custId,
                       int manId,
                       int carId,
                       String carName,
                       String carType,
                       int carCondition,
                       int carModel, int carPrice) {

        this.manId = manId;
        this.custId = custId;
        this.carId = carId;
        this.carName = carName;
        this.carCondition = carCondition;
        this.carType = carType;
        this.carModel = carModel;
        this.carPrice = carPrice;

    }

    public OwnerRecord(int custId,
                       int manId,
                       int carId) {

        this.custId = custId;
        this.manId = manId;
        this.carId = carId;

    }

    public OwnerRecord(int carId,
                       String carName,
                       String carType,
                       int carCondition,
                       int carModel, int carPrice, int carImportYear, String carAuction, int carGrade) {

        this.carId = carId;
        this.carName = carName;
        this.carType = carType;
        this.carCondition = carCondition;
        this.carModel = carModel;
        this.carPrice = carPrice;
        this.carImportYear = carImportYear;
        this.carAuction = carAuction;
        this.carGrade = carGrade;

    }

    public OwnerRecord(int custId,
                           int manId,
                           int carId,
                           String carName,
                           String carType,
                           int carCondition,
                           int carModel, int carPrice, int carImportYear, String carAuction, int carGrade) {

        this.custId = custId;
        this.manId = manId;
        this.carId = carId;
        this.carName = carName;
        this.carType = carType;
        this.carCondition = carCondition;
        this.carModel = carModel;
        this.carPrice = carPrice;
        this.carImportYear = carImportYear;
        this.carAuction = carAuction;
        this.carGrade = carGrade;

    }

    public int getCarId() {
        return carId;
    }

    public int getManId() {
        return manId;
    }

    public int getCustId() {
        return custId;
    }

    public int getCarCondition() {
        return carCondition;
    }

    public int getCarGrade() {
        return carGrade;
    }

    public int getCarImportYear() {
        return carImportYear;
    }

    public int getCarModel() {
        return carModel;
    }

    public int getCarPrice() {
        return carPrice;
    }

    public String getCarAuction() {
        return carAuction;
    }

    public String getCarName() {
        return carName;
    }

    public String getCarType() {
        return carType;
    }
}



