
/**
 * Write a description of class Note here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Note
{
    private String note;
    private long time;
    
    public Note(String note)
    {
        this.note = note;
        time = 0;
    }
    
    public Note(String note, long time)
    {
        this.note = note;
        this.time = time;
    }
    
    public Note addTime(long n)
    {
        return new Note(this.note, n);
    }
    
    public String getNote()
    {
        return note;
    }
    
    public long getTime()
    {
        return time;
    }
}