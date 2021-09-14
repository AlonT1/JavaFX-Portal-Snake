/**
 * implementation of the factory pattern, allows the creation of the questions at different
 * difficulties, just by calling the factory class with the string name of the difficulty
 * for example: Question question = QuestionFactory.getQuestion("medium")
 */
package utils;
import model.QuestionObstacle;
public class QuestionFactory
{
	public static QuestionObstacle getQuestion(String level)
	{
		if(level.contentEquals("easy"))
		{
			return new QuestionObstacle(Shortcuts.questionEasy, false, 1, 1, 0, 10, 1);
		}
		else if(level.contentEquals("medium"))
		{
			return new QuestionObstacle(Shortcuts.questionMedium, false, 2, 1, 0, 20, 2);
		}
		else if(level.contentEquals("hard"))
		{
			return new QuestionObstacle(Shortcuts.questionHard, false, 3, 1, 0, 30, 3);
		}
		else
		{
			return null;
		}
	}
}
