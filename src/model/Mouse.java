/**
 * The Mouse model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import java.util.Random;

import javafx.scene.image.Image;
import utils.Shortcuts;

public class Mouse extends Item
{

	private static Mouse mouseInstance;
	private Integer[][] moveDir;
	private Random random;
	int dirIndex, newXPos, newYPos;
	public Mouse(Image texture, boolean isEaten, int rewardCount, int respawnTime,
			int snakeGrowthFactor)
	{
		super(texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
		moveDir = new Integer[][] {{0,30},{30,0},{0,-30},{-30,0},{30,30},{-30,-30}};
		random = new Random();
		dirIndex = random.nextInt(moveDir.length);

	}

	public static Mouse getInstance()
	{
		if(mouseInstance == null)
			mouseInstance = new Mouse(Shortcuts.mouse, false, 30, 60, 2);
		return mouseInstance;
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
		mouseInstance = null;
		
	}

}
