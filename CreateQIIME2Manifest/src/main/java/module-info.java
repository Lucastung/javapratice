module org.ganalyst.test.CreateQIIME2Manifest {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;

    opens org.ganalyst.test.CreateQIIME2Manifest to javafx.fxml;
    exports org.ganalyst.test.CreateQIIME2Manifest;
}
