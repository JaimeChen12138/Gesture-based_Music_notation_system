package music;

import reaction.Gesture;
import reaction.Reaction;

import java.awt.*;

public class Rest extends Duration{
    public Staff staff;
    public Time time;
    public int line = 4; // mid
    public Rest(Staff staff, Time time){
        this.staff = staff;
        this.time = time;
        addReaction(new Reaction("E-E") { // add flags

            public int bid(Gesture gest) {
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH();
                int x = Rest.this.time.x;
                if (x1 > x || x2 < x){return UC.noBid;}
                return Math.abs(y - Rest.this.staff.yLine(4));
            }

            public void act(Gesture gest) {
                Rest.this.incFlag();

            }
        });
        addReaction(new Reaction("W-W") { // remove flags

            public int bid(Gesture gest) {
                int y = gest.vs.yM(), x1 = gest.vs.xL(), x2 = gest.vs.xH();
                int x = Rest.this.time.x;
                if (x1 > x || x2 < x){return UC.noBid;}
                return Math.abs(y - Rest.this.staff.yLine(4));
            }

            public void act(Gesture gest) {
                Rest.this.decFlag();

            }
        });


    }

    public int y(){return staff.yLine(line);}


    @Override
    public void show(Graphics g) {
        int H = staff.fmt.H, y = y();
        if (nFlag == -2){Glyph.REST_W.showAt(g,H, time.x,y);}
        if (nFlag == -1){Glyph.REST_H.showAt(g,H, time.x,y);}
        if (nFlag == 0){Glyph.REST_Q.showAt(g,H, time.x,y);}
        if (nFlag == 1){Glyph.REST_1F.showAt(g,H, time.x,y);}
        if (nFlag == 2){Glyph.REST_2F.showAt(g,H, time.x,y);}
        if (nFlag == 3){Glyph.REST_3F.showAt(g,H, time.x,y);}
        if (nFlag == 4){Glyph.REST_4F.showAt(g,H, time.x,y);}
    }
}
