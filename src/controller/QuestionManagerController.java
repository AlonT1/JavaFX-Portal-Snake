/**
 * The controller of the question manager
 * @author Viper Team
 */

package controller;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Question;
import model.SysData;
import utils.Shortcuts;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableView;

public class QuestionManagerController implements Initializable
{
    @FXML
    private AnchorPane rootMainAnchorPain;

    @FXML
    private Button btnAddQuestion;

    @FXML
    private Button btnEditQuestion;

    @FXML
    private Button btnApply;

    @FXML
    private Button btnCancel;
    
    @FXML
    private Button btnRemoveQuestion;

    @FXML
    private HBox pnlStatus;

    @FXML
    private Label lblStatus;

    @FXML
    private Label btnBackToMenu;

    @FXML
    private TextArea txtAreaQuestion;

    @FXML
    private ToggleGroup tgDifficulty;

    @FXML
    private ToggleGroup tgAnswers;

    @FXML
    private TextField txtFldAnswer1;

    @FXML
    private ToggleGroup answers;

    @FXML
    private TextField txtFldAnswer2;

    @FXML
    private TextField txtFldAnswer3;

    @FXML
    private TextField txtFldAnswer4;

    @FXML
    private TableView<Question> tblQuestions;

    @FXML
    private TableColumn<Question, Integer> colID;

    @FXML
    private TableColumn<Question, String> colQuestion;

    @FXML
    private TableColumn<Question, List<String>> colAnswers;
    
    @FXML
    private TableColumn<Question, Integer> colCorrectAns;
    
    @FXML
    private TableColumn<Question, Integer> colLevel;

    @FXML
    private TableColumn<Question, String> colTeam;

    private SysData sysData;
    
    private Question editedQuestion = null;
    
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		sysData = SysData.getInstance(); //get a reference to the system data
		
