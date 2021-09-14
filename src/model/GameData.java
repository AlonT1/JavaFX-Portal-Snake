/**
 * The GameData class holds the data about the snake game itself - 
 * the snake, all the obstacles, the timer, the gamespeed, gamescore,
 * and if the game is paused or not
 */
package model;
import java.util.ArrayList;
import java.util.Arrays;

import utils.QuestionFactory;
import utils.Shortcuts;

public class GameData
{
	private static GameData instance;
	private Snake snake;
	private Mouse mouse;
	private Bull bull;
	private ArrayList<Item> itemPool;
	private double deltaTime; //speed of game
	private int gameScore;
	private GameTimer gameTimer;
	private boolean isGamePaused;
	private boolean isGameOver;
	private Portal redPortal;
	private Portal bluePortal;
	private boolean isPausable;
	
	public GameData()
	{
		deltaTime = 0.1;
		gameScore = 0;
		isGameOver = false;
		isGamePaused = false;
		snake = Snake.getInstance();
		mouse = Mouse.getInstance();
		bull = Bull.getInstance();
		gameTimer = GameTimer.getInstance();
		redPortal = new Portal( Shortcuts.redPortal, false, 0, 5, 0);
		bluePortal = new Portal( Shortcuts.bluePortal, false, 0, 5, 0);
		//OBJECT POOL PATTERN
		itemPool = new ArrayList<Item>(Arrays.asList(Apple.getInstance() , Bull.getInstance(),
				Banana.getInstance(), Pear.getInstance(), mouse, FastForward.getInstance(),
				QuestionFactory.getQuestion("easy"), QuestionFactory.getQuestion("medium"),
				QuestionFactory.getQuestion("hard")));
	}
	
	public static GameData getInstance()
	{
		if(instance == null)
			 instance = new GameData();
		return instance;
		
	}
	
	public void resetData()
	{
		gameTimer.reset();
		snake.reset();
		for(Item item: itemPool) {item.reset();}
		bluePortal.getRenderPortal().cancel();
		redPortal.getRenderPortal().cancel();
		instance = null; // Next time getInstance() will be called, this class will be rebuilt
	}

	public double getDeltaTime()
	{
		return deltaTime;
	}

	public void setDeltaTime(double deltaTime)
	{
		this.deltaTime = deltaTime;
	}

	public int getGameScore()
	{
		return gameScore;
	}

	public void setGameScore(int gameScore)
	{
		this.gameScore = gameScore;
	}

	public boolean isGamePaused()
	{
		return isGamePaused;
	}

	public void setGamePaused(boolean isGamePaused)
	{
		this.isGamePaused = isGamePaused;
	}

	public boolean isGameOver()
	{
		return isGameOver;
	}

	public void setGameOver(boolean isGameOver)
	{
		this.isGameOver = isGameOver;
	}

	public Snake getSnake()
	{
		return snake;
	}
	public Mouse getMouse()
	{
		return mouse;
	}
	
	public ArrayList<Item> getItemPool()
	{
		return itemPool;
	}

	public GameTimer getGameTimer()
	{
		return gameTimer;
	}

	public Portal getRedPortal()
	{
		return redPortal;
	}

	public Portal getBluePortal()
	{
		return bluePortal;
	}

	public Bull getBull()
	{
		return bull;
	}

	public boolean isPausable()
	{
		return isPausable;
	}

	public void setPausable(boolean isPausable)
	{
		this.isPausable = isPausable;
	}
	
	
	
	
}


