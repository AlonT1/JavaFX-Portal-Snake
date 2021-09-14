package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.Shortcuts;
import utils.VisualEffects;

public class SceneController
{
	private Scene scene;
	private static SceneController instance;
	private AnchorPane root;
	private Stage primaryStage;

	public static SceneController getInstance()
	{
		if (instance == null)
			instance = new SceneController();
		return instance;
	}

	public SceneController() // constructor initializes the scene object with menu screen
	{
		try
		{
			root = (AnchorPane) FXMLLoader.load(getClass().getResource(Shortcuts.mainMenuScreen));
			scene = new Scene(root);
			
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
 
	public void setStage(Stage stage)
	{
		primaryStage = stage;
		primaryStage.setTitle("Viper Team - Snake");
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event)
			{
				// ensures closing of all processes when closing window (pressing "X")
				exitProgram(); 
			}
		});
	}

	public Scene getScene()
	{
		return scene;
	}
	
	public void activate(String name)
	{
		try
		{
			root = (AnchorPane) FXMLLoader.load(getClass().getResource(name));
			scene.setRoot(root);
			scene.setFill(Color.BLACK);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.sizeToScene();
			VisualEffects.openingFade(root, 200);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void centerScreen()
	{
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
	}
	
	public void exitProgram()
	{
		primaryStage.close();
		Platform.exit();
		System.exit(0);
	}
	

	


}
