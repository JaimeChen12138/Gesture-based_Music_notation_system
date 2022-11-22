package sandbox;

import graphics.G;
import graphics.Window;
import music.*;
import reaction.Gesture;
import reaction.Ink;
import reaction.Layer;
import reaction.Reaction;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Music extends Window {
    static{ new Layer ("BACK");
            new Layer ("NOTE");
            new Layer("FORE");}
    public static Page PAGE;

    static int[] xPoly = {100, 200, 200, 100};
    static int[] yPoly = {50, 70, 80, 60};
    static Polygon poly = new Polygon(xPoly, yPoly, 4);


    public static void main(String [] args){
        (PANEL = new Music()).launch();
    }


    public Music() {
        super("Music editor", UC.initialWindowWidth, UC.initialWindowHeight);
        Reaction.initialReaction.addReaction(new Reaction("E-E") {
            public int bid(Gesture gest) {
                return 10;
            }

            public void act(Gesture gest) {
                int y = gest.vs.yM();
                Sys.Fmt sysFmt = new Sys.Fmt();
                PAGE = new Page(sysFmt);
                PAGE.margin.top = y;
                PAGE.addNewSys();
                PAGE.addNewStaff(0);
                this.disable();
            }
        });

    }

    public void paintComponent(Graphics g){
        G.clear(g);
        g.setColor(Color.BLUE);
        g.drawString("music", 100, 30);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);

//        int H = 32, y = 100 + 4 * H;
//        Glyph.HEAD_Q.showAt(g, H, 200, y);
//        g.drawRect(200, y - H, 24 * H / 10, 2 * H);

//        Beam.setPoly(100, 100 + G.rnd(100), 200, 100 + G.rnd(100), 8);
//        g.fillPolygon(Beam.poly);
//        int h = 8, x1 = 100, x2 = 200;
//        Beam.setMasterBeam(x1, 100 + G.rnd(100), x2, 100 + G.rnd(100));
//        Beam.drawBeamStack(g, 0, 1, x1, x2, h);
//        g.setColor(Color.ORANGE);
//        Beam.drawBeamStack(g, 1, 3, x1 + 10, x2 - 10, h);

    }

    public void mouseDragged(MouseEvent me){Gesture.AREA.drag(me.getX(), me.getY());repaint();}

    public void mousePressed(MouseEvent me){Gesture.AREA.dn(me.getX(), me.getY());repaint();}

    public void mouseReleased(MouseEvent me){Gesture.AREA.up(me.getX(), me.getY());repaint();}


}
