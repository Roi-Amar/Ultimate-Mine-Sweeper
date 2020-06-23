package mines;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MinesFX extends Application {
	

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Mines.fxml"));
		MineControl controller = new MineControl();
		controller.setStage(primaryStage);
		loader.setController(controller);
		loader.load();
		Parent p = loader.getRoot(); 
	    Scene scene = new Scene(p);
	    primaryStage.setResizable(false);
	    scene.setCursor(new ImageCursor(new Image(this.getClass().getResource("shovel2.png").toExternalForm())));
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Mines Sweeper Ultimate");
	    primaryStage.getIcons().add(new Image(this.getClass().getResource("minerLogo.png").toExternalForm()));
	  //  primaryStage.getIcons().add(new Image("/mines/icons/icon.jpg"));
	    primaryStage.sizeToScene();
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}


	
}
