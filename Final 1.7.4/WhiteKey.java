import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
/**
 * Specialized Button that represents the white keys on a piano.
 */
public class WhiteKey extends Button
{
    public WhiteKey(int x, int y, String letter)
    {
        super(x, y, 100, 500, Color.WHITE, letter, Color.GRAY);
    }  
}

