package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLController fxmlController = new FXMLController();
			FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource("Root.fxml"));
			fxmlLoader.setController(fxmlController);
			AnchorPane rootPane = (AnchorPane) fxmlLoader.load();
			Scene scene = new Scene(rootPane);
			primaryStage.setScene(scene);
			primaryStage.getIcons()
					.add(new Image(Main.class.getClassLoader().getResourceAsStream("res/1481607310_Card_file.png")));
			primaryStage.setMinWidth(1047);
			primaryStage.setMinHeight(553);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
