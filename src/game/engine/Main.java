package game.engine;

import game.engine.monsters.Monster;
import game.gui.GameController;
import game.gui.StartController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application{
	private static Stage window;

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setResizable(false);
		window.setTitle("DoorDasH: Scare vs Laugh Touchdown");
		Image icon = new Image("MonstersLogo.png");
		window.getIcons().add(icon);
		showStartScreen(primaryStage);
		window.show();
	}
	
	public static void showStartScreen(Stage stage) {
		StartController startController = new StartController(stage);
		stage.setScene(startController.getScene());
	}
	
	public static void showGameScreen(Game game) {
	    GameController gc = new GameController(window, game);
	    window.setScene(gc.getScene());
	}
	
	public static void showGameOverScreen(Monster winner, Game game) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1a1a2e;");

        Label gameOver = new Label("GAME OVER");
        gameOver.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gameOver.setTextFill(Color.GOLD);

        Label winnerLabel = new Label(winner.getName() + " (" + winner.getRole() + ") WINS!");
        winnerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        winnerLabel.setTextFill(Color.WHITE);

        Label energyLabel = new Label("Winning Energy: " + winner.getEnergy());
        energyLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        energyLabel.setTextFill(Color.LIGHTGREEN);

        Monster loser = (game.getPlayer() == winner) ? game.getOpponent() : game.getPlayer();
        Label loserLabel = new Label(loser.getName() + " Final Energy: " + loser.getEnergy());
        loserLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        loserLabel.setTextFill(Color.LIGHTCORAL);

        Button homeBtn = new Button("Return to Main Menu");
        homeBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-font-weight: bold;");
        homeBtn.setOnAction(e -> showStartScreen(window));

        layout.getChildren().addAll(gameOver, winnerLabel, energyLabel, loserLabel, homeBtn);
        window.setScene(new Scene(layout, 600, 500));
    }

	public static void main(String[] args){
		launch(args);
	}

}
