package sandbox;

import graphics.G;
import graphics.Window;
import music.I;
import music.UC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static graphics.G.VS.theColor;

public class Squares extends Window implements ActionListener {
    //public static G.VS theVS = new G.VS(100,100,200,300);//create object of the VS(rectangle)小长方形

    public static Square.List theList = new Square.List();

    public static Square theSquare;

    public static boolean dragging = false;//distinguish either drag or create a new square

    public static G.V mouseDelta = new G.V(0,0);

    public static Timer timer;

    public static I.Area currentArea;
    public static Square BACKGROUND = new Square(0,0){
        public void dn(int x,int y){
            dragging = false;
            theSquare = new Square(x, y); // create new small square and capture
            theList.add(theSquare);
        }
        public void drag(int x,int y){
            theSquare.resize(x, y);
        }
    };
    static{
        BACKGROUND.c = Color.white;
        BACKGROUND.size.set(5000,5000);
        theList.add(BACKGROUND);

    }

    public static final int W = UC.initialWindowWidth;//in individual class can use short-easy name,in the UC use the meaningful name
    public static final int H = UC.initialWindowHeight;
    public Squares() {
        super("Squares",W,H); // the white board(外部屏幕)
        timer = new Timer(30,this);
        timer.setInitialDelay(1000);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
         G.clear(g);
        //theVS.fill(g,theColor);
        theList.draw(g);


    }
    public void mousePressed(MouseEvent me){
        /*if (theVS.hit(me.getX(), me.getY())){
            theColor = G.rndColor();
        }*/
        int x = me.getX(), y = me.getY();
        currentArea = theList.hit(x,y);
        currentArea.dn(x,y);

//        if (theSquare == null) { //point at the white board,create new square
//            dragging = false;
//            theSquare = new Square(me.getX(), me.getY()); // create new small square and capture
//            theList.add(theSquare);
//            //theList.addNew(me.getX(),me.getY());
//        } else{
//            dragging = true;
//            mouseDelta.set(x - theSquare.loc.x,y-theSquare.loc.y);
//        }
        repaint();
    }
    public void mouseDragged(MouseEvent me){ // when dragged resize the size
        int x = me.getX(), y = me.getY();
        currentArea.drag(x,y);
//        if (dragging){
//            theSquare.move(x-mouseDelta.x,y- mouseDelta.y);
//        }else{
//            theSquare.resize(me.getX(), me.getY());
//        }
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }


    // ------------Squares --------/
    public static class Square extends G.VS implements I.Area{ // create some other squares
        public Color c = G.rndColor();
        public G.V dv = new G.V(0,0); //new G.V(G.rnd(40)- 20,G.rnd(40)-20);

        public void move(int x,int y){  // move the square
            loc.x = x;
            loc.y = y;
        }

        public void moveAndBounce(){ //encounter a bound and bounce back
            loc.add(dv);
            if (loc.x < 0 && dv.x < 0){dv.x = - dv.x;} //left
            if (loc.y < 0 && dv.y < 0){dv.y = - dv.y;} //bottom
            if (loc.x + size.x > W && dv.x > 0){dv.x = - dv.x;}//right
            if (loc.y + size.y > H && dv.y > 0){dv.y = - dv.y;}//top
        }
        public void draw(Graphics g){
            fill(g,c);
            moveAndBounce();
        }

        public Square(int x, int y) { //Squares.square
            super(x, y,100,100); }// h,w = 100,100

        @Override
        public void dn(int x, int y) {
            dragging = true;
            mouseDelta.set(x - theSquare.loc.x,y-theSquare.loc.y);
        }

        @Override
        public void drag(int x, int y) {
            theSquare.move(x-mouseDelta.x,y- mouseDelta.y);
        }

        @Override
        public void up(int x, int y) {

        }

        //-----list----------------------------//
        public static class List extends ArrayList<Square> implements I.Draw{
            public void draw(Graphics g){
                for (Square s : this){s.draw(g);}
            }

            public Square hit(int x,int y){
                Square res = null;
                for (Square s: this){
                    if (s.hit(x,y)){
                        res = s;
                    }
                }
                return res;
            }

            public void addNew(int x, int y){
                add(new Square(x,y));
            }

        }
    }





}
