module com.mycompany.tortuneitorpro {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.tortuneitorpro to javafx.fxml;
    exports com.mycompany.tortuneitorpro;
}
