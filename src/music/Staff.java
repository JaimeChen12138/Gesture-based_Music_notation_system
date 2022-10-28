package music;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;

import static sandbox.Music.PAGE;

public class Staff extends Mass {
    public Sys sys;
    public int iStaff; // index
    public Staff.Fmt fmt;

    public Staff(Sys sys, int iStaff, Staff.Fmt fmt) {
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        this.fmt = fmt;

        addReaction(new Reaction("S-S") { // barline
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y1 = gest.vs.yL(), y2 = gest.vs.yH();//y start and end
                if (x < PAGE.margin.left || x > PAGE.margin.right + UC.barTooMarginSnap){
                    return UC.noBid;
                }
                System.out.println("Top" + y1 + " " + Staff.this.yTop());
                int d = Math.abs(y1 - Staff.this.yTop()) + Math.abs(y2 - Staff.this.yBot());
                return (d < 50) ? d + UC.barTooMarginSnap : UC.noBid;
            }

            public void act(Gesture gest) {
                int x = gest.vs.xM();
                if (Math.abs(x - PAGE.margin.right) < UC.barTooMarginSnap){
                    x = PAGE.margin.right;
                }
                new Bar(Staff.this.sys, x);


            }
        });

        addReaction(new Reaction("S-S") { // this is a set bar continues
            public int bid(Gesture gest) {
                if (Staff.this.sys.iSys != 0){return UC.noBid;} // only change bar continues in first system
                int y1 = gest.vs.yL(), y2 = gest.vs.yH(), iStaff = Staff.this.iStaff;
                if (iStaff == PAGE.sysFmt.size() - 1){return UC.noBid;} // last staff cannot continue
                if (Math.abs(y1 - Staff.this.yBot()) > 20){return UC.noBid;}
                Staff nextStaff = Staff.this.sys.staffs.get(iStaff + 1);
                if (Math.abs(y2 - nextStaff.yTop()) > 20){return UC.noBid;}

                return 10;
            }

            public void act(Gesture gest) {
                Staff.this.fmt.toggleBarContinues();

            }
        });
        addReaction(new Reaction("SW-SW") {
            public int bid(Gesture gest) {
                int x = gest.vs.xM(), y = gest.vs.yM();
                if (x < PAGE.margin.left || x > PAGE.margin.right){return UC.noBid;}
                int H = Staff.this.fmt.H, top = Staff.this.yTop() - H, bot = Staff.this.yBot()+H;
                if (y < top || y > bot){return UC.noBid;}
                return 10;
            }


            public void act(Gesture gest) {
                new Head(Staff.this, gest.vs.xM(), gest.vs.yM());
            }
        });
    }

    public int sysOff(){return sys.fmt.staffOffset.get(iStaff);}
    public int yTop(){return sys.yTop() + sysOff();}
    public int yBot(){return yTop() + fmt.height();}


    // ----------------------fat ----------------
    public static class Fmt{
        public int nLines = 5;
        public int H = UC.defaultStaffSpace;
        public boolean barContinues = false;

        public int height() {
            return 2 * H * (nLines-1);
        }

        public void toggleBarContinues(){barContinues = !barContinues;}



        public void showAt(Graphics g, int y){
            int LEFT = PAGE.margin.left, RIGHT = PAGE.margin.right;
            for (int i = 0; i < nLines; i++){
                g.drawLine(LEFT, y + 2 * H * i, RIGHT, y + 2 * H * i);
            }

        }

    }

}
