/**
 * Contains data about the questions and the leaderboard data
 */
package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class SysData
{
	private Player currentPlayer;
	private ArrayList<Question> questions;
	private ArrayList<Player> playerRecords;
	private static SysData instance;
	
	public SysData()
	{	
		this.questions = new ArrayList<Question>();
		this.playerRecords = new ArrayList<Player>();
		readPlayerRecords();
	}

	public static SysData getInstance()
	{
		if(instance == null)
			instance = new SysData();
		return instance;
	}
	
	/*
	 * Read questions fron JSON file
	 */
	public ArrayList<Question> readQuestions()
	{
		Gson gson = new Gson();
		JsonReader reader;
		Question[] questionsArr = null;
		questions.clear();
		try
		{
			reader = new JsonReader(new FileReader("questions.json"));
			questionsArr = gson.fromJson(reader, Question[].class);
			Collections.addAll(questions, questionsArr);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		//add id's to questions
		for(int i=0; i<questions.size(); i++)
		{
			questions.get(i).setId(i+1);
		}
		return questions;
	}
	

	/**
	 * Write questions to JSON
	 */
	public void writeQuestions()
	{
		FileWriter fw;
		try
		{
			for(Question q: questions)
			{
				//id's are used internally for us, 
				//therefore when writing back to JSON they are removed
				q.setId(null); 
			}
			fw = new FileWriter(new File("questions.json"));
			Gson gson = new Gson();
			gson.toJson(questions,fw);
			fw.flush();
			fw.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addNewQuestion(Question question)
	{
		questions.add(question);
	}
	
	public void changeExistingQuestion(Question question)
	{
		int indexOfExisitingQuestion = questions.indexOf(question);
		questions.set(indexOfExisitingQuestion, question);
	}
	
	public void removeExistingQuestion(Question question)
	{
		int indexOfExisitingQuestion = questions.indexOf(question);
		questions.remove(indexOfExisitingQuestion);
	}
	
	
	/**
	 * reads the serial file of the player records
	 * @return the player records arraylist
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Player> readPlayerRecords()
	{
		try
		{
			FileInputStream recordsFile = new FileInputStream("playerRecordsData");
			ObjectInputStream input = new ObjectInputStream(recordsFile);
			playerRecords = (ArrayList<Player>) input.readObject();
			recordsFile.close();
			input.close();			
		}
		catch (FileNotFoundException e) //if .ser file isn't found, Generate a new one 
		{
			for(int i=0 ; i<10 ; i++)
			{
				playerRecords.add(new Player(i+1, "-no name-",0, "00:00"));
			}
			writePlayerRecords();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return playerRecords;
	}
	
	/**
	 * writes the player records arraylist into a serial file
	 */
	public void writePlayerRecords()
	{
		try
		{
			FileOutputStream recordsFile = new FileOutputStream("playerRecordsData");
			ObjectOutputStream output = new ObjectOutputStream(recordsFile);
			output.writeObject(playerRecords);
			output.close();
			recordsFile.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * reset the ranks from 1 to 10 after sorting the records list
	 */
	public void resetRanks()
	{
		for(int i=1 ; i<=10 ; i++)
		{
			playerRecords.get(i-1).setRank(i);
		}
	}
	
	/**
	 * if the player already exists in the scoreboard, we point to him, otherwise it's a new player
	 * @param player
	 */
	public void addCurrentPlayer(Player player)
	{
		int indexOfPlayer = playerRecords.indexOf(player);
		if (indexOfPlayer>=0)
			currentPlayer = playerRecords.get(indexOfPlayer); 
		else
			currentPlayer = player;
	}
	
	/**
	 * checks if player exists in the leaderboard
	 * @param the player we want to check if he exists in the leaderboard
	 * @return true - if player exists, false otherwise
	 */
	public boolean isPlayerExists(Player player)
	{
		if(playerRecords.contains(player))
			return true;
		else 
			return false;
	}
	
	/**
	 * adds new record to the scoreboard
	 * @param newScore the new score the player achieved
	 * @param newTime the new time the player achieved
	 */
	public void addNewRecord(int newScore, String newTime)
	{
		if(newScore > currentPlayer.getScore())
		{
			currentPlayer.setScore(newScore);
			currentPlayer.setTime(newTime);
		}
		else if (newScore == currentPlayer.getScore()) //if the newScore eqauls to old, 
		{
			if(GameTimer.convertToSeconds(newTime)< GameTimer.convertToSeconds(currentPlayer.getTime()))
			{
				currentPlayer.setTime(newTime);
			}
			else
				return; //no record is achieved
		}
		else if(newScore < currentPlayer.getScore())
		{
			return; // no record is achieved
		}
		
		if(playerRecords.contains(currentPlayer)) //if the player exists in the list - just sort it
		{
			Collections.sort(playerRecords);
		}
		else if(!playerRecords.contains(currentPlayer) ) //else add the new player to the list
		{
			playerRecords.add(currentPlayer);
			Collections.sort(playerRecords);
			playerRecords.remove(0); //done to make sure record list always contains only 0-9 records
		}
		Collections.reverse(playerRecords); //ensures that top leaders appear on top of the table
		resetRanks();
		writePlayerRecords();	
	}
		
	
	
	
	/**
	 * Pops a question which would appear after eating the "Question" obstacle
	 */
	public Question popQuestions(int questionLevel)
	{
		Random rand = new Random();
		Question question = null;
		do
		{
			int randIndex = rand.nextInt(questions.size());
			question = questions.get(randIndex);
		}
		while(question.getLevel() != questionLevel);
		
		return question;
	}
	
	/**
	 * 
	 * @return the current player
	 */
	public Player getCurrentPlayer()
	{
		return currentPlayer;
	}


	
	
}
