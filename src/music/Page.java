package music;

import reaction.Gesture;
import reaction.Layer;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static sandbox.Music.PAGE;

public class Page extends Mass {
    public Margin margin = new Margin();
    public Sys.Fmt sysFmt;
    public int sysGap;
    public ArrayList<Sys> sysList = new ArrayList<>();

    public Page(Sys.Fmt sysFmt){
        super("BACK");
        this.sysFmt = sysFmt;


        addReaction(new Reaction("E-W") { // add a new staff to the sysfmt
            public int bid(Gesture gest) {
                int y = gest.vs.yM();
                if (y <= PAGE.margin.top + sysFmt.height() + 30){
                    return UC.noBid;
                }
                return 50;
            }

            public void act(Gesture gest) {
                int y = gest.vs.yM();
                PAGE.addNewStaff(y - PAGE.margin.top);
            }
        });

        addReaction(new Reaction("E-E"){ // add new sys
            public int bid (Gesture gest){
                int y = gest.vs.yM();
                int yBot = PAGE.sysTop(PAGE.sysList.size() - 1);
                if (y < yBot) {
                    return UC.noBid;
                }
                return 500;
            }

            public void act(Gesture gest){
                int y = gest.vs.yM();
                if (PAGE.sysList.size() == 1) {
                    PAGE.sysGap = y - PAGE.margin.top - sysFmt.height();
                }
                PAGE.addNewSys();
            }

    });
    }

    public void addNewSys(){
        sysList.add(new Sys(this, sysList.size(), sysFmt));
    }

    public void addNewStaff(int yOffset){
        Staff.Fmt fmt = new Staff.Fmt();
        int n = sysFmt.size();
        sysFmt.add(fmt);
        sysFmt.staffOffset.add(yOffset);
        for (int i = 0; i < sysList.size(); i++){
            Sys sys = sysList.get(i);
            sys.staffs.add(new Staff(sys, n, fmt));

        }
    }

    public int sysTop(int iSys){
        return margin.top + iSys * (sysFmt.height() + sysGap);
    }

    public void show(Graphics g){
        for (int i = 0; i < sysList.size(); i++){
            sysFmt.showAt(g, sysTop(i));
        }
    }



    /// -----------------Margin class------
    public static class Margin{
        private static int N = UC.defaultPageMargin;
        public int top = N, left = N, bot = UC.initialWindowHeight-N, right = UC.initialWindowWidth-N;


    }



}
