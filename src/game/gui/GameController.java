package game.gui;

import game.engine.Game;
import game.engine.Main;
import game.engine.Role;
import game.engine.cells.CardCell;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.cells.MonsterCell;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import game.engine.monsters.Schemer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

public class GameController {
	private Scene scene;
    private Game game;
    private StackPane[] cellPanes = new StackPane[100];
    
    private Label playerCurrentRoleLabel;
    private Label playerEnergyLabel;
    private Label playerPositionLabel;
    private Label playerStatusLabel;
    
    private Label opponentCurrentRoleLabel;
    private Label opponentEnergyLabel;
    private Label opponentPositionLabel;
    private Label opponentStatusLabel;
    
    private Button powerupBtn;
    private Button rollBtn;
    private Label turnLabel;
    private Label diceResultLabel;
    private TextArea logArea;
    
    private static final String COLOR_SCARER_DOOR = "#2c3e8c"; // dark blue
    private static final String COLOR_LAUGHER_DOOR = "#1a6b3c"; // dark green
    private static final String COLOR_MONSTER_CELL = "#4a235a"; // purple
    private static final String COLOR_CARD_CELL = "#7b1c1c"; // dark red
    private static final String COLOR_CONVEYOR = "#1a5276"; // teal
    private static final String COLOR_SOCK = "#784212"; // orange-brown
    private static final String COLOR_NORMAL = "#2d2d2d"; // dark grey
    private static final String COLOR_EXHAUSTED = "#1a1a1a"; // near-black
    
    public GameController(Stage stage, Game game) {
        this.game = game;
        this.scene = buildScene();
        updateAllInfo();
        refreshBoard();
    }
    
    public Scene getScene(){
    	return this.scene;
    }

	private void refreshBoard() {
        for (StackPane pane : cellPanes) {
            pane.getChildren().removeIf(n -> n instanceof Label &&
                ((Label)n).getText().startsWith("["));
        }

        placeToken(game.getPlayer(), true);
        placeToken(game.getOpponent(), false);

        refreshDoorStates();
	}

	private void refreshDoorStates() {
        Cell[][] cells = game.getBoard().getBoardCells();
        for (int index = 0; index < 100; index++) {
            int[] rc = indexToBackendCoords(index);
            Cell cell = cells[rc[0]][rc[1]];
            if (cell instanceof DoorCell) {
                DoorCell door = (DoorCell) cell;
                if (door.isActivated()) {
                    cellPanes[index].setStyle(
                        "-fx-background-color: " + COLOR_EXHAUSTED + "; " +
                        "-fx-background-radius: 3; -fx-border-color: #555; " +
                        "-fx-border-width: 1; -fx-border-radius: 3;"
                    );
                }
            }
        }
    }

	private void placeToken(Monster m, boolean isPlayer) {
		int pos = m.getPosition();
        StackPane pane = cellPanes[pos];

        Label token = new Label(isPlayer ? "[P]" : "[O]");
        token.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        token.setTextFill(Color.web(isPlayer ? "#ff6b6b" : "#74b9ff"));
        StackPane.setAlignment(token, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(token, new Insets(0, 2, 2, 0));

        pane.getChildren().add(token);
	}

	private void updateAllInfo() {
		updateMonsterInfo(game.getPlayer(), true);
        updateMonsterInfo(game.getOpponent(), false);
	}
	
	private void updateMonsterInfo(Monster m, boolean isPlayer) {
        Label energyL = isPlayer ? playerEnergyLabel : opponentEnergyLabel;
        Label posL = isPlayer ? playerPositionLabel : opponentPositionLabel;
        Label curRoleL = isPlayer ? playerCurrentRoleLabel : opponentCurrentRoleLabel;
        Label statusL = isPlayer ? playerStatusLabel : opponentStatusLabel;

        energyL.setText("Energy: " + m.getEnergy());
        posL.setText("Position: " + m.getPosition());
        curRoleL.setText("Current: " + m.getRole());


        StringBuilder status = new StringBuilder("Status: ");
        if (m.isFrozen())      
        	status.append("FROZEN  ");
        if (m.isShielded())    
        	status.append("SHIELD  ");
        if (m.isConfused())    
        	status.append("CONFUSED(" + m.getConfusionTurns() + ")  ");
        if (status.toString().equals("Status: ")) status.append("OK");

        statusL.setText(status.toString());

        curRoleL.setTextFill(m.isConfused()
            ? Color.web("#e74c3c")
            : Color.web("#cccccc"));
    }

	private void checkWinner() {
        Monster winner = game.getWinner();
        if (winner != null) {
            rollBtn.setDisable(true);
            powerupBtn.setDisable(true);
            log("=== " + winner.getName() + " WINS! ===");
            Main.showGameOverScreen(winner, game);
        }
    }

	private Scene buildScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a2e;");
        root.setPadding(new Insets(10));
        
        root.setTop(buildTopBar());
        root.setCenter(buildBoard());
        root.setLeft(buildMonsterPanel(true)); // player
        root.setRight(buildMonsterPanel(false)); // opponent
        root.setBottom(buildActionPanel());
        
        Scene scene = new Scene(root, 1200, 780);

        scene.setOnKeyPressed(event -> {
            if (game.getWinner() != null) return;

            if (event.getCode() == KeyCode.W) {
                game.getCurrent().setPosition(99);
                log("CHEAT: " + game.getCurrent().getName() + " teleported to Boo's Door (Cell 99)!");
                updateAllInfo();
                refreshBoard();
                checkWinner();
            } 
            
            else if (event.getCode() == KeyCode.E) {
                game.getCurrent().alterEnergy(500);
                log("CHEAT: " + game.getCurrent().getName() + " gained 500 energy!");
                updateAllInfo();
            }
        });

        return scene;
    }

