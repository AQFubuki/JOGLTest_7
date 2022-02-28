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

    public Vertex(String tag,boolean t){this.setT(tag);}
    public Vertex(String tag) {
        this.set(tag);
    }
    public Vertex(double X, double Y, double Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        this.setT();
    }

    public boolean isEquals(Vertex vertex) {
        return this.isEquals(vertex.getTag());
    }

    public boolean isEquals(String tag) {
        if (this.getTag().equals(tag)) {
            return true;
        }
        return false;
    }

    public void addnearVs(Vertex v) {
        this.nearVs.vertexs.put(v.getTag(), v);
    }

    public void addnearEs(Edge e) {
        renewnearEs(e);
        this.nearEs.edges.put(e.getTag(), e);
    }

    public void addnearTs(Tri t) {
        this.nearTs.tris.put(t.getTag(), t);
        for(Tri tempT:this.nearTs.tris.values()){
            if(!t.isEquals(tempT)){
            tempT.addnearTs(t);}
        }
    }

    public void shownearVs(){
        for(Vertex v:nearVs.vertexs.values()){
            System.out.println(v.getTag());
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

    public String getTag() {
        return tag;
    }

    public void setT(String tag) {
        this.tag = tag;
    }

    public void setT() {
        this.tag = String.valueOf(this.getX())+" "+String.valueOf(this.getY())+" "+String.valueOf(this.getZ());
        //留待改成科学计数法
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

    public void printnearTris(){
        nearTs.printTag();
    }
    public void printnearEdges(){
        nearEs.printTag();
    }
    public void printnearVertexs(){
        nearVs.printTag();
    }
    public void printTag() {
        System.out.println(this.getTag());
    }

}
