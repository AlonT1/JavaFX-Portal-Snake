/**
 * LeaderboardController manages the leader board screen - presents the player with the top 10
 * players, and performs updates to the leader board when new achievements are reached.  
 */
package controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import model.Player;
import model.SysData;
import utils.Shortcuts;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
public class LeaderboardController implements Initializable{
	@FXML
	private AnchorPane anchor;
	@FXML
	private Button btnBackToMenu;
	@FXML
	private TableView<Player> tblLeaderboard;
	@FXML
	private TableColumn<Player, Integer> colRank;
	@FXML
	private TableColumn<Player, String> colName;
	@FXML
	private TableColumn<Player, Integer> colScore;
	@FXML
	private TableColumn<Player, String> colTime;
	
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		//bind each table column to the fields of the class by the class field's name
		colRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colScore.setCellValueFactory(new PropertyValueFactory<>("score"));
		colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		ObservableList<Player> players = FXCollections.observableArrayList(SysData.getInstance().readPlayerRecords());	
		tblLeaderboard.setItems(players);
	}
	
	/**
	 * returns back to main menu by clicking the "Back to Menu" button
	 * @param event
	 */
	// Event Listener on Button[#btnBackToMenu].onAction
	@FXML
	public void backToMenu(ActionEvent event)
	{
		SceneController.getInstance().activate(Shortcuts.mainMenuScreen);
	}
	
	
}
