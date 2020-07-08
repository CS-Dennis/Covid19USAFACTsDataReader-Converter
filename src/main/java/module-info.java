module org.covid19.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.covid19.application to javafx.fxml;
    exports org.covid19.application;
}