module org.ganalyst.test.GuiTest {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;

    opens org.ganalyst.test.GuiTest to javafx.fxml;
    exports org.ganalyst.test.GuiTest;
}
