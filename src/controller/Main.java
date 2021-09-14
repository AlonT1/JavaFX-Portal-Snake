/**
 * Main serves as the entry point of the game.
 * (currently for the first iteration, the game level itself is loaded right away, skipping the menus).
 * @author ViperTeam
 */

package controller;
import javafx.application.Application;
import javafx.stage.Stage;
import utils.Shortcuts;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			SceneController sceneController = SceneController.getInstance();
			sceneController.setStage(primaryStage);		
			sceneController.activate(Shortcuts.mainMenuScreen);
			sceneController.centerScreen();
		} 
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
