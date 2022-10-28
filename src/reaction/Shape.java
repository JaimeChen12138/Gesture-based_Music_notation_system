package reaction;

import graphics.G;
import music.UC;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Shape implements Serializable{ // 可以序列化
    public String name;
    public Prototype.List prototypes = new Prototype.List();
    public static DataBase DB = DataBase.load();      // database, 在下面class里创了
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values();


    public Shape(String name){this.name = name;}
    public static void saveShapeDB(){DataBase.save();}

//    public static HashMap<String,Shape> loadShapeDB(){
//        String filename = UC.shapeDBfilename;
//
//        HashMap<String,Shape> res = new HashMap<>();
//        res.put("DOT", new Shape("DOT"));
//        try {
//            System.out.println("Attempting DB load...");
//            ObjectInputStream OIS = new ObjectInputStream(new FileInputStream(filename));
//            res = (HashMap<String, Shape>) OIS.readObject();
//            System.out.println("Successful load - found " + res.keySet()); // list of what the shape in your database
//            OIS.close();
//        } catch (IOException e) {
//            System.out.println("load fail");
//            System.out.println(e);
//        } catch (ClassNotFoundException e) {
//            System.out.println("load fail");
//            System.out.println(e);
//        }
//        return res;
//    }

//    public static void saveShapeDB(){
//        String filename = UC.shapeDBfilename;
//        try{
//            System.out.println("save DB ...");
//            ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(filename));
//            OOS.writeObject(DB);
//            System.out.println("save " + filename);
//            OOS.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("fail database save");
//            System.out.println(e);
//        } catch (IOException e) {
//            System.out.println("fail database save");
//            System.out.println(e);
//        }
//
//    }
    public static Shape recognize(Ink ink){  // can return null
        if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold){
            return DOT;
        }
        else{
            Shape bestMatch = null;
            int bestSoFar = UC.noMatchDis;
            for (Shape s : LIST){
                int d = s.prototypes.bestDist(ink.norm);
                if (d < bestSoFar){
                    bestMatch = s;  // 当前最匹配
                    bestSoFar = d;
                }
            }return bestMatch;
        }
    }


    //---------------------Prototype class --------------------
    public static class Prototype extends Ink.Norm implements Serializable{  // single norm
        public int nBlend = 1;
        public void blend(Ink.Norm norm){
            blend(norm, nBlend);
            nBlend++;
        }
        //------------------------------------List-------------------------------------
        public static class List extends ArrayList<Prototype> implements Serializable{
            public static Prototype bestMatch; // set by side effect of bestDist
            private static int m = 10, w = 60; // margin width
            private static G.VS showBox = new G.VS(m, m, w, w);

            public int bestDist(Ink.Norm norm){
                bestMatch = null;
                int bestSoFar = UC.noMatchDis;
                for (Prototype p : this){
                    int d = p.dist(norm);
                    if (d < bestSoFar){
                        bestMatch = p;
                        bestSoFar = d;
                    }
                }return bestSoFar;
            }
            public void train(Ink ink){  // match
                if (isDeletePrototype(ink)){return;}
                if (bestDist(ink.norm) < UC.noMatchDis){
                    bestMatch.blend(ink.norm);
                }else{
                    add(new Shape.Prototype());
                }
            }

            public boolean isDeletePrototype(Ink ink){ // if true, it deletes
                int dot = UC.dotThreshold;
                if (ink.vs.size.x > dot || ink.vs.size.y > dot){return false;}
                if (ink.vs.loc.y > m + w){return false;}
                int iProto = ink.vs.loc.x / (m+w);
                if (iProto >= size()) return false;
                remove(iProto);
                return true;
            }

            public void show(Graphics g){
                g.setColor(Color.blue);
                for (int i = 0; i < size(); i++){
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g, showBox);
                    g.drawString("" + p.nBlend, x, 20);

                }
            }


        }
    }
    // -----------------------------------DataBase class -------------------------------------------
    public static class DataBase extends HashMap<String,Shape> {
        private DataBase() {
            super();
            addNewShape("DOT");
        }
        public static DataBase load() {
            String filename = UC.shapeDBfilename;
            DataBase res;
            //res.put("DOT", new Shape("DOT"));
            try {
                System.out.println("Attempting DB load...");
                ObjectInputStream OIS = new ObjectInputStream(new FileInputStream(filename));
                res = (DataBase) OIS.readObject();
                System.out.println("Successful load - found " + res.keySet()); // list of what the shape in your database
                OIS.close();
            } catch (Exception e) { // catch whatever error
                System.out.println("load fail");
                System.out.println(e);
                res = new DataBase();
            }
            return res;
        }

        public static void save() {
            String filename = UC.shapeDBfilename;
            try {
                System.out.println("save DB ...");
                ObjectOutputStream OOS = new ObjectOutputStream(new FileOutputStream(filename));
                OOS.writeObject(DB);
                System.out.println("save " + filename);
                OOS.close();
            } catch (Exception e) {
                System.out.println("fail database save");
                System.out.println(e);
            }

        }
            public void addNewShape(String name) {
            put(name, new Shape(name));
        }

        public Shape forceGet(String name) {    // safely to get the shape, if not there create new one
            if (!DB.containsKey(name)) addNewShape(name);
            return DB.get(name);
        }

        public void train(String name, Ink ink) {
          //  if (isDeletePrototype(ink)){return;}
            if (isLegal(name)) {forceGet(name).prototypes.train(ink);}
        }



        public static boolean isLegal(String name) {
            return !name.equals("") && !name.equals("DOT");
        }


        }
    }


