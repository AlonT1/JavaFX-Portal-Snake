/**
 * The Apple model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import javafx.scene.image.Image;
import utils.Shortcuts;

public class Apple extends Item 
{
	private static Apple appleInstance;

	public Apple(Image texture, boolean isEaten, int rewardCount, int respawnTime, int snakeGrowthFactor)
	{
		super(texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
	}
	
	
	public static Apple getInstance()
	{
		if(appleInstance == null)
			appleInstance = new Apple( Shortcuts.apple, false, 10, 5, 1);
		return appleInstance;
	}


	@Override
	public void reset()
	{
		appleInstance = null;
	}

}
