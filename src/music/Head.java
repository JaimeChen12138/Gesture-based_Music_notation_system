package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass {

    public Staff staff;
    public int line;
    public Time time;

    public Glyph forceGlyph = null;
    public Stem stem = null;
    public boolean wrongSide = false;


    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        time = staff.sys.getTime(x);
        time.heads.add(this);

        int H = staff.fmt.H;
        //int top = staff.yTop() - H;
        //line = (y - top + H / 2) / H - 1;
        this.line = staff.lineOfY(y);

//       System.out.println("line: " + line);
        addReaction(new Reaction("S-S") {
            @Override
            public int bid(Gesture gest) {
                int x = gest.vs.xL();
                int y1 = gest.vs.yL(), y2 = gest.vs.yH();
                int w = Head.this.w(), hY = Head.this.y();
                if (y1 > y || y2 < y){return UC.noBid;}

                int hleft = Head.this.time.x, hright = hleft + w;
                if (x < hleft - w || x > hright + w){return UC.noBid;}
                if (x < hleft + w / 2){return hleft -x;}
                if (x > hright - w/2){return x - hright;}

                return UC.noBid;
            }

            @Override
            public void act(Gesture gest) {
                int x = gest.vs.xL(), y1 = gest.vs.yL(), y2 = gest.vs.yH();
                Time t = Head.this.time;
                int w = Head.this.w();
                boolean up = x > (t.x + w / 2);
                if (Head.this.stem == null){
                    t.stemHeads(up,y1,y2);
                }
                else{
                    t.unStemHeads(y1, y2);
                }
            }
        });


    }

    public int w(){return 24 * staff.fmt.H / 10;}

    public void show(Graphics g){
        int H = staff.fmt.H;
        g.setColor(stem == null ? Color.RED : Color.BLACK);

        ((forceGlyph != null) ? forceGlyph : normalGlyph()).showAt(g, H, x(), y());
    }

    public int x(){
        // stub - placeholder
        return time.x;
    }

    public int y(){
        return staff.yLine(line);
    }

    public Glyph normalGlyph(){
        return Glyph.HEAD_Q;
    }

    public void deleteHead(){
        time.heads.remove(this);

    }
    public void unStem(){
        if (stem != null){
            stem.heads.remove(this);
            if (stem.heads.size() == 0){stem.deleteStem();}
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s){
        if (stem != null){unStem();}
        s.heads.add(this);
        stem = s;
    }

    public static class List extends ArrayList<Head>{}


}
