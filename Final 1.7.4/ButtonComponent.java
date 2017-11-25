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
 * Similar to PianoComponent, processes keystrokes and acts as a Synthesizer.  See PianoComponent for commenting.
 */
public class ButtonComponent extends JComponent
{
    private Button[][] sound;
    private Button record;
    private Button play;
    private Button clear;
    private ProfileControlButton minus;
    private ProfileControlButton plus;
    private ProfileControlButton display;
    private final int initialX = 20;
    private final int initialY = 20;
    private final int spacing = 10;
    private final int length = 150;
    private Instrument[] inst;
    public static int pitch = 60;
    private int instrument = 0;
    public static MidiChannel[] channels;
    private boolean control;
    private int label;
    private Recorder recorder;
    private long[] time = new long[12];
    private int[] order = new int[12];
    private int counter;
    private boolean recordClick;
    private boolean playClick;
    public ButtonComponent()
    {
        sound = new Button[3][4];
        sound[0][0] = new SoundButton(initialX, initialY, "Q");
        sound[0][1] = new SoundButton(sound[0][0].getX() + length + spacing, sound[0][0].getY(), "W");
        sound[0][2] = new SoundButton(sound[0][1].getX() + length + spacing, sound[0][0].getY(), "E");
        sound[0][3] = new SoundButton(sound[0][2].getX() + length + spacing, sound[0][0].getY(), "R");

        sound[1][0] = new SoundButton(sound[0][0].getX(), sound[0][0].getY() + length + spacing, "A");
        sound[1][1] = new SoundButton(sound[0][1].getX(), sound[1][0].getY(), "S");
        sound[1][2] = new SoundButton(sound[0][2].getX(), sound[1][0].getY(), "D");
        sound[1][3] = new SoundButton(sound[0][3].getX(), sound[1][0].getY(), "F");

        sound[2][0] = new SoundButton(sound[0][0].getX(), sound[1][0].getY() + length + spacing, "Z");
        sound[2][1] = new SoundButton(sound[0][1].getX(), sound[2][0].getY(), "X");
        sound[2][2] = new SoundButton(sound[0][2].getX(), sound[2][0].getY(), "C");
        sound[2][3] = new SoundButton(sound[0][3].getX(), sound[2][0].getY(), "V");

        record = new RecButton(sound[0][3].getX() + length + 2 * spacing, sound[0][0].getY(), 150, 150);
        play = new RecButton(sound[0][3].getX() + length + 2 * spacing, sound[1][0].getY() + 80, 150, 70);
        clear = new RecButton(sound[0][3].getX() + length + 2 * spacing, sound[1][0].getY(), 150, 70);  
        minus = new ProfileControlButton(record.getX(), sound[2][0].getY(), 70, 70, "-");
        plus = new ProfileControlButton(record.getX() + 80, sound[2][0].getY(), 70, 70, "+");
        display = new ProfileControlButton(record.getX(), sound[2][0].getY() + 80, 150, 70, "");
        JLabel instNum = new JLabel("" + instrument); 
        recorder = new Recorder("button");
        class Listener implements KeyListener 
        {

            private boolean Q = false;
            private boolean W = false;
            private boolean E = false;
            private boolean R = false;
            private boolean A = false;
            private boolean S = false;
            private boolean D = false;
            private boolean F = false;
            private boolean Z = false;
            private boolean X = false;
            private boolean C = false;
            private boolean V = false;
            public Listener()
            {
                try{
                    Synthesizer synth = MidiSystem.getSynthesizer();
                    synth.open();
                    inst = synth.getDefaultSoundbank().getInstruments();
                    synth.loadInstrument(inst[0]);
                    channels = synth.getChannels();
                    channels[0].programChange(0, instrument); 

                    //97 is cool
                }
                catch(Exception j)
                {
                }
            }

            public void keyPressed(KeyEvent e)
            {    
                int key = e.getKeyCode();
                recorder.instrumentChange(instrument);
                if(key == KeyEvent.VK_PERIOD)
                {
                    channels[0].controlChange(91, 127);// Reverb
                }
                else if(key == KeyEvent.VK_ESCAPE)
                {
                    Runner.frame.setSize(500, 500);
                    Runner.cl.show(Runner.cards, "Menu");
                    Runner.cards.getComponent(0).requestFocusInWindow();
                    Runner.component.revalidate();
                    Runner.component.validate();
                    MouseMenu.soundHover = false;
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
                        pitch -=12;
                        removeAll();
                        minus.changeColor(Color.LIGHT_GRAY);
                        repaint();
                    }

                }
                else if(key == KeyEvent.VK_EQUALS){
                    if (pitch < 108)
                    {
                        pitch += 12;
                        removeAll();
                        plus.changeColor(Color.LIGHT_GRAY);
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_P)
                {
                    if(label == 0)
                    {
                        sound[0][0].changeLetter("C");
                        sound[0][1].changeLetter("Db");
                        sound[0][2].changeLetter("D");
                        sound[0][3].changeLetter("Eb");
                        sound[1][0].changeLetter("E");
                        sound[1][1].changeLetter("F");
                        sound[1][2].changeLetter("Gb");
                        sound[1][3].changeLetter("G");
                        sound[2][0].changeLetter("Ab");
                        sound[2][1].changeLetter("A");
                        sound[2][2].changeLetter("A#");
                        sound[2][3].changeLetter("B");
                        label = 1;
                    }
                    else if(label == 1)
                    {
                        sound[0][0].changeLetter("C");
                        sound[0][1].changeLetter("C#");
                        sound[0][2].changeLetter("D");
                        sound[0][3].changeLetter("D#");
                        sound[1][0].changeLetter("E");
                        sound[1][1].changeLetter("F");
                        sound[1][2].changeLetter("F#");
                        sound[1][3].changeLetter("G");
                        sound[2][0].changeLetter("G#");
                        sound[2][1].changeLetter("A");
                        sound[2][2].changeLetter("A#");  
                        sound[2][3].changeLetter("B");
                        label = 2;
                    }
                    else
                    {
                        sound[0][0].changeLetter("Q");
                        sound[0][1].changeLetter("W");
                        sound[0][2].changeLetter("E");
                        sound[0][3].changeLetter("R");
                        sound[1][0].changeLetter("A");
                        sound[1][1].changeLetter("S");
                        sound[1][2].changeLetter("D");
                        sound[1][3].changeLetter("F");
                        sound[2][0].changeLetter("Z");
                        sound[2][1].changeLetter("X");
                        sound[2][2].changeLetter("C");
                        sound[2][3].changeLetter("V");
                        label = 0;
                    }
                    repaint();
                }
                else if(key == KeyEvent.VK_Q)
                {
                    if (Q == false)
                    {
                        channels[0].noteOn(pitch, 60);
                        Q = true;
                        removeAll();
                        sound[0][0].changeColor(Color.RED);
                        recorder.add("C");
                        order[0] = counter;
                        counter++;
                        time[0] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_W)
                {
                    if (W == false)
                    {
                        channels[0].noteOn(pitch + 1, 60);
                        W = true;
                        removeAll();
                        sound[0][1].changeColor(Color.ORANGE);
                        recorder.add("C#");
                        order[1] = counter;
                        counter++;
                        time[1] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_E)
                {
                    if (E == false)
                    {
                        channels[0].noteOn(pitch + 2, 60);
                        E = true;
                        removeAll();
                        sound[0][2].changeColor(Color.YELLOW);
                        recorder.add("D");
                        order[2] = counter;
                        counter++;
                        time[2] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_R)
                {
                    if (R == false)
                    {
                        channels[0].noteOn(pitch + 3, 60);
                        R = true;
                        removeAll();
                        sound[0][3].changeColor(Color.GREEN);
                        recorder.add("D#");
                        order[3] = counter;
                        counter++;
                        time[3] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_A)
                {
                    if (A == false)
                    {
                        channels[0].noteOn(pitch + 4, 60);
                        A = true;
                        removeAll();
                        sound[1][0].changeColor(Color.ORANGE);
                        recorder.add("E");
                        order[4] = counter;
                        counter++;
                        time[4] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_S)
                {
                    if (S == false)
                    {
                        channels[0].noteOn(pitch + 5, 60);
                        S = true;
                        removeAll();
                        sound[1][1].changeColor(Color.YELLOW);
                        recorder.add("F");
                        order[5] = counter;
                        counter++;
                        time[5] = System.currentTimeMillis();
                        repaint();
                    }
                }

                else if(key == KeyEvent.VK_D)
                {
                    if (D == false)
                    {
                        channels[0].noteOn(pitch + 6, 60);
                        D = true;
                        removeAll();
                        sound[1][2].changeColor(Color.GREEN);
                        recorder.add("F#");
                        order[6] = counter;
                        counter++;
                        time[6] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_F)
                {
                    if (F == false)
                    {
                        channels[0].noteOn(pitch + 7, 60);
                        F = true;
                        removeAll();
                        sound[1][3].changeColor(Color.BLUE);
                        recorder.add("G");
                        order[7] = counter;
                        counter++;
                        time[7] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_Z)
                {
                    if (Z == false)
                    {
                        channels[0].noteOn(pitch + 8, 60);
                        Z = true;
                        removeAll();
                        sound[2][0].changeColor(Color.YELLOW);
                        recorder.add("G#");
                        order[8] = counter;
                        counter++;
                        time[8] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_X)
                {
                    if (X == false)
                    {
                        channels[0].noteOn(pitch + 9, 60);
                        X = true;
                        removeAll();
                        sound[2][1].changeColor(Color.GREEN);
                        recorder.add("A");
                        order[9] = counter;
                        counter++;
                        time[9] = System.currentTimeMillis();
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_C)
                {
                    if (C == false)
                    {
                        channels[0].noteOn(pitch + 10, 60);
                        C = true;
                        removeAll();
                        recorder.add("A#");
                        order[10] = counter;
                        counter++;
                        time[10] = System.currentTimeMillis();
                        sound[2][2].changeColor(Color.BLUE);
                        repaint();
                    }
                }
                else if(key == KeyEvent.VK_V)
                {
                    if (V == false)
                    {
                        channels[0].noteOn(pitch + 11, 60);
                        V = true;
                        removeAll();
                        recorder.add("B");
                        order[11] = counter;
                        counter++;
                        time[11] = System.currentTimeMillis();
                        sound[2][3].changeColor(Color.MAGENTA);
                        repaint();
                    }
                }
            }

            public void keyTyped(KeyEvent e)
            {
            }

            public void keyReleased(KeyEvent e)
            {
                int key = e.getKeyCode();
                if(key == KeyEvent.VK_PERIOD)
                {
                    channels[0].controlChange(91, 0);// Reverb
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
                else if (key == KeyEvent.VK_Q)
                {
                    channels[0].noteOff(pitch, 60);
                    Q = false;
                    removeAll();
                    sound[0][0].setToBlack();
                    time[0] = System.currentTimeMillis() - time[0];
                    recorder.timeAdd(time[0], order[0]);
                    repaint();
                }
                else if (key == KeyEvent.VK_W)
                {
                    channels[0].noteOff(pitch + 1, 60);
                    W = false;
                    removeAll();
                    sound[0][1].setToBlack();
                    time[1] = System.currentTimeMillis() - time[1];
                    recorder.timeAdd(time[1], order[1]);
                    repaint();
                }
                else if (key == KeyEvent.VK_E)
                {
                    channels[0].noteOff(pitch + 2, 60);
                    E = false;
                    removeAll();
                    sound[0][2].setToBlack();
                    time[2] = System.currentTimeMillis() - time[2];
                    recorder.timeAdd(time[2], order[2]);
                    repaint();
                }
                else if (key == KeyEvent.VK_R)
                {
                    channels[0].noteOff(pitch + 3, 60);
                    R = false;
                    removeAll();
                    sound[0][3].setToBlack();
                    time[3] = System.currentTimeMillis() - time[3];
                    recorder.timeAdd(time[3], order[3]);
                    repaint();
                }
                else if (key == KeyEvent.VK_A)
                {
                    channels[0].noteOff(pitch + 4, 60);
                    A = false;
                    removeAll();
                    sound[1][0].setToBlack();
                    time[4] = System.currentTimeMillis() - time[4];
                    recorder.timeAdd(time[4], order[4]);
                    repaint();
                }
                else if (key == KeyEvent.VK_S)
                {
                    channels[0].noteOff(pitch + 5, 60);
                    S = false;
                    removeAll();
                    sound[1][1].setToBlack();
                    time[5] = System.currentTimeMillis() - time[5];
                    recorder.timeAdd(time[5], order[5]);
                    repaint();
                }
                else if (key == KeyEvent.VK_D)
                {
                    channels[0].noteOff(pitch + 6, 60);
                    D = false;
                    removeAll();
                    sound[1][2].setToBlack();
                    time[6] = System.currentTimeMillis() - time[6];
                    recorder.timeAdd(time[6], order[6]);
                    repaint();
                }
                else if (key == KeyEvent.VK_F)
                {
                    channels[0].noteOff(pitch + 7, 60);
                    F = false;
                    removeAll();
                    sound[1][3].setToBlack();
                    time[7] = System.currentTimeMillis() - time[7];
                    recorder.timeAdd(time[7], order[7]);
                    repaint();
                }
                else if (key == KeyEvent.VK_Z)
                {
                    channels[0].noteOff(pitch + 8, 60);
                    Z = false;
                    removeAll();
                    sound[2][0].setToBlack();
                    time[8] = System.currentTimeMillis() - time[8];
                    recorder.timeAdd(time[8], order[8]);
                    repaint();
                }
                else if (key == KeyEvent.VK_X)
                {
                    channels[0].noteOff(pitch + 9, 60);
                    X = false;
                    removeAll();
                    sound[2][1].setToBlack();
                    time[9] = System.currentTimeMillis() - time[9];
                    recorder.timeAdd(time[9], order[9]);
                    repaint();
                }
                else if (key == KeyEvent.VK_C)
                {
                    channels[0].noteOff(pitch + 10, 60);
                    C = false;
                    removeAll();
                    sound[2][2].setToBlack();
                    time[10] = System.currentTimeMillis() - time[10];
                    recorder.timeAdd(time[10], order[10]);
                    repaint();
                }
                else if (key == KeyEvent.VK_V)
                {
                    channels[0].noteOff(pitch + 11, 60);
                    V = false;
                    removeAll();
                    sound[2][3].setToBlack();
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
        this.addKeyListener(new Listener());
        this.addMouseWheelListener(new Mouse());
        this.addMouseListener(new MouseSound());
        this.setFocusable(true);
        setPreferredSize(new Dimension(840,550));
    }

    public class MouseSound implements MouseListener
    {

        public void mouseClicked(MouseEvent e)
        {

        }

        public void mouseEntered(MouseEvent e)
        {

        }

        public void mouseExited(MouseEvent e)
        {

        }

        public void mousePressed(MouseEvent e)
        {
            //              record = new RecButton(sound[0][3].getX() + length + 2 * spacing, sound[0][0].getY(), 150, 150);
            //         play = new RecButton(sound[0][3].getX() + length + 2 * spacing, sound[1][0].getY() + 80, 150, 70);
            //         clear = new RecButton(sound[0][3].getX() + length + 2 * spacing, sound[1][0].getY(), 150, 70);
            int minx = e.getX();
            int miny = e.getY();
            if(minx >= sound[0][3].getX() + length + 2 * spacing && minx <= sound[0][3].getX() + length + 2 * spacing + 150)
            {
                if(miny >= sound[0][0].getY() && miny <= sound[0][0].getY() + 150)
                {
                    if(recordClick == false)
                    {
                        record.changeColor(Color.RED);
                        recorder.state = true;
                        recordClick = true;
                        counter = 0;
                    }
                    else
                    {
                        recordClick = false;
                        recorder.state = false;
                        recorder.remove();
                        counter = 0;
                        record.setToBlack();
                    }
                    repaint();
                }
            }
            if(minx >= sound[0][3].getX() + length + 2 * spacing && minx <= sound[0][3].getX() + length + 2 * spacing + 150)
            {
                if(miny >= sound[1][0].getY() + 80 && miny <= sound[1][0].getY() + 150)
                {
                    if(recordClick == true)
                    {
                        play.changeColor(Color.RED);
                        repaint();
                    }
                }
            }
            if(minx >= sound[0][3].getX() + length + 2 * spacing && minx <= sound[0][3].getX() + length + 2 * spacing + 150)
            {
                if(miny >= sound[1][0].getY() && miny <= sound[1][0].getY() + 70)
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
            if(minx >= sound[0][3].getX() + length + 2 * spacing && minx <= sound[0][3].getX() + length + 2 * spacing + 150)
            {
                if(miny >= sound[1][0].getY() + 80 && miny <= sound[1][0].getY() + 150)
                {
                    recorder.play();
                    play.setToBlack();
                    repaint();
                }
            }
            if(minx >= sound[0][3].getX() + length + 2 * spacing && minx <= sound[0][3].getX() + length + 2 * spacing + 150)
            {
                if(miny >= sound[1][0].getY() && miny <= sound[1][0].getY() + 70)
                {
                    clear.setToBlack();
                    repaint();
                }
            }
        }

    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < sound.length; i++)
        {
            for(int j = 0; j < sound[i].length; j++){
                sound[i][j].draw(g2);
            }
        }
        record.draw(g2);

        minus.draw(g2);
        plus.draw(g2);
        display.draw(g2);

        g2.setColor(Color.white);
        Font font = new Font("Gill Sans", Font.BOLD, 20);
        Font title = new Font("Gill Sans", Font.BOLD, 40);
        g2.setFont(font);
        g2.drawString(inst[instrument].getName(), 675, 450);
        g2.drawString("Octave: " + pitch / 12, 675, 475);
        g2.drawString("Recorder", 700, record.getY() + 80);
        if(recordClick)
        {
            play.draw(g2);
            clear.draw(g2);
            g2.drawString("Play", 725, play.getY() + 40);
            g2.drawString("Clear", 725, clear.getY() + 40);
        }
        Color color = new Color(0,0,0,180);
        if(Runner.first == false)
        {
            g2.setColor(color);
            g2.fillRect(0, 0, 1000, 1000);
            g2.setColor(Color.WHITE);
            g2.setFont(title);
            FontMetrics metrics = g2.getFontMetrics();
            g2.drawString("Hold I for Controls", 50 , 50);
        }
        if(control)
        {
            g2.setColor(color);
            g2.fillRect(0, 0, 1000, 1000);
            g2.setColor(Color.white);
            g2.setFont(title);
            g2.drawString("Controls", 50, 50);
            g2.setFont(font);
            g2.drawString("QWERASDFZXC for Notes", 50, 150);
            g2.drawString("P to cycle controls, b, and #", 50, 200);
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

