/**
 * The controller of the main menu which includes the login screen
 * @author ViperTeam
 *
 */

package controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.layout.VBox;
import model.Player;
import model.SysData;
import utils.Shortcuts;
import utils.VisualEffects;

public class MainMenuController implements Initializable
{
	@FXML
	private VBox pnlMenuButtons;
	@FXML
	private Button btnPlayGame;
	@FXML
	private Button btnLeaderBoard;
	@FXML
	private Button btnQuestionManager;
	@FXML
	private Button btnBackToLogin;
	@FXML
	private Button btnHelp;
	@FXML
	private Button btnCredits;
	@FXML
	private Button btnExit;
	@FXML
	private Button btnLogin;
	@FXML
	private AnchorPane pnlNickName;
	@FXML
	private TextField fldNickName;
	@FXML
	private Button btnPlay;
	@FXML
	private FontAwesomeIconView btnExitNickNamePane;
	public static boolean nickNameEntered = false;
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		if(nickNameEntered == true)
		{
			pnlNickName.setVisible(false);
			pnlMenuButtons.setVisible(true);
		}
	}
	
	/**
	 * Pressing the "Play Game" button runs this method, which loads the gameLevel
	 * @param event
	 */
	// Event Listener on Button[#btnPlayGame].onAction
	@FXML
	public void playGame(ActionEvent event)
	{
		SceneController.getInstance().activate(Shortcuts.gameLevelScreen);
	}

	/**
	 * Pressing the "Leaderboard" button runs this method, which loads the leaderboard
	 * @param event
	 */
	// Event Listener on Button[#btnLeaderBoard].onAction
	@FXML
	public void openLeaderboard(ActionEvent event)
	{
		SceneController.getInstance().activate(Shortcuts.leaderboardScreen);
	}

	/**
	 * Pressing the "Question Manager" button runs this method, which loads the question manager
	 * @param event
	 */
	// Event Listener on Button[#btnQuestionManager].onAction
	@FXML
	public void openQuestionManager(ActionEvent event)
	{
		SceneController.getInstance().activate(Shortcuts.questionManagerScreen);
		SceneController.getInstance().centerScreen();
	}

	
	/**
	 * Pressing the "help" button runs this method, which loads the help screen
	 * @param event
	 */
	// Event Listener on Button[#btnHelp].onAction
	@FXML
	public void openHelp(ActionEvent event)
	{
		SceneController.getInstance().activate(Shortcuts.helpScreen);
	}

	/**
	 * Pressing the "credits" button runs this method, which loads the credits screen
	 * @param event
	 */
	// Event Listener on Button[#btnCredits].onAction
	@FXML
	public void openCredits(ActionEvent event)
	{
		SceneController.getInstance().activate(Shortcuts.creditsScreen);
	}
	
	
	/**
	 * pressing The "Back To Login" button, runs this method, causes the return to login
	 * @param event
	 */
	@FXML
	public void backToLogin(ActionEvent event)
	{
		VisualEffects.panelFade(pnlNickName, true, 500);
		VisualEffects.panelFade(pnlMenuButtons, false, 500);
	}
	
	
	/**
	 * Pressing the "exit" button runs this method, which exits the game
	 * @param event
	 */
	// Event Listener on Button[#btnExit].onAction
	@FXML
	public void exitGame(ActionEvent event)
	{
		SceneController.getInstance().exitProgram();
	}

	/**
	 * pressing the "Login!" button, runs this method, which runs string validation,
	 * and finally adds the player to the system
	 * @param event
	 */
	// Event Listener on Button[#btnLogin].onAction
	@FXML
	public void login(ActionEvent event)
	{
		Alert alert = new Alert (AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		SysData sysData = SysData.getInstance();		
		Player newPlayer = new Player(0, fldNickName.getText(), 0, "00:00");
		
		if(fldNickName.getText().isEmpty()) //check if field is empty
		{
			alert.setContentText("Nickname field is empty. Please enter a name.");
			alert.showAndWait();
		}
		else if(fldNickName.getText().length()>20) //check if nickname is too long
		{
			alert.setContentText("The nickname must be shorter than 20 characters.");
			alert.showAndWait();			
		}
		else if(sysData.isPlayerExists(newPlayer)) //check if the player name already exists in leaderboard
		{
			alert.setContentText("The Nickname exists in the scoreboard. "
					+ "Choose another name or risk overwriting the high-score associated with this name");
			ButtonType changeName = new ButtonType("Change Name",ButtonData.CANCEL_CLOSE);
			ButtonType keepName = new ButtonType ("Keep Name",ButtonData.APPLY);
			alert.getButtonTypes().setAll(changeName, keepName);
			Optional<ButtonType> result = alert.showAndWait();
			if(result.get() == keepName)
			{
				sysData.addCurrentPlayer(newPlayer);
				nickNameEntered = true;
				alert.close();
				VisualEffects.panelFade(pnlNickName, false, 500);
				VisualEffects.panelFade(pnlMenuButtons, true, 500);
			}
			else if (result.get() == changeName)
			{
				alert.close();
			}
		}
		else // new player which doesn't exist in scoreboard - LOGIN!!!
		{
			sysData.addCurrentPlayer(newPlayer);
			nickNameEntered = true;
			VisualEffects.panelFade(pnlNickName, false, 500);
			VisualEffects.panelFade(pnlMenuButtons, true, 500);
		}
		
	}

	// Event Listener on FontAwesomeIconView[#btnExitNickNamePane].onMouseClicked
	@FXML
	public void exitNickNamePane(MouseEvent event)
	{
		SceneController.getInstance().exitProgram();
	}

	
	

}
