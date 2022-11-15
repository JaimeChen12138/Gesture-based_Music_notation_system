package music;

import reaction.Mass;

import java.awt.*;

public abstract class Duration extends Mass {
    public int nFlag = 0, nDot = 0;
    public Duration(){
        super("NOTE");
    }

    public abstract void show(Graphics g); // children of duration implement it

    public void incFlag() {if (nFlag < 4) {nFlag++;
    System.out.println(nFlag);}}
    public void decFlag() {if (nFlag > -2) {nFlag--;}}

    public void cycleDots(){
        nDot++;
        if (nDot > 3){
            nDot = 0;
        }
    }


}
