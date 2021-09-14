/**
 * The Banana model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import javafx.scene.image.Image;
import utils.Shortcuts;

public class Banana extends Item
{


	private static Banana bananaInstance;

	public Banana(Image texture, boolean isEaten, int rewardCount, int respawnTime,
			int snakeGrowthFactor)
	{
		super( texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
	}

	public static Banana getInstance()
	{
		if(bananaInstance == null)
			bananaInstance = new Banana(Shortcuts.banana, false, 15, 10, 1);
		return bananaInstance;
	}

	@Override
	public void reset()
	{
		bananaInstance = null;
		
	}

}
