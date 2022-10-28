package graphics;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class G{
    public static Random RND = new Random();
    // random number generator

    public static int rnd(int max) {
        return RND.nextInt(max);
    }

    public static void drawCircle(Graphics g,int x,int y,int r){
        g.drawOval(x - r,y - r,2 * r, 2*r); // 把左上角点放到中心圆心
    }

    //helper function Color constructor
    public static Color rndColor() {
        return new Color(rnd(255), rnd(255), rnd(255));
    }
    public static void clear(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0,0,5000,5000);

    }
    //-------------------V---------------------// Vector
    public static class V implements Serializable{ // hold coordinate pairs(x,y)

        public static Transform T = new Transform();

        public int x,y;
        public V(int x,int y){this.set(x,y);} //constructor
        public void set(int x,int y){this.x = x; this.y = y;}

        public void set(V v){this.x = v.x;this.y = v.y;}
        public void add(V v){
            x += v.x;
            y += v.y;
        }

        public void setT(V v){set(v.tx(),v.ty());}

        public int tx(){return x * T.n/T.d + T.dx;}
        public int ty(){return y * T.n/T.d + T.dy;}


        public void blend(V v, int k){set((k*x + v.x)/(k+1), (k*y + v.y)/(k+1));}



        // --------------------------------- Transform -----------------------
        public static class Transform{
            public int dx, dy, n, d;
            public void setScale(int oW, int oH, int nW, int nH){
                n = (nW > nH) ? nW : nH;
                d = (oW > oH) ? oW : oH;
            }
            public int offSet(int oX, int oW, int nX, int nW){
                return (-oX - oW/2) * n/d + nX + nW/2;
            }
            public void set(VS oVS, VS nVS){
                setScale(oVS.size.x, oVS.size.y, nVS.size.x, nVS.size.y);
                dx = offSet(oVS.loc.x,oVS.size.x,nVS.loc.x,nVS.size.x);
                dy = offSet(oVS.loc.y,oVS.size.y,nVS.loc.y,nVS.size.y);
            }
            public void set(BBox oB, VS nVS){
                setScale(oB.h.size(), oB.v.size(), nVS.size.x, nVS.size.y);
                dx = offSet(oB.h.lo,oB.h.size(),nVS.loc.x,nVS.size.x);
                dy = offSet(oB.v.lo, oB.v.size(),nVS.loc.y,nVS.size.y);
            }
        }
    }

    //-------------------VS-------------------------------//
    public static class VS{ // used to record the rectangle's information
        public V loc,size;
        public VS(int x, int y, int w, int h){ loc = new V(x,y); size = new V(w,h);}

        public static Color theColor = Color.blue;

        public void fill(Graphics g,Color c){
            g.setColor(c);
            g.fillRect(loc.x,loc.y,size.x,size.y);
        }
        public boolean hit(int x, int y){ // check if it is in the area of the rectangle
            return loc.x < x && loc.y < y && x < (loc.x + size.x) && y < (loc.y+size.y);
            //   (x,y) should be in the rectangle loc.x is left side of rectangle, loc.y is bottom of the rectangle
        }

        public int xL(){
            return loc.x;
        }

        public int xM(){
            return loc.x + size.x / 2; // mid value
        }

        public int xH(){return loc.x + size.x;}

        public int yL(){
            return loc.y;
        }

        public int yM(){
            return loc.y + size.y / 2; // mid value
        }

        public int yH(){return loc.y + size.y;}


        public void resize(int x, int y){
            if (x > loc.x && y > loc.y){
                size.set(x- loc.x,y- loc.y); // resize the rectangle
            }

        }
    }

    //-------------------LoHi-------------------------//
    public static class LoHi{   // helpers lo hi to build BBbox
        public int lo,hi;
        public LoHi(int min, int max){lo = min; hi = max;}
        public void add(int x){   //resize
            if (x < lo){
                lo = x;
            }
            if (x > hi){
                hi = x;
            }
        }
        public void set(int x){lo = x; hi = x;}
        public int size(){return (hi - lo) == 0 ? 1 : hi - lo; } // 0 的时候return 1
    }

    //-------------------BBox-------------------------//
    public static class BBox{
        public LoHi h,v;
        public BBox(){h = new LoHi(0,0); v = new LoHi(0,0);}
        public void set(int x, int y){h.set(x); v.set(y);} //initalize
        public void add(V v){h.add(v.x); this.v.add(v.y);}
        public void add(int x, int y){h.add(x); v.add(y);}
        public VS getNewVS(){return new VS(h.lo, v.lo, h.size(), v.size());} // Bound box collect data
        public void draw(Graphics g){g.drawRect(h.lo, v.lo, h.size(), v.size());}

    }

    //-------------------PL---------------------------//
    public static class PL implements Serializable {  //PolyLine
        public V[] points;  // array of points
        public PL(int count){
            points = new V[count];
            for (int i = 0; i < count; i ++){points[i] = new V(0,0);}
        }

        public int size(){return points.length;};

        public void transform(){
            for (int i = 0; i < points.length; i++){
                points[i].setT(points[i]);
            }
        }

        public void drawN(Graphics g,int n){
            for (int i = 1; i < n; i++){
                g.drawLine(points[i-1].x,points[i-1].y,points[i].x,points[i].y);
            }
        }
        public void drawNDots(Graphics g,int n){
            for (int i = 0; i < n; i++){drawCircle(g,points[i].x,points[i].y,2);}
        }


        public void draw(Graphics g){drawN(g, points.length);}





    }




}
