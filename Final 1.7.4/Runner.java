import javax.swing.*;
import java.awt.*;
import java.awt.Color;
/**
 * The Runner.  Creates a panel, adds to the frame and establishes the menu.
 * 
 * @author Suhas Jagannath, Jensen Gao, David Yang
 * @version 1.7.0
 */
public class Runner
{    
    public static MenuComponent menu = new MenuComponent();
    public static ButtonComponent component = new ButtonComponent();
    public static PianoComponent piano = new PianoComponent();
    
    public static JPanel cards = new JPanel(new CardLayout());
    public static CardLayout cl = (CardLayout)(cards.getLayout());
    public static JFrame frame = new JFrame();
    public static boolean first;
    public static void main(String[] args)
    {
        //Building the menu interface in the panel
        cards.add(menu, "Menu");
        cards.add(component, "Soundboard");
        cards.add(piano, "Piano");

        //Setting colors and other options for menu interface
        cards.setBackground(Color.GRAY);
        frame.setResizable(false);
        frame.setTitle("MIDIJava");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        
        //adds panel to frame
        frame.add(cards);

        frame.setVisible(true);

    }
}
