/**
 * Calculates the time being displayed on the game board and returns
 * the time at a "digital format" of 00:00
 */

package model;


public class GameTimer
{
	private String lDigits, rDigits;
	private  double seconds =0;
	private  int minutes;
	private String digitalClock;
	
	private static GameTimer gameTimerInstance;
	
	public GameTimer()
	{
		lDigits = "00";
		rDigits = "00";
		seconds = 0;
		minutes = 0;
		digitalClock = "";
	}
	
	public static GameTimer getInstance()
	{
		if(gameTimerInstance==null)
			gameTimerInstance = new GameTimer();
		return gameTimerInstance;
	}
	

	/**
	 * Updates the time timer
	 * @param deltaTime the time between each frame of the game
	 * @return The time formatted as 00:00
	 */
	public String updateTimer(double deltaTime) // displaying the following text 00:00
	{
		seconds+= deltaTime;
			
		rDigits =  (int)seconds<10  ? ( "0" + String.valueOf((int)seconds) ) : String.valueOf((int)seconds);
		if(seconds>59)
		{
			seconds = 0;
			minutes++;
			lDigits = minutes<10 ? ("0"+String.valueOf(minutes)) : String.valueOf(minutes);
		}
		digitalClock = lDigits + ":" + rDigits;
		
		return digitalClock;
	}
	
	public void reset()
	{
		gameTimerInstance = null;
	}
	
	public String getDigitalTime()
	{
		return digitalClock;
	}
	
	public int getNumericTime()
	{
		return convertToSeconds(digitalClock);
	}
	
	
	public static String convertToDigitalFormat(int time)
	{
		String minutes = String.valueOf(time/60);
		if(minutes.length()==1)
			minutes = "0" + minutes;
		String seconds = String.valueOf(time%60);
		if(seconds.length()==1)
			seconds = "0" + seconds;
		return minutes + ":" + seconds;
	}
	
	public static int convertToSeconds(String time)
	{
		int numericTime = 0;
		String[] timeSplit = time.split(":");
		numericTime = 60 * Integer.valueOf(timeSplit[0]); //add minutes
		numericTime += Integer.valueOf(timeSplit[1]);
		return numericTime;
	}
}
