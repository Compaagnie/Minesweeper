package GridPAC;

import java.awt.*;

public class CellChangeEvent extends AWTEvent
{
    public final int position;
    public final boolean reveal;
    public final boolean finish;
    public final boolean won;

    public CellChangeEvent(Object source, int position, boolean reveal)
    {
        super(source, AWTEvent.RESERVED_ID_MAX);
        this.position = position;
        this.reveal = reveal;
        this.finish = false;
        this.won = false;
    }

    public CellChangeEvent(Object source, int position, boolean reveal, boolean finish, boolean won)
    {
        super(source, AWTEvent.RESERVED_ID_MAX);
        this.position = position;
        this.reveal = reveal;
        this.finish = finish;
        this.won = won;
    }


}
