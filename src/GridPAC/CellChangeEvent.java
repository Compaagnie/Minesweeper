package GridPAC;

import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventTarget;

import java.awt.*;

public class CellChangeEvent extends AWTEvent
{
    public final int position;
    public final boolean reveal;

    public CellChangeEvent(Object source, int position, boolean reveal)
    {
        super(source, AWTEvent.RESERVED_ID_MAX);
       this.position = position;
       this.reveal = reveal;
    }


}
