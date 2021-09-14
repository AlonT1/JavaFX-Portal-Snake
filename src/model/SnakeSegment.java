/**
 * Represents a part of the snake - head, body and tail.
 */
package model;
import javafx.scene.image.Image;
import utils.Direction;

public class SnakeSegment
{
	public int xPos, yPos;
	public Direction dir;
	public Image texture;
	public int spriteNum;
	
	public SnakeSegment(int xPos, int yPos, Direction dir)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.dir = dir;
	}
	
	public SnakeSegment(int xPos, int yPos, Direction dir, Image texture)
	{
		this.xPos = xPos;
		this.yPos = yPos;
		this.dir = dir;
		this.texture = texture;
		spriteNum = dir.ordinal();
	}

	@Override
	public String toString()
	{
		return xPos + " " + yPos;
	}
	
	

}
