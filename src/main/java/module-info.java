module com.triosstudent.csd214_test3_johncarlo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.triosstudent.csd214_test3_johncarlo to javafx.fxml;
    exports com.triosstudent.csd214_test3_johncarlo;
}