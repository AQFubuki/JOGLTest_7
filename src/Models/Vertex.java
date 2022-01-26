package Models;

import java.math.BigDecimal;

public class Vertex {
    protected double X;
    protected double Y;
    protected double Z;
    protected String tag;
    protected Vertexs nearVs = new Vertexs();
    protected Edges nearEs = new Edges();
    protected Tris nearTs = new Tris();

    public Vertex() {
    }

    public Vertex(double X, double Y, double Z, String tag) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.tag = tag;
    }

    public Vertex(String tag) {
        this.set(tag);
    }

    public boolean isEquals(Vertex vertex) {
        if (this.tag.equals(vertex.getT())) {
            return true;
        }
        return false;
    }

    public boolean isEquals(String tag) {
        if (this.tag.equals(tag)) {
            return true;
        }
        return false;
    }

    public void addnearVs(Vertex v) {
        this.nearVs.vertexs.put(v.getT(), v);
    }

    public void addnearEs(Edge e) {
        renewnearEs(e);
        this.nearEs.edges.put(e.getTag(), e);
    }

    public void addnearTs(Tri t) {
        this.nearTs.tris.put(t.getTag(), t);
        for(Tri tempT:this.nearTs.tris.values()){
            tempT.addnearTs(t);
        }
    }

    public void shownearVs(){
        for(Vertex v:nearVs.vertexs.values()){
            System.out.println(v.getT());
        }
    }

    public void shownearEs(){
        for(Edge e:nearEs.edges.values()){
            System.out.println(e.getTag());
        }
    }

    public void shownearTs(){
        for(Tri t:nearTs.tris.values()){
            System.out.println(t.getTag());
        }
    }

    public void renewnearEs(Edge e){
        for(Edge tempE:nearEs.edges.values()){
            e.addnearEs(tempE);
            tempE.addnearEs(e);
        }
    }

    public double getX() {
        return X;
    }

    public void setX(double x) {
        this.X = x;
    }

    public double getY() {
        return Y;
    }

    public void setY(double y) {
        this.Y = y;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double z) {
        this.Z = z;
    }

    public String getT() {
        return tag;
    }

    public void setT(String tag) {
        this.tag = tag;
    }

    public Tris getnearTs(){
        return nearTs;
    }

    public void set(Vertex vertex) {
        this.setX(vertex.getX());
        this.setY(vertex.getY());
        this.setZ(vertex.getZ());
        this.setT(vertex.tag);
    }

    public void set(String tag) {
        this.setT(tag);
        String[] spString = tag.split("\\s+");

        BigDecimal bd = new BigDecimal(spString[0]); //转化科学计数法
        String sNum = bd.toPlainString();
        double dMun = Double.parseDouble(sNum);
        this.setX(dMun);

        bd = new BigDecimal(spString[1]);
        sNum = bd.toPlainString();
        dMun = Double.parseDouble(sNum);
        this.setY(dMun);

        bd = new BigDecimal(spString[2]);
        sNum = bd.toPlainString();
        dMun = Double.parseDouble(sNum);
        this.setZ(dMun);

    }
}
