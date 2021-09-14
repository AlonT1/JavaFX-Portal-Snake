/**
 * The Mouse model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import java.util.Random;

import javafx.scene.image.Image;
import utils.Shortcuts;

public class Bull extends Item
{

	private static Bull bullInstance;
	private Integer[][] moveDir;
	private Random random;
	int dirIndex, newXPos, newYPos;
	public Bull(Image texture, boolean isEaten, int rewardCount, int respawnTime,
			int snakeGrowthFactor)
	{
		super(texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
		moveDir = new Integer[][] {{0,30},{30,0},{0,-30},{-30,0},{30,30},{-30,-30}};
		random = new Random();
		dirIndex = random.nextInt(moveDir.length);
	}

	public static Bull getInstance()
	{
		if(bullInstance == null)
			bullInstance = new Bull(Shortcuts.bull, false, 0, 30, 0);
		return bullInstance;
	}
	
	public void move()
	{
		if(!getIsEaten())
		{
			newXPos = moveDir[dirIndex][0]/2 + getXPos();
			newYPos = moveDir[dirIndex][1]/2 + getYPos();
			if(newXPos < 0|| newXPos >= Shortcuts.stageWidth-15 ||
					newYPos < 0 ||	newYPos >= Shortcuts.stageHeight-15)
			{
				//if the new pos is out of screen boundries, change direction
				dirIndex = random.nextInt(moveDir.length);
				return;
			}
			setXPos(newXPos);
			setYPos(newYPos);	
		}
		
		
	}

	@Override
	public void reset()
	{
		bullInstance = null;
		
	}

}
