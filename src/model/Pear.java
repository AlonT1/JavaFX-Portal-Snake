/**
 * The Pear model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import java.util.Random;

import javafx.scene.image.Image;
import utils.Shortcuts;


public class Pear extends Item 
{
	private static Pear pearInstance;

	public Pear(Image texture, boolean isEaten, int rewardCount, int respawnTime, int snakeGrowthFactor)
	{
		super(texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
	}

	public static Pear getInstance()
	{
		if(pearInstance == null)
			pearInstance = new Pear( Shortcuts.pear, false, 20, 1, 1);
		return pearInstance;
	}
	
	/**
	 * calculates a random spawn point but only along the 4 corners of the screen
	 */
	@Override
	public void randomSpawnPoint()
	{
		int prevX = getXPos(); 
		int prevY = getYPos();
	
		int xCoords[] = {0,810};
		int yCoords[] = {0,540};
		
		Random rand = new Random();
		
		int xRandIndex = rand.nextInt(xCoords.length);
		int yRandIndex = rand.nextInt(xCoords.length);
		
		while(xCoords[xRandIndex]==prevX && yCoords[yRandIndex]==prevY)
		{
			 xRandIndex = rand.nextInt(xCoords.length);
			 yRandIndex = rand.nextInt(yCoords.length);
		}
			
		setXPos(xCoords[xRandIndex]);
		setYPos(yCoords[yRandIndex]);
	}

	public void respawn()
	{
		randomSpawnPoint();
	}

	@Override
	public void reset()
	{
		pearInstance = null;
		
	}
	
}
