package Models;

import Utility.MyUtil;

import java.util.HashMap;

public class Vertexs {
    public HashMap<String,Vertex> vertexs=new HashMap<String,Vertex>();
    public double MAX_X=0.0f,MIN_X=0.0f,MAX_Y=0.0f,MIN_Y=0.0f;
    public Vertex LeftUp,LeftDown,RightUp,RightDown;

    public void printTag(){
        for(Vertex v:this.vertexs.values()){
            v.printTag();
        }
    }
    public void setPlaneFitting() {
        this.setM();
        double a[] = MyUtil.planeFitting(this);
        for (Vertex v : this.vertexs.values()) {
            v.setPLANE_FITTING_Z(MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], v));
        }
        LeftUp = new Vertex(MIN_X, MAX_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MIN_X, MAX_Y));
        LeftDown = new Vertex(MIN_X, MIN_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MIN_X, MIN_Y));
        RightUp = new Vertex(MAX_X, MAX_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MAX_X, MAX_Y));
        RightDown = new Vertex(MAX_X, MIN_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MAX_X, MIN_Y));
    }
    public void setM(){
        if(this.vertexs.isEmpty())return;
        for(Vertex v:this.vertexs.values()){
            MAX_X=v.getX();
            MIN_X=v.getX();
            MIN_Y=v.getY();
            MAX_Y=v.getY();
            break;
        }
        for(Vertex v:this.vertexs.values()){
            double x=v.getX();
            double y=v.getY();
            if(MAX_X<x) MAX_X=x;
            if(MIN_X>x) MIN_X=x;
            if(MAX_Y<y) MAX_Y=y;
            if(MIN_Y>y) MIN_Y=y;
        }
    }

    public void print(){
        for(Vertex v:this.vertexs.values()){
            System.out.println("VertexTag:");
            v.printTag();
            System.out.println("VertexnearVertexs:");
            v.printnearVertexs();
            System.out.println("VertexEdges:");
            v.printnearEdges();
            System.out.println("VertexTris:");
            v.printnearTris();
        }
    }
    public void printX(){
        System.out.println("MAX_X:"+MAX_X+"   MIN_X"+MIN_X);
        for(Vertex v:this.vertexs.values()){
            System.out.println(v.getX());
        }
    }
}
