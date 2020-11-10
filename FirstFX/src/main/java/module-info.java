module org.ganalyst.test.FirstFX {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.ganalyst.test.FirstFX to javafx.fxml;
    exports org.ganalyst.test.FirstFX;
}