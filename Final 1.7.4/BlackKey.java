import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
/**
 * Specialized Button that represents the black keys on a piano.
 */
public class BlackKey extends Button
{
    public BlackKey(int x, int y, String letter)
    {
        super(x, y, 50, 300, Color.BLACK, letter, Color.GRAY);
    }  
}

