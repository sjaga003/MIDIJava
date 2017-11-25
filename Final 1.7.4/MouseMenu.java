import javax.sound.midi.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
/**
 * MouseMenu processes mouse clicks and shows appropriate window.
 */
public class MouseMenu implements MouseListener, MouseMotionListener
{
    public static boolean pianoHover;
    public static boolean soundHover;
    public void mouseMoved(MouseEvent e)
    {
        //variables for cursor position
        int minx = e.getX();
        int miny = e.getY();
            
        //checks if cursor is within piano button width range and height range
        if((minx >= Runner.frame.getWidth() / 2 - 93 && minx <= Runner.frame.getWidth() / 2 + 92)
        &&(miny >= Runner.frame.getHeight() / 2 && miny <= Runner.frame.getHeight() / 2 + 50))
        {
            pianoHover = true;
            Runner.menu.repaint();
        }
        else
        {
            pianoHover = false;
            Runner.menu.repaint();
        }

        //checks if cursor is within piano button width range and height range
        if((minx >= Runner.frame.getWidth() / 2 - 93 && minx <= Runner.frame.getWidth() / 2 + 92)
        && (miny >= Runner.frame.getHeight() / 3 && miny <=  Runner.frame.getHeight() / 3 + 50))
        {
            soundHover = true;
            Runner.menu.repaint();
        }
        else
        {
            soundHover = false;
            Runner.menu.repaint();
        }
    }

    public void mousePressed(MouseEvent e)
    {
        //variables for cursor position
        int minx = e.getX();
        int miny = e.getY();
                
        //checks if cursor is within piano button width range and height range
        if((minx >= Runner.frame.getWidth() / 2 - 93 && minx <= Runner.frame.getWidth() / 2 + 92)
        &&(miny >= Runner.frame.getHeight() / 2 && miny <= Runner.frame.getHeight() / 2 + 50))
        {
            Runner.frame.setSize(920, 575);
            Runner.cl.show(Runner.cards, "Piano");
            Runner.cards.getComponent(2).requestFocusInWindow();
            Runner.piano.revalidate();
            Runner.piano.validate();
            Runner.piano.repaint();
        }

        //checks if cursor is within soundboard width and height range
        if((minx >= Runner.frame.getWidth() / 2 - 93 && minx <= Runner.frame.getWidth() / 2 + 92)
        &&(miny >= Runner.frame.getHeight() / 3 && miny <=  Runner.frame.getHeight() / 3 + 50))
        {
            Runner.frame.setSize(840, 550);
            Runner.cl.show(Runner.cards, "Soundboard");
            Runner.cards.getComponent(1).requestFocusInWindow();
            Runner.component.revalidate();
            Runner.component.validate();
            Runner.component.repaint();
        }
    }

    public void mouseClicked(MouseEvent e){}

    public void mouseEntered(MouseEvent e){}

    public void mouseExited(MouseEvent e){}

    public void mouseReleased(MouseEvent e){}

    public void mouseDragged(MouseEvent e){}

}
