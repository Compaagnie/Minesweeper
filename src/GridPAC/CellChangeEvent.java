package GridPAC;

import java.awt.*;

public class CellChangeEvent extends AWTEvent
{
    public final int position;
    public final int reveal;
    public final boolean finish;
    public final boolean flagToggle;

    public CellChangeEvent(Object source, int position, String preset)
    {
        super(source, AWTEvent.RESERVED_ID_MAX);
        this.position = position;

        switch (preset)
        {
            case "reveal" :
            {
                this.reveal = true;
                this.finish = false;
                this.won = false;
                this.flagToggle = false;
            } break;

            case "reset" :
            {
                this.reveal = false;
                this.finish = false;
                this.won = false;
                this.flagToggle = false;
            } break;

            case "flag" :
            {
                this.reveal = false;
                this.finish = false;
                this.won = false;
                this.flagToggle = true;
            } break;

            case "win" :
            {
                this.reveal = true;
                this.finish = true;
                this.won = true;
                this.flagToggle = false;
            } break;

            case "lost" :
            {
                this.reveal = true;
                this.finish = true;
                this.won = false;
                this.flagToggle = false;
            } break;

            case "revive" : // only in roguelike
            {
                this.reveal = false;
                this.finish = true;
                this.won = false;
                this.flagToggle = false;
            } break;

            default:
            {
                this.reveal = false;
                this.finish = false;
                this.won = false;
                this.flagToggle = false;
                System.out.println("[ERROR] CellChangeEvent : Preset not implemented");
            }
        }
    }

    public CellChangeEvent(Object source, int position, int reveal)
    {
        super(source, AWTEvent.RESERVED_ID_MAX);
        this.position = position;
        this.reveal = reveal;
        this.finish = false;
        this.won = false;
        this.flagToggle = false;
    }

    public final boolean won;
    
    public CellChangeEvent(Object source, int position, int reveal, boolean finish, boolean won)
    {
        super(source, AWTEvent.RESERVED_ID_MAX);
        this.position = position;
        this.reveal = reveal;
        this.finish = finish;
        this.won = won;
        this.flagToggle = false;
    }
}
