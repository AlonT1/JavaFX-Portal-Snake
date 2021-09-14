/**
 * The Apple model which inherits from Item class
 * @author ViperTeam
 *
 */

package model;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class Portal extends Item 
{
	private Canvas canvas;
	//Instantiating the Bloom class 
	private Timeline portalRespawnTimer;
	private final double CANVAS_WIDTH = 320;
	private final double CANVAS_HEIGHT = 60;
	private final double xRenderOffset = 6;
	private final double yRenderOffset = -4;
    private boolean isPortalWorking = true;
    private Timer renderPortal;
    
    
	public Portal(Image texture, boolean isEaten, int rewardCount, int respawnTime, int snakeGrowthFactor)
	{
		super(texture, isEaten, rewardCount, respawnTime, snakeGrowthFactor);
		canvas = new Canvas( CANVAS_WIDTH, CANVAS_HEIGHT ); // Create new canvas for portals
	}
	
	@Override()
	public void respawn()
	{
		scaleAnimation(false); // before respawning the portal disappears
    	isPortalWorking = false;
		portalRespawnTimer = new Timeline(new KeyFrame(Duration.seconds(getRespawnTime()), new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event) //this method will activate once according to the spawn time
			{
				//after getRespawnTime(), the portal reappears
				scaleAnimation(true); 
				randomSpawnPoint();
				canvas.setLayoutX(getXPos()+xRenderOffset);
				canvas.setLayoutY(getYPos()-yRenderOffset);
			}
		}));
		portalRespawnTimer.play(); //activate the respawn countdown 
	}
	
	
	public void spawnPortal(AnchorPane pnlGameStage)
	{
		isPortalWorking = false;
		canvas.setLayoutX(getXPos()+xRenderOffset);
		canvas.setLayoutY(getYPos()+yRenderOffset);
        pnlGameStage.getChildren().add( canvas );
        final GraphicsContext gcPortal = canvas.getGraphicsContext2D();
		renderPortal = new Timer();
		startScaleAnimation();
		renderPortal.scheduleAtFixedRate((new TimerTask()
		{
			
			final int WIDTH = 40; // image width
	        final int HEIGHT = 60; // image height
	        int frameNumber = 0;
	        int x=0; 
	        int y=0;
			int counter = 0;
			@Override
			public void run()
			{
				Platform.runLater(() -> 
				{
					if(frameNumber>7)
						frameNumber=0;
	                x= frameNumber * WIDTH;
					gcPortal.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
					gcPortal.drawImage(getTexture(), x, y, WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT);
	            	frameNumber++;
					counter++;
					if(counter>100)
					{
					}
				});
			}
		}), new Date(), 100);
	}

	public void scaleAnimation(boolean appear)
	{
		int target = (appear == true) ? 1 : 0;
	    ScaleTransition st = new ScaleTransition(Duration.millis(2000), canvas);
        st.setToY(target);
        st.play();
        if(appear==false)
        	st.setOnFinished(event -> this.disappear());
        if(appear == true)
        	st.setOnFinished(event -> isPortalWorking = true);
	}

	public void startScaleAnimation()
	{
	    ScaleTransition st = new ScaleTransition(Duration.millis(2000), canvas);
	    st.setFromY(0);
        st.setToY(1);
        st.play();
        st.setOnFinished(event -> isPortalWorking = true);
	}
	
	@Override
	public void reset()
	{
		// TODO Auto-generated method stub
		
	}
	
	public Timer getRenderPortal()
	{
		return renderPortal;
	}

	public boolean isPortalWorking()
	{
		return isPortalWorking;
	}
}
