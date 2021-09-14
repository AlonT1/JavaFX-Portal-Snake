/**
 * The Question Obstacle model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import javafx.scene.image.Image;

public class QuestionObstacle extends Item
{
	private int questionLevel;
	private int punishScore;
	public QuestionObstacle(Image texture, boolean isEaten, int rewardCount, int respawnTime, int snakeGrowthFactor, int punishScore, int questionLevel)
	{
		super(texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
		this.punishScore = punishScore;
		this.questionLevel = questionLevel;
	}

	
	public int getQuestionLevel()
	{
		return questionLevel;
	}


	public int getPunishScore()
	{
		return punishScore;
	}


	public void popQuestion()
	{
		SysData.getInstance().popQuestions(questionLevel);
	}
	
	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		
	}

	

}
