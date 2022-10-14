package GridPAC;

import java.awt.*;

public class CellChangeEvent extends AWTEvent
{
    public final int position;
    public final boolean reveal;
    public final boolean finish;
    public final boolean won;

    public final boolean flagToggle;

    public CellChangeEvent(Object source, int position, String preset)
    {
        super(source, AWTEvent.RESERVED_ID_MAX);
        this.position = position;

        switch (preset) {
            case "reveal" -> {
                this.reveal = true;
                this.finish = false;
                this.won = false;
                this.flagToggle = false;
            }
            case "reset" -> {
                this.reveal = false;
                this.finish = false;
                this.won = false;
                this.flagToggle = false;
            }
            case "flag" -> {
                this.reveal = false;
                this.finish = false;
                this.won = false;
                this.flagToggle = true;
            }
            case "win" -> {
                this.reveal = true;
                this.finish = true;
                this.won = true;
                this.flagToggle = false;
            }
            case "lost" -> {
                this.reveal = true;
                this.finish = true;
                this.won = false;
                this.flagToggle = false;
            }
            case "revive" -> // only in roguelike
            {
                this.reveal = false;
                this.finish = true;
                this.won = false;
                this.flagToggle = false;
            }
            default -> {
                this.reveal = false;
                this.finish = false;
                this.won = false;
                this.flagToggle = false;
                System.out.println("[ERROR] CellChangeEvent : Preset not implemented");
            }
        }
    }
}
