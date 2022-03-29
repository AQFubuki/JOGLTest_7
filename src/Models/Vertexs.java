package Models;

import Utility.MyUtil;
import glm.vec._3.Vec3;

import java.util.HashMap;

import static java.lang.Math.abs;

public class Vertexs {
    public HashMap<String,Vertex> vertexs=new HashMap<String,Vertex>();
    public double MAX_X=0.0f,MIN_X=0.0f,MAX_Y=0.0f,MIN_Y=0.0f;
    public Vertex LeftUp,LeftDown,RightUp,RightDown;
    public double a[]=new double[]{0.0f,0.0f,0.0f};

    public void printTag(){
        for(Vertex v:this.vertexs.values()){
            v.printTag();
        }
    }
    public void setFitting() {
        this.setM();
        a = MyUtil.planeFitting(this);
        for (Vertex v : this.vertexs.values()) {
            v.setPLANE_FITTING_Z(MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], v));
        }
        LeftUp = new Vertex(MIN_X, MAX_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MIN_X, MAX_Y));
        LeftDown = new Vertex(MIN_X, MIN_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MIN_X, MIN_Y));
        RightUp = new Vertex(MAX_X, MAX_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MAX_X, MAX_Y));
        RightDown = new Vertex(MAX_X, MIN_Y, MyUtil.setPlaneFittingPoint(a[0], a[1], a[2], MAX_X, MIN_Y));
        this.changeDimensionTo2D();
        MyUtil.circleFitting(this);
        this.changeDimensionTo3D();
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
        for(Vertex v:this.vertexs.values()){
            System.out.println(v.getX());
        }
    }
    public void changeDimensionTo2D(){
        for(Vertex v:this.vertexs.values()){
            v.Two_Dimension_X=this.getDistance(LeftUp,LeftDown,v);
            v.Two_Dimension_Y=this.getDistance(LeftDown,RightDown,v);
        }
        RightUp.Two_Dimension_X=this.getDistance(LeftUp,LeftDown,RightUp);
        RightUp.Two_Dimension_Y=this.getDistance(LeftDown,RightDown,RightUp);

        RightDown.Two_Dimension_X=this.getDistance(LeftUp,LeftDown,RightDown);
        RightDown.Two_Dimension_Y=this.getDistance(LeftDown,RightDown,RightDown);

        LeftDown.Two_Dimension_X=this.getDistance(LeftUp,LeftDown,LeftDown);
        LeftDown.Two_Dimension_Y=this.getDistance(LeftDown,RightDown,LeftDown);

        LeftUp.Two_Dimension_X=this.getDistance(LeftUp,LeftDown,LeftUp);
        LeftUp.Two_Dimension_Y=this.getDistance(LeftDown,RightDown,LeftUp);
    }
    public void changeDimensionTo3D(){
        //z=a0x+a1y+a2
        Vec3 y=new Vec3(LeftUp.getX()-LeftDown.getX(),
                LeftUp.getY()-LeftDown.getY(),
                LeftUp.getZ()-LeftDown.getZ());
        Vec3 x=new Vec3(RightDown.getX()-LeftDown.getX(),
                RightDown.getY()-LeftDown.getY(),
                RightDown.getZ()-LeftDown.getZ());
        double x_length=RightDown.Two_Dimension_X-LeftDown.Two_Dimension_X;
        double y_length=LeftUp.Two_Dimension_Y-LeftDown.Two_Dimension_Y;
        for(Vertex v:this.vertexs.values()){
            double rat_x=v.Two_Dimension_X/x_length;
            double rat_y=v.Two_Dimension_Y/y_length;
            v.CIRCLE_X=LeftDown.X+rat_x*x.x+rat_y*y.x;
            v.CIRCLE_Y=LeftDown.Y+rat_x*x.y+rat_y*y.y;
            v.CIRCLE_Z=LeftDown.Z+rat_x*x.z+rat_y*y.z;
        }
    }

    public double getDistance(Vertex sv,Vertex ev,Vertex v){
        Vec3 SV_EV=new Vec3(ev.getX()-sv.getX(),ev.getY()-sv.getY(),
                ev.getPLANE_FITTING_Z()-sv.getPLANE_FITTING_Z());
        Vec3 SV_V=new Vec3(v.getX()-sv.getX(),v.getY()-sv.getY(),
                v.getPLANE_FITTING_Z()-sv.getPLANE_FITTING_Z());
        Vec3 res=new Vec3();
        SV_EV.cross(SV_V,res);
        double res_L=res.length();
        double SV_EV_L=SV_EV.length();
        if(abs(SV_EV_L)<0.000001){
            return 0.0f;
        }
        return res_L/SV_EV_L;
    }

    public void CircleFitting(){

    }
}
