/**
 * The Snake model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import javafx.scene.input.KeyEvent;
import utils.Direction;
import utils.KeyObserver;
import utils.Shortcuts;


public class Snake implements Iterable<SnakeSegment>, KeyObserver
{
	private static Snake snakeSingleton;
	public SnakeSegment snakeHead;
	public LinkedList<SnakeSegment> snake;
	public boolean isAlive;
	public int length;
	public Direction pressedDirection;
	public int growthFactor;
	private boolean isWallCollision;
	private boolean isSelfCollision;
	private boolean isPortalCollision;
	private int snakeLife;
	public int shrinkFactor;
	
	public Snake(LinkedList<SnakeSegment> snake, boolean isAlive, int length, int snakeLife)
	{
		this.snake = snake;
		this.isAlive = isAlive;
		this.length = length;
		this.snakeLife = snakeLife;
		snakeHead = snake.getLast();
		pressedDirection = Direction.RIGHT;
		growthFactor=0;
	}

	public static Snake getInstance()
	{
		if (snakeSingleton == null)
			snakeSingleton = new Snake(new LinkedList<SnakeSegment>(Arrays.asList(
					new SnakeSegment(120, 270, Direction.RIGHT, Shortcuts.snakeTail),
					new SnakeSegment(150, 270, Direction.RIGHT, Shortcuts.snakeLinearBody),
					new SnakeSegment(180, 270, Direction.RIGHT, Shortcuts.snakeHead))),
					true, 3,3);
		return snakeSingleton;
	}
	

	@Override
	public void inputKey(KeyEvent e)
	{
		switch (e.getCode())
		{
		case UP:
			pressedDirection = Direction.UP;
			break;
		case DOWN:
			pressedDirection = Direction.DOWN;
			break;
		case LEFT:
			pressedDirection = Direction.LEFT;
			break;
		case RIGHT:
			pressedDirection = Direction.RIGHT;
			break;
		default:
			break;
		}
	}
	

	/**
	 * Moves the snake according to the input arrow keys 
	 */
	public void move()
	{
		directionPrevention();
		switch (pressedDirection)
		{
		case UP:
			reposSegments(0, -30);
			break;
		case DOWN:
			reposSegments(0, 30);
			break;
		case LEFT:
			reposSegments(-30, 0);
			break;
		case RIGHT:
			reposSegments(30, 0);
			break;
		}
	}
	
	/**
	 * Prevents the snake to move in certain directions. For example, If the snake moves down,
	 * it cannot move up when pressing the up arrow.
	 */
	private void directionPrevention()
	{
		if (snakeHead.dir == Direction.UP && pressedDirection == Direction.DOWN) pressedDirection = Direction.UP;
		else if (snakeHead.dir == Direction.DOWN && pressedDirection == Direction.UP) pressedDirection = Direction.DOWN;
		else if (snakeHead.dir == Direction.RIGHT && pressedDirection == Direction.LEFT) pressedDirection = Direction.RIGHT;
		else if (snakeHead.dir == Direction.LEFT && pressedDirection == Direction.RIGHT) pressedDirection = Direction.LEFT;
	}
	
	
	/** Repositions all the snake segments according to the input direction.
	 * 
	 * @param xHeadPos The new direction of the head on the x axis.
	 * @param yHeadPos The new direction of the head on the y axis.
	 */
	private void reposSegments(int xHeadPos, int yHeadPos )
	{
		if(!isPortalCollision)
			isWallCollision = wallCollisionDetection(snakeHead.xPos+xHeadPos, snakeHead.yPos+yHeadPos);
			isSelfCollision = selfCollisionDetection(snakeHead.xPos+xHeadPos, snakeHead.yPos+yHeadPos);
		if(isWallCollision || isSelfCollision)
			return;
		if(growthFactor ==0 ) //if not growing(==0) then move regulary, otherwise skip this stage of tail chopping 
		{
			// 1. remove tail - to give the illusion of movement we regularly chop one tail
						//when the encountering the bull, we remove 2 segments
			for(int i=0; i<shrinkFactor+1; i++)
				snake.pollFirst();
			shrinkFactor = 0;
			SnakeSegment snakeTail = snake.getFirst();
			//2. repaint tail - if encountered a curved section - changed rotation of tail
			snakeTail.texture = Shortcuts.snakeTail;
			snakeTail.spriteNum = snakeTail.dir.ordinal(); //tail is oriented as the body
		}
		else if (growthFactor>0) // after each growth, decrease the factor so it won't grow again
			growthFactor--;
		
		//3.set head as body -  if snake is turning  set head as curveBody 
		if (pressedDirection != snakeHead.dir) 
		{
			curvedBody();
			snakeHead.dir = pressedDirection; //direction of curvedBody must be be updated
		} 
		else //not turning - body parts are added with identical direction as head
		{
			snakeHead.texture = Shortcuts.snakeLinearBody;
		}
		//4.add new head
		if(isPortalCollision)
		{
			snake.add(new SnakeSegment(xHeadPos, yHeadPos, pressedDirection, Shortcuts.snakeHead));
			isPortalCollision = false;
		}
		else
			snake.add(new SnakeSegment(snakeHead.xPos + xHeadPos, snakeHead.yPos + yHeadPos, pressedDirection, Shortcuts.snakeHead));
		
		snakeHead = snake.getLast(); // update snake head pointer to the newly created head		
	}

	/**
	 * Decides which sprite of the curve body will be displayed according to the snakes direction
	 */
	private void curvedBody()
	{
		SnakeSegment curvedSection = snakeHead;
		curvedSection.texture = Shortcuts.snakeCurvedBody;
		
		if(curvedSection.dir==Direction.RIGHT && pressedDirection == Direction.UP) curvedSection.spriteNum = 2;
		else if(curvedSection.dir==Direction.RIGHT && pressedDirection == Direction.DOWN) curvedSection.spriteNum = 3;
		else if (curvedSection.dir==Direction.LEFT && pressedDirection == Direction.UP) curvedSection.spriteNum = 1;
		else if (curvedSection.dir==Direction.LEFT && pressedDirection == Direction.DOWN) curvedSection.spriteNum = 0;
		else if (curvedSection.dir==Direction.UP && pressedDirection == Direction.RIGHT) curvedSection.spriteNum = 0;
		else if (curvedSection.dir==Direction.UP && pressedDirection == Direction.LEFT) curvedSection.spriteNum = 3;
		else if(curvedSection.dir==Direction.DOWN && pressedDirection == Direction.RIGHT) curvedSection.spriteNum = 1;
		else if (curvedSection.dir==Direction.DOWN && pressedDirection == Direction.LEFT) curvedSection.spriteNum = 2;
	}


	/**
	 * Detects a wall collision by checking if the head of the snake has gone out of the boundaries
	 * @return True if collided with a wall, False if it didn't
	 */
	public boolean wallCollisionDetection(int nextHeadXPos, int nextHeadYPos)
	{
		if( nextHeadXPos <= -30 || nextHeadXPos >= Shortcuts.stageWidth ||
				nextHeadYPos <=-30 ||	nextHeadYPos >= Shortcuts.stageHeight )
			return true;
		return false;		
	}
	
	/**
	 * Detects a wall collision by checking if the head of the snake has gone out of the boundaries
	 * @return True if collided with a wall, False if it didn't
	 */
	public void portalHitDetection(Portal bluePortal, Portal redPortal)
	{
		if( bluePortal.getXPos() == snakeHead.xPos &&
				bluePortal.getYPos() == snakeHead.yPos && bluePortal.isPortalWorking())
		{			
			isPortalCollision = true;
			reposSegments(redPortal.getXPos(), redPortal.getYPos());
		}
		else if( redPortal.getXPos() == snakeHead.xPos &&
				redPortal.getYPos() == snakeHead.yPos && redPortal.isPortalWorking())
		{
			isPortalCollision = true;
			reposSegments(bluePortal.getXPos(), bluePortal.getYPos());
		}
	}
	
	public void teleport(int nextHeadXPos, int nextHeadYPos)
	{
		reposSegments(nextHeadXPos, nextHeadYPos);
	}
	
	/**
	 * Detects if the snake has collided with himself by checking if its head position
	 * is equal to any one of its body parts
	 * @return
	 */
	public boolean selfCollisionDetection(int nextHeadXPos, int nextHeadYPos)
	{
		for(SnakeSegment s: snake)
			if( s!=snakeHead && nextHeadXPos == s.xPos && nextHeadYPos == s.yPos)
				return true;
		return false;
	}
	
	/**
	 * Detects if the head of the snake is at the same point as one the items on the board
	 * @param itemPool The array of items which are currently on the board
	 * @return If an item was eaten - the same item is returned. Null otherwise.
	 */
	public Item itemCollisionDetection(ArrayList<Item> itemPool)
	{
		for(Item item : itemPool)
			if(Math.abs(snakeHead.xPos - item.getXPos()) <=15 && Math.abs(snakeHead.yPos - item.getYPos())<=15)
				return item;
		 return null;
	}
			


	@Override
	public Iterator<SnakeSegment> iterator()
	{
		return snake.iterator();
	}


	public void reset()
	{
		snake.clear();
		snakeSingleton = null;
	}
	
	public void die()
	{

	}

	
	public int getSnakeLife()
	{
		return snakeLife;
	}
	
	public void setSnakeLife(int punish)
	{
		snakeLife += punish;
	}
	
	
	
	public boolean isWallCollision()
	{
		return isWallCollision;
	}

	
	public boolean isSelfCollision()
	{
		return isSelfCollision;
	}

	public boolean isPortalCollision()
	{
		return isPortalCollision;
	}

	@Override
	public String toString()
	{
		String toString = null;
		for(int i=0; i<snake.size() ;i++)
		{
			toString += (snake.get(i).toString() + " ");
		}
		return toString;
	}

	
}