		//bind each table column, to the corresponding class field of the Question class by name
		colID.setCellValueFactory(new PropertyValueFactory<>("id"));
		colQuestion.setCellValueFactory(new PropertyValueFactory<>("question"));
		colAnswers.setCellValueFactory(new PropertyValueFactory<>("answers"));
		colCorrectAns.setCellValueFactory(new PropertyValueFactory<>("correct_ans"));
		colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
		colTeam.setCellValueFactory(new PropertyValueFactory<>("team"));
		wrapText(); // causes the text in the table cells to wrap and not to overflow
		refreshTable(); // reloads the table data
				
	}

	
	/**
	 * Pressing the "add question" buttons, fires this method
	 * @param event
	 */
	// Event Listener on Button[#btnAddQuestion].onAction
	@FXML
	public void addQuestion(ActionEvent event)
	{
		createQuestion(-1); //-1 means a totally new question will be added
	}


	/**
	 * creates a new question, adds it to the json file, and refreshes the table
	 * @param questionID creating a question with -1 id means a new question will be added.
	 * editing an existing question will fire this method with the id of editted question, causing
	 * the edited question's content to be replaced, instead of adding a totally new question
	 */
	private void createQuestion(int questionID)
	{
		if(isFieldsOK())
		{
			int id = (questionID==-1) ? (tblQuestions.getItems().size()+1) : questionID; 
			String questionText = txtAreaQuestion.getText(); 
			List<String> answers = Arrays.asList(txtFldAnswer1.getText(), txtFldAnswer2.getText(), txtFldAnswer3.getText(), txtFldAnswer4.getText());
			int level = 0;
			for(int i=0; i<tgDifficulty.getToggles().size(); i++)
			{
				Toggle toggle = tgDifficulty.getToggles().get(i);
				if(toggle.isSelected())
					level = i+1;
			}
			int correctAns = 0;
			for(int i=0; i<tgAnswers.getToggles().size(); i++)
			{
				Toggle toggle = tgAnswers.getToggles().get(i);
				if(toggle.isSelected())
					correctAns = i+1;
			}
			String team = "Viper";
			
			Question question = new Question (id, questionText, answers, correctAns, level, team);
			
			if(questionID==-1) sysData.addNewQuestion(question);
			else sysData.changeExistingQuestion(question);
			
			sysData.writeQuestions();
			refreshTable();
		}
	}

	/**
	 * Pressing "Edit Questions" fires this method, which Starts the "Question Edit Mode"
	 * @param event
	 */
	// Event Listener on Button[#btnEditQuestion].onAction
	@FXML
	public void editQuestion(ActionEvent event)
	{
		//get the selected question from table we want to edit
		editedQuestion = tblQuestions.getSelectionModel().getSelectedItem(); 
		if (editedQuestion==null) //display a warning if we didn't chose any question
		{
			Alert alert = new Alert (AlertType.ERROR);
			alert.setTitle("Field Error");
			alert.setHeaderText(null);
			alert.setContentText("Please choose an entry from the table before editing a question");
			alert.showAndWait();
			return;		
		}
		else  //else a question has been chosen
		{
			setButtonsOff(true); // disables other buttons while in edit mode
			//loads the text fields with the chosen question we've chosen from the table 
			txtAreaQuestion.setText(editedQuestion.getQuestion());
			txtFldAnswer1.setText(editedQuestion.getAnswers().get(0));
			txtFldAnswer2.setText(editedQuestion.getAnswers().get(1));
			txtFldAnswer3.setText(editedQuestion.getAnswers().get(2));
			txtFldAnswer4.setText(editedQuestion.getAnswers().get(3));
			tgAnswers.getToggles().get(editedQuestion.getCorrect_ans()-1).setSelected(true);
			tgDifficulty.getToggles().get(editedQuestion.getLevel()-1).setSelected(true);
		}
		
	}
	
	/**
	 * Pressing "Cancel" Button in edit mode, fires the this method,
	 * causes certain buttons to turn on
	 * @param event
	 */
    @FXML
    void cancelQuestionEdit(ActionEvent event)
    {
		setButtonsOff(false);
    }

    /**
     * Pressing "Apply" Button in edit mode, fires the this method,
     * which causes a rewrite of the edited question
     * @param event
     */
    @FXML
    void applyQuestionEdit(ActionEvent event) 
    {
		setButtonsOff(false);
		createQuestion(editedQuestion.getId()); 
    }
    
    /**
     * Pressing "remove" Button, fires the this method,
     * which causes the removal of a question from the system
     * @param event
     */
	// Event Listener on Button[#btnRemoveQuestion].onAction
	@FXML
	public void removeQuestion(ActionEvent event)
	{
		editedQuestion = tblQuestions.getSelectionModel().getSelectedItem();
		if(editedQuestion == null)
		{
			Alert alert = new Alert (AlertType.ERROR);
			alert.setTitle("Field Error");
			alert.setHeaderText(null);
			alert.setContentText("Please choose an entry from the table before removing a question");
			alert.showAndWait();
			return;	
		}
		sysData.removeExistingQuestion(editedQuestion);
		sysData.writeQuestions();
		refreshTable();
	}

	   /**
     * Pressing "back to menu" Button, fires the this method,
     * which causes a return to the menu
     * @param event
     */
	// Event Listener on Label[#btnBackToMenu].onMouseClicked
	@FXML
	public void backToMenu(MouseEvent event)
	{
		SceneController.getInstance().activate(Shortcuts.mainMenuScreen);
		SceneController.getInstance().centerScreen();
	}
	
	/**
	 * reads data from the json file and refreshes the table
	 */
	private void refreshTable()
	{
		ObservableList<Question> list = FXCollections.observableArrayList(sysData.readQuestions());
		tblQuestions.setItems(list);
		tblQuestions.refresh();
	}
	
	/**
	 * disables/enables certain buttons while in "Question Edit Mode"
	 * @param status
	 */
	private void setButtonsOff(boolean status)
	{
		btnAddQuestion.setDisable(status);
		btnRemoveQuestion.setDisable(status);
		btnEditQuestion.setDisable(status);
	}

	/**
	 * wraps the text inside the question table, to prevent text overflow
	 */
	private void wrapText()
	{
		colQuestion.setCellFactory(new Callback<TableColumn<Question,String>, TableCell<Question,String>>() {
			@Override
			public TableCell<Question, String> call( TableColumn<Question, String> param) {
				final TableCell<Question, String> cell = new TableCell<Question, String>() {
					private Text text;
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = new Text(item.toString());
							text.setWrappingWidth(315); // Setting the wrapping width to the Text
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});
		
		colAnswers.setCellFactory(new Callback< TableColumn<Question,List<String>>, TableCell<Question,List<String>> >() {
			@Override
			public TableCell<Question, List<String>> call( TableColumn<Question, List<String>> param) {
				final TableCell<Question, List<String>> cell = new TableCell<Question, List<String>>() {
					private Text text;
					@Override
					public void updateItem(List<String> item, boolean empty) {
						super.updateItem(item, empty);
						if (!isEmpty()) {
							text = new Text(item.toString());
							text.setWrappingWidth(335); // Setting the wrapping width to the Text
							setGraphic(text);
						}
					}
				};
				return cell;
			}
		});
		
	}

	/**
	 * checks if fields are not empty, or too long. display a warning per each field
	 * and for each situation
	 * @return
	 */
	private boolean isFieldsOK()
	{
		Alert alert = new Alert (AlertType.ERROR);
		alert.setTitle("Field Error");
		alert.setHeaderText(null);
		if(txtAreaQuestion.getLength()==0)
		{
			alert.setContentText("Question text box is empty! Please enter a question");
			alert.showAndWait();
			return false;
		}
		else if(txtFldAnswer1.getText().length()==0 )
		{
			alert.setContentText("Answer field 1 is empty! please fill answer 1");
			alert.showAndWait();
			return false;
		}
		else if(txtFldAnswer2.getText().length()==0)
		{
			alert.setContentText("Answer field 2 is empty! please fill answer 2");
			alert.showAndWait();
			return false;
		}
		else if(txtFldAnswer3.getText().length()==0 )
		{
			alert.setContentText("Answer field 3 is empty! please fill answer 3");
			alert.showAndWait();
			return false;
		}
		else if(txtFldAnswer4.getText().length()==0 )
		{
			alert.setContentText("Answer field 4 is empty! please fill answer 4");
			alert.showAndWait();
			return false;
		}
		
		else if(txtFldAnswer1.getText().length()>60 )
		{
			alert.setContentText("Answer field 1 is too long. Please shorten the answer 1");
			alert.showAndWait();
			return false;
		}
		else if(txtFldAnswer2.getText().length()>60)
		{
			alert.setContentText("Answer field 2 is too long. Please shorten the answer 2");
			alert.showAndWait();
			return false;
		}
		else if(txtFldAnswer3.getText().length()>60 )
		{
			alert.setContentText("Answer field 3 is too long. Please shorten the answer 3");
			alert.showAndWait();
			return false;
		}
		else if(txtFldAnswer4.getText().length()>60)
		{
			alert.setContentText("Answer field 4 is too long. Please shorten the answer 4");
			alert.showAndWait();
			return false;
		}
		
		return true;
	}

}
