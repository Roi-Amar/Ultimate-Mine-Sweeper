package mines;

//import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MineControl implements Initializable {
	private Mines logic; //logic of the game in mines class
	private MineButton[][] buttonBoard; //board of buttons
	private GridPane grid;
	private Stage stage;
	private MediaPlayer backgroundPlayer; //music players
	private MediaPlayer loopPlayer;
	private MediaPlayer player;
	private boolean isLooping = false; //boolean variables for music use
	private boolean bagroundPlayed = false;
	private boolean lostGame;
	private boolean easterFound = false;
	private Media youWin = new Media(this.getClass().getResource("youWin.mp3").toString()); //import music files for later use
	private Media winner = new Media(this.getClass().getResource("winner.mp3").toString());
	private Media explosion = new Media(this.getClass().getResource("explosion.mp3").toString());
	private Media loser = new Media(this.getClass().getResource("loser.mp3").toString());
	private Media error = new Media(this.getClass().getResource("error.mp3").toString());
	private Media easter = new Media(this.getClass().getResource("easter.mp3").toString());
	private Media dig = new Media(this.getClass().getResource("dig.mp3").toString());
	private Media cantdig = new Media(this.getClass().getResource("cantdig.mp3").toString());
	private Media drop = new Media(this.getClass().getResource("drop.mp3").toString());
	private Media pickup = new Media(this.getClass().getResource("pickup.mp3").toString());
	private Media background = new Media(this.getClass().getResource("Roi Amar - In The Eyes Of The Observer.mp3").toString());
    @FXML
    private HBox root; //fxml declaration

    @FXML
    private VBox settingsPart; //fxml declaration

    @FXML
    private GridPane minerPic; //fxml declaration

    @FXML
    private Button reset; //fxml declaration

    @FXML
    private TextField width; //fxml declaration

    @FXML
    private TextField height; //fxml declaration

    @FXML
    private TextField mines; //fxml declaration

    @FXML
    private StackPane stack; //fxml declaration

    @FXML
    void resetClick(MouseEvent event) {
    	try {
    		if (lostGame) { //if game is lost
    			play(loser, false, false); //play looser sound
    			lostGame=false;
    		}
    	} catch(Exception e) {} //to avoid if not-defind yet
    	Integer heightVal, widthVal, minesVal;
    	try { //if any of the vals is misconfigurd than set it to null
    		heightVal = Integer.parseInt(height.getText().toString());
		} catch (Exception e) {
			heightVal = null;
		}
    	try {
    		widthVal = Integer.parseInt(width.getText().toString());
		} catch (Exception e) {
			widthVal = null;
		}
    	try {
    		minesVal = Integer.parseInt(mines.getText().toString());
		} catch (Exception e) {
			minesVal = null;
		}
    	if (heightVal==null || widthVal==null || minesVal==null) { //if any of the vals null pop up error
    		popUp("Error","Some of the values are not defined","Please make sure you enter Width Height and Mines...");
    		return;
    	}
    	if (minesVal<1) { //if less than 1 mine pop up error
    		popUp("Error","Insufficient number of Mines!!","Please enter more mines...");
    		return;
    	}
    	if (heightVal*widthVal<minesVal+1) { //if too many mines pop up error  
    		popUp("Error","Too many Mines!!","Please enter less mines...");
    		return;
    	}
    	if (widthVal>60 || widthVal<3) { //if width out of bound
    		popUp("Error","Width out of bound!!","Min width allowed is 3, Max width allowed is 60, Please try again...");
    		return;
    	}
    	if (heightVal>40 || heightVal<3) { //if height out of bound
    		popUp("Error","Hight out of bound!!","Min height allowed is 3, Max height allowed is 40, Please try again...");
    		return;
    	}
    	play(background, true, true); //play backgeound music
    	easterFound=false;
		stack.getChildren().remove(grid); //remove former grid
    	newGame(heightVal,widthVal,minesVal,getSize(heightVal,widthVal)); //start a new game with new game method
    	stack.getChildren().add(grid); //add new buttons to the grid
    	refreshStyle();//use refresh style method
    	stage.setResizable(false); //set screen to not be resizeable
		stage.sizeToScene(); //make the screen larger/smaller considering number of buttons
    }
    
	public void setStage(Stage primaryStage) { //set stage to late access of stage
		this.stage = primaryStage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) { //this will run in the first run
		newGame(10,10,10,35);
		stack.getChildren().add(grid);
		minerPic.getChildren().add(new ImageView(new Image(this.getClass().getResource("miner.png").toExternalForm())));
		play(background, true, true);
		refreshStyle();
	}
	
	public void newGame(int height, int width, int numMines, double buttonSize) { //start a new game
		logic = new Mines(height,width,numMines); //start new logic
		grid = new GridPane(); //create new grid
		buttonBoard = new MineButton[height][width]; //create new board
		for (int i = 0; i < height; i++) { //for each board space
			for (int j = 0; j < width; j++) { 
				MineButton button = new MineButton(logic.get(i, j),i,j); //create new button
				buttonBoard[i][j] = button; //set it in the board
				button.setMinSize(buttonSize, buttonSize); //set its size
				button.setPrefSize(buttonSize, buttonSize);
				button.setOnMouseClicked(event -> { //defines what to do when mouse click the button
					if (event.getButton() == MouseButton.PRIMARY) { //rightClick
						if (logic.get(button.iGet(), button.jGet())=="F") { //if marked flagged
							play(cantdig,false,false); //play cant dig
						}
						else {
							if (logic.get(button.iGet(), button.jGet())==".") //if marked close
								play(dig,false,false); //play dig
							if (logic.open(button.iGet(), button.jGet())==false) { //if the open returns false
								play(explosion, false, false); //sound boom
								endgamePopUP("LOSSER","you lost!\n\nLoser..."); //popup looser
								lostGame = true; //set lost game to true
								logic.setShowAll(true); //set show all to true
							}
						}
					}
					else if (event.getButton() == MouseButton.SECONDARY) { //leftClick
						if (logic.get(button.iGet(), button.jGet())=="." || logic.get(button.iGet(), button.jGet())=="F") { //if close or flagged
							if (logic.get(button.iGet(), button.jGet())=="F")
								play(pickup,false,false); //if flaged sound pickup
							else
								play(drop,false,false); //if not flagged sound drop
							logic.toggleFlag(button.iGet(), button.jGet()); //toggle flag
						}
					}
					else if (event.getButton() == MouseButton.MIDDLE) { //rollClick easter egg (play/stop funky music)
						if (easterFound==false) { 
							play(easter , true, false);
							easterFound=true;
						}
						else {
							play(background, true, true);
							easterFound=false;
						}
						
						
					}
					if (logic.isDone()) { //if game is done
						play(youWin, false, false); //play you win
						if (!easterFound)
							play(winner,true, false);
						endgamePopUP("WINNER","wow you are so good!!"); //popup winner
						lostGame = false;
						logic.setShowAll(true);
					}
					refreshStyle(); //use refresh style method
				});
				grid.add(buttonBoard[i][j], j, i);
			}
		}
		grid.setAlignment(Pos.CENTER);
	}
	
	double getSize(int heightVal, int widthVal) { //sizing buttons to fit screen
		int switchKey=0;
		if (heightVal>27)
			switchKey+=1;
		if (widthVal>42)
			switchKey+=2;
		switch(switchKey) {
			case 1: //if only height is more than 27
				return 35*(27.0/heightVal);
			case 2: //if only width is more than 42
				return 35*(42.0/widthVal);
			case 3: //if both height is more than 27 and width is more than 42
				double size,size2;
				size = 35*(27.0/heightVal);
				size2 = 35*(42.0/widthVal);
				return Math.min(size, size2); //select minimum size of them
			default:
				break;
		}
		return 35; //default is 35
	}
	
	void popUp(String title, String header, String content) { //popup for errors
		play(error, false, false); //play music
		Alert alert = new Alert(Alert.AlertType.ERROR); //create alert
		ImageView imageView = new ImageView();
		imageView.setImage(new Image(this.getClass().getResource("oops.gif").toExternalForm())); //import oops gif
		alert.setGraphic(imageView); //define alert configuration
		imageView.setFitHeight(100);
		imageView.setFitWidth(150);
		alert.getDialogPane().setMaxSize(350, 150);
		alert.setTitle(title);
		alert.setHeaderText("");
		alert.setContentText(header+"\n"+content);
		alert.show(); //show alert
	}
	
	void endgamePopUP(String title, String content) { //popup for end of the game
		Alert alert = null;
		ImageView imageView = new ImageView();
		if (title=="WINNER") { //if winner
			alert = new Alert(Alert.AlertType.CONFIRMATION);
			imageView.setImage(new Image(this.getClass().getResource("wow.gif").toExternalForm())); //import gif
		}
		if (title=="LOSSER") { //if loser
			alert = new Alert(Alert.AlertType.WARNING);
			imageView.setImage(new Image(this.getClass().getResource("boom.gif").toExternalForm())); //import gif
		}
		imageView.setFitHeight(100); //define alert configuration
		imageView.setFitWidth(150);
		alert.setGraphic(imageView);
		alert.getDialogPane().setMaxSize(150, 100);
		alert.setHeaderText("");
		alert.setTitle(title);
		alert.setContentText(content);
		alert.show(); //show alert
	}
	 
	void refreshStyle() {  //updating board status of buttons
		ObservableList<Node> children = grid.getChildren();
		for (Node child : children) {
			if (child instanceof MineButton) {
				buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setText("");
				String retString = logic.get(((MineButton) child).iGet(), ((MineButton) child).jGet()); //X=mine, F=flag, .=closed , " "-8 = open
				if (logic.getShowAll()) {
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setDisable(true);
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setOpacity(1);
				}
				if (retString==".") {
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setStyle("-fx-background-image: url('/mines/rock.png');" + "-fx-background-size: 100%;" + "-fx-background-repeat: no-repeat;" + "-fx-background-color: transparent;" + "-fx-border-color: transparent;");
					continue;
				}
				else if (retString == " "){
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setDisable(true);
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setOpacity(0);
					continue;
				}
				else {
					buttonBoard[((MineButton) child).iGet()][((MineButton) child).jGet()].setStyle("-fx-background-image: url('/mines/" + retString + ".png');" + "-fx-background-size: 100%;" + "-fx-background-repeat: no-repeat;" + "-fx-background-color: transparent;" + "-fx-border-color: transparent;");
					continue;
				}
				
			}
		}
	}
	
	void play(Media sound, boolean loop, boolean background) { //this method incharge of music and sound effect
		if (background) {
			if (isLooping) {
				loopPlayer.stop();
				isLooping = false;
			}
			if (!bagroundPlayed) {
				backgroundPlayer = new MediaPlayer(sound);
				backgroundPlayer.setVolume(0.1);
				backgroundPlayer.setOnEndOfMedia(new Runnable() { //set as a loop
				       public void run() {
				    	   backgroundPlayer.seek(Duration.ZERO); 
				       }
				   });
				backgroundPlayer.play();
				bagroundPlayed = true;
			}
			else {
				backgroundPlayer.play();
			}
		}
		else if (loop) {
			backgroundPlayer.pause();
			loopPlayer = new MediaPlayer(sound);
			loopPlayer.setVolume(0.1);
			loopPlayer.setOnEndOfMedia(new Runnable() { //set as a loop
			       public void run() {
			    	   loopPlayer.seek(Duration.ZERO);
			       }
			   });
			loopPlayer.play();
			isLooping = true;
		}
		else {
			player = new MediaPlayer(sound);
			player.setVolume(0.4);
			player.stop();
			player.play();
		}
	}
	

}
