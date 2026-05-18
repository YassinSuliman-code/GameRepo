package game.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomAlertBox {

    public static void display(String title, String message) {
        Stage window = new Stage();
        
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);
        window.setMinHeight(150);

        Label label = new Label(message);
        label.setFont(Font.font("Arial", 14));
        label.setTextFill(Color.WHITE);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);

        Button closeButton = new Button("OK");
        closeButton.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4;");
        closeButton.setPadding(new Insets(5, 20, 5, 20));
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1a1a2e; -fx-border-color: #555555; -fx-border-width: 2;");

        Scene scene = new Scene(layout);
        window.setScene(scene);
        
        window.showAndWait(); 
    }
}
