package junit;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import model.GameTimer;
import model.Player;
import model.Snake;
import model.SysData;

@RunWith(JfxTestRunner.class)
public class JUnitTester
{

	@Test
	public void testSecondsToDigitalFormat()
	{
		assertEquals("02:37", GameTimer.convertToDigitalFormat(157));
	}
	
	@Test
	public void testDigitalFormat()
	{
		assertEquals(157, GameTimer.convertToSeconds("02:37"));
	}
	
	@Test
	public void testSankeWallCollision()
	{
		Snake snake = Snake.getInstance();
		assertEquals(true, snake.wallCollisionDetection(-30, -30));
	}
	
	@Test
	public void testSankeSelfCollision()
	{
		Snake snake = Snake.getInstance();
		assertEquals(true, snake.selfCollisionDetection(150, 270));
	}
	
	@Test
	public void testPlayerExistsInLeaderBoard()
	{
		SysData sysData = SysData.getInstance();
		assertEquals(true, sysData.isPlayerExists(new Player(0,"Donatello",0,"")));
	}
	

	

}
