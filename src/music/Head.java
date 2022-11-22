package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass implements Comparable<Head>{

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
                Time t = time;
                int w = Head.this.w();
                boolean up = x > (t.x + w / 2);
                if (Head.this.stem == null){
//                    t.stemHeads(Head.this.staff.sys, up,y1,y2);
                    stem.getStem(t, y1, y2, up);
                }
                else{
                    t.unStemHeads(y1, y2);
                }
            }
        });

        addReaction(new Reaction("DOT") {

            public int bid(Gesture gest) {
                if (Head.this.stem == null){
                    return UC.noBid;
                }
                int xh = Head.this.x(), yh = Head.this.y(), h = Head.this.staff.fmt.H, w = Head.this.w();
                int x = gest.vs.xM(), y = gest.vs.yM();
                if (x < xh || x > xh + 2 * w || y < yh-h || y > yh + h){
                    return UC.noBid;
                }
                return Math.abs(xh + w - x) + Math.abs(yh - y);
            }

            public void act(Gesture gest) {
                Head.this.stem.cycleDots();
            }
        });


    }

    public int w(){return 24 * staff.fmt.H / 10;}

    public void show(Graphics g){
        int H = staff.fmt.H;
        g.setColor(wrongSide ? Color.RED : Color.BLACK);
        ((forceGlyph != null) ? forceGlyph : normalGlyph()).showAt(g, H, x(), y());
        if (stem != null){
            int off = UC.augDotOffset, sp = UC.augDotSpace;
            for (int i = 0; i < stem.nDot; i++){
                g.fillOval(time.x + off + i * sp, y() - 3 * H / 2, 2*H / 3,2*H/3);
            }

        }
    }

    public int x(){
        // stub - placeholder
        int res = time.x;
        if (wrongSide){
            res += (stem != null && stem.isUp) ? w() : -w();
        }
        return res;
    }

    public int y(){
        return staff.yLine(line);
    }

    public Glyph normalGlyph(){
        if (stem == null){return Glyph.HEAD_Q;}
        if (stem.nFlag == - 1){return Glyph.HEAD_HALF;}
        if (stem.nFlag == -2){return Glyph.HEAD_W;}
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

    @Override
    public int compareTo(Head h) {
        return (staff.iStaff != h.staff.iStaff) ? staff.iStaff - h.staff.iStaff : this.line - h.line;
    }

    public static class List extends ArrayList<Head>{}


}
