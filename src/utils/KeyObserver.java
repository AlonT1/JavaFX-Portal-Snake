/**
 * This interface is implemented by classes who waits for key inputs, as a part of the
 * key observer pattern. The gamelevelcontroller, gets the input key, 
 * and sends it to classes which implement this interface.
 * @author ViperTeam
 *
 */

package utils;

import javafx.scene.input.KeyEvent;

public interface KeyObserver
{
	public void inputKey(KeyEvent e);
}
