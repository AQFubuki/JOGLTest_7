import Models.Model;
import Models.Tri;
import Models.Vertex;
import Models.Vertexs;
import Utility.MyUtil;

public class deleteTest {
    public void deleteTest(){
        Model testModel=new Model();
        Vertexs Vs=new Vertexs();

        Vertex v0=new Vertex(0.0f,0.0f,7.0f);
        Vertex v1=new Vertex(0.0f,6.0f,13.0f);
        Vertex v2=new Vertex(4.0f,5.0f,28.0f);
        Vertex v3=new Vertex(4.0f,8.0f,31.0f);
        Vertex v4=new Vertex(2.0f,4f,19.0f);
        //z=4x+y+7
        Vs.vertexs.put(v0.getTag(),v0);
        Vs.vertexs.put(v1.getTag(),v1);
        Vs.vertexs.put(v2.getTag(),v2);
        Vs.vertexs.put(v3.getTag(),v3);
        Vs.vertexs.put(v4.getTag(),v4);
        Vs.setFitting();
        System.out.println(Vs.LeftUp.getX()+" "+Vs.LeftUp.getY()+" "+Vs.LeftUp.getZ());
        System.out.println(Vs.LeftUp.Two_Dimension_X+" "+Vs.LeftUp.Two_Dimension_Y);
        System.out.println(Vs.LeftDown.getX()+" "+Vs.LeftDown.getY()+" "+Vs.LeftDown.getZ());
        System.out.println(Vs.LeftDown.Two_Dimension_X+" "+Vs.LeftDown.Two_Dimension_Y);
        System.out.println(Vs.RightUp.getX()+" "+Vs.RightUp.getY()+" "+Vs.RightUp.getZ());
        System.out.println(Vs.RightUp.Two_Dimension_X+" "+Vs.RightUp.Two_Dimension_Y);
        System.out.println(Vs.RightDown.getX()+" "+Vs.RightDown.getY()+" "+Vs.RightDown.getZ());
        System.out.println(Vs.RightDown.Two_Dimension_X+" "+Vs.RightDown.Two_Dimension_Y);
        for(Vertex v:Vs.vertexs.values()){
            System.out.println("////////////////////////");
            System.out.println(v.getX()+" "+v.getY()+" "+v.getZ());
            System.out.println(v.Two_Dimension_X+" "+v.Two_Dimension_Y);
            System.out.println(v.CIRCLE_X+" "+v.CIRCLE_Y+" "+v.CIRCLE_Z);
        }
        //z=-x+5y+0


        //0.0 0.0 -3.269230769230769
        //1.0 1.0 -1.3076923076923075
        //4.0 4.0 4.5769230769230775
        //0.5448717948717948 1.4166666666666667 -3.269230769230769
    }
}
