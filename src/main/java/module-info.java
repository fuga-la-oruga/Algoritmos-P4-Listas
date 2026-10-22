module dev.reveles.main {
    requires javafx.controls;
    requires javafx.fxml;


    opens dev.reveles.main to javafx.fxml;
    exports dev.reveles.main;
}