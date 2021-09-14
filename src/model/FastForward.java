/**
 * The Apple model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import javafx.scene.image.Image;
import utils.Shortcuts;

public class FastForward extends Item 
{
	private static FastForward fastForwardInstance;

	public FastForward(Image texture, boolean isEaten, int rewardCount, int respawnTime, int snakeGrowthFactor)
	{
		super(texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
	}
	
	
	public static FastForward getInstance()
	{
		if(fastForwardInstance == null)
			fastForwardInstance = new FastForward( Shortcuts.fastForward, false, 40, 60, 2);
		return fastForwardInstance;
	}


	@Override
	public void reset()
	{
		fastForwardInstance = null;
	}

}
