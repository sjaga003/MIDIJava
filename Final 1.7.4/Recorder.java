import java.util.*;
import javax.sound.midi.*;
/**
 * Write a description of class Recording2 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Recorder
{
    public static ArrayList<Note> list = new ArrayList<Note>();
    public static MidiChannel[] channels = ButtonComponent.channels;
    public static int delayToPlay = 250;
    public static boolean state; //tracks whether the recorder should record
    public static int pitch;
    public Recorder(String str) //on creation of Recorder initialize a new Synthesizer
    {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            Instrument[] ins = synth.getDefaultSoundbank().getInstruments();
            synth.loadInstrument(ins[3]);
            channels = synth.getChannels();
            channels[0].programChange(0, 3);

        }
        catch(Exception e)
        {

        }
        if (str.equals("piano"))
        {
            pitch = PianoComponent.pitch;
        }
        else if (str.equals("button"))
        {
            pitch = ButtonComponent.pitch;
        }
    }

    public static void instrumentChange(int instrument)
    {
        channels[0].programChange(0, instrument); 
    }

    /**
     * This method plays back what has been recorded.  Takes information from list and timeList.
     */
    public static void play()
    {
        if(state)
        {
            for(int i = 0; i < list.size(); i++) // increments down the ArrayList of note names
            {
                try{
                    if (i != 0)
                    {
                        Thread.sleep(delayToPlay); // delays a specified amount before playing
                    }
                }
                catch(Exception e){}
                try{
                    if(list.get(i).getNote().equalsIgnoreCase("C")) // if the letter at position "i" is "C"
                    {
                        channels[0].noteOn(pitch, 60); // play a C
                        Thread.sleep(list.get(i).getTime()); // do nothing for the appropriate time
                        channels[0].noteOff(pitch); // turn off the note.                    
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("C#"))
                    {
                        channels[0].noteOn(pitch + 1, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 1);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("D"))
                    {
                        channels[0].noteOn(pitch + 2, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 2);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("D#"))
                    {
                        channels[0].noteOn(pitch + 3, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 3);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("E"))
                    {
                        channels[0].noteOn(pitch + 4, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 4);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("F"))
                    {
                        channels[0].noteOn(pitch + 5, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 5);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("F#"))
                    {
                        channels[0].noteOn(pitch + 6, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 6);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("G"))
                    {
                        channels[0].noteOn(pitch + 7, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 7);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("G#"))
                    {
                        channels[0].noteOn(pitch + 8, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 8);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("A"))
                    {
                        channels[0].noteOn(pitch + 9, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 9);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("A#"))
                    {
                        channels[0].noteOn(pitch + 10, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 10);
                    }
                    else if(list.get(i).getNote().equalsIgnoreCase("B"))
                    {
                        channels[0].noteOn(pitch + 11, 60);
                        Thread.sleep(list.get(i).getTime());
                        channels[0].noteOff(pitch + 11);
                    }
                }catch(Exception e){}
            }
        }
    }

    public static void remove() //method clears the recorded information
    {
        list.clear();
    }

    public static void add(String note)
    {
        if(state) //if the recorder is on
        {
            list.add(new Note(note));
        }
    }

    public static void timeAdd(long time, int order)
    {
        if(state)
        {
            list.set(order, list.get(order).addTime(time));
        }
    }
}
