module com.uady.sistemavotaciones {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires org.apache.logging.log4j;
    requires com.google.gson;

    opens com.uady.sistemavotaciones to javafx.fxml;
    exports com.uady.sistemavotaciones;
    exports com.uady.sistemavotaciones.controller;
    opens com.uady.sistemavotaciones.controller to javafx.fxml;
    exports com.uady.sistemavotaciones.model;
    opens com.uady.sistemavotaciones.model to javafx.fxml;
    exports com.uady.sistemavotaciones.util;
    opens com.uady.sistemavotaciones.util to javafx.fxml;
}