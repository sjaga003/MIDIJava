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

/**
 * PianoComponent draws the keys for the piano window.  
 * This class also contains the listener for the piano, which processes keystrokes in a similar manner to the soundboard version.
 */
public class PianoComponent extends JComponent
{
    //button fields
    private WhiteKey[] whiteKeys;  //arrays for black and white keys (for organizational purposes)
    private BlackKey[] blackKeys; 
    private final int initialX = 20; //initial coordinates for positioning of keys
    private final int initialY = 20;
    private final int whiteWidth = 100; //key dimensions
    private final int blackWidth = 75;

    private Button record; //Soundboard-style buttons
    private Button play;
    private Button clear;
    private Button minus;
    private Button plus;
    private Button display;

    //MIDI fields
    private Instrument[] inst;
    private int instrument = 0; //initial instrument
    public static int pitch = 60;  //the starting pitch
    private MidiChannel[] channels;

    private boolean control; //boolean to determine whether key "I" is pressed (opens the controls window)
    private int label; //int to determine whether the labels displayed are sharps, flats, or controls

    //Recorder fields
    private Recorder recorder;
    private long[] time = new long[12];
    private int[] order = new int[12];
    private int counter;
    private boolean recordClick;
    private boolean playClick;
    public PianoComponent()
    {
        //initializing the white keys on the piano
        whiteKeys = new WhiteKey[7];
        whiteKeys[0] = new WhiteKey(initialX, initialY, "A");
        whiteKeys[1] = new WhiteKey(whiteKeys[0].getX() + whiteWidth, initialY, "W");
        whiteKeys[2] = new WhiteKey(whiteKeys[1].getX() + whiteWidth, initialY, "S");
        whiteKeys[3] = new WhiteKey(whiteKeys[2].getX() + whiteWidth, initialY, "E");
        whiteKeys[4] = new WhiteKey(whiteKeys[3].getX() + whiteWidth, initialY, "D");
        whiteKeys[5] = new WhiteKey(whiteKeys[4].getX() + whiteWidth, initialY, "F");
        whiteKeys[6] = new WhiteKey(whiteKeys[5].getX() + whiteWidth, initialY, "T");

        //initializing the black keys on the piano
        blackKeys = new BlackKey[5];
        blackKeys[0] = new BlackKey(whiteKeys[0].getX() + blackWidth, initialY, "G");
        blackKeys[1] = new BlackKey(whiteKeys[1].getX() + blackWidth, initialY, "Y");
        blackKeys[2] = new BlackKey(whiteKeys[3].getX() + blackWidth, initialY, "H");
        blackKeys[3] = new BlackKey(whiteKeys[4].getX() + blackWidth, initialY, "U");
        blackKeys[4] = new BlackKey(whiteKeys[5].getX() + blackWidth, initialY, "J");

        //initialize the Soundboard-style buttons
        record = new Button(whiteKeys[6].getX() + whiteWidth + 20, initialY, 150, 150, Color.BLACK, "", Color.WHITE);
        clear = new Button(record.getX(), 185, 150, 70, Color.BLACK, "", Color.WHITE);
        play = new Button(record.getX(), 270, 150, 70, Color.BLACK, "", Color.WHITE);
        minus = new Button(record.getX(), clear.getY() + 180, 70, 70, Color.BLACK, "", Color.WHITE);
        plus = new Button(record.getX() + 80, minus.getY(), 70, 70, Color.BLACK, "", Color.WHITE);
        display = new Button(record.getX(), minus.getY() + 85, 150, 70, Color.BLACK, "", Color.WHITE);

        JLabel instNum = new JLabel("" + instrument); 

        counter = 0;
        recorder = new Recorder("piano");
        /**
         * Listener class for the piano.  Acts as a synthezizer.  Takes raw keystroke data and translates it into notes and other functions.
         */
        class PianoListener implements KeyListener 
        {
            //set all key fields to false
            private boolean A = false, S = false, D = false, F = false, G = false, H = false, J = false,  W = false, E = false, T = false, Y = false, U = false;

            public PianoListener()
            {
                try{
                    Synthesizer synth = MidiSystem.getSynthesizer(); //creates a new Synthesizer
                    synth.open(); //"opens" the synthesizer for instrument input
                    inst = synth.getDefaultSoundbank().getInstruments(); //loads the default sound bank into the previously declared Instrument array
                    synth.loadInstrument(inst[0]); //loads Instrument[0] into the synthesizer
                    channels = synth.getChannels(); //assigns the Synthesizer's channels to the previously declared MidiChannels array
                    channels[0].programChange(0, instrument); //changes MidiChannel 0 to bank 0, instrument 0 (previously declared)
                }
                catch(Exception j)
                {
                }
            }

            public void keyPressed(KeyEvent e)
            {    
                int key = e.getKeyCode(); //takes key identification data from the KeyEvent
                if(key == KeyEvent.VK_PERIOD) //if the key pressed was a comma
                {
                    //change the control to Reverb
                    channels[0].controlChange(91, 127);// Reverb
                }
                else if(key == KeyEvent.VK_ESCAPE) //if the key pressed was escape, return to the main menu.
                {
                    Runner.frame.setSize(500, 500);
                    Runner.cl.show(Runner.cards, "Menu");
                    Runner.cards.getComponent(0).requestFocusInWindow();
                    Runner.component.revalidate();
                    Runner.component.validate();
                    MouseMenu.pianoHover = false;
                    recordClick = false;
                    record.setToBlack();
                    Runner.component.repaint();
                }
                else if(key == KeyEvent.VK_COMMA)
                {
                    channels[0].controlChange(93, 127);// Chorus Send
                }
                else if(key == KeyEvent.VK_M)
                {
                    channels[0].controlChange(66, 127);// sostenuto holds notes that are already playing
                }
                else if(key == KeyEvent.VK_N)
                {
                    channels[0].controlChange(1, 127);// modulation
                }
                else if(key == KeyEvent.VK_B)
                {
                    channels[0].controlChange(64, 127);//sustain pedal
                }
                else if(key == KeyEvent.VK_I)
                {
                    control = true;
                    Runner.first = true;
                    repaint();
                }
                else if(key == KeyEvent.VK_MINUS){
                    if(pitch > 0)
                    {
                        pitch -=12; //every key is pitched relative to this reference pitch.  Lowering the pitch variable by 12 takes all keys down 1 octave.
                        removeAll();
                        minus.changeColor(Color.LIGHT_GRAY);
                        repaint();
                    }

                }
                else if(key == KeyEvent.VK_EQUALS){
                    if (pitch < 108)
                    {
                        pitch += 12; //adding 12 to the reference pitch brings all keys up 1 octave
                        removeAll();
                        plus.changeColor(Color.LIGHT_GRAY);
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_P) //switches between sharp, flat, and control notation
                {

                    if(label == 0)
                    {
                        whiteKeys[0].changeLetter("C");
                        blackKeys[0].changeLetter("Db");
                        whiteKeys[1].changeLetter("D");
                        blackKeys[1].changeLetter("Eb");
                        whiteKeys[2].changeLetter("E");
                        whiteKeys[3].changeLetter("F");
                        blackKeys[2].changeLetter("Gb");
                        whiteKeys[4].changeLetter("G");
                        blackKeys[3].changeLetter("Ab");
                        whiteKeys[5].changeLetter("A");
                        blackKeys[4].changeLetter("A#");
                        whiteKeys[6].changeLetter("B");
                        label = 1;
                    }
                    else if(label == 1)
                    {
                        whiteKeys[0].changeLetter("C");
                        blackKeys[0].changeLetter("C#");
                        whiteKeys[1].changeLetter("D");
                        blackKeys[1].changeLetter("D#");
                        whiteKeys[2].changeLetter("E");
                        whiteKeys[3].changeLetter("F");
                        blackKeys[2].changeLetter("F#");
                        whiteKeys[4].changeLetter("G");
                        blackKeys[3].changeLetter("G#");
                        whiteKeys[5].changeLetter("A");
                        blackKeys[4].changeLetter("A#");  
                        whiteKeys[6].changeLetter("B");
                        label = 2;
                    }
                    else
                    {
                        whiteKeys[0].changeLetter("A");
                        blackKeys[0].changeLetter("W");
                        whiteKeys[1].changeLetter("S");
                        blackKeys[1].changeLetter("E");
                        whiteKeys[2].changeLetter("D");
                        whiteKeys[3].changeLetter("F");
                        blackKeys[2].changeLetter("T");
                        whiteKeys[4].changeLetter("G");
                        blackKeys[3].changeLetter("Y");
                        whiteKeys[5].changeLetter("H");
                        blackKeys[4].changeLetter("U");
                        whiteKeys[6].changeLetter("J");
                        label = 0;
                    }
                    repaint();
                }
                else if(key == KeyEvent.VK_A) //if the key pressed was A
                {
                    if (A == false) //this check prevents the keyboard's automatic rapid-pressing.
                    {
                        channels[0].noteOn(pitch, 60); //plays the reference pitch
                        A = true;
                        removeAll(); //deletes the component
                        whiteKeys[0].changeColor(Color.lightGray); //sets colors to gray
                        recorder.add("C"); //adds the note to the recprder
                        order[0] = counter;
                        counter++;
                        time[0] = System.currentTimeMillis();//sets the time variable to the current time.  Used for calculating how long the key was pressed for recording purposes.
                        repaint(); //repaints the component
                    }
                }
                else if(key == KeyEvent.VK_W)
                {
                    if (W == false)
                    {
                        channels[0].noteOn(pitch + 1, 60); // plays 1 half-step above the reference pitch
                        W = true;
                        removeAll();
                        blackKeys[0].changeColor(Color.lightGray);
                        recorder.add("C#");
                        order[1] = counter;
                        counter++;
                        time[1] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_S)
                {
                    if (S == false)
                    {
                        channels[0].noteOn(pitch + 2, 60); // plays 2 half-steps above the reference pitch
                        S = true;
                        removeAll();
                        whiteKeys[1].changeColor(Color.lightGray);
                        recorder.add("D");
                        order[2] = counter;
                        counter++;
                        time[2] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_E)
                {
                    if (E == false)
                    {
                        channels[0].noteOn(pitch + 3, 60);
                        E = true;
                        removeAll();
                        blackKeys[1].changeColor(Color.lightGray);
                        recorder.add("D#");
                        order[3] = counter;
                        counter++;
                        time[3] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_D)
                {
                    if (D == false)
                    {
                        channels[0].noteOn(pitch + 4, 60);
                        D = true;
                        removeAll();
                        whiteKeys[2].changeColor(Color.lightGray);
                        recorder.add("E");
                        order[4] = counter;
                        counter++;
                        time[4] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_F)
                {
                    if (F == false)
                    {
                        channels[0].noteOn(pitch + 5, 60);
                        F = true;
                        removeAll();
                        whiteKeys[3].changeColor(Color.lightGray);
                        recorder.add("F");
                        order[5] = counter;
                        counter++;
                        time[5] = System.currentTimeMillis();
                        repaint();
                    }
                }

                else if(key == KeyEvent.VK_T)
                {
                    if (T == false)
                    {
                        channels[0].noteOn(pitch + 6, 60);
                        T = true;
                        removeAll();
                        blackKeys[2].changeColor(Color.lightGray);
                        recorder.add("F#");
                        order[6] = counter;
                        counter++;
                        time[6] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_G)
                {
                    if (G == false)
                    {
                        channels[0].noteOn(pitch + 7, 60);
                        G = true;
                        removeAll();
                        whiteKeys[4].changeColor(Color.lightGray);
                        recorder.add("G");
                        order[7] = counter;
                        counter++;
                        time[7] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_Y)
                {
                    if (Y == false)
                    {
                        channels[0].noteOn(pitch + 8, 60);
                        Y = true;
                        removeAll();
                        blackKeys[3].changeColor(Color.lightGray);
                        recorder.add("G#");
                        order[8] = counter;
                        counter++;
                        time[8] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_H)
                {
                    if (H == false)
                    {
                        channels[0].noteOn(pitch + 9, 60);
                        H = true;
                        removeAll();
                        whiteKeys[5].changeColor(Color.lightGray);
                        recorder.add("A");
                        order[9] = counter;
                        counter++;
                        time[9] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_U)
                {
                    if (U == false)
                    {
                        channels[0].noteOn(pitch + 10, 60);
                        U = true;
                        removeAll();
                        blackKeys[4].changeColor(Color.lightGray);
                        recorder.add("A#");
                        order[10] = counter;
                        counter++;
                        time[10] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_J)
                {
                    if (J == false)
                    {
                        channels[0].noteOn(pitch + 11, 60);
                        J = true;
                        removeAll();
                        whiteKeys[6].changeColor(Color.lightGray);
                        recorder.add("B");
                        order[11] = counter;
                        counter++;
                        time[11] = System.currentTimeMillis();
                        repaint();
                    }
                }
            }

            public void keyTyped(KeyEvent e){}

            public void keyReleased(KeyEvent e)
            {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_PERIOD) //if the key released was a period
                {
                    channels[0].controlChange(91, 0);// reset the channel control to its default state (turns off reverb)
                }
                else if(key == KeyEvent.VK_COMMA)
                {
                    channels[0].controlChange(93, 0);// Chorus Send
                }
                else if(key == KeyEvent.VK_M)
                {
                    channels[0].controlChange(66, 0);// sostenuto holds notes that are already playing
                }
                else if(key == KeyEvent.VK_N)
                {
                    channels[0].controlChange(1, 0);// modulation
                }
                else if(key == KeyEvent.VK_B)
                {
                    channels[0].controlChange(64, 0);//sustain pedal
                }
                else if(key == KeyEvent.VK_I)
                {
                    control = false;
                    repaint();
                }
                else if (key == KeyEvent.VK_A)//if the key pressed was A
                {
                    channels[0].noteOff(pitch, 60); //turns off note
                    A = false; //makes the key available to be pressed again
                    removeAll();
                    whiteKeys[0].setToWhite();
                    time[0] = System.currentTimeMillis() - time[0]; //calculates the difference of the current time and the time the key was pressed
                    recorder.timeAdd(time[0], order[0]); //adds the calculated time value into the recorder
                    repaint();
                }
                else if (key == KeyEvent.VK_W)
                {
                    channels[0].noteOff(pitch + 1, 60);
                    W = false;
                    removeAll();
                    blackKeys[0].setToBlack();
                    time[1] = System.currentTimeMillis() - time[1];
                    recorder.timeAdd(time[1], order[1]);
                    repaint();
                }
                else if (key == KeyEvent.VK_S)
                {
                    channels[0].noteOff(pitch + 2, 60);
                    S = false;
                    removeAll();
                    whiteKeys[1].setToWhite();
                    time[2] = System.currentTimeMillis() - time[2];
                    recorder.timeAdd(time[2], order[2]);
                    repaint();
                }
                else if (key == KeyEvent.VK_E)
                {
                    channels[0].noteOff(pitch + 3, 60);
                    E = false;
                    removeAll();
                    blackKeys[1].setToBlack();
                    time[3] = System.currentTimeMillis() - time[3];
                    recorder.timeAdd(time[3], order[3]);
                    repaint();
                }
                else if (key == KeyEvent.VK_D)
                {
                    channels[0].noteOff(pitch + 4, 60);
                    D = false;
                    removeAll();
                    whiteKeys[2].setToWhite();
                    time[4] = System.currentTimeMillis() - time[4];
                    recorder.timeAdd(time[4], order[4]);
                    repaint();
                }
                else if (key == KeyEvent.VK_F)
                {
                    channels[0].noteOff(pitch + 5, 60);
                    F = false;
                    removeAll();
                    whiteKeys[3].setToWhite();     
                    time[5] = System.currentTimeMillis() - time[5];
                    recorder.timeAdd(time[5], order[5]);
                    repaint();
                }
                else if (key == KeyEvent.VK_T)
                {
                    channels[0].noteOff(pitch + 6, 60);
                    T = false;
                    removeAll();
                    blackKeys[2].setToBlack();
                    time[6] = System.currentTimeMillis() - time[6];
                    recorder.timeAdd(time[6], order[6]);
                    repaint();
                }
                else if (key == KeyEvent.VK_G)
                {
                    channels[0].noteOff(pitch + 7, 60);
                    G = false;
                    removeAll();
                    whiteKeys[4].setToWhite();
                    time[7] = System.currentTimeMillis() - time[7];
                    recorder.timeAdd(time[7], order[7]);
                    repaint();
                }
                else if (key == KeyEvent.VK_Y)
                {
                    channels[0].noteOff(pitch + 8, 60);
                    Y = false;
                    removeAll();
                    blackKeys[3].setToBlack();
                    time[8] = System.currentTimeMillis() - time[8];
                    recorder.timeAdd(time[8], order[8]);
                    repaint();
                }
                else if (key == KeyEvent.VK_H)
                {
                    channels[0].noteOff(pitch + 9, 60);
                    H = false;
                    removeAll();
                    whiteKeys[5].setToWhite();
                    time[9] = System.currentTimeMillis() - time[9];
                    recorder.timeAdd(time[9], order[9]);
                    repaint();
                }
                else if (key == KeyEvent.VK_U)
                {
                    channels[0].noteOff(pitch + 10, 60);
                    U = false;
                    removeAll();
                    blackKeys[4].setToBlack();
                    time[10] = System.currentTimeMillis() - time[10];
                    recorder.timeAdd(time[10], order[10]);
                    repaint();
                }
                else if (key == KeyEvent.VK_J)
                {
                    channels[0].noteOff(pitch + 11, 60);
                    J = false;
                    removeAll();
                    whiteKeys[6].setToWhite();
                    time[11] = System.currentTimeMillis() - time[11];
                    recorder.timeAdd(time[11], order[11]);
                    repaint();
                }
                else if(key == KeyEvent.VK_MINUS){
                    removeAll();
                    minus.changeColor(Color.BLACK);
                    repaint();
                }
                else if(key == KeyEvent.VK_EQUALS){
                    removeAll();
                    plus.changeColor(Color.BLACK);
                    repaint();      
                }   
            }
        }

        /**
         * This listener processes the mouse wheel's input and uses it to switch instruments.  It is fairly self-explanatory.
         */
        class Mouse implements MouseWheelListener
        {
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                int rot = e.getWheelRotation();
                if(rot == -1)
                {
                    if (instrument < 234){
                        instrument++;
                        channels[0].programChange(0, instrument);
                        repaint();
                    }
                }
                else
                {
                    if(instrument > 0){
                        instrument--;
                        channels[0].programChange(0, instrument);
                        repaint();
                    }
                }
            }
        }
        this.addKeyListener(new PianoListener());
        this.addMouseWheelListener(new Mouse());
        this.addMouseListener(new MousePiano());
        this.setFocusable(true);
        setPreferredSize(new Dimension(840,550));
    }

    /**
     * This listener processes mouse clicks.  It is able to open up the recording GUI button and control its actions.
     */
    public class MousePiano implements MouseListener
    {

        public void mouseClicked(MouseEvent e){}

        public void mouseEntered(MouseEvent e){}

        public void mouseExited(MouseEvent e){}

        public void mousePressed(MouseEvent e)
        {
            int minx = e.getX(); //gets cursor's position data
            int miny = e.getY();
            if((minx >= record.getX() && minx <= record.getX() + 150) 
            && (miny >= initialY && miny <= initialY + 150)) //if the click was within the bounds of the Record button
            {
                if(recordClick == false) //if the recorder was off, open it.
                {
                    record.changeColor(Color.RED);
                    recorder.state = true;
                    recordClick = true;
                    counter = 0;
                }
                else //otherwise, close the recorder.
                {
                    recordClick = false;
                    recorder.state = false;
                    recorder.remove();
                    record.setToBlack();
                }
                repaint();
            }

            if(minx >= record.getX() && minx <= record.getX() + 150)
            {
                if(miny >= 270 && miny <= 340)
                {
                    if(recordClick == true)
                    {
                        play.changeColor(Color.RED);
                        repaint();
                    }
                }
            }
            if(minx >= record.getX() && minx <= record.getX() + 150)
            {
                if(miny >= 185 && miny <= 255)
                {
                    if(recordClick == true)
                    {
                        clear.changeColor(Color.RED);
                        recorder.remove();
                        counter = 0;
                        repaint();
                    }
                }
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            int minx = e.getX();
            int miny = e.getY();
            if(minx >= record.getX() && minx <= record.getX() + 150)
            {
                if(miny >= 270 && miny <= 340)
                {
                    recorder.play();
                    play.setToBlack();
                    repaint();
                }
            }
            if(minx >= record.getX() && minx <= record.getX() + 150)
            {
                if(miny >= 185 && miny <= 255)
                {
                    clear.setToBlack();
                    repaint();
                }
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        Font bodyFont = new Font("Gill Sans", Font.BOLD, 20);
        Font titleFont = new Font("Gill Sans", Font.BOLD, 40);
        Color transGrey = new Color(0,0,0,180);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < whiteKeys.length; i++) // draw the white keys
        {
            whiteKeys[i].draw(g2);
        }
        for (int i = 0; i < blackKeys.length; i++) // draw the black keys
        {
            blackKeys[i].draw(g2);
        }

        //drawing the soundboard style buttons
        record.draw(g2);
        minus.draw(g2);
        plus.draw(g2);
        display.draw(g2);

        //drawing the instrument and octave information
        g2.setColor(Color.white);
        g2.setFont(bodyFont);
        g2.drawString(inst[instrument].getName(), 745, 475);
        g2.drawString("Octave: " + pitch / 12, 745, 500);
        g2.drawString("Recorder", record.getX() + 30, record.getY() + 80);
        g2.drawString("+", plus.getX() + 55, plus.getY() + 65);
        g2.drawString("-", minus.getX() + 55, minus.getY() + 65);

        //if the Record button has been clicked
        if(recordClick)
        {
            play.draw(g2);
            clear.draw(g2);
            g2.setColor(Color.WHITE);
            g2.drawString("Play", record.getX() + 50, play.getY() + 45);
            g2.drawString("Clear", record.getX() + 50, clear.getY() + 45);
        }

        //if this is the first time the window has been opened
        if(Runner.first == false)
        {
            g2.setColor(transGrey);
            g2.fillRect(0, 0, 1000, 1000);
            g2.setColor(Color.WHITE);
            g2.setFont(titleFont);
            g2.drawString("Hold I for Controls", 50, 50);
        }

        //if "I" is pressed
        if(control)
        {
            g2.setColor(transGrey);
            g2.fillRect(0, 0, 1000, 1000);
            g2.setColor(Color.white);
            g2.setFont(titleFont);
            g2.drawString("Controls", 50, 50);
            g2.setFont(bodyFont);
            g2.drawString("AWSEDFTGYHUJ for Notes", 50, 150);
            g2.drawString("P to toggle b and #", 50, 200);
            g2.drawString("Mouse Wheel to change instruments", 50, 250);
            g2.drawString("+- to change octaves", 50, 300);
            g2.drawString("ESC returns to Main Menu", 50, 350);
            g2.drawString("Hold B for Sustain Pedal", 550, 150);
            g2.drawString("Hold N for Pitch Shift", 550, 200);
            g2.drawString("Hold M for Sostenuto", 550, 250);
            g2.drawString("Hold , for Chorus", 550, 300);
            g2.drawString("Hold . for Reverb", 550, 350);
        }
    }
}

