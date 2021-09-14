/**
 * GameLevelController is responsible for the actual game logic, and performs
 * all actions needed to run the game, for example: getting input, collision detection & rendering.
 * @author Viper Team
 */

package controller;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.*;
import utils.KeyObserver;
import utils.Shortcuts;
import utils.VisualEffects;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class GameLevelController implements Initializable
{
	@FXML
	private AnchorPane pnlBackground;
	@FXML
	private AnchorPane pnlGameStage;
	@FXML
	private AnchorPane pnlScoring;
	@FXML
	private Canvas pnlGameCanvas;
	@FXML
	private Canvas pnlSnakeBody;
	@FXML
	private FontAwesomeIconView btnPlayPause, btnExit;
	@FXML
	private Label lblLife;
	@FXML
	private Label lblScore;
	@FXML
	private Label lblLength;
	@FXML
	private Label lblTimer;
	@FXML
	private AnchorPane pnlGameNotification;
	@FXML
	private FontAwesomeIconView heart1, heart2, heart3;
	@FXML
	private Label lblStrikeStatus;
	@FXML
	private Label lblActionTimeCounter;
	@FXML
	private Button btnPlayAgain;
	@FXML
	private Button btnAnswer1;
	@FXML
	private Button btnAnswer2;
	@FXML
	private Button btnAnswer3;
	@FXML
	private Button btnAnswer4;
	@FXML
	private Label txtQuestion;
	@FXML
	private AnchorPane pnlQuestion;
	@FXML
	private FontAwesomeIconView vxAnswerSymbol;
	@FXML
	private Button btnBackToGame;
	@FXML
	private FontAwesomeIconView btnHelp;
	@FXML
	private AnchorPane pnlHelp;

	private GameData gameData;
	private Snake snake;
	private Mouse mouse;
	private Bull bull;
	private GraphicsContext gc;
	public ArrayList<Item> itemPool;
	private GameTimer gameTimer;
	private Scene scene;
	private List<KeyObserver> keyObserver;
	private Timeline timeline;
	private boolean arrowKeyPressed = false;
	private Timer redirectTimer;
	private Question poppedQuestion;
	private QuestionObstacle questionObstacle;
	private DropShadow itemShadow;
	private double portalRespawnTimer;
	private Portal redPortal;
	private Portal bluePortal;

	// constructor equivalent for javafx
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		gameData = GameData.getInstance();
		snake = gameData.getSnake();
		mouse = gameData.getMouse();
		bull = gameData.getBull();
		gameTimer = gameData.getGameTimer();
		itemPool = gameData.getItemPool();
		setPausable(true);
		
		portalRespawnTimer = 0;
		redPortal = gameData.getRedPortal();
		bluePortal = gameData.getBluePortal();
		redPortal.spawnPortal(pnlGameStage);
		bluePortal.spawnPortal(pnlGameStage);
		gc = pnlGameCanvas.getGraphicsContext2D();
		keyObserver = new ArrayList<KeyObserver>(Arrays.asList(snake));
		SysData.getInstance().readQuestions();
		itemShadow = new DropShadow(4, 1, 7, new Color(0, 0, 0, 0.5));
		
		
		// ************THE GAME LOOP PATTERN************//
		timeline = new Timeline(new KeyFrame(Duration.seconds(gameData.getDeltaTime()), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				processInput();
				transform();
				render();
			}
		}));
		timeline.setCycleCount(Animation.INDEFINITE); // causes timeline to loop every second (deltatime)
		timeline.play(); // activates the game loop
	}

	
	
	/**
	 * Waits for input from an event handler and sends the input to all observing
	 * objects
	 */
	private void processInput()
	{
		scene = SceneController.getInstance().getScene();
		scene.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				KeyCode key = event.getCode();
				if(key == KeyCode.P && gameData.isPausable())
					playPause(null);
				else if(key == KeyCode.H && gameData.isPausable())
					showHelp(null);
				for (utils.KeyObserver obs : keyObserver)
				{
					if (key == KeyCode.UP || key == KeyCode.RIGHT 
							|| key == KeyCode.DOWN || key == KeyCode.LEFT)
					{
						arrowKeyPressed = true;
						obs.inputKey(event);
					}
				}
			}
		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent event)
			{
				arrowKeyPressed = false;
			}
		});
	}

	
	/**
	 * Updates all game logic
	 */
	private void transform()
	{
		snake.move();
		mouse.move();
		bull.move();
		strikeDetection();
		eatDetection();
		respawnPortals();
		snake.portalHitDetection(bluePortal, redPortal);
		detectGameOver();
	}


	/**
	 * Draws the graphics of entities on screen - each frame the canvas is cleaned and re-rendered
	 */
	private void render()
	{
		gc.setEffect(null); //clear any effects with new frame, otherwise the effects are multiplied
		gc.clearRect(0, 0, pnlGameCanvas.getWidth(), pnlGameCanvas.getHeight()); // clear canvas
		renderSnake();
		renderItem();
		gc.applyEffect(itemShadow); //apply drop shadow effect to all items
		updateUI();
	}

	
	/**
	 * Detects if the snake collided with itself or the walls in order to lower the
	 * life score or to present a gameover screen
	 */
	private void strikeDetection()
	{
		if (snake.isSelfCollision() || snake.isWallCollision())
		{
			timeline.pause();
			snake.setSnakeLife(-1);
		}
	}
	
	
	/**
	 * 
	 * @param score of the points
	 * @param x position of the text
	 * @param y position of the text
	 */
	private void generateScoringAnimation(int score, int x, int y)
	{
		Label myLabel = new Label(String.valueOf("+"+score));
		myLabel.setLayoutX((x+8));
		myLabel.setLayoutY((y+6));
		myLabel.getStylesheets().add("/view/ScoreText.css");
		pnlGameStage.getChildren().add(myLabel);
		FadeTransition ft = new FadeTransition(Duration.millis(1500), myLabel);
	    ft.setFromValue(1);
	    ft.setToValue(0);
	    ft.play();
	    ft.setOnFinished((event) -> pnlGameStage.getChildren().remove(myLabel));
	}
	

	/**
	 * Lengthens the snake, changes the score, and respawns the eaten item
	 */
	private void eatDetection()
	{
		//items array is sent to snake - if he collides with an item, it will be returned to eatenItem
		Item eatenItem = snake.itemCollisionDetection(gameData.getItemPool());
		if (eatenItem != null) // if ate an entity...
		{
			if (eatenItem instanceof QuestionObstacle) // if question obstacle is eaten
			{
				questionObstacle = (QuestionObstacle) eatenItem;
				poppedQuestion = SysData.getInstance().popQuestions(questionObstacle.getQuestionLevel());
				displayQuestion(poppedQuestion);
			}
			else // if any other obstacle is eaten
			{
				int growthFactor = eatenItem.getSnakeGrowthFactor();
				snake.growthFactor = growthFactor;
				snake.length += growthFactor;
				gameData.setGameScore((gameData.getGameScore() + eatenItem.getRewardScore()));
				generateScoringAnimation(eatenItem.getRewardScore(),eatenItem.getXPos(),eatenItem.getYPos());
				if(eatenItem instanceof FastForward)
					fastForwardSpeed();
				else if(eatenItem instanceof Bull)
				{
					rotateScreen();
					if(snake.length>3)snake.shrinkFactor = 1;
					VisualEffects.blur(pnlGameStage, 2);
				}
			}
			eatenItem.respawn();
		}
	}


