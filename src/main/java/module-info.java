module com.example.mj_motors {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;
    requires java.sql;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens com.example.mj_motors to javafx.fxml;
    exports com.example.mj_motors;
}