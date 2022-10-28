package sandbox;

import graphics.G;
import graphics.Window;
import music.UC;
import reaction.Ink;
import reaction.Shape;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static String recognized = "";
    public static Ink.List inkList = new Ink.List();
//    static{inkList.add(new Ink());} // create a single ink obj
    public static Shape.Prototype.List pList = new Shape.Prototype.List();


    public PaintInk() {
        super("PaintInt", UC.initialWindowWidth, UC.initialWindowHeight);
    }
    public void paintComponent(Graphics g){
        G.clear(g);
        inkList.show(g);
        g.setColor(Color.red);
        Ink.BUFFER.show(g);
        if (inkList.size() > 1){
            int last = inkList.size()-1;
            int dis = inkList.get(last).norm.dist(inkList.get(last-1).norm);
            g.setColor((dis < UC.noMatchDis ? Color.green : Color.red));
            g.drawString("dist: " + dis,600,60);
        }
        g.drawString("points " + Ink.BUFFER.n, 600, 30);
        pList.show(g);
        g.drawString(recognized,700,40);
    }

    public void mousePressed(MouseEvent me){
        Ink.BUFFER.dn(me.getX(),me.getY());
        repaint();
    }
    public void mouseDragged(MouseEvent me){
        Ink.BUFFER.drag(me.getX(),me.getY());
        repaint();
    }

    public void mouseReleased(MouseEvent me){
        Ink ink = new Ink();
        Shape s = Shape.recognize(ink);
        recognized = "recognized: " + ((s != null) ? s.name : "unrecognized");

        Shape.Prototype proto;
        inkList.add(ink);
        if (pList.bestDist(ink.norm) < UC.noMatchDis){
            proto = Shape.Prototype.List.bestMatch;
            proto.blend(ink.norm);
        }
        else{
            proto = new Shape.Prototype();
            pList.add(proto);
        }
        ink.norm = proto;
        repaint();
    }



}