/**
 * Simulates the snake's dizziness caused by getting hit by the bull
 */
	private void rotateScreen()
	{
		Random rand = new Random();
		setPausable(false);
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(pnlGameStage);
		rotate.setInterpolator(Interpolator.EASE_BOTH);
		rotate.setAxis(Rotate.Z_AXIS);
		rotate.setByAngle(rand.nextInt(60+180)-180);
		rotate.setCycleCount(2);
		rotate.setAutoReverse(true);
		rotate.setDuration(Duration.seconds(1));
		rotate.play();
		rotate.setOnFinished(event -> setPausable(true));
	}

	

/**
 * Respawns the portals at different locations after portalRespawnTimer time
 */
	private void respawnPortals()
	{
		boolean isSnakeInPortal = false;
		for(SnakeSegment s: snake)
		{
			if(checkSnakeInPortal(s))
			{
				isSnakeInPortal = true;
				break;
			}
		}
		
		if( !isSnakeInPortal && portalRespawnTimer>12)
		{
			portalRespawnTimer=0;
			redPortal.respawn();
			bluePortal.respawn();
		}
		portalRespawnTimer+=gameData.getDeltaTime();
	}
	
	private boolean checkSnakeInPortal(SnakeSegment s)
	{
		boolean isSnakeInBluePortal = ( s.xPos == redPortal.getXPos() ) && ( s.yPos == redPortal.getYPos() );
		boolean isSnakeInRedPortal = ( s.xPos == redPortal.getXPos() ) && ( s.yPos == redPortal.getYPos() );
		return isSnakeInBluePortal || isSnakeInRedPortal;
	}

	
	private void fastForwardSpeed()
	{
		Timer increaseSpeedTimer = new Timer();
		increaseSpeedTimer.scheduleAtFixedRate((new TimerTask()
		{
			int counter = 0;
			@Override
			public void run()
			{
				Platform.runLater(() -> 
				{
					counter++;
					timeline.setRate(1.5);
					if(counter>100)
					{
						timeline.setRate(1);
						increaseSpeedTimer.cancel();
					}
				});
			}
		}), new Date(), 100);
	}



	/**
	 * if snakes life is 0, then the game is stopped and a "Play again" button is
	 * revealed
	 */
	private void detectGameOver()
	{
		if (snake.getSnakeLife() == 0)
		{
			SysData.getInstance().addNewRecord(gameData.getGameScore(), gameData.getGameTimer().getDigitalTime());
			gameData.setGameOver(true);
			setPausable(false);
			timeline.stop();
		}
	}


	/**
	 * Renders each part of the snake according to the sprite number on the sprite
	 * texture
	 */
	private void renderSnake()
	{
		for (SnakeSegment s : snake)
			gc.drawImage(s.texture, 30 * s.spriteNum, 0, 30, 30, s.xPos + 10, s.yPos + 10, 32, 32);
	}

	/*
	 * Renders all the items on the game board
	 */
	private void renderItem()
	{
		for (Item item : itemPool)
			gc.drawImage(item.getTexture(), item.getXPos()+8, item.getYPos() + 4);

	}
	


	/*
	 * Updates the texts on the game level UI - the score, length and time labels
	 */
	private void updateUI()
	{
		double gameSpeed = gameData.getDeltaTime();
		lblLife.setText("Life: " + String.valueOf(snake.getSnakeLife()));
		lblScore.setText("Score: " + String.valueOf(gameData.getGameScore()));  // update score
		lblLength.setText("Length: " + String.valueOf(snake.length)); // update length
		lblTimer.setText(gameTimer.updateTimer(gameSpeed));// update time
		if (!gameData.isGameOver() && (snake.isSelfCollision() || snake.isWallCollision()))
			renderStrikeNotification();
		else if (gameData.isGameOver())
			renderGameOverNotification();
	}

	/**
	 * renders a notification telling the player to chage direction within 3 seconds
	 */
	private void renderStrikeNotification()
	{
		setPausable(false);
		pnlGameNotification.setVisible(true);
		if (snake.getSnakeLife() == 2)
			heart3.setFill(Color.BLACK);
		else if (snake.getSnakeLife() == 1)
			heart2.setFill(Color.BLACK);
		redirectTimer = new Timer();
		redirectTimer.scheduleAtFixedRate((new TimerTask()
		{
			int counter = 0;
			@Override
			public void run()
			{
				Platform.runLater(() -> 
				{
					timeline.pause();
					counter++;
					if (counter >= 0 && counter < 10)
						lblActionTimeCounter.setText("3");
					else if (counter >= 10 && counter < 20)
						lblActionTimeCounter.setText("2");
					else if (counter >= 20 && counter < 30)
						lblActionTimeCounter.setText("1");
					if (counter >= 30 || arrowKeyPressed == true)
					{
						pnlGameNotification.setVisible(false);
						timeline.play();
						btnPlayPause.setDisable(false);
						redirectTimer.cancel();
						setPausable(true);
					}
				});
			}
		}), new Date(), 100);
	}

	/**
	 * Render game over notification, displays a "game over" text and play again
	 * button
	 */
	private void renderGameOverNotification()
	{
		heart1.setFill(Color.BLACK);
		pnlGameNotification.setVisible(true);
		lblActionTimeCounter.setVisible(false);
		btnPlayAgain.setVisible(true);
		lblStrikeStatus.setText("Game Over!");
		setPausable(true);
	}

	/**
	 * resets the game, which allows the player to play again
	 * 
	 * @param event the mouse click on play again
	 */
	@FXML
	public void playAgain(ActionEvent event)
	{
		gameData.resetData();
		SceneController.getInstance().activate(Shortcuts.gameLevelScreen);
	}

	/**
	 * allows pausing, and resuming during gameplay
	 * 
	 * @param event
	 */
	@FXML
	public void playPause(MouseEvent event)
	{
		if(gameData.isPausable())
		{
			if (gameData.isGamePaused() == false)
			{
				btnPlayPause.setGlyphName("PLAY");
				gameData.setGamePaused(true);
				timeline.pause();
			} 
			else if (gameData.isGamePaused() == true)
			{
				btnPlayPause.setGlyphName("PAUSE");
				gameData.setGamePaused(false);
				timeline.play();
			}	
		}
		
	}
	
	private void setPausable(boolean status)
	{
		btnHelp.setDisable(!status);
		btnPlayPause.setDisable(!status);
		gameData.setPausable(status); 
	}
	

	/**
	 * displays exit dialog
	 * 
	 * @param event
	 */
	// Event Listener on FontAwesomeIconView[#btnExit].onMouseClicked
	@FXML
	public void exit(MouseEvent event)
	{
		timeline.pause();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit Notification");
		alert.setHeaderText(null);
		alert.setContentText("Your current score will be lost!");
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		ButtonType exitToMenu = new ButtonType("Exit to Main Menu");
		ButtonType exitToDesktop = new ButtonType("Exit to Desktop");
		alert.getButtonTypes().setAll(cancel, exitToMenu, exitToDesktop);
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == cancel)
		{
			if (!gameData.isGameOver())
				timeline.play();
			alert.close();
		} else if (result.get() == exitToMenu)
		{
			timeline.stop();
			if (redirectTimer != null)
				redirectTimer.cancel();
			gameData.resetData();
			SceneController.getInstance().activate(Shortcuts.mainMenuScreen);
		} else if (result.get() == exitToDesktop)
		{
			SceneController.getInstance().exitProgram();
		}

	}

	/**
	 * Detects a click on answer 1, when the question is displayed during gameplay
	 * @param event
	 */
	@FXML
	void answer1(ActionEvent event)
	{
		answerQuestion(1);
	}

	/**
	 * Detects a click on answer 2, when the question is displayed during gameplay
	 * @param event
	 */
	@FXML
	void answer2(ActionEvent event)
	{
		answerQuestion(2);
	}

	/**
	 * Detects a click on answer 3, when the question is displayed during gameplay
	 * @param event
	 */
	@FXML
	void answer3(ActionEvent event)
	{
		answerQuestion(3);
	}

	/**
	 * Detects a click on answer 4, when the question is displayed during gameplay
	 * @param event
	 */
	@FXML
	void answer4(ActionEvent event)
	{
		answerQuestion(4);
	}
	
	/**
	 * Pauses the game, loads the questions and answer texts, and Displays the question
	 * @param the popped question to display
	 */
	private void displayQuestion(Question question)
	{
		timeline.pause();
		setPausable(false);
		txtQuestion.setText(question.getQuestion());
		btnAnswer1.setText(question.getAnswers().get(0));
		btnAnswer2.setText(question.getAnswers().get(1));
		btnAnswer3.setText(question.getAnswers().get(2));
		btnAnswer4.setText(question.getAnswers().get(3));
		VisualEffects.panelFade(pnlQuestion, true, 400);
	}
	
	/**
	 * Displays if the answer is correct or not, and shows the correct answers
	 * @param finalAnswer the number of answer the user has clicked on
	 */
	private void answerQuestion(int finalAnswer)
	{
		vxAnswerSymbol.setVisible(true);
		if (poppedQuestion.getCorrect_ans() == finalAnswer) //if the answer is correct
		{
			vxAnswerSymbol.setGlyphName("CHECK"); //V icon is displayed
			gameData.setGameScore((gameData.getGameScore() + questionObstacle.getRewardScore()));
		} 
		else if (poppedQuestion.getCorrect_ans() != finalAnswer)
		{
			vxAnswerSymbol.setGlyphName("TIMES"); //X is displayed
			gameData.setGameScore((gameData.getGameScore() - questionObstacle.getPunishScore()));
		}

		//show the correct answer
		Button[] answers = new Button[] { btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4 };
		int answerIterator = 1;
		for (Button ans : answers)
		{
			ans.setMouseTransparent(true); //disable mouse when showing correct result
			if (poppedQuestion.getCorrect_ans() == answerIterator)
				ans.setDisable(false);
			else
				ans.setDisable(true);
			answerIterator++;
		}

		//this timer causes a slight pause after answering the question to allow the viewer
		//to check if he answered correctly, and to give him time to prepare for the game's continuation
		Timer continueGameTimer = new Timer();
		continueGameTimer.scheduleAtFixedRate((new TimerTask()
		{
			int counter = 0;
			@Override
			public void run()
			{
				Platform.runLater(() -> {
					counter++;
					if (counter > 30)
					{
						VisualEffects.panelFade(pnlQuestion, false, 150);
						hideQuestionPnl();
						timeline.play();
						setPausable(true);
						continueGameTimer.cancel();
					}
				});
			}
		}), new Date(), 100);

	}

	/**
	 * after answering the question this method hides and resets the question screen
	 */
	private void hideQuestionPnl()
	{
		pnlQuestion.setVisible(false);
		vxAnswerSymbol.setVisible(false);
		Button[] answers = new Button[] { btnAnswer1, btnAnswer2, btnAnswer3, btnAnswer4 };
		for (Button ans : answers)
		{
			ans.setDisable(false);
			ans.setMouseTransparent(false);
		}
	}

	
	/**
	 * returns to the game from the help screen
	 * @param event
	 */
	@FXML
	private void backToGame(ActionEvent event)
	{
		setPausable(true);
		VisualEffects.panelFade(pnlHelp, false, 500);
		timeline.play();
	}

	/**
	 * shows the help screen while pausing the game
	 * @param event
	 */
	@FXML
	void showHelp(MouseEvent event)
	{
		if(gameData.isPausable())
		{
			timeline.pause();
			setPausable(false);
			VisualEffects.panelFade(pnlHelp, true, 500);
		}
	}
}
