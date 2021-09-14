/**
 * The controller of the credit screen
 * @author ViperTeam
 *
 */

package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import utils.Shortcuts;

public class CreditsController {

    @FXML
    private Button btnBackToMenu;

    @FXML
    void backToMenu(ActionEvent event) 
    {
		SceneController.getInstance().activate(Shortcuts.mainMenuScreen);

    }

}
