/**
 * The Player model - this class is mainly used for the Leaderboard, 
 * @author ViperTeam
 *
 */

package model;
import java.io.Serializable;

public class Player implements Serializable, Comparable<Player>
{

	private static final long serialVersionUID = 1L;
	private int rank;
	private String name;
	private int score;
	private String time;

	public Player(int rank, String name, int score, String time)
	{
		super();
		this.rank = rank;
		this.name = name;
		this.score = score;
		this.time = time;
	}
	
	public int getRank()
	{
		return rank;
	}
	
	public void setRank(int rank)
	{
		this.rank = rank;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public String getTime()
	{
		return time;
	}
	
	public void setTime(String time)
	{
		this.time = time;
	}
	
	@Override
	public String toString()
	{
		return "Player [rank=" + rank + ", name=" + name + ", score=" + score + ", time=" + time + "]";
	}

	@Override
	public boolean equals(Object obj)
	{
		Player other = (Player) obj;
		if (this.getName().equals(other.getName()))
			return true;
		else 
			return false;
	}
	


	@Override
	public int compareTo(Player other)
	{
		if(this.getScore() != other.getScore())
			return Integer.compare(this.getScore(), other.getScore());
		else
			return -Integer.compare(GameTimer.convertToSeconds(this.time), 
					GameTimer.convertToSeconds(other.time));
	}
	
	
	
	
}
