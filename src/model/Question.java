/**
 * Question model, as represented in the JSON file
 */
package model;
import java.util.List;

public class Question implements Comparable<Question>
{
	private Integer id;
	private String question;
	private List<String> answers;
	private int correct_ans;
	private int level;
	private String team;
	
	public Question(Integer id, String question, List<String> answers, int correct_ans, int difficulty, String team)
	{
		super();
		this.id = id;
		this.question = question;
		this.answers = answers;
		this.correct_ans = correct_ans;
		this.level = difficulty;
		this.team = team;
	}

	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getQuestion()
	{
		return question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}

	public List<String> getAnswers()
	{
		return answers;
	}

	public void setAnswers(List<String> answers)
	{
		this.answers = answers;
	}

	public int getCorrect_ans()
	{
		return correct_ans;
	}

	public void setCorrect_ans(int correct_ans)
	{
		this.correct_ans = correct_ans;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public String getTeam()
	{
		return team;
	}

	public void setTeam(String team)
	{
		this.team = team;
	}

	@Override
	public boolean equals(Object obj)
	{
		Question other = (Question)obj;
		if(this.getId() == other.id)
			return true;
		else
			return false;
		
	}
	
	@Override
	public int compareTo(Question other)
	{
		return Integer.compare(id, other.id);
	}
	
	@Override
	public String toString()
	{
		return "Question [id=" + id + ", question=" + question + ", answers=" + answers + ", correct_ans=" + correct_ans
				+ ", level=" + level + ", team=" + team + "]";
	}


	

}
