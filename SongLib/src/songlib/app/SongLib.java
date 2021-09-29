/*
 * Robert Quinn
 * Christopher Chopping
 */

package songlib.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import songlib.view.SongLibController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Creates an FXML loader for the view scene, and loads the FXML file
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/songlib.fxml"));
		HBox root = (HBox)loader.load();
		
		// Creates the scene, passing in the root pane
		Scene scene = new Scene(root);
		
		// Gets reference to Controller, and then calls it's Start() method
		SongLibController songLibController = loader.getController();
		songLibController.start(primaryStage);
	
		
		// Configures the scene
		primaryStage.setScene(scene);
		primaryStage.setTitle("SongLib");
		primaryStage.setMinWidth(400);
		primaryStage.setMinHeight(300);
		primaryStage.setResizable(true);
		
		// Shows the scene
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
