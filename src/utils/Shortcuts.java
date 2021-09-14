/**
 * Provides Constant variables and shortcuts for paths.
 */
package utils;

import javafx.scene.image.Image;


public final class Shortcuts 
{
	public static final int stageWidth = 840; 
	public static final int stageHeight = 570;
	public static final int outOfScreen = -60;


	public static final Image snakeHead = new Image("/res/snakehead.png");
	public static final Image snakeLinearBody = new Image("/res/linearbody.png");
	public static final Image snakeTail = new Image("/res/snaketail.png");
	public static final Image snakeCurvedBody = new Image("/res/curvedbody.png");
	public static final Image banana = new Image("/res/banana.png");
	public static final Image questionEasy = new Image("/res/questioneasy.png");
	public static final Image questionMedium = new Image("/res/questionmedium.png");
	public static final Image questionHard = new Image("/res/questionhard.png");
	public static final Image pear = new Image("/res/pear.png");
	public static final Image apple = new Image("/res/apple.png");
	public static final Image mouse = new Image("/res/mouse.png");
	public static final Image bull = new Image("/res/bull.png");
	public static final Image fastForward = new Image("/res/fastforward.png");
	public static final Image bluePortal = new Image("/res/blueportal.png");
	public static final Image redPortal = new Image("/res/redportal.png");

	public static final String gameLevelScreen = "/view/GameLevel.fxml";
	public static final String leaderboardScreen = "/view/Leaderboard.fxml";
	public static final String questionManagerScreen = "/view/QuestionManager.fxml";
	public static final String helpScreen = "/view/Help.fxml";
	public static final String creditsScreen = "/view/Credits.fxml";
	public static final String mainMenuScreen = "/view/MainMenu.fxml";

	public static final String moveUp = "/res/moveup.wav";
	public static final String moveRight = "/res/moveright.wav";
	public static final String moveDown = "/res/movedown.wav";
	public static final String moveLeft = "/res/moveleft.wav";
	
}
