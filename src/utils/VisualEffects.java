/**
 * This class is used for fading effects
 */

package utils;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class VisualEffects
{
	//fades panel
	public static void panelFade(Pane panel, boolean fadeIn , int fadeTime)
	{
		//fadeIn false means fadeOut
		DoubleProperty opacity = panel.opacityProperty();
		int startOpacity = fadeIn==true ? 0 : 1;
		int endOpacity = fadeIn==true ? 1 : 0;
		
		BooleanProperty panelVisiblity = panel.visibleProperty();
		boolean isVisibleAtEnd = fadeIn==true ? true : false;
		
		Timeline timeline = new Timeline
				(new KeyFrame(Duration.ZERO, new KeyValue(opacity, startOpacity)),
				(new KeyFrame(Duration.ZERO, new KeyValue(panelVisiblity, true))),
				(new KeyFrame(new Duration (fadeTime), new KeyValue(opacity, endOpacity))),
				(new KeyFrame(new Duration(fadeTime+1), new KeyValue(panelVisiblity, isVisibleAtEnd))));
		timeline.play();	
	}
	
	//fading between scene transitions
	public static void openingFade(Pane root, int fadeTime)
	{
		DoubleProperty opacity = root.opacityProperty();
		Timeline timeline = new Timeline
				(new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
				(new KeyFrame(new Duration (fadeTime), new KeyValue(opacity, 1.0))));
		timeline.play();
	}
	
	//blur the screen when bull hits the snake
		public static void blur(Pane root, int fadeTime)
		{
			fadeTime *= 1000; //converting fade time from milliseconds to seconds
			GaussianBlur blur = new GaussianBlur();  
			root.setEffect(blur);
			DoubleProperty blurAmount = blur.radiusProperty();
			Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(blurAmount, 0.0)),
					(new KeyFrame(new Duration(fadeTime), new KeyValue(blurAmount, 15))),
					(new KeyFrame(new Duration(fadeTime+(fadeTime/1.5)), new KeyValue(blurAmount, 0))));
			timeline.play();
		}
		
	


	

}
