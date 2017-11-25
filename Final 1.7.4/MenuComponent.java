import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import java.awt.Color;
import javax.swing.JLabel;
import java.util.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.midi.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
/**
 * Component class for the Menu GUI.
 */
public class MenuComponent extends JComponent
{
    //***CONSTRUCTORS***
    public MenuComponent()
    {
        this.addMouseListener(new MouseMenu()); //adds MouseListener to make GUI clickable
        this.addMouseMotionListener(new MouseMenu());//adds MouseListener to check button hover
    }

    //***METHODS***
    public void paintComponent(Graphics g)
    {
        //custom colors and fonts
        Color darkBlue = new Color(30, 39, 75); 
        Font bodyFont = new Font("Gill Sans", Font.BOLD, 30);
        Font titleFont = new Font("Gill Sans", Font.BOLD, 60);
        Graphics2D g2 = (Graphics2D) g;

        //initial settings for g2
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(darkBlue);
        g2.setFont(titleFont);

        //draw the title
        FontMetrics metrics = g.getFontMetrics();
        g2.drawString("MIDIJava", (Runner.frame.getWidth() - metrics.stringWidth("MIDIJava")) / 2 , Runner.frame.getHeight() - 400);
        g2.setFont(bodyFont); //sets new font for buttons

        //draw the buttons and their text
        g2.fillRoundRect(Runner.frame.getWidth() / 2 - 93, Runner.frame.getHeight() / 2, 185, 50, 10, 10);
        g2.fillRoundRect(Runner.frame.getWidth() / 2 - 93, Runner.frame.getHeight() / 3, 185, 50, 10, 10);
        g2.setColor(Color.GRAY); //sets new color for body text
        g2.drawString("Soundboard", Runner.frame.getWidth() / 2 - 88, Runner.frame.getHeight() / 3 + 35);
        g2.drawString("Piano", Runner.frame.getWidth() / 2 - 37, Runner.frame.getHeight() / 2 + 35);
       
        //draws white text of "Piano" on hover
        if(MouseMenu.pianoHover)
        {
            g2.setColor(Color.WHITE);
            g2.drawString("Piano", Runner.frame.getWidth() / 2 - 37, Runner.frame.getHeight() / 2 + 35);
        }

        //draws white text of "SoundBoard" on hover
        if(MouseMenu.soundHover)
        {
            g2.setColor(Color.WHITE);
            g2.drawString("Soundboard", Runner.frame.getWidth() / 2 - 88, Runner.frame.getHeight() / 3 + 35);
        }
    }
}
