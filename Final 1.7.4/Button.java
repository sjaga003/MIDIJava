import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import java.awt.*;
/**
 * Generic button class.  Utilized throughout the program.
 */
public class Button 
{
    private int xLeft;
    private int yTop;
    private Color currentColor;
    private String letter;
    private int length = 150;
    private int height = 150;
    private Color fontColor;
    //***CONSTRUCTORS***
    public Button(int x, int y, Color currentColor, String letter, Color fontColor)
    {
        xLeft = x;
        yTop = y;
        this.currentColor = currentColor;
        this.letter = letter;
        this.fontColor = fontColor;
    }

    public Button(int x, int y, int length, int height, Color currentColor, String letter, Color fontColor)
    {
        xLeft = x;
        yTop = y;
        this.currentColor = currentColor;
        this.letter = letter;
        this.length = length;
        this.height = height;
        this.fontColor = fontColor;
    }

    //***METHODS***
    public void draw(Graphics2D g2)
    {
        Font custFont = new Font("Gill Sans", Font.BOLD, 20); //initialize custom font

        //draw the button shape
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D button = new RoundRectangle2D.Float(xLeft, yTop, length, height, 10, 10);
        g2.setPaint(currentColor);
        g2.draw(button);
        g2.fill(button);
        g2.setPaint(Color.BLACK);
        g2.draw(button);

        //draw the button's text
        g2.setColor(fontColor);
        g2.setFont(custFont);
        g2.drawString(letter, xLeft + (length - 30), yTop + (height - 10));
    }

    //***GETTERS AND SETTERS***
    public int getX(){return this.xLeft;} //returns x coordinate of button relative to top left corner

    public int getY(){return this.yTop;} //returns y coordinate of button relative to top left corner

    public int getLength(){return this.length;} //returns width of button

    public int getHeight(){return this.height;} //returns height of button

    public Color getColor(){return this.currentColor;} //returns current color of button

    public String getLetter(){return this.letter;} //returns the letter associated with the button (what note it plays)

    public void changeLetter(String str){this.letter = str;} //changes button letter

    public void changeColor(Color newColor){this.currentColor = newColor;} //changes button color

    public void setToBlack(){this.currentColor = Color.BLACK;} //changes color to black

    public void setToWhite(){this.currentColor = Color.WHITE;} //changes color to white
}
