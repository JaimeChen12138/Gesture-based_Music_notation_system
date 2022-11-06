package music;

import reaction.Mass;

import java.awt.*;
import java.util.ArrayList;

import static sandbox.Music.PAGE;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page;
    public int iSys;
    public Sys.Fmt fmt;

    public Time.List times;

    public Sys(Page page, int iSys, Sys.Fmt fmt){
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        this.fmt = fmt;
        times = new Time.List(this);

        for (int i = 0; i < fmt.size(); i++){
            addStaff(new Staff(this, i, fmt.get(i)));
        }
    }
    public void addStaff(Staff s){
        staffs.add(s);
    }

    public int yTop(){
        return page.sysTop(iSys);
    }

    public Time getTime(int x){return times.getTime(x);}


    public void show(Graphics g){
        int y = yTop(), x = PAGE.margin.left;
        g.drawLine(x, y, x, y + fmt.height());

    }

    public int yBot(){return staffs.get(staffs.size() - 1).yBot();} // last staff bottom


    // -------------Fmt------------------
    public static class Fmt extends ArrayList<Staff.Fmt>{
        public ArrayList<Integer> staffOffset = new ArrayList<>();


        public int height() {
            int last = size() - 1;
            return staffOffset.get(last) + get(last).height();
        }

        public void showAt(Graphics g, int y){
            for (int i = 0; i < size(); i++){
                get(i).showAt(g, y + staffOffset.get(i));
            }
        }

    }

}