	private VBox buildActionPanel() {
		powerupBtn = new Button("Use Powerup (-500 energy)");
        rollBtn = new Button("Roll Dice");
        diceResultLabel = new Label("Dice: -");

        styleActionButton(powerupBtn, "#7d3c98");
        styleActionButton(rollBtn, "#1a6b3c");
        diceResultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        diceResultLabel.setTextFill(Color.GOLDENROD);

        powerupBtn.setOnAction(e -> handlePowerup());
        rollBtn.setOnAction(e -> handleRoll());

        HBox buttons = new HBox(16, powerupBtn, rollBtn, diceResultLabel);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(6));


        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(4);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-control-inner-background: #0f0f1a; " +
                         "-fx-text-fill: #cccccc; -fx-font-size: 12px;");

        VBox panel = new VBox(6, buttons, logArea);
        panel.setPadding(new Insets(8, 0, 0, 0));
        return panel;
	}

	private void handleRoll() {
        rollBtn.setDisable(true);
        powerupBtn.setDisable(true);

        Monster active = game.getCurrent();
        Monster waiting = (active == game.getPlayer()) ? game.getOpponent() : game.getPlayer();

        int activeStartEnergy = active.getEnergy();
        int activeStartPos = active.getPosition();
        boolean activeHadShield = active.isShielded();

        int waitingStartEnergy = waiting.getEnergy();
        int waitingStartPos = waiting.getPosition();
        boolean waitingHadShield = waiting.isShielded();

        try {
            game.playTurn();
            
            int diceRoll = game.getLastRoll(); 
            
            diceResultLabel.setText("Dice: " + diceRoll); 
            log("--- " + active.getName() + " rolled a " + diceRoll + "! ---");

            int intermediatePos = activeStartPos + diceRoll;
            if (intermediatePos > 99) intermediatePos = 99; // Cap at end of board
            
            Cell[][] cells = game.getBoard().getBoardCells();
            int[] midRc = indexToBackendCoords(intermediatePos);
            Cell landedCell = cells[midRc[0]][midRc[1]];

            if (landedCell instanceof ConveyorBelt) {
                log(active.getName() + " landed on a Conveyor Belt and was pushed forward!");
            } else if (landedCell instanceof ContaminationSock) {
                log(active.getName() + " stepped on a Contamination Sock and was thrown backward!");
            }

            if (active.getPosition() != activeStartPos) {
                log(active.getName() + " finished their turn on Cell " + active.getPosition());
            }
            if (waiting.getPosition() != waitingStartPos) {
                log(waiting.getName() + " was forced to Cell " + waiting.getPosition() + "!");
            }

            int activeEnergyDiff = active.getEnergy() - activeStartEnergy;
            if (activeEnergyDiff != 0) {
                String verb = activeEnergyDiff > 0 ? "gained" : "lost";
                log(active.getName() + " " + verb + " " + Math.abs(activeEnergyDiff) + " energy.");
            } else if (activeHadShield && !active.isShielded()) {
                log(active.getName() + "'s Shield blocked a negative effect!");
            }

            int waitingEnergyDiff = waiting.getEnergy() - waitingStartEnergy;
            if (waitingEnergyDiff != 0) {
                String verb = waitingEnergyDiff > 0 ? "gained" : "lost";
                log(waiting.getName() + " " + verb + " " + Math.abs(waitingEnergyDiff) + " energy.");
            } else if (waitingHadShield && !waiting.isShielded()) {
                log(waiting.getName() + "'s Shield blocked a negative effect!");
            }

            int[] finalRc = indexToBackendCoords(active.getPosition());
            if (cells[finalRc[0]][finalRc[1]] instanceof CardCell) {
                String cardName = game.getBoard().getLastDrawnCard().getName(); 
                String cardEffect = game.getBoard().getLastDrawnCard().getDescription();
                
                log(active.getName() + " drew: " + cardName);
                CustomAlertBox.display("Card Drawn!", "Card: " + cardName + "\n\nEffect: " + cardEffect);
            }

            updateAllInfo();
            refreshBoard();
            checkWinner();

        } catch (InvalidMoveException ex) {
            log("Invalid move: destination occupied.");
            CustomAlertBox.display("Warning", "That cell is occupied! You must roll again.");
            rollBtn.setDisable(false);
            powerupBtn.setDisable(false);
            return;
        }

        Monster nextPlayer = game.getCurrent();
        turnLabel.setText("Turn: " + nextPlayer.getName());

        if (nextPlayer.isFrozen()) {
            log(nextPlayer.getName() + " is FROZEN and skips their turn!");
            turnLabel.setText(nextPlayer.getName() + " � FROZEN (skipping)");
        }

        rollBtn.setDisable(false);
        powerupBtn.setDisable(false);
    }


	private void handlePowerup() {
		try {
            game.usePowerup();
            log(game.getCurrent().getName() + " activated their powerup!");
            updateAllInfo();
        } catch (OutOfEnergyException ex) {
            showWarning("Not enough energy to use powerup (need 500).");
        }
	}
	

	private void log(String message) {
        logArea.appendText(message + "\n");
    }

	private void styleActionButton(Button btn, String color) {
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btn.setPadding(new Insets(8, 18, 8, 18));
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; " +
                     "-fx-background-radius: 6; -fx-cursor: hand;");
    }
	
	private void showWarning(String message) {
		CustomAlertBox.display("Warning", message);
    }
	
	private VBox buildMonsterPanel(boolean isPlayer) {
		Monster m = isPlayer ? game.getPlayer() : game.getOpponent();

        Label header = new Label(isPlayer ? "YOU" : "OPPONENT");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        if(isPlayer)
        	header.setTextFill(Color.LIGHTPINK);
        else
        	header.setTextFill(Color.LIGHTCYAN);

        Label nameL = styledLabel(m.getName());
        Label origRoleL = styledLabel("Role: " + m.getOriginalRole());
        Label curRoleL = styledLabel("Current: " + m.getRole());
        Label typeL = styledLabel("Type: " + getTypeName(m));
        Label energyL = styledLabel("Energy: " + m.getEnergy());
        Label posL = styledLabel("Position: " + m.getPosition());
        Label statusL = styledLabel("Status: OK");
        statusL.setWrapText(true);

        if (isPlayer) {
            playerCurrentRoleLabel = curRoleL;
            playerEnergyLabel = energyL;
            playerPositionLabel = posL;
            playerStatusLabel = statusL;
        }
        else {
            opponentCurrentRoleLabel = curRoleL;
            opponentEnergyLabel = energyL;
            opponentPositionLabel = posL;
            opponentStatusLabel = statusL;
        }

        VBox panel = new VBox(8,
            header,
            new Separator(),
            nameL, origRoleL, curRoleL, typeL,
            new Separator(),
            energyL, posL,
            new Separator(),
            statusL
        );
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(160);
        panel.setStyle("-fx-background-color: #16213e; -fx-background-radius: 8;");
        return panel;
	}



	private String getTypeName(Monster m) {
        if (m instanceof Dasher)      
        	return "Dasher";
        if (m instanceof Dynamo)      
        	return "Dynamo";
        if (m instanceof MultiTasker) 
        	return "MultiTasker";
        if (m instanceof Schemer)     
        	return "Schemer";
        return "Unknown";
    }

	private Label styledLabel(String text) {
        Label l = new Label(text);
        l.setFont(Font.font("Arial", 13));
        l.setTextFill(Color.web("#cccccc"));
        return l;
    }

	private GridPane buildBoard() {
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER); 
		grid.setHgap(2);
		grid.setVgap(2);
		grid.setStyle("-fx-background-color: #0f0f1a;");
		grid.setPadding(new Insets(4));
		
		for(int index = 0; index < 100; index++){
			StackPane pane = buildCellPane(index);
			cellPanes[index] = pane;
			
			int[] gc = indexToGridCoords(index);
			grid.add(pane, gc[1], gc[0]);
		}
		return grid;
	}

	private int[] indexToGridCoords(int index) {
        int boardRow = index / 10;
        int col = index % 10;
        if (boardRow % 2 != 0) 
        	col = 9 - col;
        int gridRow = 9 - boardRow;
        return new int[]{gridRow, col};
	}

	private StackPane buildCellPane(int index) {
		StackPane pane = new StackPane();
		pane.setPrefSize(68, 58);
		
		String bgColor = getCellColor(index);
        pane.setStyle("-fx-background-color: " + bgColor + "; " +
                "-fx-background-radius: 3; -fx-border-color: #333; " +
                "-fx-border-width: 1; -fx-border-radius: 3;");
        
        Label indexLabel = new Label(String.valueOf(index));
        indexLabel.setFont(Font.font("Arial", 9));
        indexLabel.setTextFill(Color.LIGHTGRAY);
        StackPane.setAlignment(indexLabel, Pos.TOP_LEFT);
        StackPane.setMargin(indexLabel, new Insets(2, 0, 0, 3));
        
        Label contentLabel = new Label(getCellContent(index));
        contentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        contentLabel.setTextFill(Color.WHITE);
        contentLabel.setWrapText(true);
        contentLabel.setTextAlignment(TextAlignment.CENTER);
        StackPane.setAlignment(contentLabel, Pos.CENTER);

        pane.getChildren().addAll(indexLabel, contentLabel);
        return pane;
	}
	
	
	private String getCellContent(int index) {
        if (index == 0)                        
            return "START";
        if (index == 99)                       
            return "END";

        Cell[][] cells = game.getBoard().getBoardCells();
        int[] rc = indexToBackendCoords(index);
        Cell cell = cells[rc[0]][rc[1]];

        if (cell == null) {
            return "";
        }

        if (cell instanceof DoorCell) {
            DoorCell door = (DoorCell) cell;
            String role = door.getRole() == Role.SCARER ? "S" : "L";
            return role + "\n" + door.getEnergy();
        }
        
        if (cell instanceof MonsterCell) {
            String cellName = cell.getName();
            if (cellName != null && !cellName.isEmpty()) {
            	return cellName;
                //return cellName.split(" ")[0];
            }
            return "M"; 
        }
        
        if (cell instanceof CardCell)          
            return "CARD";
        if (cell instanceof ConveyorBelt)      
            return "CONVEYOR BELT";
        if (cell instanceof ContaminationSock) 
            return "CONTAMINATION SOCK";

        return "";
    }

	private String getCellColor(int index) {
		Cell[][] cells = game.getBoard().getBoardCells();
        int[] rc = indexToBackendCoords(index);
        Cell cell = cells[rc[0]][rc[1]];
        
        if (cell == null) {
            return COLOR_NORMAL;
        }

        if (cell instanceof DoorCell) {
            DoorCell door = (DoorCell) cell;
            return (door.getRole() != null && door.getRole() == Role.SCARER) ? COLOR_SCARER_DOOR : COLOR_LAUGHER_DOOR;
        }
        if (cell instanceof MonsterCell)      
        	return COLOR_MONSTER_CELL;
        if (cell instanceof CardCell)         
        	return COLOR_CARD_CELL;
        if (cell instanceof ConveyorBelt)     
        	return COLOR_CONVEYOR;
        if (cell instanceof ContaminationSock) 
        	return COLOR_SOCK;
        return COLOR_NORMAL;
	}

	private int[] indexToBackendCoords(int index) {
		int row = index / 10;
        int col = index % 10;
        if (row % 2 != 0) col = 9 - col;
        return new int[]{row, col};
	}

	private HBox buildTopBar() {
		turnLabel = new Label("Turn: " + game.getCurrent().getName());
		turnLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		turnLabel.setTextFill(Color.LIGHTBLUE);
		
		HBox topBar = new HBox(turnLabel);
		topBar.setAlignment(Pos.CENTER);
		topBar.setPadding(new Insets(6));
		topBar.setStyle("-fx-background-color: #16213e; -fx-background-radius: 6;");
		return topBar;
	}
}
