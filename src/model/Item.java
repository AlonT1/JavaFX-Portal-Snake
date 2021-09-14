/*
 *Abstract class which represents a item which is edible by the snake 
 */
package model;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.util.Duration;
import utils.Shortcuts;

public abstract class Item 
{
	private int xPos;
	private int yPos;
	private Image texture;

	private boolean isEaten;
	private int rewardScore;
	private int respawnTime;
	private int snakeGrowthFactor;
	private Timeline respawnTimer;
	
	
	public Item(Image texture, boolean isEaten, int rewardScore, int respawnTime, int snakeGrowthFactor)
	{		
		randomSpawnPoint(); //used to calculate a random x,y positions right as the items are born
		this.texture = texture;
		this.isEaten = isEaten;
		this.rewardScore = rewardScore;
		this.respawnTime = respawnTime;
		this.snakeGrowthFactor = snakeGrowthFactor;
	}

	
	/*
	 * calculates a random spawn point on the game board.
	 */
	public void randomSpawnPoint()
	{
		//coords below represent the gameCanvas(checkerboard of the snake) within the whole screen
		int xMin = 0, xMax = Shortcuts.stageWidth, yMin = 0, yMax = Shortcuts.stageHeight;
		Random r = new Random();
		int xPosRandom = r.nextInt(xMax - xMin) + xMin;
		int yPosRandom = r.nextInt(yMax - yMin) + yMin;

		xPos = ( (xPosRandom % 30) == 0) ? xPosRandom : (xPosRandom - (xPosRandom % 30));
		yPos = ( (yPosRandom % 30) == 0) ? yPosRandom : (yPosRandom - (yPosRandom % 30));
	}
	
	
	/**
	 * Hides the eaten object from the player instead of completely destroying it.
	 * this is a part of the Object pool pattern whose concept is spawn once and keep reusing.
	 */
	public void disappear()
	{
		setIsEaten(true); //tells the render method at the controller to avoid rendering this item
		setXPos(Shortcuts.outOfScreen);
		setYPos(Shortcuts.outOfScreen);
	}
	
	/**
	 * Respawns an item by hiding it after it was eaten, then activates a timer according 
	 * to the items respawn time. After its completion, the item will reappear at a random point.
	 */
	public void respawn()
	{
		disappear(); // before respawning object must disappear
		respawnTimer = new Timeline(new KeyFrame(Duration.seconds(getRespawnTime()), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event) //this method will activate once according to the spawn time
			{
				setIsEaten(false);//tells the render method at the controller to render this item
				randomSpawnPoint();
			}
		}));
		respawnTimer.play(); //activate the respawn countdown 
	}


	public abstract void reset();
	
	

	
	public void setSnakeGrowthFactor(int snakeGrowthFactor)
	{
		this.snakeGrowthFactor = snakeGrowthFactor;
	}

	public int getXPos()
	{
		return xPos;
	}

	public int getYPos()
	{
		return yPos;
	}
	

	public void setXPos(int xPos)
	{
		this.xPos = xPos;
	}

	public void setYPos(int yPos)
	{
		this.yPos = yPos;
	}
	
		
	public Image getTexture()
	{
		return texture;
	}

	public boolean getIsEaten()
	{
		return isEaten;
	}
	
	public void setIsEaten(boolean isEaten)
	{
		this.isEaten = isEaten;
	}
	
	public int getRewardScore()
	{
		return rewardScore;
	}

	public int getRespawnTime()
	{
		return respawnTime;
	}

	public int getSnakeGrowthFactor()
	{
		return snakeGrowthFactor;
	}
	


}
