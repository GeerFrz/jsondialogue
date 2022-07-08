module com.geert.dialoguegen {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.bootstrapicons;

    requires org.kordamp.ikonli.core;
    requires com.fasterxml.jackson.kotlin;
    requires com.fasterxml.jackson.databind;
    requires org.jfxtras.styles.jmetro;
    requires java.naming;

    opens com.geert.jsondialogue to javafx.fxml;
    opens com.geert.jsondialogue.controllers to javafx.fxml;
    opens com.geert.jsondialogue.modelviews to javafx.base, com.fasterxml.jackson.databind;
    opens com.geert.jsondialogue.repositories to javafx.base, com.fasterxml.jackson.databind;
    opens com.geert.jsondialogue.data to com.fasterxml.jackson.databind;
    exports com.geert.jsondialogue.data to kotlin.reflect;
    exports com.geert.jsondialogue;

}