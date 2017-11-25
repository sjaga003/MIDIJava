import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
/**
 * Specialized Button.
 */
public class ProfileControlButton extends Button
{
    public ProfileControlButton(int x, int y, String letter)
    {
        super(x, y, Color.blue, letter, Color.WHITE);
    }
    
    public ProfileControlButton(int x, int y, int length, int height, String letter)
    {
        super(x, y, length, height, Color.black, letter, Color.WHITE);
    }
}
