package ca.pragmaticcoding.kotlinfx;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import static ca.pragmaticcoding.widgetsfx.LabelsKt.styleAs;

public class JavaTestApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
    }

    private Region createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(createCentre());
        return borderPane;
    }

    private Node createCentre() {
        Label label = new Label("Fred");
        styleAs(label, "test");
        return label;
    }
}
