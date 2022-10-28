package sandbox;

//import graphics.G;
import graphics.G;
import graphics.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paint extends Window {
    public Paint(){super("paint", 1500, 750);}// constructor
//
//
     public static Pic thePic = new Pic();// store the path in the arraylist
//
     public static Path thePath; // store the point in the arraylist
//
      public static Color c = G.rndColor();//pick a random color
      //
      public static int clicks = 0;

      public void mousePressed(MouseEvent me){
         clicks ++;
         thePath = new Path();
         thePath.add(me.getPoint());
         thePic.add(thePath); // pic pointer已经指向了第一个path的位置所以drag的时候不用存pic
         repaint();
    }

    public void mouseDragged(MouseEvent me){ //拖的时候get每一个点，无数个点
        thePath.add(me.getPoint());
        repaint();
    }

    public void paintComponent(Graphics g){
//       g.setColor(Color.BLUE);

        G.clear(g);
//      Color c = G.rndColor(); // 拖动窗口不变色了，因为用的是上面static color
        g.setColor(c);
        g.fillRect(100,100,200,300);
        g.setColor(Color.black);
        g.drawLine(100,600,600,100);

//
         String msg = "Dude";
         int x = 400, y = 200;
         g.drawString(msg,400,200);
         g.drawOval(x,y,3,3);
         // node sits at the bottom-left that's why set box y = y-a
//
//        g.fillOval(400,200,2,2);
         FontMetrics fm = g.getFontMetrics();
         int a = fm.getAscent(), d = fm.getDescent();
         //the ascent is how far above the baseline the font extends, descent is how far below for letters
         int w = fm.stringWidth(msg);
         // string width
         g.drawRect(x,y-a,w,a+d);
          // give a bound box for the String

//        g.setColor(Color.black);
         g.drawString("Clicks =" + clicks,320,300);

         thePic.draw(g);

    }
//    //-----------Path-----------------//
    public static class Path extends ArrayList<Point>{
      public void draw(Graphics g){
            for (int i = 1;i < size();i++){
               Point p = get(i-1),n = get(i);
               g.drawLine(p.x, p.y, n.x,n.y);
               // connect the two points
            }
        }
    }
//
//    //-----------------pic -----------------
    public static class Pic extends ArrayList<Path>{
          // pic is used to store path
        public void draw(Graphics g){
            for (Path p : this){ //this pic 里的path p 存进去
                p.draw(g);   // called path.draw 连接两个点
            }
        }
    }

}
