package game.engine;

import game.gui.StartController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		primaryStage.setTitle("DoorDasH: Scare vs Laugh Touchdown");
		Image icon = new Image("MonstersLogo.png");
		primaryStage.getIcons().add(icon);
		showStartScreen(primaryStage);
		primaryStage.show();
	}
	
	public void showStartScreen(Stage stage) {
		StartController startController = new StartController(stage);
		stage.setScene(startController.getScene());
	}

	public static void main(String[] args){
		launch(args);
	}

}
