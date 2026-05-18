package game.gui;

import game.engine.Game;

import game.engine.Main;
import game.engine.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StartController {
    private Scene scene;
    private Stage stage;
    private Role selectedRole = null;
    
    public StartController(Stage stage){
    	this.stage = stage;
    	this.scene = buildScene();
    }
    
    public Scene getScene(){
    	return this.scene;
    }
    
	private Scene buildScene() {
		VBox root = new VBox(24);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(40));
		root.setStyle("-fx-background-color: #1a1a2e;");
		
		Label title = new Label("DoorDasH");
		title.setFont(Font.font("Arial", FontWeight.BOLD, 52));
		title.setTextFill(Color.PURPLE);
		
		Label subTitle = new Label("Scare vs Laugh Touchdown");
		subTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 22));
		subTitle.setTextFill(Color.LAVENDER);
		
		Label quote = new Label("\"We scare because we care.\"  --  \"We laugh, that's our path.\"");
		quote.setFont(Font.font("Arial", FontWeight.LIGHT, 18));
		quote.setTextFill(Color.DARKRED);
		
		Label chooseLabel = new Label("Choose Your Side:");
		chooseLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
		chooseLabel.setTextFill(Color.WHITE);
		
		Button scarerBtn = new Button("SCARER");
		Button laugherBtn = new Button("LAUGHER");
		Button startBtn = new Button("Start Game");
		Button rulesBtn = new Button("How to Play");
		
		startBtn.setDisable(true);
		
		scarerBtn.setOnAction(e -> {
		    selectedRole = Role.SCARER;
		    startBtn.setDisable(false);
		});
		
		laugherBtn.setOnAction(e -> {
		    selectedRole = Role.LAUGHER;
		    startBtn.setDisable(false);
		});
		
		startBtn.setOnAction(e -> handleStartGame());
		
		rulesBtn.setOnAction(e -> showRules());
		
		HBox roles = new HBox(20, scarerBtn, laugherBtn);
		roles.setAlignment(Pos.CENTER);
		
		root.getChildren().addAll(title, subTitle, quote,
			    new Separator(),
			    chooseLabel, roles,
			    startBtn, rulesBtn
			);
		
		return new Scene(root, 700, 520);
	}
	
	private void handleStartGame(){
        if (selectedRole == null)
            return;
        
        Role chosenRole = selectedRole;
        try {
            Game game = new Game(chosenRole);
            Main.showGameScreen(game);
        } catch (Exception ex) {
            ex.printStackTrace(); 
            String errorMsg = (ex.getMessage() == null) ? "Check your console for the exact error line." : ex.getMessage();
            CustomAlertBox.display("Game Initialization Failed", errorMsg);
        }
    }
	
	private void showRules() {
		Stage rulesStage = new Stage();
		rulesStage.initOwner(stage);
		rulesStage.initModality(Modality.APPLICATION_MODAL);
		rulesStage.setTitle("How To Play");
		Image ruleBook = new Image("rulesIcon.png");
		rulesStage.getIcons().add(ruleBook);
		
		
		TextArea rules = new TextArea(getRulesText());
		rules.setEditable(false);
		rules.setWrapText(true);
		rules.setFont(Font.font("Monospaced", 13));
        rules.setStyle("-fx-control-inner-background: #1a1a2e; -fx-text-fill: white;");
		
		Button closeBtn = new Button("Close");
		closeBtn.setStyle(
	            "-fx-background-color: #e94560; -fx-text-fill: white; " +
	            "-fx-background-radius: 6;"
	        );
		closeBtn.setOnAction(e -> rulesStage.close());
		
		VBox layout = new VBox(12, rules, closeBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1a1a2e;");
        VBox.setVgrow(rules, Priority.ALWAYS);
        
        rulesStage.setScene(new Scene(layout, 620, 540));
        rulesStage.show();
	}

	private String getRulesText() {
	    return
	        "-----------------------------------------------\n" +
	        " DOORDASH: SCARE VS LAUGH TOUCHDOWN - RULES\n" +
	        "-----------------------------------------------\n" +
	        "\n" +
	        "OBJECTIVE\n" +
	        "---------\n" +
	        "Be the first monster to reach Cell 99 (Boo's Door)\n" +
	        "with at least 1000 energy.\n" +
	        "\n" +
	        "SETUP\n" +
	        "-----\n" +
	        "1. Choose SCARER or LAUGHER - a random monster of\n" +
	        "   your role is assigned to you.\n" +
	        "2. Both monsters start at Cell 0.\n" +
	        "3. The board has 100 cells in a zigzag layout.\n" +
	        "\n" +
	        "TURN SEQUENCE\n" +
	        "-------------\n" +
	        "1. Optionally activate your Powerup (costs 500 energy).\n" +
	        "2. Roll the dice (1-6) to move forward.\n" +
	        "3. If your destination is occupied, re-roll.\n" +
	        "4. Land on a cell and trigger its effect.\n" +
	        "\n" +
	        "CELL TYPES\n" +
	        "----------\n" +
	        "- SCARER / LAUGHER Doors: Match your role = team gains energy.\n" +
	        "  Mismatch = team loses energy. Exhausted after first use.\n" +
	        "- Monster Cells: Same role = free powerup. Different role =\n" +
	        "  energies swap if yours is lower.\n" +
	        "- Conveyor Belts: Move forward automatically.\n" +
	        "- Contamination Socks: Move backward + lose 100 energy.\n" +
	        "- Card Cells: Draw a card and apply its effect.\n" +
	        "\n" +
	        "CARDS\n" +
	        "-----\n" +
	        "- Position Swap: Swap places if you are behind.\n" +
	        "- Contamination Code: YOU return to Cell 0.\n" +
	        "- 2319 Alert: OPPONENT returns to Cell 0.\n" +
	        "- Small Snatcher: Steal 50 energy from opponent.\n" +
	        "- Sneaky Thief: Steal 100 energy.\n" +
	        "- Mega Drain: Steal 150 energy.\n" +
	        "- Super Shield: Block the next negative energy effect.\n" +
	        "- Mind Scramble: Both confused for 2 turns.\n" +
	        "- Total Confusion: Both confused for 3 turns.\n" +
	        "\n" +
	        "MONSTER TYPES\n" +
	        "-------------\n" +
	        "- Dasher: Moves at 2x speed. Powerup: 3x for 3 turns.\n" +
	        "- Dynamo: 2x energy gains AND losses. Powerup: Freeze opponent.\n" +
	        "- Multitasker: Half speed, +200 to all energy changes.\n" +
	        "  Powerup: Normal speed for 2 turns.\n" +
	        "- Schemer: +10 to all energy changes.\n" +
	        "  Powerup: Steal 10 from every monster (ignores shields).\n" +
	        "\n" +
	        "SHIELD\n" +
	        "------\n" +
	        "Blocks the next negative energy effect to your whole team.\n" +
	        "Only one shield exists - getting it removes the opponent's.\n" +
	        "\n" +
	        "CONFUSION\n" +
	        "---------\n" +
	        "Swaps SCARER and LAUGHER roles temporarily.\n" +
	        "Affects door interactions until it wears off.\n" +
	        "\n" +
	        "WIN CONDITION\n" +
	        "-------------\n" +
	        "Reach Cell 99 AND have 1000 or more energy.\n" +
	        "If you reach 99 without enough energy, the game continues!\n";
	}
}
