/**
 * The controller of the help screen from the main menu
 * @author ViperTeam
 *
 */

package controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;
import utils.Shortcuts;

public class HelpController {
	@FXML
	private AnchorPane anchor;
	@FXML
	private Button btnBackToMenu;

	// Event Listener on Button[#btnBackToMenu].onAction
	@FXML
	public void backToMenu(ActionEvent event) {
		SceneController.getInstance().activate(Shortcuts.mainMenuScreen);
	}
}


